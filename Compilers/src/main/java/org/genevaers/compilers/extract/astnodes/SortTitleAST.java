package org.genevaers.compilers.extract.astnodes;

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


import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LookupPath;

public class SortTitleAST extends LookupPathAST {

    private int sortTitleLookupId;
    private int sortTitleFieldId;

    public SortTitleAST() {
        type = Type.SORTTITLE;
    }

    @Override
    public void emit() {
        System.out.println("emit sort title key generation");
        //Treat as though we are emitting an ordinary lookup
        //but do not generate an LUSM etc
        lookup =  Repository.getLookups().get(sortTitleLookupId);
        lkEmitter.emitJoin(this, true);        
    }

    public void setSortTitleFieldId(int sortTitleFieldId) {
        this.sortTitleFieldId = sortTitleFieldId;
    }

    public int getSortTitleFieldId() {
        return sortTitleFieldId;
    }

    public int getSortTitleLookupId() {
        return sortTitleLookupId;
    }

    public void setSortTitleLookupId(int sortTitleLookupId) {
        this.sortTitleLookupId = sortTitleLookupId;
    }

    public LogicTableF1 emitKSLK() {
        LtFuncCodeFactory ltFact = LtFactoryHolder.getLtFunctionCodeFactory();
        LogicTableF1 kslk = (LogicTableF1) ltFact.getKSLK(Repository.getFields().get(sortTitleFieldId));
        return kslk;
    }

}
