package org.genevaers.compilers.extract.emitters.lookupemitters;

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


import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.jltviews.JLTView;

public class LKFieldEmitter extends LookupEmitter {

    private int srcLFID;

    public LogicTableF2 emit(LookupPathKey key) {
        // There should be a valid converson check done


        LtFuncCodeFactory ltfact = LtFactoryHolder.getLtFunctionCodeFactory();
        //Can be an LKE or and LKL
        LogicTableF2 lk;
        if(key.getStepNumber() == 1) {
            lk = (LogicTableF2) ltfact.getLKE(Repository.getFields().get(key.getFieldId()), key);
        } else {
            //we need the the mapped field not the original
            JLTView jltv = Repository.getJoinViews().getStepJLTViewFromKey(key);
            LRField redfld = jltv.getRedFieldFromLookupField(key.getFieldId());
            lk = (LogicTableF2) ltfact.getLKL(Repository.getFields().get(key.getFieldId()), key);
            if(redfld != null)
                lk.getArg1().setStartPosition(redfld.getStartPosition()); // Remap to RED LR position
        }
        return lk;
    }

    public void setLogicalFileID(int lfid) {
        srcLFID = lfid;
    }

}
