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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class MessageCoverage {
	Collection<File> baseFiles = null;
	private ArrayList<File> mr91Logs = new ArrayList<File>();
	private ErrorMessageCoverage errorsCovered;
	private WarningMessageCoverage warningsCovered;
	private InfoMessageCoverage infosCovered;
	
	private String reportName;
	private Path rootPath;
	private ErrorCoverageEnvironment env;
	
	public static void main(String[] args) {
	}

	public MessageCoverage() {
		reportName = "MessageCoverage.xml";
	}
	
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public boolean isInitialised() {
		boolean retval = true;
		File baseDirectory = new File("base/MR91");
		retval =  baseDirectory.exists() && retval;
		return retval;
	}

	public int numberOfMR91BaseFiles() {
		File baseDirectory = new File("base/MR91");
		WildcardFileFilter fileFilter = new WildcardFileFilter("*");
		baseFiles = FileUtils.listFiles(baseDirectory, fileFilter, TrueFileFilter.TRUE);
		return baseFiles.size();
	}

	public void extractLogFiles(String lookfor) throws IOException {
		for (File baseFile : baseFiles) {
			String basename = baseFile.getName();
			if (basename.equals(lookfor) == true) {
				mr91Logs.add(baseFile);
			}
		}
		System.out.println("Number of base MR91 Log files = " + mr91Logs.size());
	}

	public int numberOfLogs() throws IOException {
		return mr91Logs.size();
	}

	public int numberOfErrors() throws IOException {
		return errorsCovered.numEntriesHit();
	}

	public int numberOfWarnings() throws IOException {
		return warningsCovered.numEntriesHit();
	}

	public int numberOfInfos() throws IOException {
		return infosCovered.numEntriesHit();
	}

	public void getCoverage() throws IOException {
		Charset charset = null;
		for (File log : mr91Logs) {
			int lineNumber = 1;
			List<String> lines = FileUtils.readLines(log, charset);
			String logName = log.getPath();
		      String warningRegex = "GVB.....E";
		      String infoRegex = "GVB.....E";

		      // Create a Pattern object
		      Pattern warningPattern = Pattern.compile(warningRegex);
		      Pattern infoPattern = Pattern.compile(infoRegex);
			for (String line : lines) {
				HitSource hs = new HitSource(logName, lineNumber++, line);
				boolean hit = errorsCovered.hit(hs);
				if(hit == false) {
					hit = warningsCovered.hit(hs);
				} 
				if(hit == false) {
					infosCovered.hit(hs);
				} 
			}
		}
	}

	public int getOutputLogs(String outBase) {
		File baseDirectory = new File(outBase);
		WildcardFileFilter fileFilter = new WildcardFileFilter("*");
		baseFiles = FileUtils.listFiles(baseDirectory, fileFilter, TrueFileFilter.TRUE);
		return baseFiles.size();
	}

	public List<String> report() throws IOException {
		List<String> lines = errorsCovered.report();
		lines.addAll(warningsCovered.report());
		lines.addAll(infosCovered.report());
		if(reportName.isEmpty()) {
			System.out.println("Num Logs: " + mr91Logs.size());
		} else {
			FileWriter repFile = new FileWriter(new File(reportName));
			repFile.write("<Coverage>\n");
			repFile.write("   <levels>\n");
			for(String line: lines) {
				repFile.write(line +"\n");
			}
			repFile.write("   </levels>\n");
			repFile.write("</Coverage>\n");
			repFile.close();
		}
		return lines;
	}

	public void resetValues() {
		errorsCovered.resetValues();
		warningsCovered.resetValues();
		infosCovered.resetValues();
	}

	public void buildEnvironmentFrom(Path rootPath) {
		this.rootPath = rootPath;
		env = new ErrorCoverageEnvironment(rootPath);	
	}

	public void initialise() throws IOException {
		errorsCovered = new ErrorMessageCoverage(env);
		warningsCovered = new WarningMessageCoverage(env);;
		infosCovered = new InfoMessageCoverage(env);	
	}
}
