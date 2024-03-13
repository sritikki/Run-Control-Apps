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
import org.genevaers.repository.Repository;
import org.genevaers.repository.calculationstack.CalcStack;
import org.genevaers.repository.calculationstack.CalcStackEntry;
import org.genevaers.repository.calculationstack.CalcStackIntegerEntry;
import org.genevaers.repository.calculationstack.CalcStackLongStringEntry;
import org.genevaers.repository.calculationstack.CalcStackShortStringEntry;
import org.genevaers.repository.calculationstack.CalcStack.CalcStackOpcode;

public abstract class FormatBaseAST extends ASTBase{

    protected FormatASTFactory.Type type = null;
    protected static CalcStack calcStack;
    protected static int currentOffset = 0;
    protected boolean inverted = false;
    protected CalcStackEntry calcStackEntry;
    protected boolean negative;

    public FormatASTFactory.Type getType() {
        return type;
    }

    public CalcStackEntry emit(boolean invert) {
        CalcStackEntry cse = null;
        Iterator<ASTBase> fi = getChildIterator();
        while(fi.hasNext()) {
            FormatBaseAST f = (FormatBaseAST) fi.next();
            cse = f.emit(invert);
        }
        return cse;
    }

    public CalcStackEntry emitSingleCodeEntry(CalcStackOpcode opCode, CalcStackEntry lastChildEntry) {
        calcStackEntry = new CalcStackEntry();
        addStackEntryRelativeToLast(calcStackEntry, opCode, lastChildEntry);
        currentOffset += calcStackEntry.length();
        return calcStackEntry;
    }

    public CalcStackEntry emitIntegerCodeEntry(CalcStackOpcode opCode, CalcStackEntry lastChildEntry) {
        calcStackEntry = new CalcStackIntegerEntry();
        addStackEntryRelativeToLast(calcStackEntry, opCode, lastChildEntry);
        currentOffset += calcStackEntry.length();
        return calcStackEntry;
    }

    public CalcStackEntry emitShortStringCodeEntry(CalcStackOpcode opCode, CalcStackEntry lastChildEntry) {
        calcStackEntry = new CalcStackShortStringEntry();
        addStackEntryRelativeToLast(calcStackEntry, opCode, lastChildEntry);
        currentOffset += calcStackEntry.length();
        return calcStackEntry;
    }

    public CalcStackEntry emitLongStringCodeEntry(CalcStackOpcode opCode, CalcStackEntry lastChildEntry) {
        calcStackEntry = new CalcStackLongStringEntry();
        addStackEntryRelativeToLast(calcStackEntry, opCode, lastChildEntry);
        currentOffset += calcStackEntry.length();
        return calcStackEntry;
    }

    private void addStackEntryRelativeToLast(CalcStackEntry cse, CalcStackOpcode opCode, CalcStackEntry lastChildEntry) {
        cse.setOpCode(opCode);
        calcStack.add(cse);
        cse.setOffset(currentOffset);
    }

    /*
     * Once the calculation stack is built we need to ensure the branches are correct
     * Branches only have one value - where they goto on true condition
     * They drop through on a false and have a branch always at the end
     * 
     * C++ code inverts the logic of a branch so the true path, as defined in the text,
     * is the drop through.
     * 
     * There is also an inverted flag. This is useful/essential to manage the NOT logic.
     * 
     * An IF may not have an ELSE section.
     */
    protected void doFixups (FormatBaseAST ast, int thenIndex, int elseIndex) {
    switch (ast.getType())
    {
        case ANDOP:
            ((AndOp)ast).doFixups(thenIndex, elseIndex);
            break;
        case OROP:
            ((OrOP)ast).doFixups(thenIndex, elseIndex);
            break;
        case NOTOP:
            assert (ast.getNumberOfChildren () == 1);
            Iterator<ASTBase> ci = ast.getChildIterator();
            doFixups ((FormatBaseAST)ci.next(), elseIndex, thenIndex);
            break;

        case LT:
        case LE:
        case GT:
        case GE:
        case NE:
        case EQ:
        CalcStackIntegerEntry branch = ((CalcStackIntegerEntry)ast.getCalcStackEntry());
        branch.setValue(ast.isInverted() ? elseIndex : thenIndex );
        break;

        default:
            // Unknown Node Type!
        break;
    }
}

    public int getOffset() {
        return calcStackEntry != null ? calcStackEntry.getOffset() : 0;
    }

    public void setOffset(int offset) {
        calcStackEntry.setOffset(offset);
    }

    public boolean isInverted() {
        return inverted;
    }

    public CalcStackEntry getCalcStackEntry() {
        return calcStackEntry;
    }

    public static void resetOffset() {
        currentOffset = 0;
    }

    public void setNegative() {
        negative = true;
    }

    public static void resetStack() {
        calcStack = null;
    }

}
