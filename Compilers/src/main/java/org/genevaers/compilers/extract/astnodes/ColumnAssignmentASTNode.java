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
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.LtRecordType;

import com.google.common.flogger.FluentLogger;

public class ColumnAssignmentASTNode extends ExtractBaseAST implements EmittableASTNode{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private String tag;

    public ColumnAssignmentASTNode() {
        type = ASTFactory.Type.COLUMNASSIGNMENT;
    }

    public void setTag(String string) {
        tag = string;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void emit() {
        // C++ has emitter classes - table driven to examine datatypes and dates
        // And warnings on date conversion or possible truncation etc

        // Here we're just going to generate a DTC or DTE
        // Depending on type of the first child.
        // Ignore the error cases for the moment

        // We should be able to derive the column details locally
        // Therefore the target of DT
        // Our first child will give the source of the DT

        if (children.size() == 2) {

            Iterator<ASTBase> ci = children.iterator();
            ExtractBaseAST rhs = (ExtractBaseAST) ci.next();
            ColumnAST col = (ColumnAST) ci.next();

            LookupFieldRefAST lkref = checkForJOINandEmitIfRequired();

            if (rhs instanceof CastAST) {
                rhs = ((CastAST) rhs).decast();
            }
            ltEmitter.setSuffixSeqNbr((short) col.getViewColumn().getColumnNumber());
            if (rhs instanceof EmittableASTNode)
                ((EmittableASTNode) rhs).emit();

            // This is where we need the rules checking and data type adjustment

            // The rhs will have its own emmitter since it will be assignable
            // If not ... boom
            DataTypeChecker dc = AssignmentDataCheckerFactory.getDataChecker(col, rhs);
            DTResult res = dc.verifyOperands(col, rhs);
            if (res == DTResult.ASSIGN_OK) {
                LTRecord lto = (LTRecord) ((Assignable) rhs).getAssignmentEntry(col, (ExtractBaseAST) rhs);
                if (lto != null) {
                    lto.setSourceSeqNbr((short) (ltEmitter.getLogicTable().getNumberOfRecords()));
                }
                ltEmitter.addToLogicTable((LTRecord) lto);
            }
            if (lkref != null) {
                lkref.emitLookupDefault();
                //We now know how the set the lookup goto
            }

            col.emit(); // In case there is a sort title emit
            col.restoreDateCode();
        } else {
            int workToDo = 1;
        }
    }

    private LookupFieldRefAST checkForJOINandEmitIfRequired() {
        LookupFieldRefAST lkref = null;
        ExtractBaseAST c1 = (ExtractBaseAST) children.get(0);
        if(c1.getType() == ASTFactory.Type.CALCULATION) {
            Iterator<ASTBase> calcIterator = c1.getChildIterator();
            ExtractBaseAST setterChild = (ExtractBaseAST) calcIterator.next().getChildIterator().next();
            ExtractBaseAST opChild = (ExtractBaseAST) calcIterator.next().getChildIterator().next();
            if(setterChild.getType() == ASTFactory.Type.LOOKUPFIELDREF) {
                lkref = (LookupFieldRefAST)setterChild;
                lkref.getLkEmitter().emitJoin(lkref, false);
            }
            if(opChild.getType() == ASTFactory.Type.LOOKUPFIELDREF) {
                lkref = (LookupFieldRefAST)opChild;
                lkref.getLkEmitter().emitJoin(lkref, false);
            }
        } else if(c1.getType() == ASTFactory.Type.LOOKUPFIELDREF) {
            lkref = (LookupFieldRefAST) c1;
            lkref.getLkEmitter().emitJoin(lkref, false);
        }
        return lkref;
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

}
