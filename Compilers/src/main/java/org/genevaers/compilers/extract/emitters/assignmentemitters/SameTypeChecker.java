package org.genevaers.compilers.extract.emitters.assignmentemitters;

import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.compilers.extract.emitters.rules.CanAssignDates;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;

public class SameTypeChecker extends AssignmentRulesChecker {

    public SameTypeChecker() {
        addRule(new CanAssignDates());  //Should be able use static?
    }

    @Override
    public AssignmentRulesResult verifyOperands(ColumnAST column, FormattedASTNode rhs) {
        ViewColumn vc = column.getViewColumn();
        FormattedASTNode frhs = (FormattedASTNode) rhs;
        // LRField f = null;
        // if (rhs.getType() == ASTFactory.Type.LRFIELD) {
        //     FieldReferenceAST fr = (FieldReferenceAST) rhs;
        //     f = fr.getRef();
        // } else if (rhs.getType() == ASTFactory.Type.LOOKUPFIELDREF) {
        //     LookupFieldRefAST lfr = (LookupFieldRefAST) rhs;
        //     f = lfr.getRef();
        // }
        if (vc.getDateCode() == frhs.getDateCode()) {
            stripOffDateCodes(column, frhs);
        } else {
            apply(column, rhs);
            //generate warning
            //If both sides had compatible dates ok
            //  leave them
            //else 
            //    generate Error
            //checkDateCodeCompatibility();
            //Use bit map!!!!!!!!!!!!!!!!!!!
        }
        return AssignmentRulesResult.ASSIGN_OK;
    }


    private void stripOffDateCodes(ColumnAST column, FormattedASTNode frhs) {
            // Stip off the content codes.
            // But in a copy not the original - or use an override type
            // Which is where the TypeASTNode should come in...
            // Would deal with the above functions too
            //if (vc.getFieldLength() != frhs.getLength()) {
                // If the lengths are the same as well then just strip off the content codes
            //}
        frhs.overrideDateCode(DateCode.NONE);
        column.setWorkingCode(DateCode.NONE);
    }

    @Override
    public void generateErrorOrWarning() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateErrorOrWarning'");
    }

}