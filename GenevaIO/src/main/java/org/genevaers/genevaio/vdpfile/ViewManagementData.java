package org.genevaers.genevaio.vdpfile;

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


public class ViewManagementData {
    private int viewID;
	private VDPExtractFile extractFile;
	private VDPFormatFile formatFile;
	private VDPExtractTargetSet extractTargetSet;

    public VDPExtractFile getExtractFile() {
        return extractFile;
    }

    public void setExtractFile(VDPExtractFile extractFile) {
        this.extractFile = extractFile;
    }

    public VDPFormatFile getFormatFile() {
        return formatFile;
    }

    public void setFormatFile(VDPFormatFile formatFile) {
        this.formatFile = formatFile;
    }

    public VDPExtractTargetSet getExtractTargetSet() {
        return extractTargetSet;
    }

    public void setExtractTargetSet(VDPExtractTargetSet extractTargetSet) {
        this.extractTargetSet = extractTargetSet;
    }

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
    }

}
