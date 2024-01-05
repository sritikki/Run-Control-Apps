package org.genevaers.compilers.extract.emitters.assignmentemitters;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.compilers.extract.emitters.rules.CanAssignDates;
import org.genevaers.compilers.extract.emitters.rules.ColumnStripDate;
import org.genevaers.compilers.extract.emitters.rules.FieldStripDate;
import org.genevaers.compilers.extract.emitters.rules.Rule.RuleResult;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;

public class DateChecker extends AssignmentRulesChecker {

    public DateChecker() {
        addRule(new CanAssignDates());
        addRule(new ColumnStripDate());
        addRule(new FieldStripDate());
    }

    @Override
    public RuleResult verifyOperands(ColumnAST column, FormattedASTNode rhs) {
        RuleResult result = RuleResult.RULE_PASSED;
        ViewColumn vc = column.getViewColumn();
        FormattedASTNode frhs = (FormattedASTNode) rhs;
        if (vc.getDateCode() != DateCode.NONE && frhs.getDateCode() != DateCode.NONE) {
            updateResult(result, apply(column, rhs));
        } else {
            updateResult(result, apply(column, rhs));
        }
        return result;
    }

    @Override
    public void generateErrorOrWarning() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateErrorOrWarning'");
    }
    
}