package org.genevaers.compilers.format.astnodes;

import java.util.Iterator;

import org.genevaers.compilers.base.ASTBase;

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


import org.genevaers.compilers.format.astnodes.FormatASTFactory.Type;
import org.genevaers.repository.calculationstack.CalcStackEntry;
import org.genevaers.repository.calculationstack.CalcStackIntegerEntry;

public class AndOp extends FormatBaseAST{

    AndOp() {
        type = Type.ANDOP;
    }

    @Override
    public CalcStackEntry emit(boolean invert) {
        //An and does not emit anything
        //It has two branches
        //Each of which is a conditional operation
        //After we emit the LHS and RHS 
        //we should have the information for the branch
        //The Not of the world may mess that up but...
        Iterator<ASTBase> ci = getChildIterator();
        FormatBaseAST lhs = (FormatBaseAST) ci.next();
        FormatBaseAST rhs = (FormatBaseAST) ci.next();

        //Need an invert flag to control the logic of the branch
        //
        CalcStackIntegerEntry lastLhsEntry = (CalcStackIntegerEntry) lhs.emit(true);

        CalcStackIntegerEntry lastRhsEntry = (CalcStackIntegerEntry) rhs.emit(true);

        //At this point in time we don't know where the end is
        //The true and false branches of the parent if have not been emitted
        //lastLhsEntry.setValue(currentOffset+lastRhsEntry.length());

        return lastRhsEntry;
    }
}
