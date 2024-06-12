package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.CompilerMessage;

public class ConstStringToDateColumnError extends Rule{ 

    @Override
    public RuleResult apply(final ExtractBaseAST op1, final ExtractBaseAST op2) {
        final ViewColumn vc = ((ColumnAST)op1).getViewColumn();
        FormattedASTNode frhs = (FormattedASTNode) op2;
        if(vc.getDateCode() != DateCode.NONE && op2.getType() == Type.STRINGATOM) {
            CompilerMessage err = ExtractBaseAST.makeCompilerMessage(String.format("Cannot assign %s to column %s which has a date code.", frhs.getMessageName(), vc.getColumnNumber()));
            Repository.addErrorMessage(err);
            return RuleResult.RULE_ERROR;
        } else {
            return RuleResult.RULE_PASSED;
        }
    }
    
}
