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


import java.util.Iterator;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.compilers.extract.emitters.helpers.EmitterArgHelper;
import org.genevaers.compilers.extract.emitters.lookupemitters.LookupEmitter;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.enums.LtCompareType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.LtRecordType;

public class LookupPathAST extends FormattedASTNode implements EmittableASTNode{

    protected LookupPath lookup;
    protected LookupEmitter lkEmitter = new LookupEmitter();
    protected Integer goto1 = Integer.valueOf(0);
    protected Integer goto2 = Integer.valueOf(0);

    protected SymbolList symbols;
    protected EffDateValue effDateValue;

    @Override
    public void emit() {
        goto1 = ExtractBaseAST.getLtEmitter().getNumberOfRecords();
    }
    

    public LookupPath getLookup() {
        return lookup;
    }

    public Integer getGoto1() {
        return goto1;
    }

    public Integer getGoto2() {
        return goto2;
    }

    @Override
    public void resolveGotos(Integer compT, Integer compF, Integer joinT, Integer joinF) {
        lkEmitter.resolveGotos(joinT, joinF, isNot);
    }

    public String getSymbolValue(String symbolicName) {
        String val = null;
        if(symbols != null) {
            val = symbols.getValueFor(symbolicName);
        }
        return val;
    }

    public void setSymbols(SymbolList symbols) {
        this.symbols = symbols;
    }

    public void setEffDateValue(EffDateValue effDateValue) {
        this.effDateValue = effDateValue;
    }


    public void emitEffectiveDate() {
        if(lookup.isEffectiveDated()) {
            if(effDateValue != null) {
                effDateValue.emit();
            } else {
                emitDefaultLKDC();
            }
         }
    }


    private void emitDefaultLKDC() {

        LogicTableF1 lkd = new LogicTableF1();
        lkd.setRecordType(LtRecordType.F1);
        lkd.setFunctionCode("LKDC");

        LogicTableArg arg = new LogicTableArg();
        arg.setStartPosition((short)1);
        arg.setFieldContentId(DateCode.CYMD);
        arg.setFieldLength((short)4);
        arg.setFieldFormat(DataType.BINARY);
        EmitterArgHelper.setArgValueFrom(arg, 0);
        arg.setValueLength(-1);  //TODO make enum for the cookie values

        lkd.setCompareType(LtCompareType.EQ);
        ExtractBaseAST.getLtEmitter().addToLogicTable(lkd);
    }

    public LookupEmitter getLkEmitter() {
        return lkEmitter;
    }


    @Override
    public DataType getDataType() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public DateCode getDateCode() {
        // TODO Auto-generated method stub
        return null;
    }
}
