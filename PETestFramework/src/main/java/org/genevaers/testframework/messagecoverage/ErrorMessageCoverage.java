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

public class ErrorMessageCoverage extends Coverage {
	

	private static final int TO_MESSAGE = 15;
	private static final String ERROR = "ERROR";
	private static final String COMP_ERRORS_TXT = "COMPILER_ERROR_MESSAGES";
	private static final String MR91_ERRORS_TXT = "MR91_ERROR_MESSAGES";
	private static final String SAFR_ERRORS_TXT = "SAFR_ERROR_MESSAGES";
	private static final String VDP_ERRORS_TXT = "VDP_ERROR_MESSAGES";
	private static final String ERROR_REGEX = "GVB.....E";
	
	ErrorMessageCoverage(ErrorCoverageEnvironment env) throws IOException {
		super(env);
		hasMessagesFile();
	}

	@Override
	public boolean hasMessagesFile() throws IOException {
		String errors = env.get(COMP_ERRORS_TXT);
		System.out.println("Get compiler error messages from " + errors);
		boolean found = checkFileExists(errors);
		if(found)
			super.readMessages(ERROR_REGEX, TO_MESSAGE);

		errors = env.get(MR91_ERRORS_TXT);
		System.out.println("Get mr91 error messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(ERROR_REGEX, 12);

/*		errors = env.get(SAFR_ERRORS_TXT);
		System.out.println("Get SAFR error messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(ERROR_REGEX);*/

		errors = env.get(VDP_ERRORS_TXT);
		System.out.println("Get VDP error messages from " + errors);
		found = found && checkFileExists(errors);
		if(found)
			super.readMessages(ERROR_REGEX, 12);
		
		return found;
	}
	

	@Override
	public List<String> report() {
		List<String> reportLines = new ArrayList<String>();
		reportLines.add("<level>");
		reportLines.add("<name>ErrorMessageCoverage</name>");
		writeContent(reportLines);
		reportLines.add("</level>");
		return reportLines;
	}


	public boolean hit(HitSource hs) {
		return hit(ERROR_REGEX, hs.line);
	}


}
