package org.genevaers.runcontrolgenerator.configuration;

import java.net.URI;
import java.nio.file.Path;

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


import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.google.common.flogger.FluentLogger;

public class RunControlConfigration {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private class ConfigEntry {
        String value = "";
        boolean hidden = false;

        public ConfigEntry(String value, boolean hidden) {
            this.value = value;
            this.hidden = hidden;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }
    }
    
    public static final String DEFAULT_PARM_FILENAME = "MR91Parm.cfg";
    public static final String DEFAULT_ZOSPARM_FILENAME = "MR91PARM";

    public static final String OUTPUT_VDP_XML_FILE = "OUTPUT_VDP_XML_FILE";
    public static final String OUTPUT_WB_XML_FILES = "OUTPUT_WB_XML_FILES";
    public static final String OUTPUT_RUN_CONTROL_FILES = "OUTPUT_RUN_CONTROL_FILES";
    public static final String INPUT_TYPE = "INPUT_TYPE";

    public static final String DB2_SCHEMA = "DB2_SCHEMA";
    public static final String DB2_ENVIRONMENT_ID = "DB2_ENVIRONMENT_ID";
    public static final String DB2_SUBSYSTEM = "DB2_SUBSYSTEM";
    public static final String DB2_SERVER = "DB2_SERVER";
    public static final String DB2_PORT = "DB2_PORT";
    public static final String DB2_DATABASE = "DB2_DATABASE";
    public static final String DBFLDRS = "DBFLDRS";
    public static final String DBVIEWS = "DBVIEWS";

    private static final String PARMFILE = "PARMFILE";
    private static final String ZOSPARMFILE = "ZOSPARMFILE";
    private static final String REPORT_FILE = "MR91RPT";
    private static final String LOG_FILE = "MR91LOG";
    public static final String LOG_LEVEL = "LOG_LEVEL";

    public static final String DOT_XLT = "DOT_XLT";
    public static final String DOT_JLT = "DOT_JLT";
    public static final String VIEW_DOTS = "VIEW_DOTS";
    public static final String COLUMN_DOTS = "COLUMN_DOTS";
    private static final String PF_DOTS = "PF_DOTS";
    public static final String EMIT_ENABLED = "EMIT_ENABLED";

    public static final String WB_XML_FILES_SOURCE = "WBXMLI";

    public static final String XLT_FILE = "XLT";
    public static final String JLT_FILE = "JLT";
    public static final String VDP_FILE = "VDP";
    private static final String DOT_FORMAT = "DOT_FORMAT";

    private static final String NUMBER_MODE = "NUMBER_MODE";
    // Configuration is just a map of parm names to values
    // Make it a TreeMap so it is sorted
    Map<String, ConfigEntry> parmToValue = new TreeMap<>();

    public RunControlConfigration() {
        //Map preloaded with expect names and default values
        parmToValue.put(INPUT_TYPE, new ConfigEntry("", false));
        parmToValue.put(OUTPUT_RUN_CONTROL_FILES, new ConfigEntry("", false));
        parmToValue.put(OUTPUT_WB_XML_FILES, new ConfigEntry("", false));
        parmToValue.put(OUTPUT_VDP_XML_FILE, new ConfigEntry("", false));

        parmToValue.put(DB2_SCHEMA, new ConfigEntry("", false));
        parmToValue.put(DB2_ENVIRONMENT_ID, new ConfigEntry("", false));
        parmToValue.put(DB2_SUBSYSTEM, new ConfigEntry("", false));
        parmToValue.put(DB2_SERVER, new ConfigEntry("", false));
        parmToValue.put(DB2_PORT, new ConfigEntry("", false));
        parmToValue.put(DB2_DATABASE, new ConfigEntry("", false));
        parmToValue.put(DBFLDRS, new ConfigEntry("", false));
        parmToValue.put(DBVIEWS, new ConfigEntry("", false));

        //Hidden defaults
        parmToValue.put(PARMFILE, new ConfigEntry(DEFAULT_PARM_FILENAME, true));
        parmToValue.put(ZOSPARMFILE, new ConfigEntry(DEFAULT_ZOSPARM_FILENAME, true));
        parmToValue.put(REPORT_FILE, new ConfigEntry(REPORT_FILE, true));
        parmToValue.put(LOG_FILE, new ConfigEntry(LOG_FILE, true));
        parmToValue.put(WB_XML_FILES_SOURCE, new ConfigEntry(WB_XML_FILES_SOURCE, true));

        parmToValue.put(DOT_XLT, new ConfigEntry("N", true));
        parmToValue.put(DOT_JLT, new ConfigEntry("N", true));
        parmToValue.put(VIEW_DOTS, new ConfigEntry("", true));
        parmToValue.put(COLUMN_DOTS, new ConfigEntry("", true));
        parmToValue.put(PF_DOTS, new ConfigEntry("N", true));
        parmToValue.put(EMIT_ENABLED, new ConfigEntry("Y", true));
        parmToValue.put(DOT_FORMAT, new ConfigEntry("N", true));

        parmToValue.put(XLT_FILE, new ConfigEntry("XLT", true));
        parmToValue.put(JLT_FILE, new ConfigEntry("JLT", true));
        parmToValue.put(VDP_FILE, new ConfigEntry("VDP", true));

        parmToValue.put(LOG_LEVEL, new ConfigEntry("INFO", true));

        parmToValue.put(NUMBER_MODE, new ConfigEntry("STANDARD", false )); //Could be LARGE
    }

	public String getInputType() {
		return parmToValue.get(INPUT_TYPE).getValue();
	}

    public void addParmValue(String name, String value) {
        ConfigEntry pv = parmToValue.get(name.toUpperCase());
        if(pv != null) {
            pv.setValue(value);
        } else {
            logger.atWarning().log("Ignoring unexpected parameter %s=%s", name, value);
        }
    }

    public boolean isParmExpected(String name) {
        return parmToValue.get(name.toUpperCase()) != null;
    }

	public boolean isValid() {
        boolean valid = false;
        valid = isInputValid();
        valid &= isOutputValid();
		return valid;
	}

    private boolean isOutputValid() {
        boolean valid = false;
        if( isOutputRC()) {
            valid = true;
        }
        if(isWriteWBXML()){
            valid |= true;
        }
        if(isWriteVDPXML()){
            valid |= true;
        }
        return valid;
    }

    public boolean isOutputRC() {
        return parmToValue.get(OUTPUT_RUN_CONTROL_FILES).getValue().equalsIgnoreCase("Y");
    }

    public boolean isWriteWBXML() {
        return parmToValue.get(OUTPUT_WB_XML_FILES).getValue().equalsIgnoreCase("Y");
    }

    public boolean isWriteVDPXML() {
        return parmToValue.get(OUTPUT_VDP_XML_FILE).getValue().equalsIgnoreCase("Y");
    }

    private boolean isInputValid() {
        boolean valid = true;
        switch(getInputType()) {
            case "WBXML":
            break;
            case "VDPXML":
            break;
            case "DB2":
            break;
            case "PG":
            break;
            default:
                valid = false;

        }
        return valid;
    }

	public String getParmFileName() {
		return parmToValue.get(PARMFILE).getValue();
	}

	public Level getLogLevel() {
		if(parmToValue.get(LOG_LEVEL).getValue().equalsIgnoreCase("FINE")){
            return Level.FINE;
        } else {
            return Level.INFO;
        }
	}

	public String getZosParmFileName() {
		return parmToValue.get(ZOSPARMFILE).getValue();
	}

	public void overrideParmFile(String parmFile) {
        if(parmFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(PARMFILE);
            pv.setValue(parmFile);
        }
	}

	public void overrideReportFile(String rptFile) {
        if(rptFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(REPORT_FILE);
            pv.setValue(rptFile);
        }
	}

	public void overrideLogFile(String logFile) {
        if(logFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(LOG_FILE);
            pv.setValue(logFile);
        }
	}

	public String getLogFileName() {
        return parmToValue.get(LOG_FILE).getValue();
	}

	public String getReportFileName() {
        return parmToValue.get(REPORT_FILE).getValue();
	}

    public String getWBXMLDirectory() {
        return parmToValue.get(WB_XML_FILES_SOURCE).getValue();
    }

    public boolean isXltDotEnabled() {
        if( parmToValue.get(DOT_XLT) != null) {
            return parmToValue.get(DOT_XLT).getValue().equalsIgnoreCase("Y");
        } else {
            return false;
        }
    }

    public boolean isJltDotEnabled() {
        if( parmToValue.get(DOT_JLT) != null) {
            return parmToValue.get(DOT_JLT).getValue().equalsIgnoreCase("Y");
        } else {
            return false;
        }
    }

    public String getViewDots() {
        if( parmToValue.get(VIEW_DOTS) != null) {
            return parmToValue.get(VIEW_DOTS).getValue();
        } else {
            return "";
        }
    }

    public String getColumnDots() {
        if( parmToValue.get(COLUMN_DOTS) != null) {
            return parmToValue.get(COLUMN_DOTS).getValue();
        } else {
            return "";
        }
    }

    public void setDotFilter(String views, String cols, String pfs) {
        parmToValue.put(DOT_XLT, new ConfigEntry("Y", true));
        parmToValue.put(VIEW_DOTS, new ConfigEntry(views, true));
        parmToValue.put(COLUMN_DOTS, new ConfigEntry(cols, true));
        parmToValue.put(PF_DOTS, new ConfigEntry(pfs, true));
    }

    public void setJltDotFilter(String views, String cols, String pfs) {
        parmToValue.put(DOT_JLT, new ConfigEntry("Y", true));
        parmToValue.put(VIEW_DOTS, new ConfigEntry(views, true));
        parmToValue.put(COLUMN_DOTS, new ConfigEntry(cols, true));
        parmToValue.put(PF_DOTS, new ConfigEntry(pfs, true));
    }

    public Boolean isPFDotEnabled() {
        return parmToValue.get(PF_DOTS).getValue().equalsIgnoreCase("Y");
    }

    public String getXLTFileName() {
        return parmToValue.get(XLT_FILE).getValue();
    }

    public String getVdpFile() {
        return parmToValue.get(VDP_FILE).getValue();
    }

    public String getJLTFileName() {
        return parmToValue.get(JLT_FILE).getValue();
    }

	public void overrideVDPFile(String vdpFile) {
        if(vdpFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(VDP_FILE);
            pv.setValue(vdpFile);
        }
	}

	public void overrideXLTFile(String xltFile) {
        if(xltFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(XLT_FILE);
            pv.setValue(xltFile);
        }
	}

	public void overrideJLTFile(String jltFile) {
        if(jltFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(JLT_FILE);
            pv.setValue(jltFile);
        }
	}

    public boolean isEmitEnabled() {
        return parmToValue.get(EMIT_ENABLED).getValue().equalsIgnoreCase("Y");
    }

    public String getParm(String parm) {
        ConfigEntry cfe = parmToValue.get(parm);
        if(cfe != null) {
            return cfe.getValue();
        } else {
            return "";
        }
    }

    public boolean isFormatDotEnabled() {
        return parmToValue.get(DOT_FORMAT).getValue().equalsIgnoreCase("Y");
    }

    public boolean isNumberModeStandard() {
        return parmToValue.get(NUMBER_MODE).getValue().equalsIgnoreCase("STANDARD");      
    }

}
