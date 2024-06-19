package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.data.CompilerMessage;

public class CompareFlipLhs extends Rule{ 

    private static final String MESSAGE = "Comparing LHS operand %s as though it were ZONED";

    @Override
    public RuleResult apply(final ExtractBaseAST lhs, final ExtractBaseAST rhs) {
        FormattedASTNode frmtLhs = ((FormattedASTNode) lhs);
        FormattedASTNode frmtRhs = ((FormattedASTNode) rhs);
        if (frmtLhs.getDataType() == DataType.ALPHANUMERIC && frmtRhs.isNumeric()) {
                Repository.addWarningMessage(ExtractBaseAST.makeCompilerMessage(String.format(MESSAGE, frmtLhs.getMessageName())));
                return RuleResult.RULE_WARNING;
        } else {
                return RuleResult.RULE_PASSED;
        }
    }

    
}
