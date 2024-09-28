package org.genevaers.runcontrolgenerator.configuration;



import java.nio.file.Path;
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

public class RunControlConfigration {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    // Configuration is just a map of parm names to values
    // Make it a TreeMap so it is sorted

    //Let's make these part of the config
    private static List<String> runviewsContents = new ArrayList<>();

    public RunControlConfigration() {
        readRunViews();
    }

	private void readRunViews() {
        Repository.setRunviews(IdsReader.getIdsFrom(GersConfigration.RUNVIEWS));
        runviewsContents.addAll(IdsReader.getLinesRead());
    }

    public static List<String> getRunviewsContents() {
        if(runviewsContents.isEmpty())
            runviewsContents.add("<none>");
        return runviewsContents;
    }

    public static String getInputType() {
		return GersConfigration.getValue(GersConfigration.INPUT_TYPE);
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

	// public static void overrideParmFile(String parmFile) {
    //     if(parmFile.length() > 0) {
    //         ConfigEntry pv = parmToValue.get(PARMFILE);
    //         pv.setValue(parmFile);
    //     }
	// }

	// public static void overrideReportFile(String rptFile) {
    //     if(rptFile.length() > 0) {
    //         ConfigEntry pv = parmToValue.get(REPORT_FILE);
    //         pv.setValue(rptFile);
    //     }
	// }

	// public static void overrideLogFile(String logFile) {
    //     if(logFile.length() > 0) {
    //         ConfigEntry pv = parmToValue.get(LOG_FILE);
    //         pv.setValue(logFile);
    //     }
	// }

	// public static String getLogFileName() {
    //     return getCWDPrefix() + parmToValue.get(LOG_FILE).getValue();
	// }

	// public static String getReportFileName() {
    //     return getCWDPrefix() + parmToValue.get(REPORT_FILE).getValue();
	// }

    // public static String getWBXMLDirectory() {
    //     return parmToValue.get(WB_XML_FILES_SOURCE).getValue();
    // }

    // public static String getWBXMLWinDirectory() {
    //     return getCWDPrefix() + parmToValue.get(WB_XML_FILES_SOURCE).getValue();
    // }

    // public static boolean isXltDotEnabled() {
    //     if( parmToValue.get(DOT_XLT) != null) {
    //         return parmToValue.get(DOT_XLT).getValue().equalsIgnoreCase("Y");
    //     } else {
    //         return false;
    //     }
    // }

    // public static boolean isJltDotEnabled() {
    //     if( parmToValue.get(DOT_JLT) != null) {
    //         return parmToValue.get(DOT_JLT).getValue().equalsIgnoreCase("Y");
    //     } else {
    //         return false;
    //     }
    // }

    // public static String getViewDots() {
    //     if( parmToValue.get(VIEW_DOTS) != null) {
    //         return parmToValue.get(VIEW_DOTS).getValue();
    //     } else {
    //         return "";
    //     }
    // }

    // public static String getColumnDots() {
    //     if( parmToValue.get(COLUMN_DOTS) != null) {
    //         return parmToValue.get(COLUMN_DOTS).getValue();
    //     } else {
    //         return "";
    //     }
    // }

    // public static void setDotFilter(String views, String cols, String pfs) {
    //     parmToValue.put(DOT_XLT, new ConfigEntry("Y", true));
    //     parmToValue.put(VIEW_DOTS, new ConfigEntry(views, true));
    //     parmToValue.put(COLUMN_DOTS, new ConfigEntry(cols, true));
    //     parmToValue.put(PF_DOTS, new ConfigEntry(pfs, true));
    // }

    // public static void setJltDotFilter(String views, String cols, String pfs) {
    //     parmToValue.put(DOT_JLT, new ConfigEntry("Y", true));
    //     parmToValue.put(VIEW_DOTS, new ConfigEntry(views, true));
    //     parmToValue.put(COLUMN_DOTS, new ConfigEntry(cols, true));
    //     parmToValue.put(PF_DOTS, new ConfigEntry(pfs, true));
    // }

    // public static Boolean isPFDotEnabled() {
    //     return parmToValue.get(PF_DOTS).getValue().equalsIgnoreCase("Y");
    // }

    // public static boolean isEmitEnabled() {
    //     return parmToValue.get(EMIT_ENABLED).getValue().equalsIgnoreCase("Y");
    // }

    // public static boolean isFormatDotEnabled() {
    //     return parmToValue.get(DOT_FORMAT).getValue().equalsIgnoreCase("Y");
    // }

    // public static boolean isNumberModeStandard() {
    //     return parmToValue.get(NUMBER_MODE).getValue().equalsIgnoreCase("STANDARD");      
    // }

   
    // public static String getVDPXMLDirectory() {
    //     return VDP_XML_FILES_SOURCE;
    // }

    // public static void set(String parm, String val) {
    //     ConfigEntry pv = parmToValue.get(parm);
    //     pv.setValue(val);
    // }

}
