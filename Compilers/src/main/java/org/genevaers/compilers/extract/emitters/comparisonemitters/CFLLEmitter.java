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


import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.compilers.extract.astnodes.LookupFieldRefAST;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.jltviews.JLTView;

public class CFLLEmitter extends ComparisonEmitter{

    @Override
    public LTFileObject getLTEntry(String op, ExtractBaseAST lhs, ExtractBaseAST rhs) {
        LtFuncCodeFactory ltFact = new LtFuncCodeFactory();
        LookupFieldRefAST lkf = (LookupFieldRefAST) lhs;
        LookupFieldRefAST rkf = (LookupFieldRefAST) rhs;

        lkf.getLkEmitter().emitJoin(lkf, false);
        rkf.getLkEmitter().emitJoin(rkf, false);
        ltFact.setLogFileId(getLtEmitter().getFileId());
        LogicTableF2 cfll = (LogicTableF2) ltFact.getCFLL(((LookupFieldRefAST) lhs).getRef(), ((LookupFieldRefAST) rhs).getRef(), op);

        LogicTableArg arg = cfll.getArg1();
        JLTView jv = Repository.getJoinViews().getJLTViewFromLookup(lkf.getLookup(), false);
        LRField redFld = jv.getRedFieldFromLookupField(lkf.getRef().getComponentId());
        arg.setLogfileId(lkf.getLookup().getTargetLFID());
        arg.setLrId(lkf.getRef().getLrID());
        arg.setFieldId(lkf.getRef().getComponentId());
        arg.setStartPosition(redFld.getStartPosition());
        arg.setFieldContentId(DateCode.NONE); //TODO unsure is this is always the case?
        cfll.setArg1(arg);

        LogicTableArg arg2 = cfll.getArg2();
        JLTView jv2 = Repository.getJoinViews().getJLTViewFromLookup(rkf.getLookup(), false);
        LRField redFld2 = jv2.getRedFieldFromLookupField(rkf.getRef().getComponentId());
        arg2.setLogfileId(rkf.getLookup().getTargetLFID());
        arg2.setLrId(rkf.getRef().getLrID());
        arg2.setFieldId(rkf.getRef().getComponentId());
        arg2.setStartPosition(redFld2.getStartPosition());
        arg2.setFieldContentId(DateCode.NONE); //TODO unsure is this is always the case?
        cfll.setArg1(arg);

        return cfll;
    }

}
