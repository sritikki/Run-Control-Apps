package org.genevaers.runcontrolanalyser.ltcoverage;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TypeHitMap {
    //Can we key this on the enum?
    private Map<String, Integer> typeHits = new TreeMap<>();

    public void hit(String dts) {
        //Integers are immutable so we need to mess about
        Integer h = typeHits.get(dts);
        if(h == null) {
            h = Integer.valueOf(1);
        } else {
            h++;
        }
        typeHits.put(dts, h);
    }

    //DataType does not distinguish between the different Bin lengths

    public Iterator<Entry<String, Integer>> getTypeHitsIterator() {
        return typeHits.entrySet().iterator();
    }

    public Map<String, Integer> getTypeHits() {
        return typeHits;
    }

}
