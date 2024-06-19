package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.enums.DateCode;

public class CanCompareDates extends Rule{ 

    private static final String ERR_MESSAGE = "Incompatible date formats. Cannot compare %s to %s";

    @Override
    public RuleResult apply(final ExtractBaseAST lhs, final ExtractBaseAST rhs) {
        FormattedASTNode frmtLhs = ((FormattedASTNode) lhs);
        FormattedASTNode frmtRhs = ((FormattedASTNode) rhs);
        if (frmtLhs.getDateCode() != DateCode.NONE &&  frmtRhs.getDateCode() != DateCode.NONE) {
            if(canCompareDates(frmtLhs.getDateCode(), frmtRhs.getDateCode())) {
                return RuleResult.RULE_PASSED;                
            } else {
                Repository.addErrorMessage(ExtractBaseAST.makeCompilerMessage(String.format(ERR_MESSAGE, frmtLhs.getDateCode(), frmtRhs.getDateCode())));
                return RuleResult.RULE_ERROR;
            }
        } else {
                return RuleResult.RULE_PASSED;
        }
    }
}
