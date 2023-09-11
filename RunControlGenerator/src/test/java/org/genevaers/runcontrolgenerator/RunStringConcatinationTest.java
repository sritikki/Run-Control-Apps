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
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.genevaio.ltfile.LogicTableNameValue;
import org.genevaers.genevaio.ltfile.LogicTableWR;
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

class RunStringConcatinationTest extends RunCompilerBase {
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

    @Test void testConcatAssignment() {
        LogicTable xlt = runFromXMLOverrideLogic(12150, TestHelper.CONCAT, 
        "");
        String[] expected = new String[]{ "DTE", "DTE", "DTE", "DTE" };
        //Look at some key entries to confirm processing
        assertDte((LogicTableF2)xlt.getFromPosition(6) , 26, 2);
        assertDte((LogicTableF2)xlt.getFromPosition(7) , 28, 5);
        assertDte((LogicTableF2)xlt.getFromPosition(8) , 33, 3);
        assertDtc((LogicTableF1)xlt.getFromPosition(10) , 38, 1, "/");
        assertDte((LogicTableF2)xlt.getFromPosition(11) , 39, 3);
        assertDte((LogicTableF2)xlt.getFromPosition(13) , 43, 5);
        assertJoin((LogicTableF1)xlt.getFromPosition(14) , 17, 19);
        assertDtl((LogicTableF2)xlt.getFromPosition(17) , 48, 2);
        assertDtc((LogicTableF1)xlt.getFromPosition(19) , 48, 2, "");
        assertJoin((LogicTableF1)xlt.getFromPosition(20) , 23, 25);
        assertDtl((LogicTableF2)xlt.getFromPosition(23) , 50, 3);
        assertDtc((LogicTableF1)xlt.getFromPosition(25) , 50, 3, "");
        assertDtc((LogicTableF1)xlt.getFromPosition(38) , 61, 1, "*");
        assertDtx((LogicTableF2)xlt.getFromPosition(53) , 78, 10);
        assertDtc((LogicTableF1)xlt.getFromPosition(54) , 88, 10, " AND MORE ");
        assertDtx((LogicTableF2)xlt.getFromPosition(55) , 98, 10);
    }

    @Test void testRightFieldAssignment() {
        LogicTable xlt = runFromXMLOverrideLogic(12150, TestHelper.CONCAT, 
        "COLUMN = RIGHT({Ten}, 4)");
        assertDteSource((LogicTableF2)xlt.getFromPosition(4) , 27, 4);
    }
        
    @Test void testLeftFieldAssignment() {
        LogicTable xlt = runFromXMLOverrideLogic(12150, TestHelper.CONCAT, 
        "COLUMN = LEFT({Ten}, 4)");
        assertDteSource((LogicTableF2)xlt.getFromPosition(4) , 21, 4);
    }
        
    @Test void testDefaultSubstringFieldAssignment() {
        LogicTable xlt = runFromXMLOverrideLogic(12150, TestHelper.CONCAT, 
        "COLUMN = SUBSTR({Ten}, 4)");
        assertDteSource((LogicTableF2)xlt.getFromPosition(4) , 21, 4);
    }
        
    @Test void testSubstringFieldAssignment() {
        LogicTable xlt = runFromXMLOverrideLogic(12150, TestHelper.CONCAT, 
        "COLUMN = SUBSTR({Ten}, 3, 4)");
        assertDteSource((LogicTableF2)xlt.getFromPosition(4) , 24, 4);
    }
        
    private void assertDte(LogicTableF2 dte, int start, int len) {
        assertEquals(start, dte.getArg2().getStartPosition());
        assertEquals(len, dte.getArg2().getFieldLength());
    }

    private void assertDteSource(LogicTableF2 dte, int start, int len) {
        assertEquals(start, dte.getArg1().getStartPosition());
        assertEquals(len, dte.getArg1().getFieldLength());
    }


    private void assertDtl(LogicTableF2 dtl, int start, int len) {
        assertEquals("DTL", dtl.getFunctionCode());
        assertEquals(start, dtl.getArg2().getStartPosition());
        assertEquals(len, dtl.getArg2().getFieldLength());
    }

    private void assertDtx(LogicTableF2 dtx, int start, int len) {
        assertEquals("DTX", dtx.getFunctionCode());
        assertEquals(start, dtx.getArg2().getStartPosition());
        assertEquals(len, dtx.getArg2().getFieldLength());
    }

    private void assertDtc(LogicTableF1 dtc, int start, int len, String val) {
        assertEquals(start, dtc.getArg().getStartPosition());
        assertEquals(val, dtc.getArg().getValue());
        assertEquals(len, dtc.getArg().getFieldLength());
    }

    private void assertJoin(LogicTableF1 join, int goto1, int goto2) {
        assertEquals("JOIN", join.getFunctionCode());
        assertEquals(goto1, join.getGotoRow1());
        assertEquals(goto2, join.getGotoRow2());
    }

}
