package org.genevaers.genevaio.ltfile;

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


import org.genevaers.repository.components.enums.LtRecordType;

public interface LTRecord {
    public abstract LtRecordType getRecordType();
    public abstract void setRecordType(LtRecordType type);
    public abstract String getFunctionCode();
    public abstract void setFunctionCode(String c);
    public abstract int getViewId();
    public abstract void setViewId(int viewId);
    public abstract int getFileId();
    public abstract void setFileId(int fileId);
    public abstract int getRowNbr();
    public abstract void setRowNbr(int rowNbr);
    public abstract short getSuffixSeqNbr();
    public abstract void setSuffixSeqNbr(short suffixSeqNbr);
    public abstract short getSourceSeqNbr();
    public abstract void setSourceSeqNbr(short sourceSeqNbr);
    public abstract int getGotoRow1();
    public abstract void setGotoRow1(int row);
    public abstract int getGotoRow2();
    public abstract void setGotoRow2(int row);
}
