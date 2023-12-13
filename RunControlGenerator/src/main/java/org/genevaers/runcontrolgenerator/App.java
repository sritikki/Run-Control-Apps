package org.genevaers.runcontrolgenerator;

import java.io.IOException;
import java.util.logging.Level;

import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.utilities.GenevaLog;

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


public class App {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static void main(String[] args) {
		System.out.printf("GenevaERS RunControlGenerator version %s\n", "tbd");
		System.out.printf("Java Vendor %s\n", System.getProperty("java.vendor"));
		System.out.printf("Java Version %s\n", System.getProperty("java.version"));
        if(args.length == 2) {
            if(args[0].equals("rc")) {
                //use name as prefix
                String trgDir = args[1];
                App.run("", trgDir + ".rpt", trgDir + ".log", trgDir + ".VDP", trgDir + ".XLT", trgDir + ".JLT");
            }
        } else {
            App.run("", "", "MR91LOG", "", "", "");
        }
    } 

    public static void run(String parmFile, String reportFile, String logFile, String vdpFile, String xltFile, String jltFile) {
//        initLogger(logFile);
        RunControlGenerator rcg = new RunControlGenerator();
        ParmReader pr = new ParmReader();
        RunControlConfigration rcc = new RunControlConfigration();
        rcc.overrideParmFile(parmFile);
        rcc.overrideReportFile(reportFile);
        rcc.overrideLogFile(logFile);
        rcc.overrideVDPFile(vdpFile);
        rcc.overrideXLTFile(xltFile);
        rcc.overrideJLTFile(jltFile);
        //Maybe allow overwrite from args later
        pr.setConfig(rcc);
        try {
            pr.populateConfigFrom(rcc.getParmFileName());
            rcc.setLinesRead(pr.getLinesRead());
            if(rcc.isValid()) {
                GenevaLog.initLogger(RunControlGenerator.class.getName(), logFile, rcc.getLogLevel());
                rcg.runFromConfig(rcc);
            } else {
                logger.atSevere().log("Invalid configuration processing stopped");
            }
        } catch (IOException e) {
            logger.atSevere().log("Unable to read PARM file");
        }
        GenevaLog.closeLogger(RunControlGenerator.class.getName());
    }

	// private static void initLogger(String logFile) {
    //     GenevaLog.initLogger(RunControlGenerator.class.getName(), logFile, Level.FINE);
	// }
}
