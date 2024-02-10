package org.genevaers.compilers.extract.emitters.comparisonemitters;

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
import org.genevaers.compilers.extract.astnodes.LookupFieldRefAST;
import org.genevaers.compilers.extract.emitters.helpers.EmitterArgHelper;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.jltviews.JLTView;

public class CFLCEmitter extends ComparisonEmitter{

    @Override
    public LTFileObject getLTEntry(String op, ExtractBaseAST lhs, ExtractBaseAST rhs) {
        LtFuncCodeFactory ltFact = LtFactoryHolder.getLtFunctionCodeFactory();
        LookupFieldRefAST lkf = (LookupFieldRefAST) lhs;
        
        lkf.getLkEmitter().emitJoin(lkf, false);
        LogicTableF1 cflc = (LogicTableF1) ltFact.getCFLC(lkf.getRef(), ((GenevaERSValue)rhs).getValueString(), op);
        LogicTableArg arg = cflc.getArg();
        EmitterArgHelper.setArgVal(rhs, arg);
        JLTView jv = Repository.getJoinViews().getJLTViewFromLookup(lkf.getLookup(), false);
        LRField redFld = jv.getRedFieldFromLookupField(lkf.getRef().getComponentId());
        arg.setLogfileId(lkf.getLookup().getTargetLFID());
        arg.setLrId(lkf.getRef().getLrID());
        arg.setFieldId(lkf.getRef().getComponentId());
        arg.setStartPosition(redFld.getStartPosition());
        arg.setFieldContentId(redFld.getDateTimeFormat());
        cflc.setArg(arg);

        return cflc;  
    }
    
}
