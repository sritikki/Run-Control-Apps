package org.genevaers.repository.calculationstack;

import java.nio.ByteBuffer;

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


import org.genevaers.repository.calculationstack.CalcStack.CalcStackOpcode;

public class CalcStackShortStringEntry extends CalcStackEntry {

    static final int SHORT_LENGTH = 48;
    String value;

    //CalcStackEntries are of a limited number of types
    //The all have a length of some sort 
    //  opcode + variable for a string
    //         + 4 when there is an int value like a column number or goto
    //         + 48 for number constants
    // Make derived types to capture these
    // Then as they are created 
    //  set their offset (prev offset + length of entry)
    //          (add a get next offset function?)
    //  their value if known
    public CalcStackShortStringEntry() {

    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public void setValueFromInt(int value) {
        this.value = Integer.toString(value);
    }
    
    public String getValue() {
        return value;
    }

    public int length() {
        return Integer.BYTES +  SHORT_LENGTH;
    }
    
    @Override
    public void addTo(ByteBuffer buffer) {
        buffer.putInt(opCode.ordinal());
        addAndConvertStringIfNeeded(buffer, value, SHORT_LENGTH);
    }
    
}
