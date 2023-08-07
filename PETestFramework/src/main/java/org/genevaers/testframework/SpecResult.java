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

public class SpecResult {
        public String fileName;
        public List<SpecTestResult> testResults = new ArrayList<>();
        private String htmlPath = "";
        private String description = "";
       
        public String getFileName() {
        	return fileName;
        }
        public void setFileName(String name) {
        	fileName = name;
        }
        
        public void setTestResults(List<SpecTestResult> results){
        	testResults = results;
        }
        
        public List<SpecTestResult> getTestResults(){
        	return testResults;
        }
		public void setHtmlPath(String specAbsPath) {
			htmlPath = specAbsPath;
		}
		public String getHtmlPath() {
			return htmlPath;
		}
		public void setDescription(String nodeValue) {
			if(nodeValue.length() > 80) {
				description = nodeValue.substring(0,  80) + "...";
			} else {
				description = nodeValue;
			}
		}
		
		public String getDescription() {
			return description;
		}
   }
