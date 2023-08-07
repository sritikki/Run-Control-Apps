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


import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// From a command line generate the params.xsl file

public class ErrorCoverageEnvironment {

	private Map<String, String> environmentVariables = new HashMap<String, String>();
	
	public ErrorCoverageEnvironment(Path rootPath) {
        initialiseFromTheEnvironment(rootPath);
	}

	private void initialiseFromTheEnvironment(Path rootPath) {
		getEnvVarOrDefault("COMPILER_ERROR_MESSAGES", rootPath.toString() + "\\messageFiles\\CompErrorDefines.i"); 			
		getEnvVarOrDefault("COMPILER_WARNING_MESSAGES", rootPath.toString() + "\\messageFiles\\CompWarnDefines.i"); 			
		getEnvVarOrDefault("COMPILER_INFO_MESSAGES", rootPath.toString() + "\\messageFiles\\CompInfoDefines.i"); 			
		getEnvVarOrDefault("MR91_ERROR_MESSAGES", rootPath.toString() + "\\messageFiles\\mr91Errors.h"); 			
		getEnvVarOrDefault("MR91_WARNING_MESSAGES", rootPath.toString() + "\\messageFiles\\mr91Warnings.h"); 			
		getEnvVarOrDefault("MR91_INFO_MESSAGES", rootPath.toString() + "\\messageFiles\\mr91Info.h"); 			
		getEnvVarOrDefault("SAFR_ERROR_MESSAGES", rootPath.toString() + "\\messageFiles\\SAFRErrors.h"); 			
		getEnvVarOrDefault("SAFR_WARNING_MESSAGES", rootPath.toString() + "\\messageFiles\\SAFRWarnings.h"); 			
		getEnvVarOrDefault("SAFR_INFO_MESSAGES", rootPath.toString() + "\\messageFiles\\\\SAFRInfo.h"); 			
		getEnvVarOrDefault("VDP_ERROR_MESSAGES", rootPath.toString() + "\\messageFiles\\VDPErrors.h"); 			
		getEnvVarOrDefault("VDP_WARNING_MESSAGES", rootPath.toString() + "\\messageFiles\\VDPWarnings.h"); 			
		getEnvVarOrDefault("VDP_INFO_MESSAGES", rootPath.toString() + "\\messageFiles\\VDPInfo.h"); 			
	}

	private String getEnvVarOrDefault(String env, String def) {
		String value = System.getenv(env);
		if (value == null) {
			value = def;
		}		
		environmentVariables.put(env.toUpperCase(), value);
		return value;
	}
	
	public String get(String key) {
		return environmentVariables.get(key);
	}
	
	public void dumpVariables() {
		for(String key : environmentVariables.keySet()) {
			String value = environmentVariables.get(key);
			if(key.equalsIgnoreCase("TSO_PASSWORD")) {
				value = "******";
			}
			System.out.format("%s=%s%n", key, value);
		}
	}
	
	public Map<String, String> getEnvironmentVariables() {
		return environmentVariables;
	}
}
