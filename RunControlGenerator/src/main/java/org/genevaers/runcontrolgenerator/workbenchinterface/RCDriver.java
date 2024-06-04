package org.genevaers.runcontrolgenerator.workbenchinterface;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.utilities.CommandRunner;

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

//     private static 
//     #INPUT_TYPE=PG            # WBXML or VDPML or DB2
// #INPUT_TYPE=VDPXML            # WBXML or VDPML or DB2
// INPUT_TYPE=POSTGRES            # WBXML or VDPML or DB2
// DB2_ENVIRONMENT_ID=5
// DB2_SCHEMA=gendev
// DB2_PORT=5432
// DB2_SERVER=localhost
// DB2_DATABASE=genevaers

// DBVIEWS=12156

// OUTPUT_RUN_CONTROL_FILES=Y
// DOT_XLT=Y
// DOT_JLT=Y
// DOT_FORMAT=Y
// #COLUMN_DOTS=3
// #VIEW_DOTS=11265
// LOG_LEVEL=FINE
// TRACE=Y


    public static void initialise() {
        new RunControlConfigration();
        Repository.clearAndInitialise();
    }

    public static void addViewsToConfig(String viewIDs) {
        RunControlConfigration.set(RunControlConfigration.DBVIEWS, viewIDs);
        RunControlConfigration.set(RunControlConfigration.OUTPUT_RUN_CONTROL_FILES, "Y");
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

    public static void runRCG() {
        writeRCGParms();
		CommandRunner cmd = new CommandRunner();
        runRCG(cmd);
        runRCA(cmd);
    }

    private static void runRCG(CommandRunner cmd) {
        try {
            logger.atInfo().log("Run JMR91 from %s", rcPath.toString() );
            cmd.run("jmr91.bat", rcPath.toFile());
            cmd.clear();
        } catch (IOException | InterruptedException e) {
            logger.atSevere().log("JMR91 command failed %s", e.getMessage() );
        }
    }

    //Problem here is the exposure of the database password
    //No an issue with a local postgres really
    //Option to use workbench XML instead
    private static void writeRCGParms() {
        logger.atInfo().log("Write MR91Parms to %s", rcPath.toString() );
        try (FileWriter fw = new FileWriter(rcPath.resolve("MR91Parm.cfg").toFile())) {
            fw.write("# Auto generated Run Control Generator Parms\n");
            if(inputType.equals("WBXML")) {
                fw.write("INPUT_TYPE=" + inputType + "\n");
            } else {
                fw.write("INPUT_TYPE=" + inputType + "\n");
                fw.write("DB2_ENVIRONMENT_ID=" + environmentID + "\n");
                fw.write("DB2_SCHEMA=" + schema + "\n");
                fw.write("DB2_PORT=" + port + "\n");
                fw.write("DB2_SERVER=" + server + "\n");
                fw.write("DB2_DATABASE=" + database + "\n");
                fw.write("DBVIEWS="+ dbviews + "\n");
            }
            fw.write("OUTPUT_RUN_CONTROL_FILES=Y\n");
            fw.close();
        } catch (IOException e) {
            logger.atSevere().log("Unable to write RCG Parms %s", e.getMessage() );
        }
    }

    public static void runRCA(CommandRunner cmd) {
        writeRCAParms();
        logger.atInfo().log("Run gersrca from %s", rcPath.toString() );
        try {
            cmd.run("gersrca.bat", rcPath.toFile());
            cmd.clear();
        } catch (IOException | InterruptedException e) {
            logger.atSevere().log("gersrca command failed %s", e.getMessage() );
        }
    }

    private static void writeRCAParms() {
        logger.atInfo().log("Write RCAParms to %s text format %s", rcPath.toString(), rcaTextType);
        try (FileWriter fw = new FileWriter(rcPath.resolve("RCAPARM").toFile())) {
            fw.write("# Auto generated Run Control Analyser Parms\n");
            fw.write("XLT_REPORT=Y\n");
            fw.write("JLT_REPORT=Y\n");
            fw.write("VDP_REPORT=Y\n");
            fw.write("REPORT_FORMAT=" + rcaTextType + "\n");
            fw.write("TRACE=N\n");
            fw.close();
        } catch (IOException e) {
            logger.atSevere().log("Unable to write RCA Parms %s", e.getMessage() );
        }
    }

    public static void setRCATextType(String t) {
        rcaTextType = t;
    }

}
