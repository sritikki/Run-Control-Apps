package org.genevaers.runcontrolgenerator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.format.astnodes.FormatBaseAST;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.wbxml.RecordParser;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;
import org.genevaers.runcontrolgenerator.compilers.FormatRecordsBuilder;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;
import org.genevaers.utilities.GenevaLog;
import org.genevaers.utilities.GersConfigration;
import org.genevaers.utilities.ParmReader;

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


import com.google.common.flogger.FluentLogger;


public class RCGApp {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private static Status result = Status.ERROR;
    private static String cwd;

    public static void main(String[] args) {
		System.out.printf("GenevaERS RunControlGenerator version %s\n", "tbd");
		System.out.printf("Java Vendor %s\n", System.getProperty("java.vendor"));
		System.out.printf("Java Version %s\n", System.getProperty("java.version"));
        if(args.length == 2) {
            if(args[0].equals("rc")) {
                //use name as prefix
                String trgDir = args[1];
                RCGApp.run("", trgDir + ".rpt", trgDir + ".log", trgDir + ".VDP", trgDir + ".XLT", trgDir + ".JLT");
            }
        } else {
            RCGApp.run("", "", RunControlConfigration.LOG_FILE, "", "", "");
        }
        exitWithRC();
    } 

    public static void run(String parmFile, String reportFile, String logFile, String vdpFile, String xltFile, String jltFile) {
        FormatRecordsBuilder.reset();
        Repository.clearAndInitialise();
        FormatBaseAST.resetStack();
        Repository.setGenerationTime(Calendar.getInstance().getTime());
        ExtractPhaseCompiler.reset();
        ExtractBaseAST.setCurrentColumnNumber((short)0);
        LtFactoryHolder.getLtFunctionCodeFactory().clearAccumulatorMap();
        RecordParser.clearAndInitialise();

        RunControlGenerator rcg = new RunControlGenerator();
        ParmReader pr = new ParmReader();
        pr.setConfig(new RunControlConfigration());
        RunControlConfigration.overrideParmFile(parmFile);
        RunControlConfigration.overrideReportFile(reportFile);
        RunControlConfigration.overrideLogFile(logFile);
        GersConfigration.overrideVDPFile(vdpFile);
        GersConfigration.overrideXLTFile(xltFile);
        GersConfigration.overrideJLTFile(jltFile);
        GersConfigration.setCurrentWorkingDirectory(cwd);
        try {
            pr.populateConfigFrom(RunControlConfigration.getParmFileName());
            GersConfigration.setLinesRead(pr.getLinesRead());
            if(RunControlConfigration.isValid()) {
                GenevaLog.initLogger(RunControlGenerator.class.getName(), RunControlConfigration.getLogFileName(), GersConfigration.getLogLevel());
                result = rcg.runFromConfig();
            } else {
                logger.atSevere().log("Invalid configuration processing stopped");
            }
        } catch (IOException e) {
            logger.atSevere().log("Unable to read PARM file");
        }
        GenevaLog.closeLogger(RunControlGenerator.class.getName());
    }

    public static Status getResult() {
        return result;
    }

    private static void exitWithRC() {
        switch (result) {
            case ERROR:
                System.exit(8);
                break;
            case WARNING:
                System.exit(4);
                break;
            default:
                System.exit(0);
                break;
        }
    }

    public static void setCurrentWorkingDirectory(String dir) {
        cwd = dir;
    }

}
