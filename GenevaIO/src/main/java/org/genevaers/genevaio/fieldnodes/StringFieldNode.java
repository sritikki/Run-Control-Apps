package org.genevaers.genevaio.fieldnodes;

public class StringFieldNode extends FieldNodeBase {

    private String value;
    private String diffValue;

    public StringFieldNode(String name, String val) {
        setName(name);
        value = val;
        type = FieldNodeBase.FieldNodeType.STRINGFIELD;
        state = ComparisonState.ORIGINAL;
    }

    public String getValue() {
        if(state == ComparisonState.DIFF) {
            return value + " -> " + diffValue;
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDiffValue() {
        return diffValue;
    }

    public void setDiffValue(String diffValue) {
        this.diffValue = diffValue;
    }

    @Override
    public void compareTo(FieldNodeBase rn) {
        if(value.equals(((StringFieldNode)rn).getValue())) {
            state = ComparisonState.INSTANCE;
        } else {
            state = ComparisonState.DIFF;
            diffValue = ((StringFieldNode)rn).getValue();
        }
    }
}
