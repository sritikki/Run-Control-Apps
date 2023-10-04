package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.repository.components.enums.LtRecordType;

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


import org.genevaers.repository.components.ViewSource;

public class EndOfSetASTNode extends ExtractBaseAST implements EmittableASTNode{

    private ViewSource vs;
    private int lfID;

    public EndOfSetASTNode() {
        type = ASTFactory.Type.EOS;
    }

    @Override
    public void emit() {
        LogicTableF0 end = new LogicTableF0();
        end.setFunctionCode("ES");
        end.setRecordType(LtRecordType.F0);
        end.setViewId(0);
        ltEmitter.setSuffixSeqNbr((short)0);
        ltEmitter.setFileId(lfID);
        ltEmitter.addToLogicTable(end);
    }

    public void setViewSource(ViewSource viewSource) {
        vs = viewSource;
    }

    public int getLfID() {
        return lfID;
    }

    public void setLfID(int lfID) {
        this.lfID = lfID;
    }

}
