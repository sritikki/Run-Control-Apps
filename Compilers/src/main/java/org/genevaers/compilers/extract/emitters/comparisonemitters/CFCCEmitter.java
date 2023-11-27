package org.genevaers.compilers.extract.emitters.comparisonemitters;

import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;

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


import org.genevaers.compilers.extract.astnodes.GenevaERSValue;
import org.genevaers.compilers.extract.astnodes.RundateAST;
import org.genevaers.compilers.extract.emitters.helpers.EmitterArgHelper;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableCC;

public class CFCCEmitter extends ComparisonEmitter{

    @Override
    public LTFileObject getLTEntry(String op, ExtractBaseAST lhs, ExtractBaseAST rhs) {
        LtFuncCodeFactory ltFact = LtFactoryHolder.getLtFunctionCodeFactory();
        LogicTableCC cfcc = (LogicTableCC) ltFact.getCFCC(((GenevaERSValue)lhs).getValueString(), ((GenevaERSValue)rhs).getValueString(), op);
        if(lhs.getType() == ASTFactory.Type.RUNDATE) {
            RundateAST rd = ((RundateAST)lhs);
            cfcc.setValue1Length(rd.rawDateValue());
            cfcc.setValue1(rd.getValueBinaryString());
            cfcc.setFieldContentCode(rd.getDateCode());
        }
        if(rhs.getType() == ASTFactory.Type.RUNDATE) {
            RundateAST rd = ((RundateAST)rhs);
            cfcc.setValue2Length(((RundateAST)rhs).rawDateValue());
            cfcc.setValue2(rd.getValueBinaryString());
            cfcc.setFieldContentCode(rd.getDateCode());
        }
        return cfcc;
    }

}
