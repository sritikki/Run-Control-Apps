package org.genevaers.runcontrolgenerator;

import java.io.IOException;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
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
            App.run("", "", RunControlConfigration.LOG_FILE, "", "", "");
        }
    } 

    public static void run(String parmFile, String reportFile, String logFile, String vdpFile, String xltFile, String jltFile) {
        RunControlGenerator rcg = new RunControlGenerator();
        ParmReader pr = new ParmReader();
        pr.setConfig(new RunControlConfigration());
        RunControlConfigration.overrideParmFile(parmFile);
        RunControlConfigration.overrideReportFile(reportFile);
        RunControlConfigration.overrideLogFile(logFile);
        GersConfigration.overrideVDPFile(vdpFile);
        GersConfigration.overrideXLTFile(xltFile);
        GersConfigration.overrideJLTFile(jltFile);
        try {
            pr.populateConfigFrom(RunControlConfigration.getParmFileName());
            GersConfigration.setLinesRead(pr.getLinesRead());
            if(RunControlConfigration.isValid()) {
                GenevaLog.initLogger(RunControlGenerator.class.getName(), logFile, GersConfigration.getLogLevel());
                rcg.runFromConfig();
            } else {
                logger.atSevere().log("Invalid configuration processing stopped");
            }
        } catch (IOException e) {
            logger.atSevere().log("Unable to read PARM file");
        }
        GenevaLog.closeLogger(RunControlGenerator.class.getName());
    }

}
