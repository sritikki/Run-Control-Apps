package org.genevaers.compilers.extract.astnodes;

import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
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


public class RundateAST extends FormattedASTNode implements GenevaERSValue, Assignable{

    private String value;

    public RundateAST() {
        type = ASTFactory.Type.RUNDATE;
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

    @Override
    public String getValueString() {
        return value;
    }

    @Override
    public DataType getDataType() {
        //Note the Cookie thing comes into play here
        return overriddenDataType != DataType.INVALID ? overriddenDataType : DataType.ALPHANUMERIC;
    }

    private DateCode rawDateCode() {
        //or is it the 
        switch (value) {
        case "RUNDAY":
            return DateCode.CCYYMMDD;
        case "RUNMONTH":
            return DateCode.CYM;
        case "RUNYEAR":
            return DateCode.CCYY;
        default:
            return DateCode.NONE;
        }
    }
/*

uint16_t
RunDateASTNode::getLength() const
{
    switch (getType()) {
    case SAFRLexerTokenTypes::RUNDAY:
        return 8;
    case SAFRLexerTokenTypes::RUNMONTH:
        return 6;
    case SAFRLexerTokenTypes::RUNYEAR:
        return 4;
    }
    return 0;
}
 */

    @Override
    public DateCode getDateCode() {
        //Depends on value
        return (overriddenDateCode != null) ? overriddenDateCode : rawDateCode();
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST col, ExtractBaseAST rhs) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        LTRecord fc;
        if(currentViewColumn.getExtractArea() == ExtractArea.AREACALC) {
            fc = (LTRecord) fcf.getCTC(String.valueOf(value), currentViewColumn);
        } else if(currentViewColumn.getExtractArea() == ExtractArea.AREADATA) {
            fc = (LTRecord) fcf.getDTC(String.valueOf(value), currentViewColumn);
        } else {
            fc = (LTRecord) fcf.getSKC(String.valueOf(value), currentViewColumn);
        }
        fc.setSourceSeqNbr((short) (ltEmitter.getLogicTable().getNumberOfRecords() + 1));
        ltEmitter.addToLogicTable((LTRecord)fc);
        return null;
    }

}
