package org.genevaers.testframework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.genevaers.genevaio.recordreader.ZosHelper;
import org.genevaers.testframework.sdsf.HeldJobs;
import org.genevaers.testframework.sdsf.TestJobs;
import org.genevaers.testframework.yamlreader.GersTest;
import org.genevaers.testframework.yamlreader.OutputFile;
import org.genevaers.testframework.yamlreader.Replacement;
import org.genevaers.testframework.yamlreader.Spec;
import org.genevaers.testframework.yamlreader.XMLFile;
import org.genevaers.utilities.CommandRunner;
import org.genevaers.utilities.FileProcessor;
import org.genevaers.utilities.Substitution;
import org.genevaers.utilities.GersEnvironment;
import org.genevaers.utilities.menu.Menu;
import org.w3c.dom.NodeList;

import com.google.common.flogger.FluentLogger;
import com.ibm.jzos.FileFactory;
import com.ibm.jzos.MvsJobSubmitter;
import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileException;
import com.ibm.zos.sdsf.core.ISFJobStep;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TestDriver {
	private static final int JES_WARNING = 4;

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	/**
	 *
	 */
	private static final String TEST_SRC = "target/generated-sources/org/genevaers/test/";
	/**
	 *
	 */
	private static final String DELALL = "DELALL";
	private static final String LOCALROOT = "LOCALROOT";
	private static final String TSO_USERID = "TSO_USERID";
	private Map<String, String> envVars;

	TestDriver() {
		GersEnvironment.initialiseFromTheEnvironment();
		envVars = GersEnvironment.getEnvironmentVariables();
	}


	private static Configuration cfg;

	public NodeList tests;
	private static TestPaths testPaths = new TestPaths();
	private static Path testJCLDirectory;

	private static HeldJobs heldJobs;

	private static String appname = "GRCG";

	public static boolean processSpecList() {
		try {
			initFreeMarkerConfiguration();
			setupTestPaths();
			checkExistsAndProcessSpeclist();
			if (envVariablesAreValid()) {
				putEventFiles();
			} else {
				logger.atSevere().log("Invalid variable values");
			}
		} catch (Exception e) {
			logger.atSevere().log("Exception in testing");
			e.printStackTrace();
		}

		// if we ran tests run overview no matter what
		return GersEnvironment.get("RUNTESTS").compareToIgnoreCase("Y") == 0;
	}

	private static void putEventFiles() throws Exception {
		String testHLQ = GersEnvironment.get("GERS_TEST_HLQ");
		if(testHLQ.equalsIgnoreCase("LOCAL")) {
        	logger.atInfo().log("Local testing");
		} else {
			String destHLQ = testHLQ + ".INPUT";
			logger.atInfo().log("Write Event Files to " + destHLQ);
			List<EventFile> eventFiles = getFileInfo(testPaths.getEventPath());
			if(GersEnvironment.get("OSNAME").startsWith("Win")) {
				logger.atInfo().log("Ignore put event files for Windows");
			} else {
				for (EventFile ef : eventFiles) {
					Path input = testPaths.getEventPath().resolve("data").resolve(ef.getName());
					ZosHelper.putEventFile(destHLQ, input.toFile(), ef.getName(), ef.getLrecl());
				}
			}
		}
	}

	private static void setupTestPaths() {
		Path root = Paths.get(GersEnvironment.get(LOCALROOT));
		testPaths.setRoot(root);
		testPaths.setSpecDirPath(root.resolve("spec"));
		testPaths.setJclPath(root.resolve("jcl"));
		testPaths.setConfigPath(root.resolve("Config"));
		testPaths.setTemplateSetDir(root.resolve("templateSets"));
		testPaths.setEventPath(root.resolve("event"));
		TestDataGenerator.setTestPaths(testPaths);
	}

	private void clearGeneratedJunit() {
		try {
			FileProcessor.deleteRecursive(new File(GersEnvironment.get(LOCALROOT) + File.separator + TEST_SRC));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearNonJunitGeneratedFiles() {
		try {
			FileProcessor.deleteRecursive(new File(GersEnvironment.get(LOCALROOT) + File.separator + "tmp"));
			FileProcessor.deleteRecursive(new File(GersEnvironment.get(LOCALROOT) + File.separator + "jcl"));
			FileProcessor.deleteRecursive(new File(GersEnvironment.get(LOCALROOT) + File.separator + "cfg"));
			FileProcessor.deleteRecursive(new File(GersEnvironment.get(LOCALROOT) + File.separator + "out"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean envVariablesAreValid() {
		boolean valid = true;
		if (GersEnvironment.get("GERS_TEST_HLQ").isEmpty()) {
			logger.atSevere().log("GERS_TEST_HLQ is empty. We need an HLQ");
			valid = false;
		}
		if (GersEnvironment.get("GERS_ENV_HLQ").isEmpty()) {
			logger.atSevere().log("GERS_ENV_HLQ is empty. We need an HLQ");
			valid = false;
		}
		return valid;
	}

	private static void checkExistsAndProcessSpeclist() throws Exception {
		String specFileListName = GersEnvironment.get("GERS_TEST_SPEC_LIST");
		if (specFileListName != null) {
			File specFileList = new File(GersEnvironment.get(LOCALROOT) + File.separator + specFileListName);
			if (!specFileList.exists()) {
				logger.atSevere().log("Specfile list doesn't exist " + specFileList);
			} else {
				processYamlSpecFileList(specFileList);
			}
		}
	}

	private static void processYamlSpecFileList(File specFileList) {
		TestRepository.buildTheRepo(specFileList);
	}

	private static void initFreeMarkerConfiguration() throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_30);
		cfg.setDirectoryForTemplateLoading(new File(GersEnvironment.get(LOCALROOT) + "/FreeMarkerTemplates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		TestDataGenerator.setFreemarkerConfig(cfg);
	}


	public String getVersion() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		String ver = "";
		try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
			properties.load(resourceStream);
			ver = properties.getProperty("app.name") + ": " + properties.getProperty("build.version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ver;
	}

	public static void runTest(GersTest testToRun) throws IOException {
        logger.atInfo().log("Running test '%s'", testToRun.getName());

		String testHLQ = GersEnvironment.get("GERS_TEST_HLQ");
		String testDataset = testHLQ + "." + testToRun.getDataSet();
		TestDataGenerator.applyTemplatesToTest(testToRun);
		if ( testHLQ.equalsIgnoreCase("LOCAL"))  {
			runLocalTest(testToRun);
			return;
		} else if(GersEnvironment.get("OSNAME").startsWith("Win")) {
			System.out.println("delete existing test datasets " + testDataset);
			System.out.println("Run " + testToRun.getFullName());
			return;
		} else {
			// ZosHelper.deleteDataSet(testDataset);
		}

		Path xmlDir = buildTheXMLFiles(testToRun);
		copyXMLFilesToPDS(xmlDir, testDataset + "." + appname + ".XMLS");

		copyTheConfigFilesToPDS(testToRun, testDataset + ".PARM");
		testJCLDirectory = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("jcl").resolve(testToRun.getFullName());
		copyTheJCLToPDS(testToRun, testDataset + ".JCL");

		purgeOldJobs(testToRun);
		submitJobsAndWaitForCompletion(testToRun);
		runComparePhaseIfNeeded(testToRun);
		if(heldJobs.maxReturnCodeDoesNotExceed(JES_WARNING)) {
			getExpectedOutputs(testToRun);
		} else {
			if(heldJobs.getMaxRC() == testToRun.getExpectedresult().getRcAsInt()) {
				logger.atInfo().log("Expect RC %d found", testToRun.getExpectedresult().getRcAsInt() );
				getExpectedErrorOutputs(testToRun);
			}
		}
		processResult(testToRun);
	}

	private static void runLocalTest(GersTest testToRun) throws IOException {
		//ah build XMLs above is wrong because of 1047
		//create a dir to execute test
		Path localTest = createLocalTestDirectory(testToRun);
		Path outxmlPath = localTest.resolve("WBXMLI");
		outxmlPath.toFile().mkdirs();
		logger.atInfo().log("Make dir " + outxmlPath.toString());
		Path xmlfile;
		List<Substitution> substs = new ArrayList<Substitution>();
		for (XMLFile xml : testToRun.getXmlfiles()) {
			xmlfile = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("xml").resolve(xml.getName());

			for (Replacement r : xml.getReplacements()) {
				substs.add(new Substitution(r.getReplace(), r.getWith()));
			}
			FileProcessor.sed(xmlfile.toFile(), outxmlPath.resolve(xmlfile.getFileName()).toFile(), substs);
			substs.clear();
		}
		Path configFolder = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("Config").resolve(testToRun.getFullName());
		Files.copy(configFolder.resolve(appname + "PARM"), localTest.resolve(appname + "PARM.cfg"), StandardCopyOption.REPLACE_EXISTING);
		//copy config
		//Create WBXMLI
		//copy XML to above
		//cd to or run gvbrcg from test dir 
		CommandRunner cr = new CommandRunner();
		try {
			String mr91String;
			String rcaString;
			if(GersEnvironment.get("OSNAME").startsWith("Win")) {
				mr91String = "gvbrcg.bat";
				rcaString = "gvbrca.bat";
			} else {
				mr91String = "gvbrcg";
				rcaString = "gvbrca";
			}
			cr.run(mr91String, localTest.toFile());
			logger.atInfo().log(cr.getCmdOutput().toString());
			cr.clear();
			cr.run(rcaString, localTest.toFile());
			logger.atInfo().log(cr.getCmdOutput().toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// dittto gvbrca
		processLocalResult(testToRun);

		//how to we check a pretend pass? Just return code?
	}

	private static void processLocalResult(GersTest test) {
		Path rootPath = Paths.get(GersEnvironment.get(LOCALROOT));
		Path outPath = rootPath.resolve("out");
		// Look to see VDP and XLT generated -> Gennerated/Pass
		Path resultFolder = outPath.resolve(test.getFullName());
		Path vdp = resultFolder.resolve("VDP");
		Path xlt = resultFolder.resolve("XLT");
		logger.atInfo().log("Check existenc in " + resultFolder.toString());
		if (vdp.toFile().exists() && xlt.toFile().exists()) {
			test.getResult().setMessage("generated");
		} else {
			test.getResult().setMessage("no outputs");
		}

		Map<String, Object> nodeMap = new HashMap<>();
		nodeMap.put("specName", test.getSpecPath());
		nodeMap.put("testName", test.getName());

		try {
			Template template = cfg.getTemplate("test/localResult.ftl");
			Path resultFilePath;
			Path resultPath = outPath.resolve(test.getFullName());
			resultPath.toFile().mkdirs();
			if (test.getResult().getMessage().startsWith("generated")) {
				nodeMap.put("result", "SUCCESS");
				logger.atInfo().log(Menu.GREEN + test.getResult().getMessage() + Menu.RESET);
				resultFilePath = resultPath.resolve("pass.html");
			} else {
				nodeMap.put("result", "FAILCOMPARE");
				logger.atSevere().log(Menu.RED + test.getResult().getMessage() + Menu.RESET);
				resultFilePath = resultPath.resolve("fail.html");
			}
			nodeMap.put("outFiles", test.getFormatfiles());
			Path cssPath = resultFilePath.relativize(outPath).resolve("w3.css");
			nodeMap.put("cssPath", cssPath);
			TemplateApplier.generateTestTemplatedOutput(template, nodeMap, resultFilePath);
		} catch (IOException | TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Path createLocalTestDirectory(GersTest testToRun) {
		Path localTest = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("out").resolve(testToRun.getFullName());
		localTest.toFile().mkdirs();
		return localTest;
	}

	private static void getExpectedErrorOutputs(GersTest testToRun) {
		if(testToRun.getErrorfiles().size() > 0) {
			for(OutputFile ef : testToRun.getErrorfiles()) {
				logger.atInfo().log("get %s from last job", ef.getDdname());
				Path outFile = getOutFilePathForTest(ef.getDdname(), testToRun);
				String outdata = heldJobs.getDatasetFromLastJob(ef.getDdname());
				writeOutput(outdata, outFile);
			}
		} else {
			logger.atInfo().log("No expected error files");
		}
	}

	private static void writeOutput(String outdata, Path outFile) {
		try( FileWriter fw = new FileWriter(outFile.toFile()) ) {
			logger.atInfo().log("write to " + outFile.toString());
			fw.append(outdata);
			fw.close();
		} catch (IOException e) {
			logger.atSevere().log(e.getMessage());
		}
	}

	private static void runComparePhaseIfNeeded(GersTest testToRun) throws IOException {
		if (isOkayToContinue(testToRun)) {
			if (testToRun.hasComparePhase()) {
				runComparePhase(testToRun);
			}
		} else {
			testToRun.getResult().setMessage(heldJobs.getResultRC());
		}
	}

	private static boolean isOkayToContinue(GersTest testToRun) {
		return heldJobs.allJobsCompleted(testToRun.getNumExpectedJobs()) && heldJobs.maxReturnCodeDoesNotExceed(JES_WARNING);
	}

	private static void runComparePhase(GersTest testToRun) throws IOException {
		logger.atInfo().log("Start compare phase");
		String compPrefix = testToRun.getName() + "G";
		Path superCJob = testJCLDirectory.resolve(compPrefix);
		submitJobs(superCJob);
		int numExpectedJobs = 1;
		if( testToRun.getFormatfiles().size() > 0) {
			numExpectedJobs = testToRun.getExtractfiles().size();
		}
		waitForJobsWithTimeoutAndCollectFailures(compPrefix, numExpectedJobs, Integer.valueOf(testToRun.getTimeout()));
		comparePhaseCheck(testToRun);
	}


	private static void waitForJobsWithTimeoutAndCollectFailures(String compPrefix, int numExpectedJobs, Integer timeout) {
		waitForJobsWithTimeout(compPrefix, numExpectedJobs, timeout);
		heldJobs.collectFailures();
	}

	private static void comparePhaseCheck(GersTest testToRun) {
		List<ISFJobStep> steps = heldJobs.getJobSteps();	
		if(heldJobs.getMaxRC() == 0) {
			testToRun.getResult().setMessage("pass compare phase");
		} else {
			testToRun.getResult().setMessage("fail compare phase");
			heldJobs.showFailedSteps();
		}
		saveStepResult(steps, testToRun);
	}

	private static void saveStepResult(List<ISFJobStep> steps, GersTest testToRun) {
		//Need to mark the failed and passed based on the contents of the heldJobs failedsteps ddname
		for(ISFJobStep s : steps) {
			String name = s.getStepName();
			//Could this be done earlier as the test is run?
			//Or init all to passed and then fail those that do fail
			//since a map and can find
			//!! negative logic here !!!
			testToRun.setViewResult(Integer.parseInt(name.substring(1)), heldJobs.didStepPass(name));
		}
	}

	private static void copyTheJCLToPDS(GersTest testToRun, String pds) {
		logger.atInfo().log("Copy the JCL files to %s", pds);
		deletePdsIfExists(pds);
		if (GersEnvironment.get("OSNAME").startsWith("z")) {
			for (File f : testJCLDirectory.toFile().listFiles()) {
				ZosHelper.convertA2EAndCopyFile2Dataset(f, "//'" + pds + "(" + f.getName() + ")'", "fb", "80");
			}
		}
	}

	private static void copyTheConfigFilesToPDS(GersTest testToRun, String pds) {
		logger.atInfo().log("Copy the config files to %s", pds);
		deletePdsIfExists(pds);
		if (GersEnvironment.get("OSNAME").startsWith("z")) {
			Path configFolder = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("Config").resolve(testToRun.getFullName());
			for (File f : configFolder.toFile().listFiles()) {
				ZosHelper.convertA2EAndCopyFile2Dataset(f, "//'" + pds + "(" + f.getName() + ")'", "fb", "80");
			}
		}
	}

	private static void copyXMLFilesToPDS(Path xmlDir, String pds) {
		logger.atInfo().log("Copy the XML to %s", pds);
		deletePdsIfExists(pds);
		if (GersEnvironment.get("OSNAME").startsWith("z")) {
			for (File f : xmlDir.toFile().listFiles()) {
				ZosHelper.convertA2EAndCopyFile2Dataset(f, "//'" + pds + "(" + f.getName() + ")'", "vb", "1000");
			}
		}
	}

	private static void deletePdsIfExists(String pds) {
		String dataset = "//'" + pds  + "'";
		try {
			if(ZFile.dsExists(dataset)) {
				ZFile.remove(dataset);
			}
		} catch (ZFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Path buildTheXMLFiles(GersTest testToRun) throws IOException {
		Path xmlFolder = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("xmlout").resolve(testToRun.getFullName());
		xmlFolder.toFile().mkdirs();

		List<File> filesIn = new ArrayList<File>();
		int xmlNum = 1;
		List<Substitution> substs = new ArrayList<Substitution>();
		for (XMLFile xml : testToRun.getXmlfiles()) {
			File xmlfile = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("xml").resolve(xml.getName()).toFile();
			filesIn.add(xmlfile);
			File outxmlfile = xmlFolder.resolve("XML" + xmlNum++).toFile();
			substs.add(new Substitution("?>", " encoding=\"IBM-1047\"?>", 1, 1));
			for (Replacement r : xml.getReplacements()) {
				substs.add(new Substitution(r.getReplace(), r.getWith()));
			}
			FileProcessor.sed(xmlfile, outxmlfile, substs);
			substs.clear();
		}
		return xmlFolder;
	}

	public static void runSpec(Spec specToRun) {
		boolean specPassed = true;
		for( GersTest t : specToRun.getTests()) {
			try {
                t.setSpecPath(specToRun);
				runTest(t);
				specPassed &= t.getResult().getMessage().startsWith("pass") ? true : false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(specPassed) {
			specToRun.getResult().setMessage("pass");
		} else {
			specToRun.getResult().setMessage("fail");
		}
	}

	private static void submitJobsAndWaitForCompletion(GersTest testToRun) throws IOException {
		Path firstJob = testJCLDirectory.resolve(testToRun.getName() + (testToRun.getDb2bind().equals("Y") ? "N" : "L"));
		if (GersEnvironment.get("OSNAME").startsWith("Win")) {
			System.out.println("Submit " + firstJob.toString());
		} else {
			submitJobs(firstJob);
		}
		waitForJobsWithTimeout(testToRun.getName(), testToRun.getNumExpectedJobs(), Integer.valueOf(testToRun.getTimeout()));
	}

	private static void submitJobs(Path mr91Job) throws IOException {
		String jobname = null;
		MvsJobSubmitter jobSubmitter = new MvsJobSubmitter();
		BufferedReader rdr = FileFactory.newBufferedReader(mr91Job.toString());
		try {
			String line;
			while ((line = rdr.readLine()) != null) {
				if (jobname == null) {
					StringTokenizer tok = new StringTokenizer(line);
					String jobToken = tok.nextToken();

					if (jobToken.startsWith("//")) {
						jobname = jobToken.substring(2);
					}
				}
				jobSubmitter.write(line);
			}
		} finally {
			if (rdr != null) {
				rdr.close();
			}
		}
		// Submits the job to the internal reader
		jobSubmitter.close();
	}

	private static void purgeOldJobs(GersTest testToRun) {
		TestJobs tjs = new TestJobs(testToRun.getName());
		tjs.run();
		int p =tjs.purge();
		logger.atInfo().log("purged %d jobs for %s",p, testToRun.getName());
	}

	private static void waitForJobsWithTimeout(String prefix, int expectedNumJobs, int timeout) {
		heldJobs = new HeldJobs(prefix);
		int numCompleted = 0;
		int timeoutCount = 2;
		logger.atInfo().log("Expecting %d completed jobs for %s within %d seconds", expectedNumJobs, prefix, timeout);
		snooze(2500); //Most tests seem to run within this time
		int rc = 0;
		while(numCompleted < expectedNumJobs && rc < 8 && timeoutCount < timeout) {
			heldJobs.run();
			numCompleted = heldJobs.getNumJobs();
			//could also look for abend/fail rc?
			rc = heldJobs.getMaxRC();
			if(rc > 4) {
				logger.atInfo().log("(%d) RC %d for %s - %s", timeoutCount, rc, prefix, heldJobs.getResultRC());
			} else {
				logger.atInfo().log("(%d) %d completed jobs for %s", timeoutCount, numCompleted, prefix);
			}
			snooze(1000);
			timeoutCount++;
		}
		if(System.getenv("SHOWJOBS") != null && System.getenv("SHOWJOBS").equals("Y")) {
			heldJobs.show();
		}
		if(timeoutCount == timeout) {
			logger.atSevere().log("%s timed out. Held jobs are:", prefix);
			heldJobs.show();
		}
	}

	private static boolean getExpectedOutputs(GersTest testToRun) {
		boolean found = true;
		if (testToRun.getRunOnly().equals("Y") || testToRun.getComparephase().equalsIgnoreCase("Y")) {
			found = true; //No output files to get
		} else {
			String outFile = null;
			String datasetBase = "//'" + GersEnvironment.get("GERS_TEST_HLQ") + "." + testToRun.getDataSet() + ".";
			String dataset;
			if (testToRun.getFormatfiles().size() > 0) {
				for (OutputFile f : testToRun.getFormatfiles()) {
					if (f.getDsn() != null) {
						dataset = f.getDsn();
						logger.atInfo().log("Look for format file " + f.getFilename() + " dsn " + dataset);
					} else {
						outFile = "OUTF.MR88." + f.getDdname();
						dataset = datasetBase + outFile + "'";
						logger.atInfo().log("Look for format file " + dataset);
					}
					getOutputFile(dataset, f, outFile, testToRun);
				}
			} else {
				if (testToRun.getExtractfiles().size() > 0) {
					for (OutputFile f : testToRun.getExtractfiles()) {
						if (f.getComparable().equalsIgnoreCase("Y")) {
							if (f.getDsn() != null) {
								dataset = f.getDsn();
								logger.atInfo().log("Look for extract file " + f.getFilename() + " dsn " + dataset);
							} else {
								outFile = "OUTE.MR95." + f.getDdname();
								dataset = datasetBase + outFile + "'";
								logger.atInfo().log("Look for extract file " + dataset);
							}
							getOutputFile(dataset, f, outFile, testToRun);
						}
					}
				}
			}
		}
		return found;
	}


	private static void getOutputFile(String dataset, OutputFile f, String fileName, GersTest testToRun) {
		Path outFile = getOutFilePathForTest(fileName, testToRun);
		if (GersEnvironment.get("OSNAME").startsWith("Win")) {
			System.out.println("Get output file from " + dataset + " to " + outFile.toFile().toString() + " :" + f.getRecfm() + "," + f.getLrecl());
		} else {
			if(ZosHelper.convertE2AAndCopyDataset2File(dataset, outFile.toFile(), f.getRecfm(), f.getLrecl()) == false) {
				//try again it may have been busy
				snooze(1000);
				logger.atInfo().log("retrying copy");
				ZosHelper.convertE2AAndCopyDataset2File(dataset, outFile.toFile(), f.getRecfm(), f.getLrecl());
			}
		}
	}

	private static Path getOutFilePathForTest(String fileName, GersTest testToRun) {
		Path testDirectory = Paths.get(testToRun.getFullName());
		Path outFolder = Paths.get(GersEnvironment.get("LOCALROOT")).resolve("out").resolve(testDirectory);
		outFolder.toFile().mkdirs();
		Path outFile = outFolder.resolve(fileName);
		return outFile;
	}

	private static void snooze(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // 1 second
	}

	private static boolean outputExists(String dataset) {
		boolean found = false;
		try {
			logger.atInfo().log("check %s exists", dataset);
			found = ZFile.exists(dataset);
		} catch (ZFileException e) {
			e.printStackTrace();
		}
		return found;
	}

	protected static void processResult(GersTest test) {
		Path rootPath = Paths.get(GersEnvironment.get(LOCALROOT));
		Path outPath = rootPath.resolve("out");
		Path baseFolder = rootPath.resolve("base").resolve(test.getFullName());
		if(test.getRunOnly().equals("Y")) {
			System.out.println(Menu.GREEN + "Run Only " + test.getName() + Menu.RESET);
		} else {
		//Get actual result
			if (test.hasComparePhase()) {
			} else {
				if (heldJobs.allJobsCompleted(test.getNumExpectedJobs())) {
						if (heldJobs.getMaxRC() > test.getExpectedresult().getRcAsInt()) {
							test.getResult().setMessage(heldJobs.getResultRC());
						} else {
							if (compareOutFiles(baseFolder, test, outPath)) {
								test.getResult().setMessage("pass");
							} else {
								test.getResult().setMessage("fail compare");
							}
						}
				} else if (heldJobs.getNumJobs() < test.getNumExpectedJobs()) {
					if (heldJobs.getMaxRC() > 8) {
						test.getResult().setMessage(heldJobs.getResultRC());
					} else {
						test.getResult().setMessage("fail timedout");
					}
				}
			}

			test.verifyExpected();


			// Messed up world of paths
			// let's make them once somewhere and keep them around

			// the testOutputPath
			// the testBasePath
			// And the story is a little messed up with extract or format file too
			// Needs different prefix but we should be able to just say getOuptutFileName()
			// and it sorts the type etc internally?

			// Not sure we need the TestResult types either?

			Map<String, Object> nodeMap = new HashMap<>();
			nodeMap.put("specName", test.getSpecPath());
			nodeMap.put("testName", test.getName());

			try {
				Template template = cfg.getTemplate("test/result.ftl");
				Path resultFilePath;
				Path resultPath = outPath.resolve(test.getFullName());
				resultPath.toFile().mkdirs();
				if (test.getResult().getMessage().startsWith("pass")) {
					nodeMap.put("result", "SUCCESS");
					logger.atInfo().log(Menu.GREEN + test.getResult().getMessage() + Menu.RESET);
					resultFilePath = resultPath.resolve("pass.html");
				} else {
					nodeMap.put("result", "FAILCOMPARE");
					logger.atSevere().log(Menu.RED + test.getResult().getMessage() + Menu.RESET);
					resultFilePath = resultPath.resolve("fail.html");
				}
				nodeMap.put("outFiles", test.getFormatfiles());
				Path cssPath = resultFilePath.relativize(outPath).resolve("w3.css");
				nodeMap.put("cssPath", cssPath);
				TemplateApplier.generateTestTemplatedOutput(template, nodeMap, resultFilePath);
			} catch (IOException | TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean compareOutFiles(Path baseFolder, GersTest test, Path outPath) {
		boolean allMatch = true; // if there was a comparephase comparison has been done
		Iterator<OutputFile> ofi = test.getOutputFileIterator();
		while (ofi.hasNext()) {
			allMatch &= outputFilesMatch(ofi.next(), baseFolder, test, outPath);
		}
		return allMatch;
	}

	private static boolean outputFilesMatch(OutputFile of, Path baseFolder, GersTest test, Path outPath) {
		boolean diffFound = false;
		String outputFileName = test.getOutputFilePrefix() + of.getDdname();
		if(of.getComparable().equalsIgnoreCase("y")) {
			Path basePath = baseFolder.resolve(outputFileName);
			if (!basePath.toFile().exists()) {
				logger.atSevere().log("Base file does not exist " + basePath.toString());
			} else {
				Path outFilePath = outPath.resolve(test.getFullName()).resolve(outputFileName);
				Path diffPath = outPath.resolve(test.getFullName()).resolve(outputFileName + ".diff");
				try {
					FileProcessor.toPrintableFile(outFilePath.toFile());
					if(of.getStartkey() != null) {
						logger.atInfo().log("Keyed diff");
						diffFound = FileProcessor.diff(basePath.toFile(), outFilePath.toFile(), diffPath.toFile(), of.getStartkey(), of.getStopkey(), false);
					} else {
						diffFound = FileProcessor.diff(basePath.toFile(), outFilePath.toFile(), diffPath.toFile(), 0, 100000, true) ;
					}
					if(diffFound){
						System.out.println(Menu.RED + "ERROR differences found for " + outFilePath.toString() + Menu.RESET);
					} else {
						System.out.println(Menu.GREEN + "PASS for " + outFilePath.toString() + Menu.RESET);
					}
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return !diffFound;
	}

    public static void runAllTests() {
		Iterator<Spec> si = TestRepository.getSpecIterator();
		while(si.hasNext()) {
			runSpec(si.next());
		}
    }

	private static List<EventFile> getFileInfo(Path path)  {
		List<EventFile> eventFiles = new ArrayList<EventFile>();
		try (BufferedReader fr = new BufferedReader(new FileReader(path.resolve("datasetParms.txt").toFile()))) {
			String line;
			while ((line = fr.readLine()) != null) {
				if(!line.startsWith("#")) {
					String[] bits = line.split(",");
					EventFile eventFile = new EventFile();
					eventFile.setName(bits[0]);
					eventFile.setLrecl(bits[1]);
					eventFiles.add(eventFile);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventFiles;
	}	

}
