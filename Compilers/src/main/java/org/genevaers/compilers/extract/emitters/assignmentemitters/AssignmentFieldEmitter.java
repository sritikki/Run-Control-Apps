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


import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.ExtractArea;

public class AssignmentFieldEmitter extends AssignmentEmitter{

    @Override
    public LTFileObject makeLTEntry(ExtractBaseAST lhs, ExtractBaseAST rhs) {
        ViewColumn vc = ((ColumnAST)lhs).getViewColumn();
        LTFileObject ltEntry = null;
        LRField field = ((FieldReferenceAST)rhs).getRef();
        if(field != null) {
            ltEntry = ((ColumnAST)lhs).getFieldLtEntry(field);
            LogicTableArg arg;
            if(vc.getExtractArea() == ExtractArea.AREACALC) {
                arg = ((LogicTableF1)ltEntry).getArg();        
            } else {
                arg = ((LogicTableF2)ltEntry).getArg1();  
            }
            arg.setLogfileId(getLtEmitter().getFileId());   
            flipDataTypeIfFieldAlphanumeric(arg, field);
        }
        return ltEntry;
    }

    private void flipDataTypeIfFieldAlphanumeric(LogicTableArg arg, LRField field) {
        if(field.getDatatype() == DataType.ALPHANUMERIC) {
            arg.setFieldFormat(DataType.ZONED);
        }
    }
    
}
