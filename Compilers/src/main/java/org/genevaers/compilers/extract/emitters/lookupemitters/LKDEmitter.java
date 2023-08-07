package org.genevaers.compilers.extract.emitters.lookupemitters;

import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.components.enums.LtCompareType;
import org.genevaers.repository.components.enums.LtRecordType;

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


public class LKDEmitter extends LookupEmitter {

    public LogicTableF1 emit(LookupPathKey key) {
        // There should be a valid converson check done

        //arg1 from the source field
        //arg2 is the target field

        LogicTableF1 lkd = new LogicTableF1();
        lkd.setRecordType(LtRecordType.F1);
        lkd.setFunctionCode("LKDC");

        //TODO We need to check the conversion details
        //key source field format -> target field format

        //Let's just assume okay for the moment
        LogicTableArg arg = new LogicTableArg();
        populateArgFromKeyTarget(lkd.getArg(), key);
        lkd.setArg(arg);

        lkd.setCompareType(LtCompareType.EQ);
        return lkd;
    }
}
