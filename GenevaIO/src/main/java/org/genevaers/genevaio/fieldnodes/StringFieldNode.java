package org.genevaers.genevaio.fieldnodes;

public class StringFieldNode extends FieldNodeBase {

    private String value;
    private String diffValue;

    public StringFieldNode(String name, String val) {
        setName(name);
        value = val;
    }

    public String getValue() {
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

}
