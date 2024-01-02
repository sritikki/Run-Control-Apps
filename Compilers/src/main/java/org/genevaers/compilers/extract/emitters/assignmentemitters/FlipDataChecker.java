package org.genevaers.compilers.extract.emitters.assignmentemitters;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.compilers.extract.emitters.rules.ColumnZonedMaxLength;
import org.genevaers.compilers.extract.emitters.rules.FieldZonedMaxLength;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.CompilerMessageSource;

public class FlipDataChecker extends AssignmentRulesChecker {

    public FlipDataChecker() {
        addRule(new FieldZonedMaxLength());  //Should be able use static?
    }

    @Override
    public AssignmentRulesResult verifyOperands(ColumnAST column, FormattedASTNode rhs) {
        ViewColumn vc = column.getViewColumn();
        FieldReferenceAST f = ((FieldReferenceAST)rhs);
        apply(column, rhs);
        CompilerMessage warn = new CompilerMessage(
                                        vc.getViewId(), 
                                        CompilerMessageSource.COLUMN, 
                                        ExtractBaseAST.getCurrentViewSource().getSourceLRID(), 
                                        ExtractBaseAST.getCurrentViewSource().getSourceLFID(), 
                                        column.getViewColumn().getColumnNumber(),
                                        (String.format("Treating field {%s} as ZONED.", f.getRef().getName()))
                                    );
        Repository.addWarningMessage(warn);

        // Change the alnum data type to zoned
        // Then treat as a DateChecker
            // We don't want to change the actual data type of the column
            // Just how we treat it.
            // That is where the ArithInfo came in.
            // And the Formatted AST Node..
            // Also allows management of the casting
            // Which in C++ is called in the generateASTValueRef or generateASTUnaryNode in  ExtractParserBase
        return AssignmentRulesResult.ASSIGN_WARNING;
    }

    @Override
    public void generateErrorOrWarning() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateErrorOrWarning'");
    }
    
}