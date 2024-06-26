package org.genevaers.compilers.extract.astnodes;

import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;

public class StringFunctionASTNode extends FormattedASTNode {

    private String length;

    public int getLength() {
        return Integer.valueOf(length);
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public DataType getDataType() {
        return overriddenDataType != DataType.INVALID ? overriddenDataType : DataType.ALPHANUMERIC;
    }

    @Override
    public DateCode getDateCode() {
        return (overriddenDateCode != null) ? overriddenDateCode : DateCode.NONE;
    }

    @Override
    public String getMessageName() {
        return "string function";
    }

    @Override
    public int getMaxNumberOfDigits() {
        return Integer.valueOf(length);
    }

}
