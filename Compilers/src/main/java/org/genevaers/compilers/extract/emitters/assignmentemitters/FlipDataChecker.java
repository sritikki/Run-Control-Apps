package org.genevaers.compilers.extract.emitters.assignmentemitters;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.components.enums.DataType;

public class FlipDataChecker extends AssignmentRulesChecker {

    @Override
    public AssignmentRulesResult verifyOperands(ColumnAST column, FormattedASTNode rhs) {
        // Change the alnum data type to zoned
        // Then treat as a DateChecker
        if(column.getViewColumn().getDataType() == DataType.ALPHANUMERIC) {
            // We don't want to change the actual data type of the column
            // Just how we treat it.
            // That is where the ArithInfo came in.
            // And the Formatted AST Node..
            // Also allows management of the casting
            // Which in C++ is called in the generateASTValueRef or generateASTUnaryNode in  ExtractParserBase
         }
        return AssignmentRulesResult.ASSIGN_OK;
    }

    @Override
    public void generateErrorOrWarning() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateErrorOrWarning'");
    }
    
}