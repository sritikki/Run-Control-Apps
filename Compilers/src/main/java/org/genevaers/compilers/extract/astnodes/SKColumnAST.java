package org.genevaers.compilers.extract.astnodes;

import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.ViewColumn;

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


public class SKColumnAST extends ColumnAST {

    public SKColumnAST(ViewColumn vc) {
        type = ASTFactory.Type.SK_COLUMN;
        this.vc = vc;
    }

    @Override
    public void emit() {
        //Nothing to do... maybe we should be using an interface?
        currentViewColumn = vc;
        if(getNumberOfChildren() > 0) {
            //Must have a sort title
            SortTitleAST st = (SortTitleAST)getChildIterator().next();
            st.emit();
        }
    }

    @Override
    public LTFileObject getAccumLtEntry(String accumulatorName) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        return fcf.getSKA(accumulatorName, vc);
    }

    @Override
    public LTFileObject getFieldLtEntry(LRField field) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        return fcf.getSKE(field, vc);
    }

    @Override
    public LTFileObject getConstLtEntry(String value) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        return fcf.getSKC(value, vc);
    }
    
}
