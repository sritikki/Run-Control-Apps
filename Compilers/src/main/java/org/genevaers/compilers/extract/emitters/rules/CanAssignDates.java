package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.CompilerMessageSource;

public class CanAssignDates extends Rule{ 

    private static final String ERR_MESSAGE = "Incompatible date formats. Cannot assign %s to %s";

    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        final ViewColumn vc = ((ColumnAST)op1).getViewColumn();
        FieldReferenceAST f = ((FieldReferenceAST)op2);
        if (vc.getDateCode() != DateCode.NONE &&  f.getDateCode() != DateCode.NONE) {
            if(canAssignDates(vc.getDateCode(), f.getDateCode())) {
                return RuleResult.RULE_PASSED;                
            } else {
                CompilerMessage err = new CompilerMessage(
                            vc.getViewId(), 
                            CompilerMessageSource.COLUMN, 
                            ExtractBaseAST.getCurrentViewSource().getSourceLRID(), 
                            ExtractBaseAST.getCurrentViewSource().getSourceLFID(), 
                            vc.getColumnNumber(),
                            String.format(ERR_MESSAGE, f.getRef().getDateTimeFormat(), vc.getDateCode())
                        );
                Repository.addErrorMessage(err);
                return RuleResult.RULE_ERROR;

            }

        } else {
                return RuleResult.RULE_PASSED;
        }
    }
    
}
