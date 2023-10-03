package org.genevaers.repository;

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

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Level;

import com.google.common.flogger.FluentLogger;

import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LRIndex;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.jltviews.JLTView;
import org.genevaers.repository.jltviews.ReferenceJoin;
import org.genevaers.utilities.GenevaLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The generation of the JLT LRs is messy and hence deserves its own test suite
 * 
 * From a given source LR two LRs are made for the JLT
 * The genration LR - used to get the data into the REFnnnn data set
 * The RED LR which maps the lookup fields onto the REFnn data.
 * 
 */
public class JLTViewTest {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        java.nio.file.Path target = Paths.get("target/test-logs/");
        target.toFile().mkdirs();
        GenevaLog.initLogger(JLTViewTest.class.getName(), target.resolve(info.getDisplayName()).toString(), Level.FINE);
    }

    @AfterEach
    public void afterEach(TestInfo info){
		GenevaLog.closeLogger(JLTViewTest.class.getName());
    }

    @Test
    void testMakeSimpleJLTView() {
        //Make the source LR - simple 1 key 1 field
        LogicalRecord srcLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(srcLR, "Key1", DataType.ALPHANUMERIC, (short)1, (short)10, true);
        LRField r1 = makeField(srcLR, "Ref1", DataType.BINARY, (short)11, (short)4, false);

        JLTView jv = new ReferenceJoin(1, 1);
        jv.addRefField(r1);
        assertEquals(1, jv.getLRid());
        jv.buildREDLRs();
        LogicalRecord genLR = Repository.getLogicalRecords().get(3);
        LogicalRecord redLR = Repository.getLogicalRecords().get(2);
        showMeTheLR(srcLR);
        showMeTheLR(redLR);
        showMeTheLR(genLR);

        assertEquals("Ref LR for Lookup lr 1", redLR.getName());
        assertEquals(1, redLR.getValuesOfFieldsByID().size());
        assertEquals(1, redLR.getIteratorForFieldsByID().next().getStartPosition());

        assertEquals("Ref Generation LR for Lookup lr 1", genLR.getName());

        //RED LR should just have the one field... f2 transposed
        //Generation should have f1 and f2
        assertEquals(2, genLR.getValuesOfFieldsByID().size());
    }

    @Test
    void testMake2KeysJLTView() {
        //Make the source LR - simple 1 key 1 field
        LogicalRecord srcLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(srcLR, "Key1", DataType.ALPHANUMERIC, (short)1,  (short)10, true);
        LRField k2 = makeField(srcLR, "Key2", DataType.BINARY, (short)11, (short)4,  true);
        LRField r1 = makeField(srcLR, "Ref1", DataType.BCD,   (short)15, (short)4,  false);

        JLTView jv = new ReferenceJoin(1, 1);
        jv.addRefField(r1);
        assertEquals(1, jv.getLRid());
        jv.buildREDLRs();
        LogicalRecord genLR = Repository.getLogicalRecords().get(3);
        LogicalRecord redLR = Repository.getLogicalRecords().get(2);
        showMeTheLR(srcLR);
        showMeTheLR(redLR);
        showMeTheLR(genLR);

        assertEquals("Ref LR for Lookup lr 1", redLR.getName());
        assertEquals(1, redLR.getValuesOfFieldsByID().size());
        assertEquals(1, redLR.getIteratorForFieldsByID().next().getStartPosition());

        assertEquals("Ref Generation LR for Lookup lr 1", genLR.getName());

        //RED LR should just have the one field... f2 transposed
        //Generation should have f1 and f2
        assertEquals(3, genLR.getValuesOfFieldsByID().size());
    }

    @Test
    void testMake1Key2SeparateFieldsJLTView() {
        LogicalRecord srcLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(srcLR, "Key1", DataType.ALPHANUMERIC, (short)1,  (short)10, true);
        LRField r1 = makeField(srcLR, "Ref1", DataType.BCD,   (short)15, (short)4, false);
        LRField r2 = makeField(srcLR, "Ref2", DataType.ZONED, (short)28, (short)8, false);

        JLTView jv = new ReferenceJoin(1, 1);
        jv.addRefField(r2);
        jv.addRefField(r1);
        assertEquals(1, jv.getLRid());
        jv.buildREDLRs();
        LogicalRecord genLR = Repository.getLogicalRecords().get(3);
        LogicalRecord redLR = Repository.getLogicalRecords().get(2);
        showMeTheLR(srcLR);
        showMeTheLR(redLR);
        showMeTheLR(genLR);

        assertEquals("Ref LR for Lookup lr 1", redLR.getName());
        assertEquals(2, redLR.getValuesOfFieldsByID().size());
        Iterator<LRField> rfi = redLR.getIteratorForFieldsByID();
        assertEquals(1, rfi.next().getStartPosition());
        assertEquals(5, rfi.next().getStartPosition());

        assertEquals("Ref Generation LR for Lookup lr 1", genLR.getName());

        //RED LR should just have the one field... f2 transposed
        //Generation should have f1 and f2
        assertEquals(3, genLR.getValuesOfFieldsByID().size());
        Iterator<LRField> gfi = genLR.getIteratorForFieldsByID();
        assertEquals(1, gfi.next().getStartPosition());
        assertEquals(11, gfi.next().getStartPosition());
        assertEquals(15, gfi.next().getStartPosition());
    }

    @Test
    void testMake2SeparateKeys2SeparateFieldsJLTView() {
        //Make the source LR - simple 1 key 1 field
        LogicalRecord srcLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(srcLR, "Key1", DataType.ALPHANUMERIC, (short)1,  (short)10, true);
        LRField k2 = makeField(srcLR, "Key2", DataType.ALPHANUMERIC, (short)33, (short)7,  true);
        LRField r1 = makeField(srcLR, "Ref1", DataType.BCD,   (short)15, (short)4, false);
        LRField r2 = makeField(srcLR, "Ref2", DataType.ZONED, (short)53, (short)8, false);

        JLTView jv = new ReferenceJoin(1, 1);
        jv.addRefField(r2);
        jv.addRefField(r1);
        assertEquals(1, jv.getLRid());
        jv.buildREDLRs();
        LogicalRecord genLR = Repository.getLogicalRecords().get(3);
        LogicalRecord redLR = Repository.getLogicalRecords().get(2);
        showMeTheLR(srcLR);
        showMeTheLR(redLR);
        showMeTheLR(genLR);

        assertEquals("Ref LR for Lookup lr 1", redLR.getName());
        assertEquals(2, redLR.getValuesOfFieldsByID().size());
        Iterator<LRField> rfi = redLR.getIteratorForFieldsByID();
        assertEquals(1, rfi.next().getStartPosition());
        assertEquals(5, rfi.next().getStartPosition());

        assertEquals("Ref Generation LR for Lookup lr 1", genLR.getName());

        //RED LR should just have the one field... f2 transposed
        //Generation should have f1 and f2
        assertEquals(4, genLR.getValuesOfFieldsByID().size());
        Iterator<LRField> gfi = genLR.getIteratorForFieldsByID();
        assertEquals(1, gfi.next().getStartPosition());
        assertEquals(11, gfi.next().getStartPosition());
        assertEquals(18, gfi.next().getStartPosition());
        assertEquals(22, gfi.next().getStartPosition());
    }

    @Test
    void testMake2SeparateKeys2SeparateFields1RedefinedJLTView() {
        //Make the source LR - simple 1 key 1 field
        LogicalRecord srcLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(srcLR, "Key1", DataType.ALPHANUMERIC, (short)1,  (short)10, true);
        LRField k2 = makeField(srcLR, "Key2", DataType.ALPHANUMERIC, (short)33, (short)7,  true);
        LRField r1 = makeField(srcLR, "Ref1", DataType.BCD,   (short)15, (short)4, false);
        LRField r2 = makeField(srcLR, "Ref2", DataType.ZONED, (short)53, (short)8, false);
        LRField r3 = makeField(srcLR, "Rdf1", DataType.BCD,   (short)16, (short)2, false);

        JLTView jv = new ReferenceJoin(1, 1);
        jv.addRefField(r2);
        jv.addRefField(r1);
        jv.addRefField(r3);
        assertEquals(1, jv.getLRid());
        jv.buildREDLRs();
        LogicalRecord genLR = Repository.getLogicalRecords().get(3);
        LogicalRecord redLR = Repository.getLogicalRecords().get(2);
        showMeTheLR(srcLR);
        showMeTheLR(redLR);
        showMeTheLR(genLR);

        assertEquals("Ref LR for Lookup lr 1", redLR.getName());
        assertEquals(3, redLR.getValuesOfFieldsByID().size());
        Iterator<LRField> rfi = redLR.getIteratorForFieldsByID();
        assertEquals(1, rfi.next().getStartPosition());
        assertEquals(2, rfi.next().getStartPosition());
        assertEquals(5, rfi.next().getStartPosition());

        assertEquals("Ref Generation LR for Lookup lr 1", genLR.getName());

        assertEquals(4, genLR.getValuesOfFieldsByID().size());
        Iterator<LRField> gfi = genLR.getIteratorForFieldsByID();
        assertEquals(1, gfi.next().getStartPosition());
        assertEquals(11, gfi.next().getStartPosition());
        assertEquals(18, gfi.next().getStartPosition());
        assertEquals(22, gfi.next().getStartPosition());
    }

    @Test
    void testMake2SeparateKeys2SeparateFields3RedefinedJLTView() {
        //Make the source LR - simple 1 key 1 field
        LogicalRecord srcLR = Repository.makeLR("SourceLR");
        LRField k1 = makeField(srcLR, "Key1", DataType.ALPHANUMERIC, (short)1,  (short)10, true);
        LRField k2 = makeField(srcLR, "Key2", DataType.ALPHANUMERIC, (short)33, (short)7,  true);
        LRField r1 = makeField(srcLR, "Ref1", DataType.BCD,   (short)15, (short)4, false);
        LRField r2 = makeField(srcLR, "Ref2", DataType.ZONED, (short)53, (short)8, false);
        LRField r3 = makeField(srcLR, "Rdf1", DataType.ZONED, (short)53, (short)4, false);
        LRField r4 = makeField(srcLR, "Rdf2", DataType.ZONED, (short)57, (short)2, false);
        LRField r5 = makeField(srcLR, "Rdf3", DataType.ZONED, (short)59, (short)2, false);

        JLTView jv = new ReferenceJoin(1, 1);
        jv.addRefField(r2);
        jv.addRefField(r1);
        jv.addRefField(r3);
        jv.addRefField(r5);
        jv.addRefField(r4);
        assertEquals(1, jv.getLRid());
        jv.buildREDLRs();
        LogicalRecord genLR = Repository.getLogicalRecords().get(3);
        LogicalRecord redLR = Repository.getLogicalRecords().get(2);
        showMeTheLR(srcLR);
        showMeTheLR(redLR);
        showMeTheLR(genLR);

        assertEquals("Ref LR for Lookup lr 1", redLR.getName());
        assertEquals(5, redLR.getValuesOfFieldsByID().size());
        Iterator<LRField> rfi = redLR.getIteratorForFieldsByID();
        assertEquals(1, rfi.next().getStartPosition());
        assertEquals(5, rfi.next().getStartPosition());
        assertEquals(5, rfi.next().getStartPosition());
        assertEquals(9, rfi.next().getStartPosition());
        assertEquals(11, rfi.next().getStartPosition());

        assertEquals("Ref Generation LR for Lookup lr 1", genLR.getName());

        assertEquals(4, genLR.getValuesOfFieldsByID().size());
        Iterator<LRField> gfi = genLR.getIteratorForFieldsByID();
        assertEquals(1, gfi.next().getStartPosition());
        assertEquals(11, gfi.next().getStartPosition());
        assertEquals(18, gfi.next().getStartPosition());
        assertEquals(22, gfi.next().getStartPosition());
    }

    private void showMeTheLR(LogicalRecord lr) {
        logger.atFine().log("LR %s", lr.getName());
        Iterator<LRField> fi = lr.getIteratorForFieldsByID();
        while(fi.hasNext()) {
            LRField f = fi.next();
            logger.atFine().log("ID  %3d name %8s pos %3d len %3d",
            f.getComponentId(),
            f.getName(),
            f.getStartPosition(),
            f.getLength());
        }       
        logger.atFine().log(" ");
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
