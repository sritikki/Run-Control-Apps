package org.genevaers.repository.data;

public class ViewAreaValues {
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
    private short dtLen = 0;
    private short ctLen = 0;
    private short skLen = 0;
    private short skTLen = 0; 

    public short getDtLen() {
        return dtLen;
    }

    public void setDtLen(short dtLen) {
        this.dtLen = dtLen;
    }

    public void addDtLen(short dtLen) {
        this.dtLen += dtLen;
    }

    public short getCtLen() {
        return ctLen;
    }

    public void setCtLen(short ctLen) {
        this.ctLen = ctLen;
    }

    public void addCtLen(short ctLen) {
        this.ctLen += ctLen;
    }

    public short getSkLen() {
        return skLen;
    }

    public void setSkLen(short skLen) {
        this.skLen = skLen;
    }

    public void addSkLen(short skLen) {
        this.skLen += skLen;
    }

    public short getSkTLen() {
        return skTLen;
    }

    public void setSkTLen(short skTLen) {
        this.skTLen = skTLen;
    }

    public void addSkTLen(short skTLen) {
        this.skTLen += 8 + skTLen; //To allow for the LF LR ids
    }

}
