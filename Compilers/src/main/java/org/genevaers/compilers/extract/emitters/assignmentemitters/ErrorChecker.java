package org.genevaers.compilers.extract.emitters.assignmentemitters;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;

public class ErrorChecker extends AssignmentRulesChecker {

    @Override
    public AssignmentRulesResult verifyOperands(ColumnAST column, FormattedASTNode rhs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void generateErrorOrWarning() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateErrorOrWarning'");
    }
    
}