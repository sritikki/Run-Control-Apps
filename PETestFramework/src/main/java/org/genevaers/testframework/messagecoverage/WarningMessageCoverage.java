package org.genevaers.testframework.messagecoverage;

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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarningMessageCoverage extends Coverage {

	private static final String WARNING_KEY = "WARNING";
	private static final String COMP_WARNINGS_TXT = "COMPILER_WARNING_MESSAGES";
	private static final String MR91_WARNINGS_TXT = "MR91_WARNING_MESSAGES";
	private static final String SAFR_WARNINGS_TXT = "SAFR_WARNING_MESSAGES";
	private static final String VDP_WARNINGS_TXT = "VDP_WARNING_MESSAGES";
	private static final String WARNING_REGEX = "GVB.....W";

	WarningMessageCoverage(ErrorCoverageEnvironment env) throws IOException {
		super(env);
		hasMessagesFile();
	}
	
	@Override
	public boolean hasMessagesFile() throws IOException {
		String errors = env.get(COMP_WARNINGS_TXT);
		System.out.println("Get compiler warning messages from " + errors);
		boolean found = checkFileExists(errors);
		if(found)
			super.readMessages(WARNING_REGEX, 14);

		errors = env.get(MR91_WARNINGS_TXT);
		System.out.println("Get mr91 warning messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(WARNING_REGEX, 12);

		errors = env.get(SAFR_WARNINGS_TXT);
		System.out.println("Get SAFR warning messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(WARNING_REGEX, 12);

		errors = env.get(VDP_WARNINGS_TXT);
		System.out.println("Get VDP warning messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(WARNING_REGEX, 12);
		
		return found;
	}

	@Override
	public List<String> report() {
		List<String> reportLines = new ArrayList<String>();
		reportLines.add("<level>");
		reportLines.add("<name>WarningMessageCoverage</name>");
		writeContent(reportLines);
		reportLines.add("</level>");
		return reportLines;
	}

	public boolean hit(HitSource hs) {
		return hit(WARNING_REGEX, hs.line);
	}
}
