package org.genevaers.compilers.extract;

import org.genevaers.compilers.extract.astnodes.ColumnRefAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.compilers.extract.astnodes.LookupFieldRefAST;
import org.genevaers.compilers.extract.astnodes.StringAtomAST;
import org.genevaers.repository.components.enums.DataType;

public class StringDataTypeChecker {

    public static boolean allows(ExtractBaseAST lhs, ExtractBaseAST rhs, String op) {
        //The operands must both be Alphanumeric
        boolean allowed = true;
        DataType lhsdtype = getDataType(lhs);
        DataType rhsdtype = getDataType(rhs);
        if(lhsdtype != DataType.ALPHANUMERIC || rhsdtype != DataType.ALPHANUMERIC) {
            allowed = false;
        }


        //And we need to think about the bounds
        return allowed;
    }

    private static DataType getDataType(ExtractBaseAST node) {
        DataType dtype = DataType.INVALID;
        switch(node.getType()) {
            case LRFIELD:
            case PRIORLRFIELD:
            dtype = ((FieldReferenceAST)node).getRef().getDatatype();
            break;
            case LOOKUPFIELDREF:
            dtype = ((LookupFieldRefAST)node).getRef().getDatatype();
            break;
            case COLUMNREF:
            dtype = ((ColumnRefAST) node).getViewColumn().getDataType();
            break;
            case STRINGATOM:
            dtype = ((StringAtomAST)node).getDataType();
            default:
            break;

        }
        return dtype;
    }

}
