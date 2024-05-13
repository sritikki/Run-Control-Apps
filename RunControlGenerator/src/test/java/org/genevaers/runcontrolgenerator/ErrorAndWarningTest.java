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

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.CompilerMessage;
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
class ErrorAndWarningTest extends RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @BeforeEach
    public void initEach(TestInfo info){
        Repository.clearAndInitialise();
        ExtractPhaseCompiler.reset();
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

    @Test void testWarnFlipColumnAndTooSmall() {
        runFromXMLOverrideLogic(9956, TestHelper.ONE_COL, "COLUMN = {Packed}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(2, warns.size());
        assertEquals("Treating column 1 as ZONED.", warns.get(1).getDetail());
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
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "COLUMN = {ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("Incompatible date formats"));
    }

    @Test void testStripColumDate() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "COLUMN = {PACKED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(2, warns.size());
        assertEquals("Removing date from column 1.", warns.get(0).getDetail());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
        LogicTableF2 dte = (LogicTableF2) xlt.getFromPosition(4);
        assertEquals(DateCode.NONE, dte.getArg2().getFieldContentId());        
    }

    @Test void testStripFieldDate() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_FIELD_DATES, "COLUMN = {ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertEquals("Removing date from field {ZONED}.", warns.get(0).getDetail());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
        LogicTableF2 dte = (LogicTableF2) xlt.getFromPosition(4);
        assertEquals(DateCode.NONE, dte.getArg2().getFieldContentId());        
    }

    @Test void testIncompatibleLookupDates() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.INCOMPATIBLE_LOOKUP_DATES, "COLUMN = {AllTypeLookup.ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(0, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("Incompatible date formats"));
    }

    @Test void testStripColumLookupDate() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.INCOMPATIBLE_LOOKUP_DATES, "COLUMN = {AllTypeLookup.PACKED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(2, warns.size());
        assertEquals("Removing date from column 1.", warns.get(0).getDetail());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
        LogicTableF2 dtl = (LogicTableF2) xlt.getFromPosition(7);
        assertEquals(DateCode.NONE, dtl.getArg2().getFieldContentId());        
    }

    @Test void testStripLookupFieldDate() {
        LogicTable xlt = runFromXMLOverrideLogic(12044, TestHelper.INCOMPATIBLE_LOOKUPFIELD_DATES, "COLUMN = {AllTypeLookup.ZONED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertEquals("Removing date from field {AllTypeLookup.ZONED}.", warns.get(0).getDetail());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
        LogicTableF2 dtl = (LogicTableF2) xlt.getFromPosition(7);
        assertEquals(DateCode.NONE, dtl.getArg1().getFieldContentId());        
    }

    @Test void testConstStringToDate() {
        runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "COLUMN = \"66\"");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(0, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("column 1 which has a date code"));
    }

    @Test void testAssignmentTruncationFromNumericConstant() {
        runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "COLUMN = 1234");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(0, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("Truncation"));
    }

    @Test void testAssignmentTruncationLongField() {
        runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "COLUMN = {PACKED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(2, warns.size());
        assertTrue(warns.get(1).getDetail().contains("truncation"));
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
    }

    @Test void testAssignmentTruncationLongLookupField() {
        runFromXMLOverrideLogic(12044, TestHelper.INCOMPATIBLE_LOOKUPFIELD_DATES, "COLUMN = {AllTypeLookup.PACKED}");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertTrue(warns.get(0).getDetail().contains("truncation"));
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
    }

    @Test void testAssignmentTruncationZonedOK() {
        runFromXMLOverrideColNLogic(12156, TestHelper.ALL_TYPES_TARGET, 14,  "COLUMN = 12345678");
        assertEquals(0, Repository.getWarnings().size());
        assertEquals(0, Repository.getCompilerErrors().size());
    }

    @Test void testAssignmentTruncationZonedError() {
        runFromXMLOverrideColNLogic(12156, TestHelper.ALL_TYPES_TARGET, 14,  "COLUMN = 123456789");
        assertEquals(0, Repository.getWarnings().size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("Truncation"));
    }

    @Test void testAssignmentBin8ToZoned() {
        TestHelper.setupWithView(TestHelper.ALL_TYPES_TARGET);
        readConfigAndBuildRepo();
        ViewColumn vc = Repository.getViews().get(12156).getColumnNumber(14);
        vc.setFieldLength((short)15);
        TestHelper.setColumnNLogic(12156, "COLUMN = {Packed}", 14);
        CompileAndGenerateDots();

        assertEquals(0, Repository.getWarnings().size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
    }

    @Test void testInvalidDateComparison() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "IF ({ZONED} = {Binary4}) THEN COLUMN=1 ELSE COLUMN=0 ENDIF");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(0, warns.size());
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(1, errs.size());
        assertTrue(errs.get(0).getDetail().contains("Incompatible"));
    }

    @Test void testComparisonFlipLHS() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "IF ({Description} = {Binary2}) THEN COLUMN=1 ELSE COLUMN=0 ENDIF");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertTrue(warns.get(0).getDetail().contains("Comparing LHS operand {Description} as though it were ZONED"));
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
    }

    @Test void testComparisonFlipRHS() {
        LogicTable xlt = runFromXMLOverrideLogic(9956, TestHelper.INCOMPATIBLE_DATES, "IF ( {Binary2} = {Description} ) THEN COLUMN=1 ELSE COLUMN=0 ENDIF");
        List<CompilerMessage> warns = Repository.getWarnings();
        assertEquals(1, warns.size());
        assertTrue(warns.get(0).getDetail().contains("Comparing RHS operand {Description} as though it were ZONED"));
        List<CompilerMessage> errs = Repository.getCompilerErrors();
        assertEquals(0, errs.size());
    }

}
