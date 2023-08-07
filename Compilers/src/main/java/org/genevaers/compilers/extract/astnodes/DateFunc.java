package org.genevaers.compilers.extract.astnodes;

import org.antlr.v4.runtime.tree.ParseTree;

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


public class DateFunc extends ExtractBaseAST implements GenevaERSValue{

    private String value;

    public DateFunc() {
        type = ASTFactory.Type.DATEFUNC;
    }

    public String getValue() {
        return value;
    }

    public void resolve(String dateStr, String format) {
        //TODO We need to think about this some more 
        //Check the string matches the format etc
        value = dateStr.replace("\"", "");;
    }

    @Override
    public String getValueString() {
        return value;
    }

}
