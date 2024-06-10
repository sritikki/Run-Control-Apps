package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.CompilerMessageSource;

public class FieldStripDate extends Rule{ 


    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        final ViewColumn vc = ((ColumnAST)op1).getViewColumn();
        FormattedASTNode frhs = (FormattedASTNode) op2;
        if(vc.getDateCode() == DateCode.NONE && frhs.getDateCode() != DateCode.NONE) {
            ((FormattedASTNode)op2).overrideDateCode(DateCode.NONE);
            CompilerMessage warn = new CompilerMessage(
                vc.getViewId(), 
                CompilerMessageSource.COLUMN, 
                ExtractBaseAST.getCurrentViewSource().getSourceLRID(), 
                ExtractBaseAST.getCurrentViewSource().getSourceLFID(), 
                vc.getColumnNumber(),
                (String.format("Removing date from field %s.", frhs.getMessageName()))
            );
            Repository.addWarningMessage(warn);
            return RuleResult.RULE_WARNING;
        } else {
            return RuleResult.RULE_PASSED;
        }
    }
}
