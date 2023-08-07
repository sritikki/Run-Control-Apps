package org.genevaers.testframework;

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


import java.util.ArrayList;
import java.util.List;

public class SpecGroup {
    public List<String> headers;
    public List<SpecResult> specResults = new ArrayList<SpecResult>();
    
    public List<String> getHeaders(){
    	return headers;
    }
    public List<SpecResult> getSpecResults(){
    	return specResults;
    }
	public SpecResult addTestResults(String specName, List<SpecTestResult> results) {
		SpecResult specResult = new SpecResult();
		specResult.setFileName(specName);
		specResult.setTestResults(results);
		specResults.add(specResult);
		return specResult;
	}
}
