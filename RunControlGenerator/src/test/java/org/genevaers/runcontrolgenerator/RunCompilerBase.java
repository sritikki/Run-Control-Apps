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
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;
import org.genevaers.runcontrolgenerator.compilers.FormatRecordsBuilder;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.repositorybuilders.RepositoryBuilder;
import org.genevaers.runcontrolgenerator.repositorybuilders.RepositoryBuilderFactory;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.LogicGroup;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.SinglePassOptimiser;
import org.genevaers.utilities.GersConfigration;
import org.genevaers.utilities.ParmReader;
import org.genevaers.utilities.Status;

import com.google.common.flogger.FluentLogger;

/**
 * Base functions for the Compiler tests
 */
class RunCompilerBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    protected ParmReader pr;
    protected Repository repo;

    protected void readConfigAndBuildRepo() {
        pr = new ParmReader();
        try {
            pr.populateConfigFrom(GersConfigration.getParmFileName());
            RepositoryBuilder rb = RepositoryBuilderFactory.get();
            Status retval = rb.run();
            assertEquals(Status.OK, retval);
        } catch (IOException e) {
			logger.atSevere().log("readConfigAndBuildRepo error %s", e.getMessage());
        }
    }

    protected void compilerFormatPhase(int i, String oneCol, String string) {
    }

    protected ASTBase CompileAndGenerateDots() {
        ExtractAST2Dot.setFilter(GersConfigration.isXltDotEnabled());
        ExtractAST2Dot.setViews(GersConfigration.getViewDots().split(","));
        ExtractAST2Dot.setCols(GersConfigration.getColumnDots().split(","));
        ExtractAST2Dot.writeRawSources(TestHelper.getMR91origdotPath());
        SinglePassOptimiser spo = new SinglePassOptimiser();
        spo.run();
        List<LogicGroup> lgs = spo.getLogicGroups();
        ExtractPhaseCompiler.run(lgs);
        ExtractAST2Dot.write(ExtractPhaseCompiler.getXltRoot(), TestHelper.getMR91dotPath());
        assertTrue(TestHelper.getMR91dotPath().toFile().exists());
        return ExtractPhaseCompiler.getXltRoot();
    }

    protected SinglePassOptimiser runSPO() throws IOException {
        TestHelper.setupWithBaseView();
        readConfigAndBuildRepo();
        SinglePassOptimiser spo = new SinglePassOptimiser();
        spo.run();
        return spo;
    }

    protected LogicTable runFromXMLOverrideLogic(int viewNum, String fileName, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0) {
            TestHelper.setColumn1Logic(viewNum, logic);
        }
        GersConfigration.setDotFilter(Integer.toString(viewNum), "", "N");
        GersConfigration.setJltDotFilter("", "", "");
        CompileAndGenerateDots();
        return ExtractPhaseCompiler.getExtractLogicTable();
    }

    protected LogicTable runFromXMLOverrideColNLogic(int viewNum, String fileName, int c, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0){
            TestHelper.setColumnNLogic(viewNum, logic, c);
        }
        GersConfigration.setDotFilter(Integer.toString(viewNum), "", "N");
        GersConfigration.setJltDotFilter("", "", "");
        CompileAndGenerateDots();
        return ExtractPhaseCompiler.getExtractLogicTable();
    }

    protected LogicTable runFromXMLOverrideFilter(int viewNum, String fileName, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0) {
            TestHelper.setExtractFilter(viewNum, logic);
        }
        GersConfigration.setDotFilter(Integer.toString(viewNum), "1,2", "N");
        GersConfigration.setJltDotFilter("", "", "");
        CompileAndGenerateDots();
        return ExtractPhaseCompiler.getExtractLogicTable();
    }

    protected LogicTable runFromXMLOverrideOutputLogic(int viewNum, String fileName, String logic)  {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0) {
            TestHelper.setOutputLogic(viewNum, logic);
        }
        GersConfigration.setDotFilter(Integer.toString(viewNum), "1", "N");
        CompileAndGenerateDots();
        return ExtractPhaseCompiler.getExtractLogicTable();
    }

    protected FormatBaseAST runFromXMLOverrideFormatFilter(int viewNum, String fileName, String logic) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0) {
            TestHelper.setFormatFilter(viewNum, logic);
        }
        GersConfigration.setDotFilter(Integer.toString(viewNum), "1", "N");

        return FormatRecordsBuilder.run();
    }

    protected FormatBaseAST runFromXMLOverrideColumnCalculation(int viewNum, String fileName, int colNum, String logic)  {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
        if (logic.length() > 0) {
            TestHelper.setColumnCalculation(viewNum, colNum, logic);
        }
        GersConfigration.setDotFilter(Integer.toString(viewNum), "1", "N");
        FormatBaseAST.setCurrentView(viewNum);
        return FormatRecordsBuilder.run();
    }

    protected void loadRepoFrom(String fileName) {
        TestHelper.setupWithView(fileName);
        readConfigAndBuildRepo();
    }

}
