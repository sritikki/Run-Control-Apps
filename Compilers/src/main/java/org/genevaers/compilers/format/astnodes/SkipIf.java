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
import org.genevaers.repository.calculationstack.CalcStack.CalcStackOpcode;
import org.genevaers.repository.calculationstack.CalcStackEntry;
import org.genevaers.repository.calculationstack.CalcStackIntegerEntry;

public class SkipIf extends FormatBaseAST{

    SkipIf() {
        type = Type.SKIPIF;
    }

    @Override
    public CalcStackEntry emit(boolean invert) {
        Iterator<ASTBase> ci = getChildIterator();
        FormatBaseAST predicate = (FormatBaseAST) ci.next();
        //Reverse of selectif
        NumConst falseBranch = (NumConst) ci.next();
        NumConst trueBranch = (NumConst) ci.next();
        inverted = invert;

        CalcStackIntegerEntry predEntry = (CalcStackIntegerEntry) predicate.emit(invert);
        //currentOffset += predEntry.length();

        CalcStackEntry trueEntry = trueBranch.emit(invert);
        currentOffset += trueEntry.length();

        CalcStackIntegerEntry ba = (CalcStackIntegerEntry) emitIntegerCodeEntry(CalcStackOpcode.CalcStackBranchAlways, null);
        currentOffset += ba.length();

        CalcStackEntry fbEntry = falseBranch.emit(invert);
        currentOffset += fbEntry.length();

        int end = fbEntry.getOffset() + fbEntry.length();

        //Only now can we set the internal condition gotos
        doFixups(predicate, trueBranch.getOffset(), fbEntry.getOffset());

        ba.setValue(end);

        return trueEntry;
    }
}
