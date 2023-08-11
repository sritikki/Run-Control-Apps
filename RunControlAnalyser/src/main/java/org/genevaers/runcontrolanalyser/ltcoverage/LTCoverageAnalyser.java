package org.genevaers.runcontrolanalyser.ltcoverage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.file.AccumulatorPathVisitor;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.genevaers.genevaio.ltfactory.LtFunctionCodeCache;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.components.enums.LtRecordType;
import org.genevaers.utilities.GenevaLog;

import com.google.common.flogger.FluentLogger;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


public class LTCoverageAnalyser extends LtFunctionCodeCache{

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private static LTCoverageFile ltcov = new LTCoverageFile();
    private static LtCoverageYamlWriter yamlWriter = new LtCoverageYamlWriter(); 

    //Create a CoverageAnalyser that we can run from a root directory
    //It will then search for child ltcov yaml files and aggregate them
    //Probably better than trying to add as we go

    public LTCoverageAnalyser() {
        super();
    }

	public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException, InterruptedException {
		GenevaLog.formatConsoleLogger(LTCoverageAnalyser.class.getName(), Level.FINE);

		String locroot = System.getProperty("user.dir");
    	//locroot = locroot.replaceAll("^[Cc]:", "");
    	locroot = locroot.replace("\\", "/");
    	Path root = Paths.get(locroot);

        List<String> coverageFiles = new ArrayList<String>();
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(
            WildcardFileFilter.builder().setWildcards("pass.html").get(), FileFilterUtils.trueFileFilter());

        ltcov = new LTCoverageFile();
        ltcov.setSource("Aggregated Coverage");
        // Walk dir tree
        Files.walkFileTree(root, visitor);
        logger.atInfo().log("Found %d passed tests", visitor.getPathCounters().getFileCounter().get());
        for(Path p :visitor.getFileList()) {
            Path testPath = p.getParent();
            logger.atInfo().log(testPath.toString());     
            addToCoverageFrom(testPath.resolve("rca").resolve("ltcov.yaml"));
        }   
        yamlWriter.writeYaml(root.resolve("aggregateCov.yaml"), ltcov.getFunctionCodes());
        GenevaLog.closeLogger(LTCoverageAnalyser.class.getName());
	}


    private static void addToCoverageFrom(Path p) {
        LTCoverageFile ltc = LtCoverageYamlReader.readYaml(p);
        addToAggregate(ltc);
    }

    private static void addToAggregate(LTCoverageFile ltc) {
        ltcov.aggregateFrom(ltc);
    }

    public void addDataFrom(LogicTable lt) {
        Iterator<LTRecord> lti = lt.getIterator();
        while(lti.hasNext()) {
            LTRecord ltr = lti.next();
            hit(ltr);
        }
    }


    @Override
    public void addFunctionCode(String name, LtRecordType type, String desc, String category) {
        //LtCoverageEntry ce = new LtCoverageEntry();
        LtCoverageEntry ce = getCoverageEntryFromType(name, category, type);
        ce.setName(name);
        ce.setType(type);
        ce.setDescription(desc);
        ce.setCategory(category);
        ltcov.addCoverageEntry(ce);
    }

    private LtCoverageEntry getCoverageEntryFromType(String name, String category, LtRecordType type) {
        LtCoverageEntry ce = null;
        switch(type) {
            case CALC:
            case CC:
            case F0:
            case GENERATION:
            case HD:
            case INVALID:
            case NAME:
            case NAMEVALUE:
            case NV:
            case RE:
            case WR:
                ce = new LtCoverageEntry();
                break;
            case F1:
            case NAMEF1:
                ce = new LTCoverageEntry1Arg();
                if(category.equalsIgnoreCase("Arithmetic")) {
                    ce.setExpectedItems(15); //Maybe via catgory?
                } else {
                    ce.setExpectedItems(LtCoverageEntry.MAXTYPES);
                }
                break;
            case F2:
            case NAMEF2:
                ce = new LTCoverageEntry2Args();
                ce.setExpectedItems(226);
                break;
             default:
                break;
        }
        return ce;
    }

    public void hit(LTRecord ltr) {
        ltcov.hit(ltr);
    }

    public void writeCoverageHTML(Path output) {
        LtCoverageHtmlWriter.writeTo(output, ltcov.getFunctionCodes());
    }

    public void writeCoverageYAML(Path output) {
        LtCoverageYamlWriter.writeYaml(output, ltcov.getFunctionCodes());
     }

}
