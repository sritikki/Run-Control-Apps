package org.genevaers.compilers.format.astnodes;

import java.util.Iterator;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;

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
import org.genevaers.repository.calculationstack.CalcStack.CalcStackOpcode;

public class SelectIfAST extends FormatBaseAST{

    int trueEndPosition;
    int falsePosition;

    SelectIfAST() {
        type=Type.SELECTIF;
    }

    @Override
    public CalcStackEntry emit(boolean invert) {
        Iterator<ASTBase> ci = getChildIterator();
        FormatBaseAST predicate = (FormatBaseAST) ci.next();
        NumConst trueBranch = (NumConst) ci.next();
        NumConst falseBranch = (NumConst) ci.next();
        inverted = invert;

        CalcStackIntegerEntry predEntry = (CalcStackIntegerEntry) predicate.emit(invert);

        CalcStackEntry trueEntry = trueBranch.emit(invert);

        CalcStackIntegerEntry ba = (CalcStackIntegerEntry) emitIntegerCodeEntry(CalcStackOpcode.CalcStackBranchAlways, null);

        CalcStackEntry fbEntry = falseBranch.emit(invert);

        int end = fbEntry.getOffset() + fbEntry.length();

        //Only now can we set the internal condition gotos
        doFixups(predicate, trueBranch.getOffset(), fbEntry.getOffset());

        ba.setValue(end);

        return trueEntry;
    }
}
