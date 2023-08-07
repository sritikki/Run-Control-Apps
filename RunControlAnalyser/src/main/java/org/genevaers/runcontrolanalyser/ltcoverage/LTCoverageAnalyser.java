package org.genevaers.runcontrolanalyser.ltcoverage;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.genevaers.genevaio.ltfactory.LtFunctionCodeCache;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.components.enums.LtRecordType;


public class LTCoverageAnalyser extends LtFunctionCodeCache{

    private Map<String, LtCoverageEntry> coverageMap;
    //Static?
    private LtCoverageYamlWriter yamlWriter = new LtCoverageYamlWriter(); 

    public LTCoverageAnalyser() {
        super();
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
        if(coverageMap == null) {
            coverageMap = new TreeMap<>();
        }
        coverageMap.put(name, ce);
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
        coverageMap.get(ltr.getFunctionCode()).hit(ltr);
    }

    public void writeCoverageHTML(Path output) {
         LtCoverageHtmlWriter.writeTo(output, coverageMap);
    }

    public void writeCoverageYAML(Path output) {
        // addRecordFieldToYamlTree();
        // writeYaml(output);
        yamlWriter.writeTo(output, coverageMap);
    }


}
