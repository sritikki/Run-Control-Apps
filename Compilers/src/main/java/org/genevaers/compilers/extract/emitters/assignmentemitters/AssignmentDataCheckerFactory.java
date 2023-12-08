package org.genevaers.compilers.extract.emitters.assignmentemitters;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.compilers.extract.astnodes.LookupFieldRefAST;
import org.genevaers.repository.RepoHelper;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;

public class AssignmentDataCheckerFactory {

    public static class SameTypeChecker implements DataTypeChecker {

        @Override
        public DTResult verifyOperands(ColumnAST column, ExtractBaseAST rhs) {
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
                frhs.overrideDateCode(DateCode.NONE);
                column.setWorkingCode(DateCode.NONE);
                // Stip off the content codes.
                // But in a copy not the original - or use an override type
                // Which is where the TypeASTNode should come in...
                // Would deal with the above functions too
                //if (vc.getFieldLength() != frhs.getLength()) {
                    // If the lengths are the same as well then just strip off the content codes
                //}
            }
            return DTResult.ASSIGN_OK;
        }

    }

    public static class DateChecker implements DataTypeChecker {

        @Override
        public DTResult verifyOperands(ColumnAST column, ExtractBaseAST rhs) {
            // TODO Auto-generated method stub
            return DTResult.ASSIGN_OK;
        }
        
    }
     public static class FlipDataChecker implements DataTypeChecker {

        @Override
        public DTResult verifyOperands(ColumnAST column, ExtractBaseAST rhs) {
            // Change the alnum data type to zoned
            // Then treat as a DateChecker
            if(column.getViewColumn().getDataType() == DataType.ALPHANUMERIC) {
                // We don't want to change the actual data type of the column
                // Just how we treat it.
                // That is where the ArithInfo came in.
                // And the Formatted AST Node..
                // Also allows management of the casting
                // Which in C++ is called in the generateASTValueRef or generateASTUnaryNode in  ExtractParserBase
             }
            return DTResult.ASSIGN_OK;
        }
        
    }
    
   
    public static class ErrorChecker implements DataTypeChecker {

        @Override
        public DTResult verifyOperands(ColumnAST column, ExtractBaseAST rhs) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }

    private static ErrorChecker ec = new ErrorChecker();
    private static DateChecker dc = new DateChecker();
    private static FlipDataChecker fc = new FlipDataChecker();
    private static SameTypeChecker sc = new SameTypeChecker();

    private AssignmentDataCheckerFactory() {}

    // C++ takes the original data types and adds constant num, strin and date to the matrix
    // Do we need a matrix or can we program it?

    // get types as numeric or string and masked as a special
    // and same or just do same within the dater?
    // if src and targ same type call date check
    // else call flipper
    // can't assign to a const... but compiler should not all this anyway?
    public static DataTypeChecker getDataChecker(ColumnAST column, ExtractBaseAST rhs) {
        ViewColumn vc = column.getViewColumn();

        if (sameDataTypes(vc, rhs)) {
            return sc;
        } else {

            // add a static function data type helper to the repository
            boolean colNumeric = RepoHelper.isNumeric(vc.getDataType());
            boolean rhsNumeric = isRHSNumeric(rhs);

            if (colNumeric) {
                if (rhsNumeric) {
                    return dc;
                } else {
                    return fc;
                }
            } else {
                if (rhsNumeric) {
                    return fc;
                } else {
                    return dc;
                }
            }
        }

    }

    private static boolean sameDataTypes(ViewColumn vc, ExtractBaseAST rhs) {
        try {
            return vc.getDataType() == ((FormattedASTNode)rhs).getDataType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isRHSNumeric(ExtractBaseAST rhs) {
        boolean rhsNumeric = false;
        if(rhs.getType() == ASTFactory.Type.LRFIELD) {
            FieldReferenceAST fr = (FieldReferenceAST)rhs;
            LRField f = fr.getRef();
            if(f != null)
                rhsNumeric = RepoHelper.isNumeric(f.getDatatype());
        } else if (rhs.getType() == ASTFactory.Type.NUMATOM) {
                rhsNumeric = true;
        }
        return rhsNumeric;
    }
    
}
