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


import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FormattedASTNode;
import org.genevaers.repository.components.ViewColumn;

public class AssignmentRulesCheckerFactory {

    private static ErrorChecker errorChecker = new ErrorChecker();
    private static DateChecker dateChecker = new DateChecker();
    private static FlipColumnChecker flipColumnChecker = new FlipColumnChecker();
    private static FlipDataChecker flipChecker = new FlipDataChecker();
    private static SameTypeChecker sameTypesChecker = new SameTypeChecker();

    private AssignmentRulesCheckerFactory() {}

    // C++ takes the original data types and adds constant num, strin and date to the matrix
    // Do we need a matrix or can we program it?

    // get types as numeric or string and masked as a special
    // and same or just do same within the dater?
    // if src and targ same type call date check
    // else call flipper
    // can't assign to a const... but compiler should not all this anyway?
    public static AssignmentRulesChecker getChecker(ColumnAST column, FormattedASTNode rhs) {
        ViewColumn vc = column.getViewColumn();

        if (sameDataTypes(vc, rhs)) {
            return sameTypesChecker;
        } else {
            if(column.isNumeric() && rhs.isNumeric()) {
                return dateChecker;
            } else {
                if(!column.isNumeric()) {
                    return flipColumnChecker;
                } else {
                    return flipChecker;
                }
            }
        }

    }

    private static boolean sameDataTypes(ViewColumn vc, ExtractBaseAST rhs) {
        return vc.getDataType() == ((FormattedASTNode)rhs).getDataType();
    }

}
