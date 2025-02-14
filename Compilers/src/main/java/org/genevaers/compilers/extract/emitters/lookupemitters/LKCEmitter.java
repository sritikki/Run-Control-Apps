package org.genevaers.compilers.extract.emitters.lookupemitters;

import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.ArgHelper;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.components.enums.LtCompareType;

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


public class LKCEmitter extends LookupEmitter {

    public LogicTableF1 emit(LookupPathKey key) {
        LtFuncCodeFactory ltFact = LtFactoryHolder.getLtFunctionCodeFactory();
        LogicTableF1 lkc = (LogicTableF1) ltFact.getLKC(key.getValue());
        ArgHelper.populateArgFromKeyTarget(lkc.getArg(), key);
         
        lkc.setCompareType(LtCompareType.EQ);
        return lkc;
    }
    
}
