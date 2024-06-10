package org.genevaers.runcontrolanalyser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.genevaers.runcontrolanalyser.configuration.RcaConfigration;
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

    public static void  main(String[] args) {
		System.out.printf("GenevaERS Run Control Analyser %s\n", AnalyserDriver.readVersion());
		System.out.printf("Java Vendor %s\n", System.getProperty("java.vendor"));
		System.out.printf("Java Version %s\n", System.getProperty("java.version"));
        App.run();
    } 

    public static void run() {
        boolean ranOkay = false;
        ParmReader pr = new ParmReader();
        RcaConfigration rcac = new RcaConfigration();
        pr.setConfig(rcac);
        try {
            pr.populateConfigFrom(GersConfigration.getParmFileName());
            GersConfigration.setLinesRead(pr.getLinesRead());
            GenevaLog.initLogger(RunControlAnalyser.class.getName(), RcaConfigration.getLogFileName(), GersConfigration.getLogLevel());
            logger.atInfo().log("Log level %s", GersConfigration.getLogLevel());
            if(RcaConfigration.isValid()) {
                ranOkay = AnalyserDriver.runFromConfig();
            } else {
                logger.atSevere().log("Invalid configuration. Processing stopped");
            }
        } catch (IOException e) {
            logger.atSevere().log("Unable to read PARM file");
        }
        GenevaLog.closeLogger(RunControlAnalyser.class.getName());
        if(ranOkay) {
            System.out.println("Run control analyser completed");
            System.exit(0);
        } else {
            System.out.println("Run control analyser failed. See log for details.");
            System.exit(1);
        }
    }

}
