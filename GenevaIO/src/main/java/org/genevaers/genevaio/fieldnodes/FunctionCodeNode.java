package org.genevaers.genevaio.fieldnodes;

public class FunctionCodeNode extends FieldNodeBase{

    private String functionCode;

    public FunctionCodeNode() {
        type = FieldNodeBase.FieldNodeType.FUNCCODE;
        state = ComparisonState.ORIGINAL;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }
}
