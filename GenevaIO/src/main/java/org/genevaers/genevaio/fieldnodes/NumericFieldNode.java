package org.genevaers.genevaio.fieldnodes;

public class NumericFieldNode extends FieldNodeBase {

    private int value;
    private int diffValue;

    public NumericFieldNode(String name, int val) {
        setName(name);
        value = val;
        type = FieldNodeBase.Type.NUMBERFIELD;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDiffValue() {
        return diffValue;
    }

    public void setDiffValue(int diffValue) {
        this.diffValue = diffValue;
    }

}
