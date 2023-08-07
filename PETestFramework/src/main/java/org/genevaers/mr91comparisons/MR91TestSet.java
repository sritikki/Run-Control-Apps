package org.genevaers.mr91comparisons;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * A Collection of test results from a test set.
 */
public class MR91TestSet{
	/**
	 *
	 */
	private final TestReporter testReporter;

	/**
	 * @param testReporter
	 */
	MR91TestSet(TestReporter testReporter) {
		this.testReporter = testReporter;
	}

	public String name = "not set";
	private int numViewsGenerated = 0;
	public int numPassesGenerated = 0;
	TreeMap<String, TestResult>  viewsByName = new TreeMap<>();
	List<TestResult> testPassesResults = new ArrayList<>();
	private int numTargetViewsGenerated = 0;
	public int xltMatches = 0;
	public int jltMatches = 0;
	public int numJLTs = 0;
	private String comparison;
	
	public String getName() {
		return name;
	}
	public int getNumViewsGenerated() {
		return numViewsGenerated;
	}
	public int getNumPassesGenerated() {
		return numPassesGenerated;
	}
	public int getNumTargetViewsGenerated() {
		return numTargetViewsGenerated;
	}
	public Collection<TestResult> getViews() {
		return viewsByName.values();
	}
	public List<TestResult> getPasses() {
		return testPassesResults;
	}
	public int getNumXltMatches() {
		int mycount = 0;
		for(TestResult tr : viewsByName.values()){
			if(tr.getSourceVDPBuilt()) {
				boolean overall = false;
				if(tr.overridePass == false) {
					for(Path r : tr.xltPaths) {
						if(r.toString().contains("jlt") == false && r.toString().contains("pass") == false) {
							overall = false;
						} else {
							overall = true;
						}
					}
				}
				if(overall) {
					mycount++;
				}
			}
		}
		return mycount;
	}
	
	public int getNumJltMatches() {
		return jltMatches;
	}
	
	public int getNumJlts() {
		return numJLTs;
	}
	public void addViewTestResult(TestResult tr) {
		viewsByName.put(tr.getTestFileName(), tr);
	}
	public void setComparison(String comparison) {
		this.comparison = comparison;
	}
	public String getComparison() {
		return comparison;
	}
	public void incrementSourceViewsGenerated() {
		numViewsGenerated++;
	}
	public void incrementTargetViewsGenerated() {
		numTargetViewsGenerated++;
	}

}
