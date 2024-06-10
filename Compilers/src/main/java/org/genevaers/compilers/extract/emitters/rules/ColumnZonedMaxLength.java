package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.CompilerMessageSource;

public class ColumnZonedMaxLength extends Rule{ 

    private static final String ERR_MESSAGE = "Column %d length exceeds maximum ZONED length of %d";

    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        final ViewColumn vc = ((ColumnAST)op1).getViewColumn();
        if(vc.getFieldLength() < MAX_ZONED_LENGTH ) {
            return RuleResult.RULE_PASSED;
        } else {
            //We must be able to extract this to the base
            CompilerMessage err = new CompilerMessage(
                                        vc.getViewId(), 
                                        CompilerMessageSource.COLUMN, 
                                        ExtractBaseAST.getCurrentViewSource().getSourceLRID(), 
                                        ExtractBaseAST.getCurrentViewSource().getSourceLFID(), 
                                        vc.getColumnNumber(),
                                        String.format(ERR_MESSAGE, vc.getColumnNumber(), MAX_ZONED_LENGTH)
                                    );
            Repository.addErrorMessage(err);
            return RuleResult.RULE_ERROR;
        }
    }
    
}
