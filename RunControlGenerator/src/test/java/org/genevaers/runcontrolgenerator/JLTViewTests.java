package org.genevaers.runcontrolgenerator;

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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

import org.genevaers.compilers.extract.astnodes.ExtractAST2Dot;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.emitters.LogicTableEmitter;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableNV;
import org.genevaers.repository.RepoHelper;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LRIndex;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.runcontrolgenerator.compilers.JLTTreeGenerator;
import org.junit.jupiter.api.Test;

public class JLTViewTests {

    @Test void testBuildSimpleJoinView() {
        //This test is at the JLTGenerator level
        Repository.setGenerationTime(Calendar.getInstance().getTime());
        LogicTableEmitter jltEmitter = new LogicTableEmitter();
        JLTTreeGenerator jltg = new JLTTreeGenerator(jltEmitter);

        //Make the source LR - simple 1 key 1 field
        LogicalRecord lkTrgLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(lkTrgLR, "Key1", DataType.ALPHANUMERIC, (short)1, (short)10, true);
        LRField r1 = makeField(lkTrgLR, "Ref1", DataType.BINARY, (short)11, (short)4, false);

        makeLF(77, "SimpleJoinView");

        //We need to add to the repo as though the AST had found the field
        LookupPath lk = new LookupPath();
        lk.setTargetLRid(lkTrgLR.getComponentId());
        lk.setTargetLFid(77);
        Repository.getJoinViews().addJLTViewFromLookupField(lk, r1);

        ExtractBaseAST jr = jltg.buildJoinViews();
        assertNotNull(jr);
        ExtractAST2Dot.write(jr, TestHelper.getMR91JoinsdotPath());
        assertTrue(TestHelper.getMR91JoinsdotPath().toFile().exists());
        ExtractBaseAST.setLogicTableEmitter(jltEmitter);
        jltg.emit();
        assertTrue(jltEmitter.getNumberOfRecords() > 0);
        //TODO the order here is suspect
        //We need to sort out the WRDT generation
        String[] expected = new String[]{ "GEN", "HD", "DIM4", "SETC", "RENX", "NV", "DTE", "DTE", "DTC", "WRDT", "ADDC", "ES" };
        TestLTAssertions.assertFuncCodesStartingAt(0, expected, jltEmitter.getLogicTable());
    }
    
    private void makeLF(int id, String name) {
        LogicalFile lf = new LogicalFile();
        lf.setID(id);
        lf.setName(name);
        Repository.getLogicalFiles().add(lf, lf.getID(), lf.getName());
        PhysicalFile pf = new PhysicalFile();
        pf.setComponentId(id);
        pf.setName(name + "PF");
        pf.setDataSetName("");
        lf.addPF(pf);
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
