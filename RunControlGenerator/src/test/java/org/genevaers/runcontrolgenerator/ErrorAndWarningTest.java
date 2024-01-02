/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
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
import org.genevaers.genevaio.ltfile.LogicTableRE;
import org.genevaers.genevaio.ltfile.LogicTableWR;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.ComponentCollection;
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
class ErrorAndWarningTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        ExtractBaseAST.setCurrentColumnNumber((short)0);
        ExtractBaseAST.setCurrentAccumNumber(0);
        Repository.setGenerationTime(Calendar.getInstance().getTime());
        RecordParser.clearAndInitialise();
        LtFactoryHolder.getLtFunctionCodeFactory().clearAccumulatorMap();
        java.nio.file.Path target = Paths.get("target/test-logs/");
        target.toFile().mkdirs();
        GenevaLog.initLogger(ErrorAndWarningTest.class.getName(), target.resolve(info.getDisplayName()).toString(), Level.FINE);
    }

    @AfterEach
    public void afterEach(TestInfo info){
		GenevaLog.closeLogger(ErrorAndWarningTest.class.getName());
    }

    
    @Test void testBadText() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = banana");
        assertEquals(0, xlt.getNumberOfRecords());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
    }

    // Test cases
    //  Casts - both column and RHS
    

    @Test void testWarnFlipColumn() {
        runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertEquals("Treating column 1 as ZONED.", warns.get(0).getDetail());
    }

    @Test void testWarnFlipField() {
        runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertEquals("Treating column 1 as ZONED.", warns.get(0).getDetail());
    }

    @Test void testWarnFlipColumnZonedTooBig() {
        runFromXMLOverrideLogic(9956, TestHelper.COL_TOO_BIG, "COLUMN = {ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("exceeds maximum"));
    }

    @Test void testWarnFlipFieldZonedTooBig() {
        runFromXMLOverrideLogic(9956, TestHelper.FIELD_TOO_BIG, "COLUMN = {Description}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("exceeds maximum"));
    }

    @Test void testIncompatibleDates() {
        runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "COLUMN = {ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(0, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("Incompatible date formats"));
    }

}
