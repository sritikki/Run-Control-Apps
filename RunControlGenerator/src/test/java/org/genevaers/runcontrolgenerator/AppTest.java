package org.genevaers.runcontrolgenerator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


import java.io.File;

import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.genevaio.wbxml.RecordParserData;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AppTest {
    
    @BeforeEach
    public void initEach(){
        ExtractPhaseCompiler.reset();
        Repository.clearAndInitialise();
        RecordParser.clearAndInitialise();
    }

    @Test public void testOneColumnLookup() {
        //This single column lookup view should have enough
        //content to seed most tests. Not column reference.
        //We can replace the column logic with what we feel like.
        //
        //We have two levels of the tests.
        //App and compiler
        //We should merge them since they essentially repeat the same test.
        //The App just adds writing of the VDP and XLT
        //
        //Use some setup and teardown functions 
        //And common out the test checking too.
        //
        //We'll need a lookup with effective date and symbols
        //Or that can be an addon
        //We'll need lookups with multiple steps and multiple source keys of different types
        TestHelper.setupWithView("OneColLookup.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();         
        assertTrue(xltfile.exists());
        File jltfile = TestHelper.getJLT();
        assertTrue(jltfile.exists());
    }

    @Test public void testOneColumnWriteExit() {
        TestHelper.setupWithView("OneColWriteExit[12052].xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();         
        assertTrue(xltfile.exists());
        File jltfile = TestHelper.getJLT();
        assertTrue(jltfile.exists());
    }

    @Test public void testDemo1() {
        TestHelper.setupWithView("V0010689.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();         
        assertTrue(xltfile.exists());
        File jltfile = TestHelper.getJLT();
        assertTrue(jltfile.exists());
    }

    @Test public void testDemo3() {
        TestHelper.setupWithView("V0010702.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();         
        assertTrue(xltfile.exists());
        File jltfile = TestHelper.getJLT();
        assertTrue(jltfile.exists());
    }

    @Test public void testIfAndOrFieldFromWBXML() {
        TestHelper.setupWithView("IfANDORField.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }


    @Test public void testIfOrFieldFromWBXML() {
        TestHelper.setupWithView("IfORField.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }


    @Test public void testIfANDFieldFromWBXML() {
        TestHelper.setupWithView("IfANDField.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testIfFieldFromWBXML() {
        TestHelper.setupWithView("IfField.xml");
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testXLTWrittenFromWBXML() {
        TestHelper.setupWithOneColumnView();
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testDTEFromWBXML() {
        TestHelper.setupWithView(TestHelper.DTCDTE);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testSelectIFDTEFromWBXML() {
        TestHelper.setupWithView(TestHelper.SELECTIF);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testOrderSalesFromWBXML() {
        TestHelper.setupWithView(TestHelper.ORDER_SALES);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testOrderStateSalesFromWBXML() {
        TestHelper.setupWithView(TestHelper.STATE_SALES);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testDemoExtractWithLookupsFromWBXML() {
        TestHelper.setupWithView(TestHelper.DEMO_LOOKUPS);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test public void testDemoProdStateFromWBXML() {
        TestHelper.setupWithView(TestHelper.PROD_STATE);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test @Disabled
    public void testDBGeneration() {
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test 
    public void testFormatFilter() {
        TestHelper.setupWithView(TestHelper.FORMAT_COMPILE);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test 
    public void testSortKeyTitleLookup() {
        TestHelper.setupWithView(TestHelper.SKT_LOOKUP);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test 
    public void testLookupExit() {
        TestHelper.setupWithView(TestHelper.LOOKUP_EXIT);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test 
    public void testMergeAss() {
        TestHelper.setupWithView(TestHelper.MERGESS);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

    @Test 
    public void testArithAll() {
        TestHelper.setupWithView(TestHelper.ALL_ARITH);
        App.run(TestHelper.TEST_PARMNAME, TestHelper.TEST_REPORTNAME, TestHelper.TEST_LOGNAME, TestHelper.TEST_VDP, TestHelper.TEST_XLT, TestHelper.TEST_JLT);
        File report = TestHelper.getReport();
        assertTrue(report.exists());
        File logfile = TestHelper.getLog();
        assertTrue(logfile.exists());
        File xltfile = TestHelper.getXLT();
        assertTrue(xltfile.exists());
    }

}
