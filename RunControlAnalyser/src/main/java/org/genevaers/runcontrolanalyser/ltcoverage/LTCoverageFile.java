package org.genevaers.runcontrolanalyser.ltcoverage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.genevaers.genevaio.ltfile.LTRecord;

public class LTCoverageFile {
    private String ltcoverage;
    private String source;
    private String generationDate;

    private Map<String, LtCoverageEntry> functionCodes = new TreeMap<>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    public String getLtcoverage() {
        return ltcoverage;
    }

    public void setLtcoverage(String ltcoverage) {
        this.ltcoverage = ltcoverage;
    }

    public Map<String, LtCoverageEntry> getFunctionCodes() {
        return functionCodes;
    }

    public void setFunctionCodes(Map<String, LtCoverageEntry> functionCodes) {
        this.functionCodes = functionCodes;
    }

    public void aggregateFrom(LTCoverageFile ltc) {
        Map<String, LtCoverageEntry> fcs = ltc.getFunctionCodes();
        //Not really an aggregate yet.
        //But want to get the coverage map so we can write it
        for(Entry<String, LtCoverageEntry> fce : fcs.entrySet()) {
            functionCodes.computeIfAbsent(fce.getKey(), e -> fce.getValue());
        }
    }

    public void addCoverageEntry(LtCoverageEntry ce) {
        functionCodes.put(ce.getName(), ce);
    }

    public void hit(LTRecord ltr) {
        functionCodes.get(ltr.getFunctionCode()).hit(ltr);
    }
}
