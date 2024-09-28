package org.genevaers.runcontrolgenerator.workbenchinterface;

/*
 * Copyright Contributors to the GenevaERS Project.
								SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation
								2008
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


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolanalyser.RCAApp;
import org.genevaers.runcontrolgenerator.RCGApp;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.utilities.CommandRunner;
import org.genevaers.utilities.GersConfigration;


import com.google.common.flogger.FluentLogger;

public class RCDriver {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    
    private static String inputType ;
    private static String environmentID ;
    private static String schema ;
    private static String port ;
    private static String server ;
    private static String database ;
    private static String dbviews ;
    private static Path rcPath;

    private static String rcaTextType;

    public static void initialise() {
        new RunControlConfigration();
        Repository.clearAndInitialise();
    }

    public static void addViewsToConfig(String viewIDs) {
        GersConfigration.set(GersConfigration.DBVIEWS, viewIDs);
    }

    public static void setDbType(String dbt) {
        inputType = dbt;
    }

    public static void setInputType(String inputType) {
        RCDriver.inputType = inputType;
    }

    public static void setEnvironmentID(String environmentID) {
        RCDriver.environmentID = environmentID;
    }

    public static void setSchema(String schema) {
        RCDriver.schema = schema;
    }

    public static void setPort(String port) {
        RCDriver.port = port;
    }

    public static void setServer(String server) {
        RCDriver.server = server;
    }

    public static void setDatabase(String database) {
        RCDriver.database = database;
    }

    public static void setDbviews(String dbviews) {
        RCDriver.dbviews = dbviews;
    }

    public static void setOutputPath(Path reportPath) {
        logger.atInfo().log("output to %s", reportPath.toString() );
        rcPath = reportPath;
    }

    public static void clearOutputPath(Path reportPath) {
        try {
            logger.atInfo().log("Clear %s", reportPath.toString() );
            FileUtils.deleteDirectory(reportPath.toFile());
        } catch (IOException e) {
            logger.atSevere().log("Delete command failed %s", e.getMessage() );
        }
    }

    public static void runRCG(String relPath) {
        writeRCGParms();
        RCGApp.setCurrentWorkingDirectory(relPath);
        logger.atInfo().log("Run RCG from %s", relPath );
        RCGApp.run();
    }

    private static void runRCG(CommandRunner cmd) {
        try {
            logger.atInfo().log("Run gvbrcg from %s", rcPath.toString() );
            cmd.run("gvbrcg.bat", rcPath.toFile());
            cmd.clear();
        } catch (IOException | InterruptedException e) {
            logger.atSevere().log("gvbrcg command failed %s", e.getMessage() );
        }
    }

    //Problem here is the exposure of the database password
    //No an issue with a local postgres really
    //Option to use workbench XML instead
    private static void writeRCGParms() {
        logger.atInfo().log("Write RCGParms to %s", rcPath.toString() );
        try (FileWriter fw = new FileWriter(rcPath.resolve(GersConfigration.RCA_PARM_FILENAME).toFile())) {
            fw.write("# Auto generated Run Control Generator Parms\n");
            if(inputType.equals("WBXML")) {
                fw.write(GersConfigration.INPUT_TYPE +"=" + inputType + "\n");
            } else {
                fw.write(GersConfigration.INPUT_TYPE + "=" + inputType + "\n");
                fw.write(GersConfigration.ENVIRONMENT_ID + "=" + environmentID + "\n");
                fw.write(GersConfigration.DB_SCHEMA + "=" + schema + "\n");
                fw.write(GersConfigration.DB_PORT + "=" + port + "\n");
                fw.write(GersConfigration.DB_SERVER + "=" + server + "\n");
                fw.write(GersConfigration.DB_DATABASE + "=" + database + "\n");
                fw.write(GersConfigration.DBVIEWS + "="+ dbviews + "\n");
            }
            fw.close();
        } catch (IOException e) {
            logger.atSevere().log("Unable to write RCG Parms %s", e.getMessage() );
        }
    }

    public static void runRCA(String relPath) {
        writeRCAParms();
        RCAApp.setCurrentWorkingDirectory(relPath);
        logger.atInfo().log("Run RCA from %s", relPath );
        RCAApp.run();
    }

    public static void runRCA(CommandRunner cmd) {
        writeRCAParms();
        logger.atInfo().log("Run " + GersConfigration.RCA_RUNNAME + "from %s", rcPath.toString() );
        try {
            cmd.run(GersConfigration.RCA_RUNNAME + ".bat", rcPath.toFile());
            cmd.clear();
        } catch (IOException | InterruptedException e) {
            logger.atSevere().log(GersConfigration.RCA_RUNNAME +  "command failed %s", e.getMessage() );
        }
    }

    public static void runApp(CommandRunner cmd, String appname) {
        writeRCAParms();
        logger.atInfo().log("Run " + appname + "from %s", rcPath.toString() );
        try {
            cmd.run(appname, rcPath.toFile());
            cmd.clear();
        } catch (IOException | InterruptedException e) {
            logger.atSevere().log(GersConfigration.RCA_RUNNAME +  "command failed %s", e.getMessage() );
        }
    }

    private static void writeRCAParms() {
        logger.atInfo().log("Write RCAParms to %s text format %s", rcPath.toString(), rcaTextType);
        try (FileWriter fw = new FileWriter(rcPath.resolve(GersConfigration.RCA_PARM_FILENAME).toFile())) {
            fw.write("# Auto generated Run Control Analyser Parms\n");
            fw.write(GersConfigration.XLT_REPORT + "=Y\n");
            fw.write(GersConfigration.JLT_REPORT + "=Y\n");
            fw.write(GersConfigration.VDP_REPORT + "=Y\n");
            fw.write(GersConfigration.REPORT_FORMAT + "=" + rcaTextType + "\n");
            fw.write(GersConfigration.RCA_REPORT + "=Y\n");
            fw.write(GersConfigration.LOG_LEVEL + "=STANDARD\n");
            fw.close();
        } catch (IOException e) {
            logger.atSevere().log("Unable to write RCA Parms %s", e.getMessage() );
        }
    }

    public static void setRCATextType(String t) {
        rcaTextType = t;
    }

    public static String getRCAreportFileName() {
        return GersConfigration.RCA_HTMLREPORTFILENAME;
    }

}
