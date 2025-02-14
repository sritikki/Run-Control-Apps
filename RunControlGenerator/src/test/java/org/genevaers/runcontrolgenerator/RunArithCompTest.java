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
import org.genevaers.genevaio.ltfile.LTLogger;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.genevaio.ltfile.LogicTableNV;
import org.genevaers.genevaio.ltfile.LogicTableNameF1;
import org.genevaers.genevaio.ltfile.LogicTableNameValue;
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
class RunArithCompTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        ExtractPhaseCompiler.reset();
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

    @Test void testCFAC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} + 1 > 0  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFAC" };
        int expectedGotos[][] = {{7,8,10},{9,11,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableNameValue cfac = (LogicTableNameValue) xlt.getFromPosition(7);
        assertEquals("0", cfac.getValue());
        
    }

    @Test void testCFAE() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} + 1 > {Binary2}  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFAE" };
        int expectedGotos[][] = {{7,8,10},{9,11,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFAL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} + 1 > {AllTypeLookup.ZONED}  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "JOIN", "LKE", "LUSM", "CFAL" };
        int expectedGotos[][] = {{7,10,13},{9,10,13},{10,11,13}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFAP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} + 1 > PRIOR({Binary2})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFAP" };
        int expectedGotos[][] = {{7,8,10},{9,11,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFAX() {
        LogicTable xlt = runFromXMLOverrideLogic(10689, TestHelper.CFAX_TEST, "");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFAX" };
        int expectedGotos[][] = {{8,9,11},{10,12,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(5, expected, expectedGotos, xlt);
    }

    @Test void testCFAA() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} + 1 > {Binary4} - 1  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "DIMN", "SETE", "SUBC", "CFAA" };
        int expectedGotos[][] = {{10,11,13},{12,14,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFCA() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF 72 > {Binary1} + 1  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFCA" };
        int expectedGotos[][] = {{7,8,10},{9,11,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
        LogicTableNameValue cfac = (LogicTableNameValue) xlt.getFromPosition(7);
        assertEquals("72", cfac.getValue());
    }

    @Test void testCFEA() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} > {Binary2}  + 1  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFEA" };
        int expectedGotos[][] = {{7,8,10},{9,11,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFLA() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.ZONED} > {Binary1} + 1   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "JOIN", "LKE", "LUSM", "DIMN", "SETE", "ADDC",  "CFLA" };
        int expectedGotos[][] = {{4,7,13},{6,7,13},{10,11,13}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFPA() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Binary2}) > {Binary1} + 1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFPA" };
        int expectedGotos[][] = {{7,8,10},{9,11,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFXA() {
        LogicTable xlt = runFromXMLOverrideLogic(10689, TestHelper.CFXA_TEST, "");
        String[] expected = new String[]{ "DIMN", "SETE", "ADDC", "CFXA" };
        int expectedGotos[][] = {{8,9,11},{10,12,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(5, expected, expectedGotos, xlt);
    }

    // CFCC only realy makes sense for RUNDAY 
    // Otherwise the result is know at compile time
    @Test void testCFCC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF 2025 > RUNYEAR()   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFCC" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFCP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF 2025 > PRIOR({Binary2}) THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFCP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFCX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF 2025 > Col.1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFCX" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFEP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} > PRIOR({Binary2})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFEX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {Binary1} > Col.1  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFEX" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFLL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.ZONED} > {AllTypeLookup.Binary1}  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFLL" };
        int expectedGotos[][] = {{10,11,13}};
        TestLTAssertions.assertFunctionCodesAndGotos(10, expected, expectedGotos, xlt);
    }

    @Test void testCFLP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.ZONED} > PRIOR({Binary2})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFLP" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testCFLX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF {AllTypeLookup.ZONED} > Col.1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFLX" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testCFPC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Binary2}) > 0  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFPC" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFPE() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Binary2}) > {Binary1} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFPE" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFPL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Binary2}) > {AllTypeLookup.Binary1} THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFPL" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testCFPP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Binary2}) > PRIOR({Binary1}) THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFPP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFPX() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF PRIOR({Binary2}) > Col.1 THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFPX" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFXC() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 > 2025   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFXC" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFXE() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 > {Binary1}   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFXE" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testCFXL() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 > {AllTypeLookup.Binary1}   THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFXL" };
        int expectedGotos[][] = {{7,8,10}};
        TestLTAssertions.assertFunctionCodesAndGotos(7, expected, expectedGotos, xlt);
    }

    @Test void testCFXP() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF Col.1 > PRIOR({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFXP" };
        int expectedGotos[][] = {{4,5,7}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    //Note there is no col.2 - so this will fail once we get there
    @Test void testCFXX() {
        LogicTable xlt = runFromXMLOverrideColNLogic(12051, TestHelper.CFXA_TEST, 2,
        "IF Col.1 > Col.2  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CFXX" };
        int expectedGotos[][] = {{5,6,8}};
        TestLTAssertions.assertFunctionCodesAndGotos(5, expected, expectedGotos, xlt);
    }

}
