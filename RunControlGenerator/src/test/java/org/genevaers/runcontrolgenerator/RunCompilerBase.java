/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.genevaers.runcontrolgenerator;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.extract.astnodes.ExtractAST2Dot;
import org.genevaers.compilers.format.FormatAST2Dot;
import org.genevaers.compilers.format.astnodes.FormatBaseAST;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.compilers.RepositoryCompiler;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.repositorybuilders.RepositoryBuilder;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.LogicGroup;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.SinglePassOptimiser;
import org.genevaers.runcontrolgenerator.utility.Status;

import com.google.common.flogger.FluentLogger;

/**
 * Base functions for the Compiler tests
 */
class RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    protected ParmReader pr;
    protected RunControlConfigration rcc;
    protected Repository repo;
    protected RepositoryCompiler comp;

    protected void readConfigAndBuildRepo() {
        pr = new ParmReader();
        rcc = new RunControlConfigration();
        pr.setConfig(rcc);
        try {
            pr.populateConfigFrom(TestHelper.getTestParmName());
            RepositoryBuilder rb = new RepositoryBuilder(rcc);
            Status retval = rb.run();
            assertEquals(Status.OK, retval);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void compilerFormatPhase(int i, String oneCol, String string) {
    }

    protected ASTBase CompileAndGenerateDots() {
        ExtractAST2Dot.setFilter(rcc.isXltDotEnabled());
        ExtractAST2Dot.setViews(rcc.getViewDots().split(","));
        ExtractAST2Dot.setCols(rcc.getColumnDots().split(","));
        ExtractAST2Dot.writeRawSources(TestHelper.getMR91origdotPath());
        SinglePassOptimiser spo = new SinglePassOptimiser(rcc);
        spo.run();
        comp = new RepositoryCompiler(rcc);
        List<LogicGroup> lgs = spo.getLogicGroups();
        comp.setLogicGroups(lgs);
        comp.run();
        ExtractAST2Dot.write(comp.getXltRoot(), TestHelper.getMR91dotPath());
        assertTrue(TestHelper.getMR91dotPath().toFile().exists());
        return comp.getXltRoot();
    }

    protected SinglePassOptimiser runSPO() throws IOException {
        TestHelper.setupWithBaseView();
        readConfigAndBuildRepo();
        SinglePassOptimiser spo = new SinglePassOptimiser(rcc);
        spo.run();
        return spo;
    }

    protected LTFileObject getLogicTableRecordFunctionCode(String func) {
        LogicTable lt = comp.getExtractLogicTable();
        assertNotNull(lt);
        assertTrue(lt.getNumberOfRecords() > 0);
        Iterator<LTRecord> lti = lt.getIterator();
        LTFileObject lto = null;
        while (lto == null && lti.hasNext()) {
            LTRecord entry = lti.next();
            if (entry.getFunctionCode().startsWith(func)) {
                lto = (LTFileObject) entry;
            }
        }
        return lto;
    }

    protected LogicTable runFromXMLOverrideLogic(int viewNum, String fileName, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0)
            TestHelper.setColumn1Logic(viewNum, logic);
        rcc.setDotFilter(Integer.toString(viewNum), "", "N");
        rcc.setJltDotFilter("", "", "");
        CompileAndGenerateDots();
        return comp.getExtractLogicTable();
    }

    protected LogicTable runFromXMLOverrideColNLogic(int viewNum, String fileName, int c, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0)
            TestHelper.setColumnNLogic(viewNum, logic, c);
        rcc.setDotFilter(Integer.toString(viewNum), "", "N");
        rcc.setJltDotFilter("", "", "");
        CompileAndGenerateDots();
        return comp.getExtractLogicTable();
    }

    protected LogicTable runFromXMLOverrideFilter(int viewNum, String fileName, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0)
            TestHelper.setExtractFilter(viewNum, logic);
        rcc.setDotFilter(Integer.toString(viewNum), "1,2", "N");
        rcc.setJltDotFilter("", "", "");
        CompileAndGenerateDots();
        return comp.getExtractLogicTable();
    }

    protected LogicTable runFromXMLOverrideOutputLogic(int viewNum, String fileName, String logic)  {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0)
            TestHelper.setOutputLogic(viewNum, logic);
        rcc.setDotFilter(Integer.toString(viewNum), "1", "N");
        CompileAndGenerateDots();
        return comp.getExtractLogicTable();
    }

    protected FormatBaseAST runFromXMLOverrideFormatFilter(int viewNum, String fileName, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0)
            TestHelper.setFormatFilter(viewNum, logic);
        rcc.setDotFilter(Integer.toString(viewNum), "1", "N");

        return formatCompileAndGenerateDots();
    }

    private FormatBaseAST formatCompileAndGenerateDots() {
        SinglePassOptimiser spo = new SinglePassOptimiser(rcc);
        spo.run();
        comp = new RepositoryCompiler(rcc);
        List<LogicGroup> lgs = spo.getLogicGroups();
        comp.setLogicGroups(lgs);
        comp.run();
        return comp.getFFRoot();
    }

    protected FormatBaseAST runFromXMLOverrideColumnCalculation(int viewNum, String fileName, int colNum, String logic)  {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0)
            TestHelper.setColumnCalculation(viewNum, colNum, logic);
        rcc.setDotFilter(Integer.toString(viewNum), "1", "N");

        return formatCompileAndGenerateDots();
    }

}
