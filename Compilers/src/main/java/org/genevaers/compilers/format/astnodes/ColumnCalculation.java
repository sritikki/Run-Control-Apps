package org.genevaers.compilers.format.astnodes;

import java.nio.ByteBuffer;

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
import org.genevaers.repository.calculationstack.CalcStack;
import org.genevaers.repository.calculationstack.CalcStackEntry;
import org.genevaers.repository.components.ViewColumn;

import com.google.common.flogger.FluentLogger;

public class ColumnCalculation extends FormatBaseAST{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private ViewColumn viewColumn;

    ColumnCalculation() {
        type = Type.COLCALC;
    }

    public void addViewColumn(ViewColumn vc) {
        viewColumn = vc;
    }

    @Override
    public CalcStackEntry emit(boolean invert) {
		ByteBuffer buffer = ByteBuffer.allocate(8192);
        calcStack = new CalcStack(buffer, 0, 0);
        viewColumn.setColumnCalculationStack(calcStack);
        currentOffset = 0;
        CalcStackEntry cse = super.emit(invert);
        logger.atInfo().log("View Column %s calculation stack\n%s", viewColumn.getColumnNumber(), calcStack.toString());
        return cse;

    }}
