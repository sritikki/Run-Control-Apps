package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.repository.components.LRField;

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


import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DateCode;

public abstract class ColumnAST extends ExtractBaseAST implements EmittableASTNode{

    protected ViewColumn vc;
    private DateCode workingCode;
    private DateCode originalCode;

    public ColumnAST() {
    }

    public void setViewColumn(ViewColumn vc) {
        this.vc = vc;
    }

    public ViewColumn getViewColumn() {
        return vc;
    }

    @Override
    public void emit() {
        //Nothing to do... 
        //setting the currenc volumn do somewhere else?
        currentViewColumn = vc;
    }

    public LTFileObject getAccumLtEntry(String accumulatorName){
        return null;}

    public LTFileObject getFieldLtEntry(LRField field) {
        return null;
    }

    public LTFileObject getPriorFieldLtEntry(LRField field) {
        return null;
    }

    public LTFileObject getConstLtEntry(String string) {
        return null;
    }

    public void setWorkingCode(DateCode workingCode) {
        originalCode = vc.getDateCode();
        this.workingCode = workingCode;
    }

    public DateCode getWorkingCode() {
        return workingCode != null ? workingCode : vc.getDateCode();
    }

    public void restoreDateCode() {
        if(workingCode != null) {
            vc.setDateCode(originalCode);
        }
    }

}
