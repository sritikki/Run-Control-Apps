package org.genevaers.compilers.format.astnodes;

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
import org.genevaers.repository.calculationstack.CalcStack.CalcStackOpcode;

public class FormatNE extends FormatBaseAST implements EmittableFormatASTNode{
    FormatNE() {
        type = Type.NE;
    }

    @Override
    public CalcStackEntry emit(boolean invert) {
        inverted = invert;
        if(invert) {
            return emitIntegerCodeEntry(CalcStackOpcode.CalcStackBranchEQ, super.emit(invert));
        } else {
            return emitIntegerCodeEntry(CalcStackOpcode.CalcStackBranchNE, super.emit(invert));
        }
    }
}
