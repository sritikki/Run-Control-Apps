package org.genevaers.compilers.extract.astnodes;

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


import org.genevaers.genevaio.ltfile.LTFileObject;

public class SymbolEntry extends ExtractBaseAST  implements LookupKeyEntry {
    
    private String name;
    private String value;

    public SymbolEntry() {
        type = ASTFactory.Type.SYMBOL;
    }

    public void setSymbol(String name) {
        this.name = name;
    }

    public void setValue(String text) {
        value = text;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        if(value.charAt(0) == '"') {
            return value.substring(1, value.length() - 1);
        } else {
            return value;
        }
    }

    public boolean matches(String symbolicName) {
        return name.substring(1).equals(symbolicName);
    }

    @Override
    public LTFileObject emitKey() {
        // TODO Auto-generated method stub
        return null;
    }
}
