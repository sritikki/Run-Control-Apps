/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Calendar;
import java.util.logging.Level;

import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ErrorAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableNameValue;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.enums.LtCompareType;
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
 *  SearchFor Tests
 *  The operands can only be Alphanumeric.
 *  So Accumulator and numeric cases should result in an error.
 */

class RunStringProcessingTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        ExtractBaseAST.setCurrentColumnNumber((short)0);
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

    @Test void testBeginsWith() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} BEGINS_WITH \"START\"  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEC" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 cfec = (LogicTableF1) xlt.getFromPosition(4);
        assertEquals(LtCompareType.BEGINS, cfec.getCompareType());
    }

    @Test void testSFEC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} CONTAINS \"START\"  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFEC" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 cfec = (LogicTableF1) xlt.getFromPosition(4);
        assertEquals(LtCompareType.CONTAINS, cfec.getCompareType());
    }

    @Test void testEndsWith() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} ENDS_WITH \"START\"  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEC" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableF1 cfec = (LogicTableF1) xlt.getFromPosition(4);
        assertEquals(LtCompareType.ENDS, cfec.getCompareType());
    }

    @Test void testBothNumericFields() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} CONTAINS {Binary2} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        ErrorAST errs = (ErrorAST) comp.getXltRoot().getFirstNodeOfType(ASTFactory.Type.ERRORS);
        assertTrue(errs.getErrors().size()>0);
        assertTrue(errs.getErrors().get(0).contains("Incompatable"));
    }

    @Test void testLhsNumericField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} CONTAINS {Alphanumeric} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        ErrorAST errs = (ErrorAST) comp.getXltRoot().getFirstNodeOfType(ASTFactory.Type.ERRORS);
        assertTrue(errs.getErrors().size()>0);
        assertTrue(errs.getErrors().get(0).contains("Incompatable"));
    }

    @Test void testRhsNumericField() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} CONTAINS {Binary1} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        ErrorAST errs = (ErrorAST) comp.getXltRoot().getFirstNodeOfType(ASTFactory.Type.ERRORS);
        assertTrue(errs.getErrors().size()>0);
        assertTrue(errs.getErrors().get(0).contains("Incompatable"));
    }

    @Test void testSFCP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF \"2025\" CONTAINS PRIOR({Description}) THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFCP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFCX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF \"2025\" CONTAINS Col.1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFCX" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFEE() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} CONTAINS {Description}  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFEE" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFEP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} CONTAINS PRIOR({Description})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFEP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFEX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Alphanumeric} CONTAINS Col.1  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFEX" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFLL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.Alphanumeric} CONTAINS {AllTypeLookup.Description}  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFLL" };
        int expectedGotos[][] = {{10,11,13}};
        TestLTAssertions.assertFunctionCodesAndGotos(10, expected, expectedGotos, xlt);
    }

    @Test void testSFLP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.Alphanumeric} CONTAINS PRIOR({Description})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFLP" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testSFLX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.Alphanumeric} CONTAINS Col.1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFLX" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testSFPC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Alphanumeric}) CONTAINS \"99\" THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFPC" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFPE() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Alphanumeric}) CONTAINS {Description} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFPE" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFPL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Alphanumeric}) CONTAINS {AllTypeLookup.Description} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFPL" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testSFPP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Alphanumeric}) CONTAINS PRIOR({Description}) THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFPP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFPX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Alphanumeric}) CONTAINS Col.1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFPX" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFXC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 CONTAINS \"2025\"   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFXC" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFXE() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 CONTAINS {Description}   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFXE" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testSFXL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 CONTAINS {AllTypeLookup.Description}   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFXL" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testSFXP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 CONTAINS PRIOR({Description})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFXP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    //Note there is no col.2 - so this will fail once we get there
    @Test void testSFXX() {
        LogicTable xlt = runFromXMLOverrideColNLogic(12051, TestHelper.CFXA_TEST, 2,
        "IF Col.1 CONTAINS Col.2  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "SFXX" };
        int expectedGotos[][] = {{5,6,8}};
        TestLTAssertions.assertFunctionCodesAndGotos(5, expected, expectedGotos, xlt);
    }
    //There may be an error case here is either side is none alphanumeric?

}
