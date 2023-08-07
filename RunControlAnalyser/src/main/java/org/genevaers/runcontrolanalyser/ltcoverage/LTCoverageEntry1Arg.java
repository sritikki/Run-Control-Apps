package org.genevaers.runcontrolanalyser.ltcoverage;

import java.util.Iterator;
import java.util.Map.Entry;

import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableNameF1;
import org.genevaers.repository.components.enums.DataType;

public class LTCoverageEntry1Arg extends LtCoverageEntry{

    private TypeHitMap typeHits = new TypeHitMap();

    @Override
    public void hit(LTRecord ltr) {
       super.hit(ltr);
       if(ltr instanceof LogicTableF1) {
            DataType dt = ((LogicTableF1)ltr).getArg().getFieldFormat();
            String dts = getTypeName(((LogicTableF1)ltr).getArg().isSignedInd(), dt, ((LogicTableF1)ltr).getArg().getFieldLength());
            typeHits.hit(dts);
       } else {
            DataType dt = ((LogicTableNameF1)ltr).getArg().getFieldFormat();
            String dts = getTypeName(((LogicTableNameF1)ltr).getArg().isSignedInd(), dt, ((LogicTableNameF1)ltr).getArg().getFieldLength());
            typeHits.hit(dts);
       }
    }


    public Iterator<Entry<String, Integer>> getTypeHitsIterator() {
        return typeHits.getTypeHitsIterator();
    }

    public TypeHitMap getTypeHits() {
        return typeHits;
    }
    
}
