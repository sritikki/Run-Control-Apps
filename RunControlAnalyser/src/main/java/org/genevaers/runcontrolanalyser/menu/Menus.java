package org.genevaers.runcontrolanalyser.menu;

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


import java.util.HashMap;
import java.util.Map;

public enum Menus {

    INVALID("INVALID"), 
    Main("Main"), 
    DBGen("DBGen"), 
    WBXMLGen("WBXMLGen"),
    FTPFETCH("FTP"), 
    LOCAL("Local"); 

    private final String value;
    private final static Map<String, Menus> CONSTANTS = new HashMap<>();

    static {
        for (Menus c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Menus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static Menus fromValue(String value) {
        Menus constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
