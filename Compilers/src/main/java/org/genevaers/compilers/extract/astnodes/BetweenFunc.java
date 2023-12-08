package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.ArgHelper;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableNameF1;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;

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


public class BetweenFunc extends FormattedASTNode implements Assignable, CalculationSource, EmittableASTNode {

    private String accName;
    private int accumulatorNumber;
    private String value;
    private String function;

    public BetweenFunc() {
        type = ASTFactory.Type.BETWEENFUNC;
    }

    public void setValue(String value) {
        this.value = value.replace("\"", "");
    }

    public String getValue() {
        //map from the string to the magic code
        //Also there may be a child node here...
        //Keep as a node or just parse the ()?
        return value;
    }

    public void setFunction(String text) {
        function = text;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST col, ExtractBaseAST rhs) {
        // make a FNxx function code dependent upon the child nodes
        // Ah the FN writes to an accumulator?
        // So it should be treated like an accumulator? More like a CalculationAST?

        //Need to deal with different types here...
        StringAtomAST c1 = (StringAtomAST) children.get(0);
        StringAtomAST c2 = (StringAtomAST) children.get(1);
        LogicTableArg arg1 = ArgHelper.makeDefaultArg();
        arg1.setFieldFormat(getDataType());
        ArgHelper.setArgValueFrom(arg1, c1.getValue());
        LogicTableArg arg2 = ArgHelper.makeDefaultArg();
        arg2.setFieldFormat(getDataType());
        ArgHelper.setArgValueFrom(arg2, c2.getValue());
        LTFileObject fncc = LtFactoryHolder.getLtFunctionCodeFactory().getFNCC(accName, arg1, arg2);
        
        ltEmitter.addToLogicTable((LTRecord)fncc);
        LogicTableNameF1 dta = (LogicTableNameF1) LtFactoryHolder.getLtFunctionCodeFactory().getDTA(accName, col.getViewColumn());
        dta.getArg().setLogfileId(ltEmitter.getFileId());
        ltEmitter.addToLogicTable((LTRecord)dta);
        return null;
    }

    @Override
    public void emit() {
         if(accName == null) {
            generateAccumulatorName();
            ltEmitter.addToLogicTable((LTRecord)LtFactoryHolder.getLtFunctionCodeFactory().getDIMN());
        }
        emitChildNodes();
        if(((ExtractBaseAST)parent).getType() != ASTFactory.Type.COLUMNASSIGNMENT) {
            StringAtomAST c1 = (StringAtomAST) children.get(0);
            StringAtomAST c2 = (StringAtomAST) children.get(1);
            LogicTableArg arg1 = ArgHelper.makeDefaultArg();
            arg1.setFieldFormat(getDataType());
            ArgHelper.setArgValueFrom(arg1, c1.getValue());
            LogicTableArg arg2 = ArgHelper.makeDefaultArg();
            arg2.setFieldFormat(getDataType());
            ArgHelper.setArgValueFrom(arg2, c2.getValue());
            LTFileObject fncc = LtFactoryHolder.getLtFunctionCodeFactory().getFNCC(accName, arg1, arg2);
            ltEmitter.addToLogicTable((LTRecord)fncc);
        }
    }

    @Override
    public LTFileObject emitSetFunctionCode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emitSetFunctionCode'");
    }

    @Override
    public LTFileObject emitAddFunctionCode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emitAddFunctionCode'");
    }

    @Override
    public LTFileObject emitSubFunctionCode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emitSubFunctionCode'");
    }

    @Override
    public LTFileObject emitMulFunctionCode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emitMulFunctionCode'");
    }

    @Override
    public LTFileObject emitDivFunctionCode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emitDivFunctionCode'");
    }

    public String getAccName() {
        return accName;
    }

    //Make common in a base?
    private void generateAccumulatorName() {
        if(accName == null) {
            if(currentViewColumn != null) {
                accName = LtFactoryHolder.getLtFunctionCodeFactory().generateAccumulatorName(currentViewSource, currentViewColumn.getColumnNumber());
            } else {
                accName = LtFactoryHolder.getLtFunctionCodeFactory().generateAccumulatorName(currentViewSource, 0);
            }
        }
    }

    @Override
    public DataType getDataType() {
        return overriddenDataType != DataType.INVALID ? overriddenDataType : DataType.ALPHANUMERIC;
    }

    @Override
    public DateCode getDateCode() {
        return (overriddenDateCode != null) ? overriddenDateCode : DateCode.NONE;
    }
}
