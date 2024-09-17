/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.logging.Level;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ColumnAssignmentASTNode;
import org.genevaers.compilers.extract.astnodes.ErrorAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.LTLogger;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableName;
import org.genevaers.genevaio.ltfile.LogicTableNameF1;
import org.genevaers.genevaio.ltfile.LogicTableNameValue;
import org.genevaers.genevaio.ltfile.LogicTableRE;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
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
class RunCompilerArithTests extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        ExtractPhaseCompiler.reset();
        Repository.clearAndInitialise();
        Repository.setGenerationTime(Calendar.getInstance().getTime());
        LtFactoryHolder.getLtFunctionCodeFactory().clearAccumulatorMap();
        RecordParser.clearAndInitialise();
        java.nio.file.Path target = Paths.get("target/test-logs/");
        target.toFile().mkdirs();
        GenevaLog.initLogger(RunCompilerArithTests.class.getName(), target.resolve(info.getDisplayName()).toString(), Level.FINE);
    }

    @AfterEach
    public void afterEach(TestInfo info){
		GenevaLog.closeLogger(RunCompilerArithTests.class.getName());
        LtFactoryHolder.closeLtFunctionCodeFactory();
    }
    
// Constants 
    @Test void testFieldPlusConstant() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} + 1");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableName dimn = (LogicTableName) xlt.getFromPosition(4);
        //Horible old accumulator naming....
        assertEquals("g_9956_1506_1762_1_0", dimn.getAccumulatorName());
        LogicTableNameF1 sete = (LogicTableNameF1) xlt.getFromPosition(5);
        assertEquals("g_9956_1506_1762_1_0", sete.getAccumulatorName());
        LogicTableNameValue addc = (LogicTableNameValue) xlt.getFromPosition(6);
        assertEquals("g_9956_1506_1762_1_0", addc.getTableName());
        LogicTableNameF1 dta = (LogicTableNameF1) xlt.getFromPosition(7);
        assertEquals("g_9956_1506_1762_1_0", dta.getAccumulatorName());
     }

     @Test void testFieldTimesConstant() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = (((({Binary1} * 3))))");
        String[] expected = new String[]{ "DIMN", "SETE", "MULC", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testFieldMinusConstant() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} - 3");
        String[] expected = new String[]{ "DIMN", "SETE", "SUBC", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testFieldDivideConstant() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} / 3");
        String[] expected = new String[]{ "DIMN", "SETE", "DIVC", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

// Fields
     @Test void testFieldPlusField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} + {Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testFieldMinusField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} - {Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "SUBE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testFieldTimesField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} * {Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "MULE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testFieldDivideField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} / {Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "DIVE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

// Lookups
    @Test void testFieldPlusLookupField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "COLUMN = {Binary1} + {AllTypeLookup.Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDL", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
        LogicTable jlt = ExtractPhaseCompiler.getJoinLogicTable();
        String[] jexpected = new String[]{ "HD", "DIM4", "SETC", "RENX" };
        int jexpectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(1, jexpected, jexpectedGotos, jlt);
        LogicTableRE renx = (LogicTableRE)jlt.getFromPosition(4);
        assertEquals(1670, renx.getFileId());
        LogicTableNameValue addc = (LogicTableNameValue) jlt.getFromPosition(10);
        assertEquals("lRecordCount", addc.getTableName());
    }

    @Test void testFieldMinusLookupField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "COLUMN = {Binary1} - {AllTypeLookup.Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "SUBL", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testFieldTimesLookupField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "COLUMN = {Binary1} * {AllTypeLookup.Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "MULL", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testFieldDivideLookupField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "COLUMN = {Binary1} / {AllTypeLookup.Binary2}");
        String[] expected = new String[]{ "DIMN", "SETE", "DIVL", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

// Accumulators - needs BIDMAS to work too
    @Test void testFieldPlusAccumulator() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} + {Binary2} * 3");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETE", "MULC", "DIMN", "SETE", "ADDA", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableNameValue addc = (LogicTableNameValue) xlt.getFromPosition(9);
        assertEquals("g_9956_1506_1762_1_1", addc.getTableName());        
        assertEquals("g_9956_1506_1762_1_0", addc.getValue());        
    }

    @Test void testFieldMinusAccumulator() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} - {Binary2} * 3");
        String[] expected = new String[]{ "DIMN", "SETE", "MULC", "DIMN", "SETE", "SUBA", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testFieldTimesAccumulator() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} * ({Binary2} + 3)");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "DIMN", "SETE", "MULA", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableNameValue mula = (LogicTableNameValue) xlt.getFromPosition(9);
        assertEquals("g_9956_1506_1762_1_1", mula.getTableName());        
        assertEquals("g_9956_1506_1762_1_0", mula.getValue());        
    }

    @Test void testFieldDivideAccumulator() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} /  ({Binary2} + 3)");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "DIMN", "SETE", "DIVA", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    // Const and Field RHS Operand
    @Test void testConstantPlusField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = 3 + {Binary2}");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETC", "ADDE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testConstantSubField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = 3 - {Binary2}");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETC", "SUBE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testConstantTimesField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = 3 * {Binary2}");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETC", "MULE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testConstantDivideField() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = 3 / {Binary2}");
        logger.atInfo().log("XLT\n" + LTLogger.logRecords(xlt));
        String[] expected = new String[]{ "DIMN", "SETC", "DIVE", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

    // @Test void testFieldTimesAccumulator() {
    //     LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = ({Binary1} + 1) * {Binary2}");
    //     String[] expected = new String[]{ "DIMN", "DIMN", "SETE", "ADDC", "SETA", "MULE", "DTA" };
    //     int expectedGotos[][] = {{}};
    //     TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    //     LogicTableNameValue seta = (LogicTableNameValue) xlt.getFromPosition(8);
    //     assertEquals("g_9956_1506_1762_1_0", seta.getTableName());        
    //     assertEquals("g_9956_1506_1762_1_1", seta.getValue());        
    // }

    // @Test void testFieldDivideAccumulator() {
    //     LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = ({Binary1} + 1)  / {Binary2}");
    //     String[] expected = new String[]{ "DIMN", "DIMN", "SETE", "ADDC", "SETA", "DIVE", "DTA" };
    //     int expectedGotos[][] = {{}};
    //     TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    // }
// Prioir Fields
// Column References

// BIDMAS combos

     @Test void testBIMDAS() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} + {Binary2} * 99");
        String[] expected = new String[]{ "DIMN", "SETE", "MULC", "DIMN", "SETE", "ADDA", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

     @Test void testBIMDAS2() throws IOException {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} * 33 - {Binary2} * 99");
        String[] expected = new String[]{ "DIMN", "SETE", "MULC", "DIMN", "SETE", "MULC", "DIMN", "SETA", "SUBA", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }

// 

     @Test void testFieldPlusChain() throws IOException {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Binary1} + {Binary2} - 99");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDE", "SUBC", "DTA" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
     }


    @Test void testFieldAssignment() throws IOException {
        TestHelper.setupWithOneColumnView();
        readConfigAndBuildRepo();
        TestHelper.setColumn1Logic(9956, "COLUMN = {ZONED}");
        RunControlConfigration.setDotFilter("9956", "1,2", "N");
        ExtractBaseAST root = (ExtractBaseAST)CompileAndGenerateDots();
        ColumnAssignmentASTNode colAss = (ColumnAssignmentASTNode) root.getChildNodesOfType(ASTFactory.Type.COLUMNASSIGNMENT).get(0);
        assertEquals("ZONED", ((FieldReferenceAST)colAss.getFirstLeafNode()).getName());
    }

    @Test void testStringConstAssignment() throws IOException {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = \"Ginger\"");
        String[] expected = new String[]{ "DTC" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 dtc =  (LogicTableF1) xlt.getFromPosition(4);
        assertEquals("Ginger", dtc.getArg().getValue().getString());
    }

    //     //This gets us into the whole Cookie debacle 
    // @Test void testRundayAssignment() throws IOException {
    //     LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = RUNDAY()");
    //     String[] expected = new String[]{ "DTC" };
    //     int expectedGotos[][] = {{}};
    //     TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    //     LogicTableF1 dtc =  (LogicTableF1) xlt.getFromPosition(4);
    //     assertEquals("Ginger", dtc.getArg().getValue());
    // }

    @Test void testNumricConstAssignment() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, "COLUMN = 25");
        String[] expected = new String[]{ "DTC" };
        int expectedGotos[][] = {{}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 dtc =  (LogicTableF1) xlt.getFromPosition(4);
        assertEquals("25", dtc.getArg().getValue().getString());
    }

    @Test void testNoneExistingFieldAssignment() throws IOException {
        TestHelper.setupWithOneColumnView();
        readConfigAndBuildRepo();
        TestHelper.setColumn1Logic(9956, "COLUMN = {Rubbish}");
        RunControlConfigration.setDotFilter("9956", "1", "N");
        ExtractBaseAST root = (ExtractBaseAST) CompileAndGenerateDots();
        assertTrue(Repository.getCompilerErrors().size() > 0);
    }

    @Test void testBadSyntaxAssignment() throws IOException {
        TestHelper.setupWithOneColumnView();
        readConfigAndBuildRepo();
        TestHelper.setColumn1Logic(9956, "COLUMN = oops}");
        RunControlConfigration.setDotFilter("9956", "1", "N");
        ExtractBaseAST root = (ExtractBaseAST) CompileAndGenerateDots();
        assertTrue(Repository.getCompilerErrors().size() > 0);
    }

    // @Test void testEventArith() {
    //     LogicTable xlt = runFromXMLOverrideLogic(11250, TestHelper.ALL_ARITH, "");
    //     assertTrue(xlt.getNumberOfRecords() > 3);
    //     LogicTableNameF1 dta =  (LogicTableNameF1) xlt.getFromPosition(35);
    //     assertTrue(dta.getArg().isSignedInd());
    // }


}
