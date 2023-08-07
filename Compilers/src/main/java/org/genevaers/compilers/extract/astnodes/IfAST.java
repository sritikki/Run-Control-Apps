package org.genevaers.compilers.extract.astnodes;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008.
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


import java.util.Iterator;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.repository.components.enums.LtRecordType;

public class IfAST extends ExtractBaseAST implements EmittableASTNode {

    Integer thenStart = -1; //End of the predicate
    Integer thenEnd = -1;
    Integer elseEnd = -1;
    boolean hasElse;

    public IfAST() {
        type = ASTFactory.Type.IFNODE;
    }

    @Override
    public void emit() {
        //An if node will hve 2 possibly 3 children
        EmittableASTNode predicate = (EmittableASTNode) children.get(0);
        predicate.emit();
        thenStart = ltEmitter.getNumberOfRecords();
        EmittableASTNode thenBody = (EmittableASTNode) children.get(1);
        thenBody.emit();
        thenEnd = ltEmitter.getNumberOfRecords();
        if(getNumberOfChildren() == 3) {
            LogicTableF0 gt = emitGoto();
            thenEnd++;
            hasElse = true;
            EmittableASTNode elseBody = (EmittableASTNode) children.get(2);
            elseBody.emit();
            elseEnd = ltEmitter.getNumberOfRecords();
            gt.setGotoRow1(elseEnd);
        }
    }

    @Override
    public void resolveGotos(Integer compT, Integer compF, Integer joinT, Integer joinF) {
        Iterator<ASTBase> ci = children.iterator();
        while(ci.hasNext()) {
            ExtractBaseAST c = (ExtractBaseAST) ci.next();
            c.resolveGotos(thenStart, thenEnd, null, thenEnd);
        }
    }

    private LogicTableF0 emitGoto() {
        LogicTableF0 f0 = new LogicTableF0();
        f0.setRecordType(LtRecordType.F0);
        f0.setFunctionCode("GOTO");
        f0.setGotoRow2(0);
        ltEmitter.addToLogicTable(f0);
        return f0;
    }
    
}
