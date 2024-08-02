/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Calendar;
import java.util.logging.Level;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.LTLogger;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.genevaio.ltfile.LogicTableNV;
import org.genevaers.genevaio.ltfile.LogicTableNameF1;
import org.genevaers.genevaio.ltfile.LogicTableRE;
import org.genevaers.genevaio.ltfile.LogicTableWR;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.ComponentCollection;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;
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
class RunCompilerTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        ExtractPhaseCompiler.reset();
        Repository.clearAndInitialise();
        ExtractBaseAST.setCurrentColumnNumber((short)0);
        ExtractBaseAST.setCurrentAccumNumber(0);
        Repository.setGenerationTime(Calendar.getInstance().getTime());
        RecordParser.clearAndInitialise();
        LtFactoryHolder.getLtFunctionCodeFactory().clearAccumulatorMap();
        java.nio.file.Path target = Paths.get("target/test-logs/");
        target.toFile().mkdirs();
        GenevaLog.initLogger(RunCompilerTest.class.getName(), target.resolve(info.getDisplayName()).toString(), Level.FINE);
    }

    @AfterEach
    public void afterEach(TestInfo info){
		GenevaLog.closeLogger(RunCompilerTest.class.getName());
    }

    
    @Test void testNegativeConstant() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = -25");
        String[] expected = new String[]{ "DTC", "WRDT"};
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 dtc =  (LogicTableF1) xlt.getFromPosition(4);
        assertEquals("-25", dtc.getArg().getValue().getString());
    }

    @Test void testAssignments() {
        LogicTable xlt = runFromXMLOverrideLogic(12075, TestHelper.ASSIGNMENTS, "");
        assertTrue(xlt.getNumberOfRecords() > 3);
    }


    //Write Tests
    @Test void testWriteSourceData() {
        //WRITE(SOURCE=DATA,DEST=DEFAULT)
        LogicTable xlt = runFromXMLOverrideOutputLogic(9956, TestHelper.ONE_COL, "");
        String[] expected = new String[]{ "DTC", "WRDT" };
        int expectedGotos[][] = {{}};
        //Need to confirm other aspects of the WR
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(0, wr.getSuffixSeqNbr());
    }

    @Test void testWriteSourceInput() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(9956, TestHelper.ONE_COL, "WRITE(SOURCE=INPUT,DEST=DEFAULT)");
        String[] expected = new String[]{ "DTC", "WRIN" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testWriteSourceView() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(9956, TestHelper.ONE_COL, "WRITE(SOURCE=VIEW,DEST=DEFAULT)");
        String[] expected = new String[]{ "DTC", "WRXT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testPlainWrite() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(9956, TestHelper.ONE_COL, "WRITE()");
        String[] expected = new String[]{ "DTC", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testWriteDestExtract() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(9956, TestHelper.ONE_COL, "WRITE(SOURCE=DATA,DEST=EXTRACT=3)");
        String[] expected = new String[]{ "DTC", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(3, wr.getSuffixSeqNbr());
    }

    @Test void testWriteBadSource() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(9956, TestHelper.ONE_COL, "WRITE(SOURCE=banana,DEST=DEFAULT)");
        //String[] expected = new String[]{ "DTC", "WRDT" };
        //int expectedGotos[][] = {{}};
        assertEquals(0, xlt.getNumberOfRecords());
        assertTrue(Repository.getCompilerErrors().size() > 0);
    }

    @Test void testWriteFileExit() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(469, wr.getWriteExitId());
    }

    @Test void testWriteFileExitWithArg() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA, USEREXIT=({IansTestExit},\"fred\"))");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(469, wr.getWriteExitId());
        assertEquals("fred", wr.getWriteExitParms());
    }

    @Test void testWriteFileDestFile() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=FILE={ExtractOut.ExtractOut})");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(8549, wr.getOutputFileId());
    }

    @Test void testWriteFileDestFileBadLF() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=FILE={badLF.ExtractOut})");
        String[] expected = new String[]{};
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(0, expected, expectedGotos, xlt);
        //Look for correct error message
    }

    @Test void testWriteFileDestFileBadPF() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=FILE={ExtractOut.rubbish})");
        String[] expected = new String[]{};
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(0, expected, expectedGotos, xlt);
        //Look for correct error message
    }

    @Test void testWriteFileDestExtract() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=EXTRACT=5)");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(5, wr.getSuffixSeqNbr());
    }

    @Test void testWriteFileDestExtractWithExit() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=EXTRACT=5, USEREXIT=({IansTestExit},\"magic\"))");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(5, wr.getSuffixSeqNbr());
        assertEquals(469, wr.getWriteExitId());
        assertEquals("magic", wr.getWriteExitParms());
    }

    @Test void testWriteFileDestDefault() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=DEFAULT)");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testWriteKitchenSink() {
        LogicTable xlt = runFromXMLOverrideOutputLogic(12052, TestHelper.ONE_COL_WRITE_EXIT, "WRITE(SOURCE=DATA,DEST=FILE={ExtractOut.ExtractOut}, USEREXIT=({IansTestExit},\"fred\"))");
        String[] expected = new String[]{ "DTE", "WRDT" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableWR wr =  (LogicTableWR) xlt.getFromPosition(5);
        assertEquals(0, wr.getSuffixSeqNbr());
        assertEquals(469, wr.getWriteExitId());
        assertEquals("fred", wr.getWriteExitParms());
        assertEquals(8549, wr.getOutputFileId());
    }

    //TODO Need tests to deal with symbol errors
    // bad name
    // bad type
    // bad lookup name

    @Test void testIfORField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "IF {Binary1} > 0 OR {ZONED} > 0 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEC", "CFEC", "DTC", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,6,5},{5,6,8},{7,9,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testIfANDField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "IF {Binary1} > 0 AND {ZONED} > 0 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEC", "CFEC", "DTC", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,5,8},{5,6,8},{7,9,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testIfField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "IF {Binary1} > 0 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEC", "DTC", "GOTO", "DTC" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSelectFilter() {
        LogicTable xlt = runFromXMLOverrideLogic(12040, TestHelper.SELECTIF, "");
        String[] expected = new String[]{ "CFEC", "DTC", "DTE" };
        int expectedGotos[][] = {{4,5,8}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testDemoCustomerOrderSales() {
        LogicTable xlt = runFromXMLOverrideLogic(10702, TestHelper.ORDER_SALES, "");
        assertTrue(xlt.getNumberOfRecords() > 0);
        LogicTableF1 cfec = (LogicTableF1) xlt.getFromPosition(11);
        assertEquals("CFEC", cfec.getFunctionCode());
        assertEquals(cfec.getArg().getFieldFormat(), DataType.ZONED);
        LogicTableArg arg = cfec.getArg();
        assertEquals(-3, arg.getValue().length());
        assertEquals(10208, arg.getLogfileId());
        LogicTableNameF1 mule = (LogicTableNameF1) xlt.getFromPosition(14);
        assertEquals("MULE", mule.getFunctionCode());
        assertEquals(10208, mule.getArg().getLogfileId());
        assertEquals("g_10702_10208_10249_3_0", mule.getAccumulatorName());
        LogicTableWR wrsu = (LogicTableWR) xlt.getFromPosition(38);
        assertEquals("WRSU", wrsu.getFunctionCode());
        assertEquals(4000, wrsu.getExtrSumRecCnt());
        LogicTableNameF1 sete = (LogicTableNameF1) xlt.getFromPosition(13);
        assertEquals(10208, sete.getArg().getLogfileId());
        LogicTableNameF1 sete21 = (LogicTableNameF1) xlt.getFromPosition(21);
        assertEquals("g_10702_10208_10249_4_0", sete21.getAccumulatorName());
        LogicTableNameF1 cta = (LogicTableNameF1) xlt.getFromPosition(15);
        assertEquals("CTA", cta.getFunctionCode());
        assertEquals(10208, cta.getArg().getLogfileId());
        LogicTableF1 ctc = (LogicTableF1) xlt.getFromPosition(17);
        assertEquals("CTC", ctc.getFunctionCode());
        //Override DateCode
        LogicTableF2 dtl = (LogicTableF2) xlt.getFromPosition(8);
        assertEquals(DateCode.NONE, dtl.getArg1().getFieldContentId());
        assertEquals(DateCode.NONE, dtl.getArg2().getFieldContentId());
        LogicTable jlt = ExtractPhaseCompiler.getJoinLogicTable();
        LogicTableF2 dte =(LogicTableF2) jlt.getFromPosition(7);
        assertEquals(DateCode.NONE, dte.getArg1().getFieldContentId());
        assertEquals(DateCode.NONE, dte.getArg2().getFieldContentId());
    }

    @Test void testDemoOrderByStateSales() {
        LogicTable xlt = runFromXMLOverrideLogic(10714, TestHelper.STATE_SALES, "");
        LTLogger.logRecords(xlt);
        LTLogger.writeRecordsTo(xlt, Paths.get("target/xlt.txt").toString(), "");
        assertTrue(xlt.getNumberOfRecords() > 0);
        LogicTableF2 lkde = (LogicTableF2) xlt.getFromPosition(7);
        assertEquals("LKDE", lkde.getFunctionCode());
        LogicTableArg arg1 = lkde.getArg1();
        assertEquals(DataType.ALPHANUMERIC, arg1.getFieldFormat());
        LogicTableArg arg2 = lkde.getArg2();
        assertEquals(DataType.BINARY, arg2.getFieldFormat());
        LogicTableF2 skl = (LogicTableF2) xlt.getFromPosition(9);
        assertEquals("SKL", skl.getFunctionCode());
        LogicTableF1 skc = (LogicTableF1) xlt.getFromPosition(11);
        assertEquals("SKC", skc.getFunctionCode());

        LogicTable jlt = ExtractPhaseCompiler.getJoinLogicTable();
        LogicTableF2 dte = (LogicTableF2) jlt.getFromPosition(8);
        assertEquals(400618, dte.getArg1().getFieldId());
        LogicTableF1 dtc = (LogicTableF1) jlt.getFromPosition(20);
        assertEquals("10", dtc.getArg().getValue().getString());
        LogicTableF1 effdates = (LogicTableF1) jlt.getFromPosition(23);
        assertEquals("2", effdates.getArg().getValue().getString());
    }

    @Test void testDemoExtractWithLookups() {
        LogicTable xlt = runFromXMLOverrideLogic(10689, TestHelper.DEMO_LOOKUPS, "");
        assertTrue(xlt.getNumberOfRecords() > 46);
        LogicTableF1 join2 = (LogicTableF1) xlt.getFromPosition(26);
        assertEquals("2", join2.getArg().getValue().getString());
        LogicTableF1 join = (LogicTableF1) xlt.getFromPosition(32);
        assertEquals("JOIN", join.getFunctionCode());
        LogicTable jlt = ExtractPhaseCompiler.getJoinLogicTable();
        LogicTableF2 dte = (LogicTableF2) jlt.getFromPosition(7);
        assertEquals(DateCode.NONE, dte.getArg1().getFieldContentId());
        assertEquals(DateCode.NONE, dte.getArg2().getFieldContentId());
        LogicTableNV nv = (LogicTableNV) jlt.getFromPosition(11);
        assertEquals(9000003, nv.getViewID());
        LTRecord addc = jlt.getFromPosition(35);
        assertEquals("ADDC", addc.getFunctionCode());
    }

    @Test void testDemoProdState() {
        LogicTable xlt = runFromXMLOverrideLogic(10715, TestHelper.PROD_STATE, "");
        assertTrue(xlt.getNumberOfRecords() > 0);
        LogicTableF1 cte = (LogicTableF1) xlt.getFromPosition(5);
        assertEquals("CTE", cte.getFunctionCode());
        LogicTableF1 lklr = (LogicTableF1) xlt.getFromPosition(15);
        assertEquals("LKLR", lklr.getFunctionCode());
        LogicTableF1 join = (LogicTableF1) xlt.getFromPosition(12);
        assertEquals("JOIN", join.getFunctionCode());
        assertEquals(join.getArg().getLogfileId(), 10201); //C++ MR91 has it wrong
        assertEquals(18, join.getGotoRow1());
        assertEquals(20, join.getGotoRow2());
        LogicTableRE lusm = (LogicTableRE) xlt.getFromPosition(14);
        assertEquals("LUSM", lusm.getFunctionCode());
        assertEquals(15, lusm.getGotoRow1()); 
        assertEquals(20, lusm.getGotoRow2()); 
        LogicTableF2 lkl = (LogicTableF2) xlt.getFromPosition(16);
        assertEquals("LKL", lkl.getFunctionCode());
        assertEquals(10201, lkl.getArg1().getLogfileId());
        assertEquals(400585, lkl.getArg1().getFieldId());
        assertEquals(1, lkl.getArg1().getStartPosition());

        LogicTable jlt = ExtractPhaseCompiler.getJoinLogicTable();
//        assertEquals(75, jlt.getNumberOfRecords());
    }

    @Test void testSkeAndDt() {
        LogicTable xlt = runFromXMLOverrideLogic(11074, TestHelper.SKE_DT, "");
        assertTrue(xlt.getNumberOfRecords() > 3);
    }

    @Test void testBigass() {
        LogicTable xlt = runFromXMLOverrideLogic(11074, TestHelper.MERGESS, "");
        assertTrue(xlt.getNumberOfRecords() > 3);
    }

    @Test 
    void testSortTitle() {
        LogicTable xlt = runFromXMLOverrideLogic(11074, TestHelper.SKT_LOOKUP, "");
        assertTrue(xlt.getNumberOfRecords() > 3);
        ComponentCollection<ViewNode> vws = Repository.getViews();
        assertEquals(6, vws.size());
        LogicTableNV nv = (LogicTableNV) xlt.getFromPosition(3);
        assertEquals(27, nv.getSortTitleLen());
        LogicTableF1 lklr = (LogicTableF1) xlt.getFromPosition(5);
        assertEquals("LKLR", lklr.getFunctionCode());
        assertEquals("0", lklr.getArg().getValue().getString());
        LogicTableF1 kslk = (LogicTableF1) xlt.getFromPosition(7);
        assertEquals("KSLK", kslk.getFunctionCode());
        LogicTable jlt = ExtractPhaseCompiler.getJoinLogicTable();
        LogicTableNV jnv = (LogicTableNV)jlt.getFromPosition(15);
        assertEquals(0, jnv.getSuffixSeqNbr());
        LogicTableF0 es = (LogicTableF0)jlt.getFromPosition(36);
        assertEquals("ES", es.getFunctionCode());
    }

}
