package org.genevaers.compilers.format.astnodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private List<CalcStackIntegerEntry> nodeEntries = new ArrayList<>();

    AndOp() {
        type = Type.ANDOP;
    }

    @Override
    public CalcStackEntry emit(boolean invert) {
        //An and does not emit anything
        //It has two or more branches
        //Each of which is a conditional operation
        //As we emit each branch we know the offset for the previous.
        //In a cascade ... each case jumps to the end if it fails
        //The Not of the world may mess that up but...


        CalcStackIntegerEntry lastNodeEntry = null;
        Iterator<ASTBase> ci = getChildIterator();
        while (ci.hasNext()) {
            FormatBaseAST term = (FormatBaseAST) ci.next();
            if(ci.hasNext()) {
                lastNodeEntry = (CalcStackIntegerEntry) term.emit(invert);
            } else {
                //last entry is treated differently
                lastNodeEntry = (CalcStackIntegerEntry) term.emit(invert);
            }
            nodeEntries.add(lastNodeEntry);
        }
         return lastNodeEntry;
    }

    public void doFixups(int thenIndex, int elseIndex) {
        Iterator<CalcStackIntegerEntry> ni = nodeEntries.iterator();
        while(ni.hasNext()) {
            CalcStackIntegerEntry ne = ni.next();
            ne.setValue(elseIndex);
        }
        Iterator<ASTBase> ci = getChildIterator();
        while (ci.hasNext()) {
            FormatBaseAST c = (FormatBaseAST) ci.next();
            doFixups(c, thenIndex, elseIndex);
        }
    }
}
