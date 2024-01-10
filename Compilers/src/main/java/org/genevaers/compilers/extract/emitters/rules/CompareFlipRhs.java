package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.CompilerMessageSource;

public class CompareFlipRhs extends Rule{ 

    private static final String MESSAGE = "Comparing RHS operand %s as though it were ZONED";

    @Override
    public RuleResult apply(final ExtractBaseAST lhs, final ExtractBaseAST rhs) {
        FormattedASTNode frmtLhs = ((FormattedASTNode) lhs);
        FormattedASTNode frmtRhs = ((FormattedASTNode) rhs);
        if (frmtLhs.isNumeric() && frmtRhs.getDataType() == DataType.ALPHANUMERIC ) {
                CompilerMessage warn = new CompilerMessage(
                            999, 
                            CompilerMessageSource.COLUMN, 
                            ExtractBaseAST.getCurrentViewSource().getSourceLRID(), 
                            ExtractBaseAST.getCurrentViewSource().getSourceLFID(), 
                            333,
                            String.format(MESSAGE, frmtRhs.getMessageName())
                        );
                Repository.addWarningMessage(warn);
                return RuleResult.RULE_WARNING;
        } else {
                return RuleResult.RULE_PASSED;
        }
    }

}
