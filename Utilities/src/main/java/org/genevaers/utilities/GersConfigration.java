package org.genevaers.utilities;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
import java.util.Map.Entry;
import java.util.logging.Level;

import com.google.common.flogger.FluentLogger;

public class GersConfigration {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static List<String> linesRead = new ArrayList<>();

    protected static final String PARMFILE = "PARMFILE";
    protected static final String ZOSPARMFILE = "ZOSPARMFILE";

    public static final String LOG_LEVEL = "LOG_LEVEL";
    public static final String TRACE = "TRACE";
    public static final String LOG_FILE = "LOGFILE";

    public static final String XLT_DDNAME = "XLT";
    public static final String JLT_DDNAME = "JLT";
    public static final String VDP_DDNAME = "VDP";
    
    public static final String ZOS = "ZOS";
    protected static Map<String, ConfigEntry> parmToValue = new TreeMap<>();

    private static boolean zos;

    public GersConfigration() {
		String os = System.getProperty("os.name");
		logger.atFine().log("Operating System %s", os);
		zos = os.startsWith("z");
    }

    public void addParmValue(String name, String value) {
        ConfigEntry pv = parmToValue.get(name.toUpperCase());
        if(pv != null) {
            pv.setValue(value);
        } else {
            logger.atWarning().log("Ignoring unexpected parameter %s=%s", name, value);
        }
    }

    public static boolean isParmExpected(String name) {
        return parmToValue.get(name.toUpperCase()) != null;
    }

	public static Level getLogLevel() {
		if(parmToValue.get(LOG_LEVEL).getValue().equalsIgnoreCase("TRACE")){
            return Level.FINE;
        } else {
            return Level.INFO;
        }
	}

    public static String getXLTFileName() {
        return parmToValue.get(XLT_DDNAME).getValue();
    }

    public static String getVdpDdname() {
        return parmToValue.get(VDP_DDNAME).getValue();
    }

    public static String getJLTFileName() {
        return parmToValue.get(JLT_DDNAME).getValue();
    }

	public static void overrideVDPFile(String vdpFile) {
        if(vdpFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(VDP_DDNAME);
            pv.setValue(vdpFile);
        }
	}

	public static void overrideXLTFile(String xltFile) {
        if(xltFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(XLT_DDNAME);
            pv.setValue(xltFile);
        }
	}

	public static void overrideJLTFile(String jltFile) {
        if(jltFile.length() > 0) {
            ConfigEntry pv = parmToValue.get(JLT_DDNAME);
            pv.setValue(jltFile);
        }
	}

    public static String getParm(String parm) {
        ConfigEntry cfe = parmToValue.get(parm);
        if(cfe != null) {
            return cfe.getValue();
        } else {
            return "";
        }
    }

    public static void setLinesRead(List<String> lr) {
        linesRead = lr;
    }

    public static List<String> getLinesRead() {
        return linesRead;
    }

    public static List<String> getOptionsInEffect() {
        List<String> optsInEfect = new ArrayList<>();
        for(Entry<String, ConfigEntry> parm : parmToValue.entrySet()) {
            if(!parm.getValue().isHidden()) {
                optsInEfect.add(String.format("%-33s = %s", parm.getKey(), parm.getValue().getValue()));
            }
        };
        return optsInEfect;
    }

    public static String getZosParmFileName() {
		return parmToValue.get(ZOSPARMFILE).getValue();
	}

	public static String getParmFileName() {
		return parmToValue.get(PARMFILE).getValue();
	}

    public static boolean isZos() {
        return zos;
    }

}
