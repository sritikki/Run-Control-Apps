package org.genevaers.repository.components;

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
import java.util.Iterator;
import java.util.List;

public class LookupPathStep extends ComponentNode {

    private List<LookupPathKey> keys = new ArrayList<>();
    private int stepNum;
    private int targetLR = 0;
    private int targetLF = 0;
    private int sourceLR = 0;
    private int lrLfAssocid;
    private int stepID;

    public LookupPathStep() {
        // super.record = new LookupPathRecord();
    }

    public void addKey(LookupPathKey key) {
        keys.add(key);
    }

    public int getNumberOfKeys() {
        return keys.size();
    }

    public Iterator<LookupPathKey> getKeyIterator() {
        return keys.iterator();
    }

    public void setStepNum(int i) {
        stepNum = i;
    }

    public int getStepNum() {
        return stepNum;
    }

    public String getName() {
        return keys.get(0).getJoinName();
    }

    public int getSourceLR() {
        if(sourceLR == 0)
            sourceLR = keys.get(0).getSourceLrId();
        return sourceLR;
    }

    public int getTargetLR() {
        if(targetLR == 0)
            targetLR = keys.get(0).getTargetLrId();
        return targetLR;
    }

	public int getTargetLF() {
        if(targetLF == 0)
            targetLF = keys.get(0).getTargetlfid();
        return targetLF;
	}

    public void setTargetLRid(int t) {
        targetLR = t;
    } 

    public void setTargetLFid(int t) {
        targetLF = t;
    } 

    public void setSourceLRid(int t) {
        sourceLR = t;
    }

    public void setLrLfId(int lrLfAssocid) {
        this.lrLfAssocid = lrLfAssocid;
    } 

    public int getLrLfAssocid() {
        return lrLfAssocid;
    }

    public void setId(int stepID) {
        this.stepID = stepID;
    }

    public int getStepID() {
        return stepID;
    }

}
