package org.genevaers.compilers.extract.emitters.assignmentemitters;

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


import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.NumAtomAST;
import org.genevaers.compilers.extract.astnodes.StringAtomAST;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableF1;

public class AssignmentConstEmitter extends AssignmentEmitter{

    @Override
    public LTFileObject makeLTEntry(ExtractBaseAST lhs, ExtractBaseAST rhs) {
        LTFileObject ltEntry = null;
        if(rhs.getType() == ASTFactory.Type.NUMATOM) {
            ltEntry = ((ColumnAST)lhs).getConstLtEntry(String.valueOf(((NumAtomAST)rhs).getValue()));
        } else {
            ltEntry = ((ColumnAST)lhs).getConstLtEntry(((StringAtomAST)rhs).getValue());
        }
        ((LogicTableF1)ltEntry).getArg().setLogfileId(getLtEmitter().getFileId());        
        return ltEntry;
        
    }
    
}
