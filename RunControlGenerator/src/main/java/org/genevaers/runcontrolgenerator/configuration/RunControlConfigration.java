package org.genevaers.runcontrolgenerator.configuration;



import java.util.ArrayList;
import java.util.List;
import org.genevaers.repository.Repository;

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


import org.genevaers.utilities.ConfigEntry;
import org.genevaers.utilities.GersConfigration;
import org.genevaers.utilities.IdsReader;

import com.google.common.flogger.FluentLogger;

public class RunControlConfigration extends GersConfigration{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static final String RCG_PARM_FILENAME = RCG_BASENAME + "PARM.cfg";
    public static final String RCG_ZOSPARM_FILENAME = RCG_BASENAME +  "PARM";

    public static final String INPUT_TYPE = "INPUT_TYPE";

    public static final String DB_SCHEMA = "DB_SCHEMA";
    public static final String ENVIRONMENT_ID = "ENVIRONMENT_ID";
    public static final String DB_SUBSYSTEM = "DB_SUBSYSTEM";
    public static final String DB_SERVER = "DB_SERVER";
    public static final String DB_PORT = "DB_PORT";
    public static final String DB_DATABASE = "DB_DATABASE";
    public static final String DBFLDRS = "DBFLDRS";
    public static final String DBVIEWS = "DBVIEWS";
    public static final String RUNVIEWS = "RUNVIEWS";

    public static final String REPORT_FILE = "GRCGRPT";
    public static final String LOG_FILE = "GRCGLOG";

    public static final String DOT_XLT = "DOT_XLT";
    public static final String DOT_JLT = "DOT_JLT";
    public static final String VIEW_DOTS = "VIEW_DOTS";
    public static final String COLUMN_DOTS = "COLUMN_DOTS";
    private static final String PF_DOTS = "PF_DOTS";
    public static final String EMIT_ENABLED = "EMIT_ENABLED";

    public static final String WB_XML_FILES_SOURCE = "WBXMLI";
    public static final String VDP_XML_FILES_SOURCE = "VDPXMLI";

    private static final String DOT_FORMAT = "DOT_FORMAT";

    private static final String NUMBER_MODE = "NUMBER_MODE";
    // Configuration is just a map of parm names to values
    // Make it a TreeMap so it is sorted

    //Let's make these part of the config
    private static List<String> runviewsContents = new ArrayList<>();

    public RunControlConfigration() {
        //Map preloaded with expect names and default values
        super();
        parmToValue.clear();
        parmToValue.put(INPUT_TYPE, new ConfigEntry("", false));

        parmToValue.put(DB_SCHEMA, new ConfigEntry("", false));
        parmToValue.put(ENVIRONMENT_ID, new ConfigEntry("", false));
        parmToValue.put(DB_SUBSYSTEM, new ConfigEntry("", false));
        parmToValue.put(DB_SERVER, new ConfigEntry("", false));
        parmToValue.put(DB_PORT, new ConfigEntry("", false));
        parmToValue.put(DB_DATABASE, new ConfigEntry("", false));
        parmToValue.put(LOG_LEVEL, new ConfigEntry("STANDARD", false));

        //Hidden defaults
        parmToValue.put(PARMFILE, new ConfigEntry(RCG_PARM_FILENAME, true));
        parmToValue.put(ZOSPARMFILE, new ConfigEntry(RCG_ZOSPARM_FILENAME, true));
        parmToValue.put(REPORT_FILE, new ConfigEntry(REPORT_FILE, true));
        parmToValue.put(LOG_FILE, new ConfigEntry(LOG_FILE, true));
        parmToValue.put(WB_XML_FILES_SOURCE, new ConfigEntry(WB_XML_FILES_SOURCE, true));

        parmToValue.put(CURRENT_WORKING_DIRECTORY, new ConfigEntry("", true));
        parmToValue.put(DOT_XLT, new ConfigEntry("N", true));
        parmToValue.put(DOT_JLT, new ConfigEntry("N", true));
        parmToValue.put(VIEW_DOTS, new ConfigEntry("", true));
        parmToValue.put(COLUMN_DOTS, new ConfigEntry("", true));
        parmToValue.put(PF_DOTS, new ConfigEntry("N", true));
        parmToValue.put(EMIT_ENABLED, new ConfigEntry("Y", true));
        parmToValue.put(DOT_FORMAT, new ConfigEntry("N", true));

        parmToValue.put(XLT_DDNAME, new ConfigEntry("XLT", true));
        parmToValue.put(JLT_DDNAME, new ConfigEntry("JLT", true));
        parmToValue.put(VDP_DDNAME, new ConfigEntry("VDP", true));

        parmToValue.put(LOG_LEVEL, new ConfigEntry("STANDARD", false));

        parmToValue.put(NUMBER_MODE, new ConfigEntry("STANDARD", false )); //Could be LARGE

        readRunViews();
    }

	private void readRunViews() {
        Repository.setRunviews(IdsReader.getIdsFrom(RUNVIEWS));
        runviewsContents.addAll(IdsReader.getLinesRead());
    }

    public static List<String> getRunviewsContents() {
        if(runviewsContents.isEmpty())
            runviewsContents.add("<none>");
        return runviewsContents;
    }

    public static String getInputType() {
		return parmToValue.get(INPUT_TYPE).getValue();
	}

	public static boolean isValid() {
		return isInputValid();
	}

    private static boolean isInputValid() {
        boolean valid = true;
        switch(getInputType()) {
            case "WBXML":
            break;
            case "VDPXML":
            break;
            case "DB2":
            break;
            case "POSTGRES":
            break;
            default:
                valid = false;

        }
        return valid;
    }

	public static void overrideParmFile(String parmFile) {
        if(parmFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(PARMFILE);
            pv.setValue(parmFile);
        }
	}

	public static void overrideReportFile(String rptFile) {
        if(rptFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(REPORT_FILE);
            pv.setValue(rptFile);
        }
	}

	public static void overrideLogFile(String logFile) {
        if(logFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(LOG_FILE);
            pv.setValue(logFile);
        }
	}

	public static String getLogFileName() {
        return parmToValue.get(LOG_FILE).getValue();
	}

	public static String getReportFileName() {
        return parmToValue.get(REPORT_FILE).getValue();
	}

    public static String getWBXMLDirectory() {
        return parmToValue.get(WB_XML_FILES_SOURCE).getValue();
    }

    public static boolean isXltDotEnabled() {
        if( parmToValue.get(DOT_XLT) != null) {
            return parmToValue.get(DOT_XLT).getValue().equalsIgnoreCase("Y");
        } else {
            return false;
        }
    }

    public static boolean isJltDotEnabled() {
        if( parmToValue.get(DOT_JLT) != null) {
            return parmToValue.get(DOT_JLT).getValue().equalsIgnoreCase("Y");
        } else {
            return false;
        }
    }

    public static String getViewDots() {
        if( parmToValue.get(VIEW_DOTS) != null) {
            return parmToValue.get(VIEW_DOTS).getValue();
        } else {
            return "";
        }
    }

    public static String getColumnDots() {
        if( parmToValue.get(COLUMN_DOTS) != null) {
            return parmToValue.get(COLUMN_DOTS).getValue();
        } else {
            return "";
        }
    }

    public static void setDotFilter(String views, String cols, String pfs) {
        parmToValue.put(DOT_XLT, new ConfigEntry("Y", true));
        parmToValue.put(VIEW_DOTS, new ConfigEntry(views, true));
        parmToValue.put(COLUMN_DOTS, new ConfigEntry(cols, true));
        parmToValue.put(PF_DOTS, new ConfigEntry(pfs, true));
    }

    public static void setJltDotFilter(String views, String cols, String pfs) {
        parmToValue.put(DOT_JLT, new ConfigEntry("Y", true));
        parmToValue.put(VIEW_DOTS, new ConfigEntry(views, true));
        parmToValue.put(COLUMN_DOTS, new ConfigEntry(cols, true));
        parmToValue.put(PF_DOTS, new ConfigEntry(pfs, true));
    }

    public static Boolean isPFDotEnabled() {
        return parmToValue.get(PF_DOTS).getValue().equalsIgnoreCase("Y");
    }

    public static boolean isEmitEnabled() {
        return parmToValue.get(EMIT_ENABLED).getValue().equalsIgnoreCase("Y");
    }

    public static boolean isFormatDotEnabled() {
        return parmToValue.get(DOT_FORMAT).getValue().equalsIgnoreCase("Y");
    }

    public static boolean isNumberModeStandard() {
        return parmToValue.get(NUMBER_MODE).getValue().equalsIgnoreCase("STANDARD");      
    }

   
    public static String getVDPXMLDirectory() {
        return VDP_XML_FILES_SOURCE;
    }

    public static void set(String parm, String val) {
        ConfigEntry pv = parmToValue.get(parm);
        pv.setValue(val);
    }


}
