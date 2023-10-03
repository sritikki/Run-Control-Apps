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


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public abstract class Coverage {

	protected File messagesFile;
	protected List<String> unknownMessages = new ArrayList<>();

	public abstract List<String> report() throws IOException;

	protected SortedMap<String, Hit> coverageHits = new TreeMap<>();
	protected ErrorCoverageEnvironment env;
	
	private int keyEndOffset;

	protected Coverage(ErrorCoverageEnvironment env) {
		this.env = env;
	}

	public abstract boolean hasMessagesFile() throws IOException;

	public int coverageSize() {
		return coverageHits.size();
	}

	protected void readMessages(String errorRegex, int toMessage) throws IOException {
		Charset charset = null;
		List<String> lines = FileUtils.readLines(messagesFile, charset);
		for (String line : lines) {
			String key = findMessageKey(errorRegex, line);
			//Grimyness managing the format with silly quotes
	    	String message = '"' + line.substring(keyEndOffset+toMessage);

			coverageHits.put(key, new Hit(key, message));
		}
	}

	protected boolean checkFileExists(String fileName) {
		messagesFile = new File(fileName);
		boolean found = messagesFile.exists();
		if(found == false) {
			System.out.println("Cannot find the errors file " + messagesFile.toString());
		}
		return found;
	}
	
	public int numEntriesHit() {
		int numHitEntries = 0;
		for (Hit h : coverageHits.values()) {
			if (h.hits > 0)
				numHitEntries++;
		}
		return numHitEntries;
	}

	public void hit(int e) {
		int s = coverageHits.size();
		if(e < s+1 ) {
			Hit cov = coverageHits.get(e);
			if(cov != null ) {
				cov.hits++;
			} else {
				System.out.println("WTF " + e);				
			}
		} else {
			System.out.println("Here");
		}
	}

	public void hit(String key) {
		Hit cov = coverageHits.get(key);
		if(cov != null ) {
			cov.hits++;
			coverageHits.put(key, cov);
		} else {
			System.out.println("WTF " + key);				
		}
	}

	public int unkownHits() {
		return unknownMessages.size();
	}

	public void resetValues() {
		for (Hit h : coverageHits.values()) {
			h.hits = 0;
		}
	}
	protected boolean hit(String pattern, String line) {
		boolean hit = false;
		String key = findMessageKey(pattern, line);
		if(key.length() > 0) {
			hit(key);
			hit = true;
		}
		return hit;
	}

	protected String findMessageKey(String pattern, String line) {
		String key = "";
	    Pattern errorPattern = Pattern.compile(pattern);
	    Matcher matcher = errorPattern.matcher(line);
	    if(line.length() > 15) {
		    if(matcher.find()) {
		    	key =line.substring(matcher.start(), matcher.end());
		    	keyEndOffset = matcher.end();
		    }
	    }
	    return key;
	}

	protected void writeContent(List<String> reportLines) {
		reportLines.add("    <Messages>");
		for(Hit errcov : coverageHits.values()) {
			reportLines.add("        <entry>");
			reportLines.add("            <code>" + errcov.key + "</code>");
			reportLines.add("            <hits>" + errcov.hits + "</hits>");
			reportLines.add("            <message>" + errcov.message + "</message>");
			reportLines.add("        </entry>");
		}
		reportLines.add("    </Messages>");
		reportLines.add("    <UnknownErrorMessages>");
		for(String unk : unknownMessages) {
			reportLines.add("        <message>" + unk + "</message>");
		}
		reportLines.add("    </UnknownErrorMessages>");
	}

}
