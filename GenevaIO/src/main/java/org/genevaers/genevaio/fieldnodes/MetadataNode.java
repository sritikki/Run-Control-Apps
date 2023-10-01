package org.genevaers.genevaio.fieldnodes;

public class MetadataNode extends FieldNodeBase{

    private Integer typeNumber;

    public MetadataNode() {
        type = FieldNodeBase.FieldNodeType.METADATA;
        state = ComparisonState.ORIGINAL;
    }

    public void setTypeNumber(Integer typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Integer getTypeNumber() {
        return typeNumber;
    }

}
