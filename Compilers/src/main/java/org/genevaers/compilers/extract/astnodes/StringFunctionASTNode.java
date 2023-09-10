package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;

public class StringFunctionASTNode extends FormattedASTNode {

    private String length;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public DataType getDataType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataType'");
    }

    @Override
    public DateCode getDateCode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDateCode'");
    }
}
