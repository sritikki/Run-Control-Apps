package org.genevaers.runcontrolanalyser.ltcoverage;

public class TypeHits {
    //Can we key this on the enum?
    private String type;
    private int hits;

    public TypeHits() {
    }


    public TypeHits(String t, Integer h) {
        type = t;
        hits = h;
    }

    public void hit(String dts) {
        hits++;
    }

    //DataType does not distinguish between the different Bin lengths

    // public Iterator<Entry<String, Integer>> getTypeHitsIterator() {
    //     return typeHits.entrySet().iterator();
    // }

    // public Map<String, Integer> getTypeHits() {
    //     return typeHits;
    // }

    // public void setTypeHits(Map<String, Integer> typeHits) {
    //     this.typeHits = typeHits;
    // }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHits() {
        return hits;
    }

    public String getType() {
        return type;
    }

}
