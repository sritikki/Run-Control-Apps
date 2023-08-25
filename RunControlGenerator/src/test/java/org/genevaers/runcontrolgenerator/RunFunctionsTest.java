/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.logging.Level;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableNameValue;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
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

class RunFunctionsTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        ExtractBaseAST.setCurrentColumnNumber((short)0);
        Repository.setGenerationTime(Calendar.getInstance().getTime());
        RecordParser.clearAndInitialise();
        java.nio.file.Path target = Paths.get("target/test-logs/");
        target.toFile().mkdirs();
        GenevaLog.initLogger(RunCompilerTest.class.getName(), target.resolve(info.getDisplayName()).toString(), Level.FINE);
    }

    @AfterEach
    public void afterEach(TestInfo info){
		GenevaLog.closeLogger(RunCompilerTest.class.getName());
    }

    @Test void testIsSpaces() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF ISSPACES({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CSE" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

   @Test void testIsNotSpaces() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF ISNOTSPACES({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CSE" };
        int expectedGotos[][] = {{4,7,5},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testIsNull() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF ISNULL({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CXE" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testIsNotNull() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF ISNOTNULL({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CXE" };
        int expectedGotos[][] = {{4,7,5},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testIsNumeric() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF ISNUMERIC({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CNE" };
        int expectedGotos[][] = {{4,5,7},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }

    @Test void testIsNotNumeric() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.ONE_COL_LOOKUP, 
        "IF ISNOTNUMERIC({Binary1})  THEN COLUMN = 9 ELSE COLUMN = 3 ENDIF");
        String[] expected = new String[]{ "CNE" };
        int expectedGotos[][] = {{4,7,5},{6,8,0}};
        TestLTAssertions.assertFunctionCodesAndGotos(4, expected, expectedGotos, xlt);
    }


}
