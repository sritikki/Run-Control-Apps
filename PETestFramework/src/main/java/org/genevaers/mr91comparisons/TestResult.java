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


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestResult{
	private String testFileName;

	//Source 
	private Path sourcePath;
	private boolean sourceVDPBuilt = false;

	//Target
    private Path targetPath;
	private boolean targetVDPBuilt = false;

	public int numViewsPassed;
	public String environment;
	
	public String note = "";
	
	public List<Path> xltPaths = new ArrayList<>();
	public boolean allmatch = true;
	public boolean overridePass = false;
	public Boolean jltmatch = false;
	public Path jltPath = Paths.get("");
	public boolean overrideJLTPass = false;
	private Path xmlInput;

	public String getTestName() {
		return testFileName;
	}
	public String getTestEnvironment() {
		return environment;
	}

	public void setSourcePath(Path testPath) {
		this.sourcePath = testPath;
	}
	public Path getSourcePath() {
		return sourcePath;
	}
	public String getSourcePathName() {
		return sourcePath.toString();
	}

	public List<Path> getXltPaths() {
		return xltPaths;
	}
	
	public boolean getAllXLTMatch() {
		if(overridePass) {
			return true;
		} else {
			for(Path r : xltPaths) {
				if(r.toString().contains("pass") == false) {
					allmatch = false;
				}
			}
		}
		return allmatch;
	}
	
	public boolean getOverridePass() {
		return overridePass;
	}
	
	public boolean getOverrideJLTPass() {
		return overrideJLTPass;
	}
	
	/* This is really a tri state - there may be no JLT*/
	public Boolean getJltMatch() {
		return jltmatch || overrideJLTPass;
	}
	
	public Path getJltPath() {
		return jltPath;
	}
	

	public String getNote() {
		return note;
	}
	
	public void setVdpFlowPath(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setTargetVDPBuilt() {
		targetVDPBuilt = true;
	}
	public boolean getTargetVDPBuilt(){
		return targetVDPBuilt;
	}

    public String getTestFileName() {
        return testFileName;
    }
    
    public void setTestFileName(String testFileName) {
        this.testFileName = testFileName;
    }

    public void addRelativeXMLInputPath(Path relPath) {
		xmlInput = relPath;
    }
    public String getRelativeXMLInputPath() {
		return xmlInput.toString();
    }

	public Path getTargetPath() {
		return targetPath;
	}

	public String getTargetPathName() {
		return targetPath.toString();
	}

	public void setTargetPath(Path testTarget) {
		this.targetPath = testTarget;
	}

    public boolean getSourceVDPBuilt() {
        return sourceVDPBuilt;
    }
	public void setSourceVDPBuilt() {
		sourceVDPBuilt = true;
	}


}
