package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.data.CompilerMessage;

public class Truncation extends Rule{ 

    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        final ViewColumn vc = ((ColumnAST)op1).getViewColumn();
        FormattedASTNode frhs = (FormattedASTNode) op2;
        int rhsDigits = frhs.getMaxNumberOfDigits();
        if( rhsDigits > 0 && rhsDigits > ((ColumnAST)op1).getMaxNumberOfDigits()) {
            if(op2.getType() == Type.NUMATOM) {
                CompilerMessage err = ExtractBaseAST.makeCompilerMessage(String.format("Truncation error assigning to column %d from %s.", vc.getColumnNumber(), frhs.getMessageName()));
                Repository.addErrorMessage(err);
            } else {
                CompilerMessage warn = ExtractBaseAST.makeCompilerMessage(String.format("Possible truncation assigning to column %d from %s.", vc.getColumnNumber(), frhs.getMessageName()));
                Repository.addWarningMessage(warn);
            }
            return RuleResult.RULE_WARNING;
        } else {
            return RuleResult.RULE_PASSED;
        }
    }
}
