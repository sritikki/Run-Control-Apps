package org.genevaers.compilers.extract.emitters.comparisonemitters;

import org.genevaers.compilers.extract.astnodes.CalculationAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.compilers.extract.astnodes.GenevaERSValue;
import org.genevaers.compilers.extract.astnodes.LookupFieldRefAST;

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


import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableNameF1;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.jltviews.JLTView;

public class CFALEmitter extends ComparisonEmitter{

    @Override
    public LTFileObject getLTEntry(String op, ExtractBaseAST lhs, ExtractBaseAST rhs) {
        CalculationAST calcNode = ((CalculationAST) lhs);
        calcNode.emit();
        LtFuncCodeFactory ltFact = new LtFuncCodeFactory();
        LookupFieldRefAST lkf = (LookupFieldRefAST) rhs;
        

        lkf.getLkEmitter().emitJoin(lkf, false);
        LogicTableNameF1 cfal = (LogicTableNameF1) ltFact.getCFAL(calcNode.getAccName(), lkf.getRef(), op);
        LogicTableArg arg = cfal.getArg();
        JLTView jv = Repository.getJoinViews().getJLTViewFromLookup(lkf.getLookup(), false);
        LRField redFld = jv.getRedFieldFromLookupField(lkf.getRef().getComponentId());
        arg.setLogfileId(lkf.getLookup().getTargetLFID());
        arg.setLrId(lkf.getRef().getLrID());
        arg.setFieldId(lkf.getRef().getComponentId());
        arg.setStartPosition(redFld.getStartPosition());
        arg.setFieldContentId(DateCode.NONE); //TODO unsure is this is always the case?
        cfal.setArg(arg);

        return cfal;  
    }

}
