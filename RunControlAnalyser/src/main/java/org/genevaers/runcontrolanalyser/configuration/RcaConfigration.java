package org.genevaers.runcontrolanalyser.configuration;

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

import com.google.common.flogger.FluentLogger;

public class RcaConfigration extends GersConfigration {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static final String DEFAULT_PARM_FILENAME = "RCAPARM";
    public static final String DEFAULT_ZOSPARM_FILENAME = "RCAPARM";

    public static final String XLT_REPORT = "XLT_REPORT_ONLY";
    public static final String JLT_REPORT = "JLT_REPORT_ONLY";
    public static final String COMPARE = "COMPARE";
    public static final String COVERAGE = "COVERAGE";
 
    public static final String RCA_DIR = "RCA/";
    public RcaConfigration() {
        //Map preloaded with expect names and default values
        parmToValue.put(TRACE, new ConfigEntry("N", false));

        //Hidden defaults
        parmToValue.put(PARMFILE, new ConfigEntry(DEFAULT_PARM_FILENAME, true));
        parmToValue.put(ZOSPARMFILE, new ConfigEntry(DEFAULT_ZOSPARM_FILENAME, true));
        // parmToValue.put(REPORT_FILE, new ConfigEntry(REPORT_FILE, true));
        parmToValue.put(LOG_FILE, new ConfigEntry("RCALOG", true));
        parmToValue.put(LOG_LEVEL, new ConfigEntry("INFO", true));
        parmToValue.put(XLT_FILE, new ConfigEntry("XLT", true));
        parmToValue.put(JLT_FILE, new ConfigEntry("JLT", true));
        
        
        parmToValue.put(XLT_REPORT, new ConfigEntry("XLT_REPORT", false));
        parmToValue.put(JLT_REPORT, new ConfigEntry("JLT_REPORT", false));



    }

    public static boolean isValid() {
        if(isXltReportOnly() || isJltReportOnly()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getLogFileName() {
        return getParm(LOG_FILE);
    }

    public static boolean isXltReportOnly() {
        return parmToValue.get(XLT_REPORT).getValue().equalsIgnoreCase("Y");
    }

    public static boolean isJltReportOnly() {
        return parmToValue.get(JLT_REPORT).getValue().equalsIgnoreCase("Y");
    }
}
