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
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.grammar.ExtractParserLexer;
import org.genevaers.grammar.GenevaERSParser;

public class SkipIfAST extends ExtractBaseAST implements EmittableASTNode {

    private Integer nextViewPosition = 0;

     public SkipIfAST() {
        type = ASTFactory.Type.SKIPIF;
    }

    @Override
    public void emit() {
        // emit a CF
        // C++ we have a 
        emitChildNodes();
        //Note the end of logic table so we can resolve gotos
        //But could be done via the logictable itself?
        setEndOfLogic(ltEmitter.getNumberOfRecords());
    }

    @Override
    public void resolveGotos(Integer comparisonT, Integer comparisonF, Integer joinT, Integer joinfF) {
        Iterator<ASTBase> ci = children.iterator();
        while(ci.hasNext()) {
            ExtractBaseAST c = (ExtractBaseAST) ci.next();
            c.resolveGotos(nextViewPosition, getEndOfLogic(), null, getEndOfLogic());
        }
    }   

    public void setNextViewPosition(Integer nextViewPosition) {
        this.nextViewPosition = nextViewPosition.intValue();
    }

}
