package org.genevaers.genevaio.fieldnodes;

public class NoComponentNode extends FieldNodeBase {


    public NoComponentNode(String name) {
        setName(name);
        type = FieldNodeBase.FieldNodeType.NOCOMPONENT;
        state = ComparisonState.ORIGINAL;
    }

    public String getValue() {
         return "N/A";
    }

    public void setValue(String value) {
    }

    public String getDiffValue() {
         return "N/A";
    }

    public void setDiffValue(String diffValue) {
    }

    @Override
    public boolean compareTo(FieldNodeBase rn) {
        return true;
    }
}
