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

public class InfoMessageCoverage extends Coverage {

	private static final String INFO_KEY = "INFO";
	private static final String COMP_INFO_TXT = "COMPILER_INFO_MESSAGES";
	private static final String MR91_INFO_TXT = "MR91_INFO_MESSAGES";
	private static final String SAFR_INFO_TXT = "SAFR_INFO_MESSAGES";
	private static final String VDP_INFO_TXT = "VDP_INFO_MESSAGES";
	private static final String INFO_REGEX = "GVB.....I";

	InfoMessageCoverage(ErrorCoverageEnvironment env) throws IOException {
		super(env);
		if(hasMessagesFile())
			super.readMessages(INFO_REGEX, 14);
	}
	
	@Override
	public boolean hasMessagesFile() throws IOException {
		String errors = env.get(COMP_INFO_TXT);
		System.out.println("Get compiler warning messages from " + errors);
		boolean found = checkFileExists(errors);
		if(found)
			super.readMessages(INFO_REGEX, 12);

		errors = env.get(MR91_INFO_TXT);
		System.out.println("Get mr91 warning messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(INFO_REGEX, 12);

		errors = env.get(SAFR_INFO_TXT);
		System.out.println("Get SAFR warning messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(INFO_REGEX, 12);

		errors = env.get(VDP_INFO_TXT);
		System.out.println("Get VDP warning messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(INFO_REGEX, 12);
		
		return found;
	}

	@Override
	public List<String> report() {
		List<String> reportLines = new ArrayList<String>();
		reportLines.add("<level>");
		reportLines.add("<name>InfoMessageCoverage</name>");
		writeContent(reportLines);
		reportLines.add("</level>");
		return reportLines;
	}

	public void hit(HitSource hs) {
		hit(INFO_REGEX, hs.line);
	}
}
