package org.genevaers.utilities;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.common.flogger.FluentLogger;
import com.ibm.jzos.ZFile;

public class ParmReader {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	GersConfigration rcc;
	List<String> linesRead = new ArrayList<>();

	public enum PARM_RESULT {
		OK, WARN_IGNORED, FAIL
	}

	PARM_RESULT result = PARM_RESULT.OK;

	public void setConfig(GersConfigration rcc) {
		this.rcc = rcc;
	}

	public void populateConfigFrom(String parmName) throws IOException {
		if (System.getProperty("os.name").startsWith("z")) {
			readDDParm();
		} else {
			readSuppliedParm(parmName);	
		}
	}

	private void readDDParm() throws IOException {
		ZFile dd = null;
			String ddname = "//DD:" + rcc.getZosParmFileName();
			dd = new ZFile(ddname, "r");
			BufferedReader br = new BufferedReader(new InputStreamReader(new ZFile(ddname, "r").getInputStream()));
			logger.atInfo().log("Reading %s", ddname);
			parseLines(br);
			dd.close();
	}

	private void readSuppliedParm(String parmName) {
		Path parmPath = Paths.get(parmName);
		try (BufferedReader parmReader = new BufferedReader(new FileReader(parmPath.toFile()))) {
			logger.atInfo().log("Reading %s", parmName);
			parseLines(parmReader);
		} catch (IOException e) {
			result = PARM_RESULT.FAIL;
			logger.atSevere().withCause(e).log("Read failed. Cannot open %s", parmName);
		}
	}

	private void parseLines(BufferedReader parmReader) throws IOException {
		String line = parmReader.readLine();
		while (line != null) {
			// Parse the line to extract the parm name and value
			// set in the config - if expected
			parse(line);
			linesRead.add(line);
			line = parmReader.readLine();
		}
	}

	private void parse(String line) {
		if (line.length() > 0 && line.charAt(0) != '#' && line.charAt(0) != '[') {
			String[] parts = line.split("=");
			if (parts.length == 2) {
				String parmName = parts[0].trim();
				String value = parts[1].trim();
				// There may be comments or something after the value
				String[] values = value.split(" ");
				if (rcc.isParmExpected(parmName)) {
					rcc.addParmValue(parmName, values[0].trim());
				} else {
					result = PARM_RESULT.WARN_IGNORED;
					logger.atWarning().log("Ignoring unknown parm %s=%s", parmName, values[0].trim());
				}
			}
		}

	}

	public PARM_RESULT getResult() {
		return result;
	}

	public List<String> getLinesRead() {
		return linesRead;
	}

}
