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


public class SpecTestResult {
	public String name="";
    //This can be our result enum?
    public String result="";
    public String absPath="";
	public String coveragePath = "";
	public String vdpFlowPath = "";
	public String wbxmlFileName = "";
	public String viewhtmlPath= "";
	public String description= "";
	private String RTC = "";
	private String classRunName = "";
	
    public String getVdpFlowPath() {
		return vdpFlowPath;
	}
	public void setVdpFlowPath(String vdpFlowPath) {
		this.vdpFlowPath = vdpFlowPath;
	}
	public String getName() {
		return name;
	}
	public String getResult() {
		return result;
	}
	public String getAbsPath() {
		return absPath;
	}
	public String getCoveragePath() {
		return coveragePath;
	}
	public void setCoveragePath(String path) {
		coveragePath = path;
	}
	public boolean passed() {
		return result.equalsIgnoreCase("pass");
	}
	public boolean isUnknown() {
		return result.equalsIgnoreCase("unknown");
	}
	public void setDescription(String nodeValue) {
		if(nodeValue.length() > 60) {
			description = nodeValue.substring(0,  60) + "...";
		} else {
			description = nodeValue;
		}
	}
	public String getDescription() {
		return description;
	}
	public void setRTC(String nodeValue) {
		RTC = nodeValue;
	}
	public String getRtc() {
		return RTC;
	}

	public String getClassRunName() {
		return classRunName;
	}

	public void setClassRunName(String classRunName) {
		this.classRunName = classRunName;
	}
}
