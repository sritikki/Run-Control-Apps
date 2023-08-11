package org.genevaers.runcontrolanalyser.ltcoverage;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF2;

public class LTCoverageEntry2Args extends LtCoverageEntry{

    private Map<String, TypeHits> typeHitsMatrix = new TreeMap<>();

    @Override
    public void hit(LTRecord ltr) {
        super.hit(ltr);
        hit(((LogicTableF2)ltr).getArg1(), ((LogicTableF2)ltr).getArg2());;

    }

    public void hit(LogicTableArg arg1, LogicTableArg arg2) {
        String dts1 = getTypeName(arg1.isSignedInd(), arg1.getFieldFormat(), arg1.getFieldLength());
        String dts2 = getTypeName(arg2.isSignedInd(), arg2.getFieldFormat(), arg2.getFieldLength());
        TypeHits th = typeHitsMatrix.computeIfAbsent(dts2, d -> makeTypeHits(dts1));
        th.hit(dts2);
    }

    private TypeHits makeTypeHits(String dts1) {
        return new TypeHits(dts1, 0);
    }

    public Map<String, TypeHits> getTypeHitsMatrix() {
        return typeHitsMatrix;
    }

    public void setTypeHitsMatrix(Map<String, TypeHits> typeHitsMatrix) {
        this.typeHitsMatrix = typeHitsMatrix;
    }

    public Iterator<Entry<String, TypeHits>> getTypeMatrixIterator() {
        return typeHitsMatrix.entrySet().iterator();
    }
    
}
