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
        //C++ has emitter classes - table driven to examine datatypes and dates
        //And warnings on date conversion or possible truncation etc

        //Here we're just going to generate a DTC or DTE
        //Depending on type of the first child.
        //Ignore the error cases for the moment

        //We should be able to derive the column details locally
        // Therefore the target of DT
        // Our first child will give the source of the DT

        Iterator<ASTBase> ci = children.iterator();
        ExtractBaseAST rhs = (ExtractBaseAST) ci.next();
        ColumnAST col = (ColumnAST)ci.next();

        if(rhs instanceof CastAST) {
            rhs = ((CastAST)rhs).decast();
        }
            ltEmitter.setSuffixSeqNbr((short)col.getViewColumn().getColumnNumber());
            if(rhs instanceof EmittableASTNode)
                 ((EmittableASTNode)rhs).emit();

            // This is where we need the rules checking and data type adjustment
            
            
            // The rhs will have its own emmitter since it will be assignable
            // If not ... boom
            DataTypeChecker dc = AssignmentDataCheckerFactory.getDataChecker(col, rhs);
            DTResult res = dc.verifyOperands(col, rhs);
            if(res ==DTResult.ASSIGN_OK) {
            // ae.setLtEmitter(ltEmitter);
            // CodeEmitter.setRepo();
            // LTFileObject lto = ae.getLTEntry(col, rhs);
                LTFileObject lto = ((Assignable)rhs).getAssignmentEntry(col, (ExtractBaseAST)rhs);


            // It should be able to add its own entries?

                ltEmitter.addToLogicTable((LTRecord)lto);
            }
            // if(ae.isLookup()) {
            //     emitLookupDefault(col);
            // }
        //} else {
        //    logger.atSevere().log("Should never get here");
        //}
        col.emit(); //In case there is a sort title emit
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
