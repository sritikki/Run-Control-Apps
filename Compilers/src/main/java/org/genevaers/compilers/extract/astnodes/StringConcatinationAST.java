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


import java.util.Iterator;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.emitters.assignmentemitters.AssignmentDataCheckerFactory;
import org.genevaers.compilers.extract.emitters.assignmentemitters.AssignmentEmitter;
import org.genevaers.compilers.extract.emitters.assignmentemitters.AssignmentEmitterFactory;
import org.genevaers.compilers.extract.emitters.assignmentemitters.DataTypeChecker;
import org.genevaers.compilers.extract.emitters.assignmentemitters.DataTypeChecker.DTResult;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.LtRecordType;

import com.google.common.flogger.FluentLogger;

public class StringConcatinationAST extends FormattedASTNode implements EmittableASTNode, Assignable{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private String tag;

    public StringConcatinationAST() {
        type = ASTFactory.Type.STRINGCONCAT;
    }

    public void setTag(String string) {
        tag = string;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void emit() {
        //Simply iterate through the children
        //And generate the appropriate DTs
        //The we will need to fix up the start position in the column?
        // if(children.size() == 2) {
        //     Iterator<ASTBase> ci = children.iterator();
        //     ExtractBaseAST rhs = (ExtractBaseAST) ci.next();
        //     ExtractBaseAST lhs = (ExtractBaseAST) ci.next();
        //     ColumnAST col = (ColumnAST)ci.next();

        //     if(lhs instanceof CastAST) {
        //         lhs = ((CastAST)rhs).decast();
        //     }
        //     if(rhs instanceof CastAST) {
        //         rhs = ((CastAST)rhs).decast();
        //     }
        //     ltEmitter.setSuffixSeqNbr((short)col.getViewColumn().getColumnNumber());
        //     //This should really just result in a simple string assignment
        //     if(rhs instanceof EmittableASTNode)
        //             ((EmittableASTNode)rhs).emit();

        //     // This is where we need the rules checking and data type adjustment
            
            
        //     // The rhs will have its own emmitter since it will be assignable
        //     // If not ... boom
        //     DataTypeChecker dc = AssignmentDataCheckerFactory.getDataChecker(col, rhs);
        //     DTResult res = dc.verifyOperands(col, rhs);
        //     if(res ==DTResult.ASSIGN_OK) {
        //         LTFileObject lto = ((Assignable)rhs).getAssignmentEntry(col, (ExtractBaseAST)rhs);
        //         ltEmitter.addToLogicTable((LTRecord)lto);
        //     }
        //     // if(ae.isLookup()) {
        //     //     emitLookupDefault(col);
        //     // }
        //     //} else {
        //     //    logger.atSevere().log("Should never get here");
        //     //}
        //     col.emit(); //In case there is a sort title emit
        //     } else {
        //         int workToDo = 1;
        //     }
    }

    private void emitLookupDefault(ColumnAST col) {
        //Emit goto followed by correct DTC
        LogicTableF0 gotoRec = emitGoto();
        LTFileObject dtc;
        if(col.getViewColumn().getDataType() == DataType.ALPHANUMERIC) {
            dtc = emitDTC(col, " ");
        } else {
            dtc = emitDTC(col, "0");
        }
        ltEmitter.addToLogicTable((LTRecord)dtc);
        gotoRec.setGotoRow1(ltEmitter.getNumberOfRecords());
    }

    private LTFileObject emitDTC(ColumnAST col, String val) {
        ViewColumn vc = col.getViewColumn();
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        LogicTableF1 ccol;
        if(vc.getExtractArea() == ExtractArea.SORTKEY) {
            ccol = ((LogicTableF1)fcf.getSKC(val, vc));
        } else if(vc.getExtractArea() == ExtractArea.AREADATA) {
            ccol = ((LogicTableF1)fcf.getDTC(val, vc));
        } else {
            ccol = ((LogicTableF1)fcf.getCTC(val, vc));
        }
        ccol.getArg().setFieldContentId(DateCode.NONE);
        return ccol;
    }

    private LogicTableF0 emitGoto() {
        LogicTableF0 f0 = new LogicTableF0();
        f0.setRecordType(LtRecordType.F0);
        f0.setFunctionCode("GOTO");
        f0.setGotoRow2(0);
        ltEmitter.addToLogicTable(f0);
        return f0;
    }

    @Override
    public LTFileObject getAssignmentEntry(ColumnAST col, ExtractBaseAST rhs) {
        //Iterate through the child nodes and get the appropriate function code;
        Iterator<ASTBase> ci = children.iterator();
        short currentPos = col.getViewColumn().getStartPosition();
        while(ci.hasNext()) {
            Assignable c = (Assignable) ci.next();
            LTRecord ltfo = (LTRecord) c.getAssignmentEntry(col, (ExtractBaseAST)c);
            switch(ltfo.getRecordType()) {
                case F2:
                    LogicTableArg arg1 = ((LogicTableF2)ltfo).getArg1();
                    LogicTableArg arg2 = ((LogicTableF2)ltfo).getArg2();
                    arg2.setStartPosition(currentPos);
                    arg2.setFieldLength(arg1.getFieldLength());
                    currentPos += arg1.getFieldLength();
                    break;
                case F1:
                    LogicTableArg arg = ((LogicTableF1)ltfo).getArg();
                    arg.setStartPosition(currentPos);
                    arg.setFieldLength((short)arg.getValueLength());
                    currentPos += arg.getValueLength();
                    break;
                default:
                    System.out.println("Not handling type" + ltfo.getRecordType());
                break;
            }
            ltEmitter.addToLogicTable(ltfo);
        }

        return null;

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
