/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.logging.Level;

import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.genevaio.ltfile.LogicTableNV;
import org.genevaers.genevaio.ltfile.LogicTableRE;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.utilities.GenevaLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

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


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.google.common.flogger.FluentLogger;

/**
 *  Build on the SPO Tests
 *  Check the AST Tree built as a result
 */
class RunCompilerLookupTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        RecordParser.clearAndInitialise();
        java.nio.file.Path target = Paths.get("target/test-logs/");
        target.toFile().mkdirs();
        GenevaLog.initLogger(RunCompilerTest.class.getName(), target.resolve(info.getDisplayName()).toString(), Level.FINE);
    }

    @AfterEach
    public void afterEach(TestInfo info){
		GenevaLog.closeLogger(RunCompilerTest.class.getName());
    }
    
    @Test void testEffectiveDateViaFieldLookupfield() {
        LogicTable xlt = runFromXMLOverrideLogic(12046, TestHelper.EFF_DATE_SYM_LOOKUP, "COLUMN = {SymbolKeyEffDateLookup.Description, {Binary4} }");
        String[] expected = new String[]{ "JOIN", "LKS", "LKS", "LKS", "LKDE", "LUSM", "DTL", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,10,12},{9,10,12},{11,13,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF2 lkde = (LogicTableF2)xlt.getFromPosition(8);
        assertEquals("BINARY", lkde.getArg1().getFieldFormat().toString());
        assertEquals("NONE", lkde.getArg1().getFieldContentId().toString());
    }

    @Test void testEffectiveDateLookupfield() {
        LogicTable xlt = runFromXMLOverrideLogic(12046, TestHelper.EFF_DATE_SYM_LOOKUP, "COLUMN = {SymbolKeyEffDateLookup.Description }");
        String[] expected = new String[]{ "JOIN", "LKS", "LKS", "LKS", "LKDC", "LUSM", "DTL", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,10,12},{9,10,12},{11,13,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSymValueLookupfield() {
        LogicTable xlt = runFromXMLOverrideLogic(12045, TestHelper.SYM_LOOKUP, "COLUMN = {SymbolKeyLookup.Description; $Sym2=\"pppqqq\"}");
        String[] expected = new String[]{ "JOIN", "LKS", "LKS", "LKS", "LUSM", "DTL", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,9,11},{8,9,11},{10,12,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 lks = (LogicTableF1)xlt.getFromPosition(6);
        //Gotta come back to this
        //assertEquals("pppqqq", ArgHelper.getArgString(lks.getArg()));
    }

    @Test void testSymLookupfield() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.SYM_LOOKUP, "");
        String[] expected = new String[]{ "JOIN", "LKS", "LKS", "LKS", "LUSM", "DTL", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,9,11},{8,9,11},{10,12,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testLookupfield() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "");
        String[] expected = new String[]{ "JOIN", "LKE", "LUSM", "DTL", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,7,9},{6,7,9},{8,10,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTable jlt = comp.getJoinLogicTable();
        String[] jexpected = new String[]{ "HD", "DIM4", "SETC", "RENX" };
        int jexpectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(1, jexpected, jexpectedGotos, jlt);
        LogicTableRE renx = (LogicTableRE)jlt.getFromPosition(4);
        assertEquals(1670, renx.getFileId());
        LogicTableNV redNV = (LogicTableNV) jlt.getFromPosition(5);
        assertEquals(9000001, redNV.getViewId());
        assertEquals(32, redNV.getDtAreaLen());
        LogicTableF2 dtl = (LogicTableF2) xlt.getFromPosition(7);
        assertEquals(1670, dtl.getArg1().getLogfileId());
        assertEquals(1801, dtl.getArg1().getLrId());
        assertEquals(96901, dtl.getArg1().getFieldId());
        assertEquals(1, dtl.getArg1().getStartPosition());
        LogicTableNV rehNV = (LogicTableNV) jlt.getFromPosition(12);
        assertEquals(9000002, rehNV.getViewId());
        ViewNode vn = Repository.getViews().get(9000001);
        assertEquals("REFR001",vn.getOutputFile().getOutputDDName());

    }

    @Test void testIfLookupField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "IF {AllTypeLookup.ZONED} > 0 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "JOIN", "LKE", "LUSM", "CFLC", "DTC", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,7,10},{6,7,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testLookupExit() {
        LogicTable xlt = runFromXMLOverrideLogic(9829, TestHelper.LOOKUP_EXIT, "");
        assertTrue(Repository.getJoinViews().getExitJLTViews().getNumberOfJoins() > 0);
        LogicTableF1 lklr = (LogicTableF1)xlt.getFromPosition(5);
        assertEquals(0, lklr.getGotoRow1());
        LogicTableRE luex = (LogicTableRE)xlt.getFromPosition(7);
        assertEquals(8, luex.getGotoRow1());
    }


    @Test void testSelectFilterWithJoin() {
        LogicTable xlt = runFromXMLOverrideFilter(10689, TestHelper.DEMO1, "SELECTIF({DEMO_ORDER_ITEM_TO_ORDER_LP.ORDER_DATE_CCYY}=DATE(\"2000\",CCYY))");
        String[] expected = new String[]{ "JOIN" };
        int expectedGotos[][] = {{4,7,37}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

}
