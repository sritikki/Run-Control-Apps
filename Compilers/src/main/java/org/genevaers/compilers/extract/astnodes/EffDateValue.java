package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.Cookie;

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


import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.JustifyId;
import org.genevaers.repository.components.enums.LtCompareType;
import org.genevaers.repository.components.enums.LtRecordType;

import com.google.common.flogger.FluentLogger;

public class EffDateValue extends ExtractBaseAST implements EmittableASTNode{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    EffDateValue() {
        type = ASTFactory.Type.EFFDATEVALUE;
    }

    @Override
    public void emit() {
        ExtractBaseAST child = (ExtractBaseAST) children.get(0);
        if(child.getType() == ASTFactory.Type.LRFIELD) {
            emitLKDE();
        } else if(child.getType() == ASTFactory.Type.STRINGATOM) {
            //TODO Is there an LKDC?
        }

    }

    private void emitLKDE() {
        LogicTableF2 lkd = new LogicTableF2();
        lkd.setRecordType(LtRecordType.F2);
        lkd.setFunctionCode("LKDE");

        //TODO Get the field data...
        FieldReferenceAST fld = (FieldReferenceAST)children.get(0);

        LogicTableArg arg = new LogicTableArg();
        fld.populateArg(arg);
        lkd.setArg1(arg);
        arg.setLogfileId(ltEmitter.getFileId());
        arg.setValue(new Cookie(""));

        LogicTableArg arg2 = new LogicTableArg();
        arg2.setFieldContentId(DateCode.CCYYMMDD);
        arg2.setFieldFormat(DataType.BINARY);
        arg2.setStartPosition((short)1);
        arg2.setFieldLength((short)4);
        arg2.setJustifyId(JustifyId.NONE);
        arg2.setValue(new Cookie(""));
        lkd.setArg2(arg2);

        lkd.setCompareType(LtCompareType.EQ);
        ExtractBaseAST.getLtEmitter().addToLogicTable(lkd);
    }

    public String getUniqueKey() {
        String key = "";
        if(getNumberOfChildren() > 0) {
            //There will only be one
            ExtractBaseAST c = (ExtractBaseAST) getChildIterator().next();
            if(c.getType() == ASTFactory.Type.LRFIELD) {
                key = "FLD_" + ((FieldReferenceAST)c).getRef().getComponentId();
            } else if(c.getType() == ASTFactory.Type.DATEFUNC) {
                key = "DTF_" + ((DateFunc)c).getValueString();
            } else if(c.getType() == ASTFactory.Type.STRINGATOM) {
                key = "STR_" + ((StringAtomAST)c).getValueString();
            } else {
                logger.atSevere().log("Unexpected Effective Date type " + c.getType());
            }
        }
        return key;
    }

}
