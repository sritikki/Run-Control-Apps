package org.genevaers.genevaio.fieldnodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RecordTypeNode extends FieldNodeBase{

    private Integer typeNumber;

    public RecordTypeNode() {
        type = FieldNodeBase.Type.RECORDTYPE;
    }

    public void setTypeNumber(Integer typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Integer getTypeNumber() {
        return typeNumber;
    }

}
