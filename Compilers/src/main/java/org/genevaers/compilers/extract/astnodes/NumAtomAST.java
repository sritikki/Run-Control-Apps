package org.genevaers.compilers.extract.astnodes;

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


import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.repository.components.enums.DataType;

public class NumAtomAST extends FormattedASTNode  implements GenevaERSValue, Assignable, CalculationSource{

    private int value;

    public NumAtomAST() {
        type = ASTFactory.Type.NUMATOM;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getValueString() {
        return String.valueOf(value);
    }

    @Override
    public void setNegative() {
        value = -value;
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST col, ExtractBaseAST rhs) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        if(currentViewColumn.getExtractArea() == ExtractArea.AREACALC) {
            ltEmitter.addToLogicTable((LTRecord)fcf.getCTC(String.valueOf(value), currentViewColumn));
        } else if(currentViewColumn.getExtractArea() == ExtractArea.AREADATA) {
            ltEmitter.addToLogicTable((LTRecord)fcf.getDTC(String.valueOf(value), currentViewColumn));
        } else {
            ltEmitter.addToLogicTable((LTRecord)fcf.getSKC(String.valueOf(value), currentViewColumn));
        }
        return null;
    }

    @Override
    public LTFileObject emitSetFunctionCode() {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltEmitter.addToLogicTable((LTRecord)fcf.getSETC(((CalculationAST)parent.getParent()).getAccName(), String.valueOf(value)));
        return null;
    }

    @Override
    public LTFileObject emitAddFunctionCode() {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltEmitter.addToLogicTable((LTRecord)fcf.getADDC(((CalculationAST)parent.getParent()).getAccName(), String.valueOf(value)));
        return null;
    }

    @Override
    public LTFileObject emitSubFunctionCode() {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltEmitter.addToLogicTable((LTRecord)fcf.getSUBC("", String.valueOf(value)));
        return null;
    }

    @Override
    public LTFileObject emitMulFunctionCode() {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltEmitter.addToLogicTable((LTRecord)fcf.getMULC("", String.valueOf(value)));
        return null;
    }

    @Override
    public LTFileObject emitDivFunctionCode() {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltEmitter.addToLogicTable((LTRecord)fcf.getDIVC("", String.valueOf(value)));
        return null;
    }

    @Override
    public DataType getDataType() {
        return overriddenDataType != DataType.INVALID ? overriddenDataType : DataType.ZONED;
    }

    @Override
    public DateCode getDateCode() {
        return (overriddenDateCode != null) ? overriddenDateCode : DateCode.NONE;
    }

}
