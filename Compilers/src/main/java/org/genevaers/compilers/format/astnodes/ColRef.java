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
import org.genevaers.repository.calculationstack.CalcStackIntegerEntry;
import org.genevaers.repository.calculationstack.CalcStack.CalcStackOpcode;

public class ColRef extends FormatBaseAST {
    String text;

    ColRef() {
        type = Type.COLREF;
    }

    public void setText(String colText) {
        text = colText;
    }

    public String getText() {
        return text;
    }

    //A Column Ref is a terminal node and has no children to emit
    @Override
    public CalcStackEntry emit(boolean invert) {
        CalcStackIntegerEntry colEntry = (CalcStackIntegerEntry) emitIntegerCodeEntry(CalcStackOpcode.CalcStackPushColumn, null);
        colEntry.setValue(getColumnNumber());
        return colEntry;
    }

    private String getColumnNumber() {
        String[] strs = text.split("\\.");
        return strs[1];
    }

}
