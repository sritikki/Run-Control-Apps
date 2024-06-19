package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.Assignable;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.Repository;

public class FieldZonedMaxLength extends Rule{ 

    private static final String ERR_MESSAGE = "Field {%s} length exceeds maximum ZONED length of %d";

    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        FormattedASTNode f = ((FormattedASTNode)op2);
        if(((Assignable)f).getAssignableLength() < MAX_ZONED_LENGTH ) {
            return RuleResult.RULE_PASSED;
        } else {
            Repository.addErrorMessage(ExtractBaseAST.makeCompilerMessage(String.format(ERR_MESSAGE, f.getMessageName(), MAX_ZONED_LENGTH)));
            return RuleResult.RULE_ERROR;
        }
    }
}
