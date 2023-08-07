package org.genevaers.runcontrolanalyser.ltcoverage;

import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.LtRecordType;

public class LtCoverageEntry {

    private String name;
    private int hits;
    private LtRecordType type;
    private int expectedItems;
    private String description;   
    private String category;

    public static final int MAXTYPES = 17;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public LtRecordType getType() {
        return type;
    }

    public void setType(LtRecordType type) {
        this.type = type;
    }

    public int getExpectedItems() {
        return expectedItems;
    }

    public void setExpectedItems(int expectedItems) {
        this.expectedItems = expectedItems;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void hit(LTRecord ltr) {
        hits++;
    }

    protected String getTypeName(boolean signed, DataType dt, int len) {
        String dts;
           String sign =  signed ? "S" : "";
           if(dt == DataType.BINARY){
                dts = len < 8 ? sign : "";
                dts += dt.toString(); 
                dts += len;
           } else if(dt == DataType.PACKED || dt == DataType.ZONED ) {
                dts = sign + dt.toString(); 
           } else {
                dts = dt.toString();
           }
        return dts;
    }


    // Derive class to hold the argType list
    // of matrix
    // typeHitMap m_typeHitMap;
    // don't really want this for all codes - so create specific classes
    // or new the arrays when needed
    // typeHitMatrixMap m_typeHitMatrixMap;

}
