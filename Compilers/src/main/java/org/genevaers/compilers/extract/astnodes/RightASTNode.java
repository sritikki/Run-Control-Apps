package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;

public class RightASTNode extends StringFunctionASTNode implements EmittableASTNode, Assignable{


    public RightASTNode() {
        type = ASTFactory.Type.RIGHT;
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST col, ExtractBaseAST rhs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAssignmentEntry'");
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

    @Override
    public void emit() {
        //Need to generate a function code base on child?
    }

}
