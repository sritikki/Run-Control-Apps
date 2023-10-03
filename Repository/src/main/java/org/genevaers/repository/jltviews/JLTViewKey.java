package org.genevaers.repository.jltviews;

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


import org.genevaers.repository.components.LookupType;

public class JLTViewKey implements Comparable<JLTViewKey>{
    
    private int logicalRecordId;
    private boolean sortKeyTitle;

    public JLTViewKey(int lr, boolean skt) {
        logicalRecordId = lr;
        sortKeyTitle = skt;
    }

    @Override
    public int compareTo(JLTViewKey in) {
        int retval = 0;
        if(logicalRecordId > in.logicalRecordId) {
            retval = 1;
        } else if(logicalRecordId == in.logicalRecordId){
            retval = compareSkt(in); 
        } else if(logicalRecordId < in.logicalRecordId){
            retval = -1 ;
        }
        return retval;
    }

    private int compareSkt(JLTViewKey in) {
        if(sortKeyTitle == in.sortKeyTitle) {
            return 0;
        } else {
            return -1;
        }
    }

    public int getLogicalRecordId() {
        return logicalRecordId;
    }

    public boolean isSortKeyTitle() {
        return sortKeyTitle;
    }
    
}
