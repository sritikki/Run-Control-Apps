package org.genevaers.runcontrolanalyser;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.genevaers.genevaio.ltfile.LTLogger;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.XLTFileReader;
import org.genevaers.runcontrolanalyser.ltcoverage.LTCoverageAnalyser;
import org.genevaers.utilities.CommandRunner;
import org.genevaers.utilities.FTPSession;


public class AnalyserDriver {

	/**
	 *
	 */
	private static final String TO = "' to ";
	/**
	 *
	 */
	private static final String FTP_GET = "FTP Get ";
	private RunControlAnalyser fa = new RunControlAnalyser();
	private Object cwd;
	private Path dataStore;
	private LTCoverageAnalyser ltCoverageAnalyser = new LTCoverageAnalyser();

	public void ftpRunControlDatasets(String host, String dataset, String rc, String  user, String password) throws IOException {

		FTPSession session = new FTPSession(host);
		session.setUserAndPassword(user, password);
		session.connect();
		Path newcwd = Paths.get(rc);
		newcwd.toFile().mkdirs();
		String vdpDataset = dataset + ".VDP";
		String xltDataset = dataset + ".XLT";
		String jltDataset = dataset + ".JLT";
		Path vdpPath = Paths.get(rc).resolve("VDP");
		Path xltPath = Paths.get(rc).resolve("XLT");
		Path jltPath = Paths.get(rc).resolve("JLT");
		session.setRDW();
		session.setBinary();
		System.out.println(FTP_GET + vdpDataset + TO
				+ vdpPath.toString());
		session.getFile("'" + dataset + ".VDP" + "'", vdpPath.toFile());
		System.out.println(FTP_GET + xltDataset + TO
				+ xltPath.toString());
		session.getFile("'" + dataset + ".XLT" + "'", xltPath.toFile());
		System.out.println(FTP_GET + jltDataset + TO
				+ jltPath.toString());
		session.getFile("'" + dataset + ".JLT" + "'", jltPath.toFile());
		System.out.println("FTP transfers complete");
		session.disconnect();
	}

	public void generateFlowDataFrom(String baseFileName, boolean withCSV, boolean noBrowse, String joinsFilter) throws Exception {
		fa.generateFlowDataFrom(baseFileName, withCSV, noBrowse, joinsFilter);
	}

	public void setTargetDirectory(String trg) {
		Path trgPath = dataStore.resolve(trg);
		if(trgPath.toFile().exists() == false) {
			trgPath.toFile().mkdirs();
		}
		fa.setTargetDirectory(trgPath);
	}

	public void listRunControlDataSets() {
		WildcardFileFilter fileFilter = new WildcardFileFilter("*.VDP");
		Collection<File> rcFiles = FileUtils.listFiles(fa.getCurrentWorkiingDirectory().toFile(), fileFilter, DirectoryFileFilter.INSTANCE);
		
		for(File rc : rcFiles) {
			System.out.println(rc.toString());
		}
	}

	public void openDataStore() {
		try {
			//On a mac this will be different
			Runtime.getRuntime().exec("explorer.exe /root," + dataStore.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeRunControlAnalyserDataStore(Path root) {
		if(root == null) {
			String home = System.getProperty("user.home");
			//locroot = locroot.replaceAll("^[Cc]:", "");
			home = home.replace("\\", "/");
			Path homePath = Paths.get(home);
			dataStore = homePath.resolve(".gersflows");
		} else {
			dataStore = root;
		}
		if(dataStore.toFile().exists() == false) {
			dataStore.toFile().mkdirs();
		}
	}

	public Path getDataStore() {
		return dataStore;
	}

	public void openHtmlFor(String rcSet)  {
		Path showme = dataStore.resolve(rcSet).resolve("gersrca.html");
		CommandRunner cmdRunner = new CommandRunner();
		try {
			cmdRunner.run("cmd /C " + showme.toString(), showme.getParent().toFile());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getRcSets() {
		return dataStore.toFile().list();
	}

	public void generateXltPrint(Path root, boolean withCoverage) {
		XLTFileReader xltr = new XLTFileReader();
		xltr.open(root.resolve("XLT").toString());
		LogicTable xlt = xltr.makeLT();
        LTLogger.logRecords(xlt);
        LTLogger.writeRecordsTo(xlt, root.resolve("rca").resolve("xltrpt.txt"));
		if(withCoverage) {
			collectCoverageData(xlt);
		}
	}

    private void collectCoverageData(LogicTable xlt) {
		ltCoverageAnalyser.addDataFrom(xlt);
	}

	public void generateJltPrint(Path root, boolean withCoverage) {
		XLTFileReader jltr = new XLTFileReader();
		jltr.open(root.resolve("JLT").toString());
		LogicTable jlt = jltr.makeLT();
        LTLogger.logRecords(jlt);
        LTLogger.writeRecordsTo(jlt, root.resolve("rca").resolve("jltrpt.txt"));
    }

	public void writeCoverageHTML(Path root) {
		ltCoverageAnalyser.writeCoverageHTML(root.resolve("rca").resolve("ltcov.html"));
		ltCoverageAnalyser.writeCoverageYAML(root.resolve("rca").resolve("ltcov.yaml"));
	}

}
