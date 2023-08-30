package org.genevaers.compilers.extract.astnodes;

import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.LTFileObject;

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


public class BetweenFunc extends ExtractBaseAST implements GenevaERSValue, Assignable{

    private String value;
    private String function;

    public BetweenFunc() {
        type = ASTFactory.Type.BETWEENFUNC;
    }

    public void setValue(String value) {
        this.value = value.replace("\"", "");
    }

    public String getValue() {
        //map from the string to the magic code
        //Also there may be a child node here...
        //Keep as a node or just parse the ()?
        return value;
    }

    @Override
    public String getValueString() {
        return value;
    }

    public void setFunction(String text) {
        function = text;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST col, ExtractBaseAST rhs) {
        // make a FNxx function code dependent upon the child nodes
        // Ah the FN writes to an accumulator?
        // So it should be treated like an accumulator? More like a CalculationAST?
        LTFileObject fncc = LtFactoryHolder.getLtFunctionCodeFactory().getFNCC(function, null, null);
        return fncc;
    }

}
