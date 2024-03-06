package org.genevaers.runcontrolgenerator;

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


import org.genevaers.compilers.extract.emitters.LogicTableEmitter;
import org.genevaers.repository.RepoHelper;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LRIndex;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.runcontrolgenerator.compilers.JLTTreeGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JLTEmitTests {

    @Test @Disabled void testEmitJoinView() {
        //This test is at the JLTGenerator level
        LogicTableEmitter jltEmitter = new LogicTableEmitter();
        JLTTreeGenerator jltg = new JLTTreeGenerator(jltEmitter);

        //Make the source LR - simple 1 key 1 field
        LogicalRecord lkTrgLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(lkTrgLR, "Key1", DataType.ALPHANUMERIC, (short)1, (short)10, true);
        LRField r1 = makeField(lkTrgLR, "Ref1", DataType.BINARY, (short)11, (short)4, false);

        //We need to add to the repo as though the AST had found the field
        LookupPath lk = new LookupPath();
        lk.setTargetLRid(lkTrgLR.getComponentId());
        lk.setTargetLFid(77);
        //We haven't really made the LF referred too
        Repository.getJoinViews().addJLTViewFromLookupField(lk, r1);

        jltg.emit();
    }
    
    private LRField makeField(LogicalRecord lr, String fieldName, DataType frmt, short pos, short len, boolean ndx) {
        LRField f = Repository.makeNewField(lr);
        f.setName(fieldName);
        RepoHelper.setField(f, frmt, pos, len);
        if (ndx) {
            LRIndex indx = Repository.makeNewIndex(lr);
            indx.setFieldID(f.getComponentId());
            indx.setName("PK");
        }
        return f;
    }
}
