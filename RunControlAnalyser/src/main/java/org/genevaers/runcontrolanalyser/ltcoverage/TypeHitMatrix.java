package org.genevaers.runcontrolanalyser.ltcoverage;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class TypeHitMatrix {
    //Can we key this on the enum?
    private Map<String, TypeHitMap> typeHitMatrix = new TreeMap<>();

    public void hit(String dts1, String dts2) {
        TypeHitMap thm = typeHitMatrix.computeIfAbsent(dts1, d -> getTypeHits(d));
        thm.hit(dts2.toString());
    }

    private TypeHitMap getTypeHits(String d) {
        return new TypeHitMap();
    }

    public Iterator<Entry<String, TypeHitMap>> getIterator() {
        return typeHitMatrix.entrySet().iterator();
    }

    public Map<String, TypeHitMap> getTypeHitMatrix() {
        return typeHitMatrix;
    }
}
