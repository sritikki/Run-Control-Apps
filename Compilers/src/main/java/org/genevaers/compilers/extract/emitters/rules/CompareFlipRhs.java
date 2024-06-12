package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.enums.DataType;

public class CompareFlipRhs extends Rule{ 

    private static final String MESSAGE = "Comparing RHS operand %s as though it were ZONED";

    @Override
    public RuleResult apply(final ExtractBaseAST lhs, final ExtractBaseAST rhs) {
        FormattedASTNode frmtLhs = ((FormattedASTNode) lhs);
        FormattedASTNode frmtRhs = ((FormattedASTNode) rhs);
        if (frmtLhs.isNumeric() && frmtRhs.getDataType() == DataType.ALPHANUMERIC ) {
                Repository.addWarningMessage(ExtractBaseAST.makeCompilerMessage(String.format(MESSAGE, frmtRhs.getMessageName())));
                return RuleResult.RULE_WARNING;
        } else {
                return RuleResult.RULE_PASSED;
        }
    }

}
