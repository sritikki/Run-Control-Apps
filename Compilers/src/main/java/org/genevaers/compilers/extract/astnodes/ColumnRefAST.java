package org.genevaers.compilers.extract.astnodes;

import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;

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

public class ColumnRefAST extends FormattedASTNode implements CalculationSource, Assignable, Concatable {

    private ViewColumn vc;

    public ColumnRefAST() {
        type = ASTFactory.Type.COLUMNREF;
    }

     @Override
    public LTFileObject emitSetFunctionCode() {
        return null;
    }

    @Override
    public LTFileObject emitAddFunctionCode() {
        return null;
    }

    @Override
    public LTFileObject emitSubFunctionCode() {
        return null;
    }

    @Override
    public LTFileObject emitMulFunctionCode() {
        return null;
    }

    @Override
    public LTFileObject emitDivFunctionCode() {
        return null;
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST lhs, ExtractBaseAST rhs) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        if(currentViewColumn.getExtractArea() == ExtractArea.AREACALC) {
            ltEmitter.addToLogicTable((LTRecord)fcf.getCTX(vc, currentViewColumn));
        } else if(currentViewColumn.getExtractArea() == ExtractArea.AREADATA) {
            ltEmitter.addToLogicTable((LTRecord)fcf.getDTX(vc, currentViewColumn));
        } else {
            ltEmitter.addToLogicTable((LTRecord)fcf.getSKX(vc, currentViewColumn));
        }
        return null;
    }

    public void setViewColumn(ViewColumn c) {
        vc = c;
    }

    public ViewColumn getViewColumn() {
        return vc;
    }

    @Override
    public DataType getDataType() {
        return vc.getDataType();
    }

    @Override
    public DateCode getDateCode() {
        return vc.getDateCode();
    }

    @Override
    public short getConcatinationEntry(ColumnAST col, ExtractBaseAST rhs, short start) {
        return ((ColumnRefAST)rhs).getViewColumn().getFieldLength();
    }

}
