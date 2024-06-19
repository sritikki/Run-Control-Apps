package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;

public class FieldStripDate extends Rule{ 


    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        final ViewColumn vc = ((ColumnAST)op1).getViewColumn();
        FormattedASTNode frhs = (FormattedASTNode) op2;
        if(vc.getDateCode() == DateCode.NONE && frhs.getDateCode() != DateCode.NONE) {
            ((FormattedASTNode)op2).overrideDateCode(DateCode.NONE);
            Repository.addWarningMessage(ExtractBaseAST.makeCompilerMessage(String.format("Removing date from field %s.", frhs.getMessageName())));
            return RuleResult.RULE_WARNING;
        } else {
            return RuleResult.RULE_PASSED;
        }
    }
}
