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


import java.util.Iterator;

import org.genevaers.compilers.base.ASTBase;

public class SymbolList extends ExtractBaseAST {
    
    public SymbolList() {
        type = ASTFactory.Type.SYMBOLLIST;
    }

    public String getValueFor(String symbolicName) {
        String val = null;
        boolean notFound = true;
        Iterator<ASTBase> ci = children.iterator();
        while(notFound && ci.hasNext()) {
            SymbolEntry sym = (SymbolEntry)ci.next();
            if(sym.matches(symbolicName)) {
                val = sym.getValue();
                notFound = false;
            }
        }
        return val;
    }

    public String getUniqueKey() {
        String key = "";
        Iterator<ASTBase> ci = children.iterator();
        while(ci.hasNext()) {
            SymbolEntry sym = (SymbolEntry)ci.next();
            key += "SY_" +sym.getValue() + "_";
        }
        return key ;
    }

}
