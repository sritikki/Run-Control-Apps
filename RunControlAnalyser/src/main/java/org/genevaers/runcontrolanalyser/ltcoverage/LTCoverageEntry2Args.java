package org.genevaers.runcontrolanalyser.ltcoverage;

import java.util.Iterator;
import java.util.Map.Entry;

import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF2;

public class LTCoverageEntry2Args extends LtCoverageEntry{

    private TypeHitMatrix typeMatrix = new TypeHitMatrix();

    @Override
    public void hit(LTRecord ltr) {
        super.hit(ltr);
        hit(((LogicTableF2)ltr).getArg1(), ((LogicTableF2)ltr).getArg2());;

    }

    public void hit(LogicTableArg arg1, LogicTableArg arg2) {
        String dts1 = getTypeName(arg1.isSignedInd(), arg1.getFieldFormat(), arg1.getFieldLength());
        String dts2 = getTypeName(arg2.isSignedInd(), arg2.getFieldFormat(), arg2.getFieldLength());
        typeMatrix.hit(dts1, dts2);
    }

    public Iterator<Entry<String, TypeHitMap>> getTypeMatrixIterator() {
        return typeMatrix.getIterator();
    }

    public TypeHitMatrix getTypeMatrix() {
        return typeMatrix;
    }

    
}
