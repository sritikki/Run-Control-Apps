package org.genevaers.compilers.extract.emitters.comparisonemitters;

import org.genevaers.compilers.extract.astnodes.ColumnRefAST;
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


import org.genevaers.compilers.extract.astnodes.LookupFieldRefAST;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.jltviews.JLTView;

public class CFXLEmitter extends ComparisonEmitter{

    @Override
    public LTFileObject getLTEntry(String op, ExtractBaseAST lhs, ExtractBaseAST rhs) {
        LtFuncCodeFactory ltFact = LtFactoryHolder.getLtFunctionCodeFactory();
        LookupFieldRefAST lkf = (LookupFieldRefAST) rhs;

        lkf.emitJoin(false);
        ltFact.setLogFileId(getLtEmitter().getFileId());
        LogicTableF2 cfxl = (LogicTableF2) ltFact.getCFXL(((ColumnRefAST) lhs).getViewColumn(), ((LookupFieldRefAST) rhs).getRef(), op);
        LogicTableArg arg1 = cfxl.getArg1();
        arg1.setFieldId(((ColumnRefAST) lhs).getViewColumn().getColumnNumber());
        arg1.setLogfileId(((ColumnRefAST) lhs).getViewColumn().getExtractArea().ordinal());
        

        LogicTableArg arg2 = cfxl.getArg2();
        JLTView jv = Repository.getJoinViews().getJLTViewFromLookup(lkf.getLookup(), false);
        LRField redFld = jv.getRedFieldFromLookupField(lkf.getRef().getComponentId());
        arg2.setLogfileId(lkf.getLookup().getTargetLFID());
        arg2.setLrId(lkf.getRef().getLrID());
        arg2.setFieldId(lkf.getRef().getComponentId());
        arg2.setStartPosition(redFld.getStartPosition());
        arg2.setFieldContentId(DateCode.NONE);
        arg2.setLogfileId(lkf.getLookup().getTargetLFID());
        return cfxl;
    }

}
