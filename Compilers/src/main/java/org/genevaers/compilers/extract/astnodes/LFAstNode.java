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


import com.google.common.flogger.FluentLogger;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfile.LogicTableF0;
import org.genevaers.genevaio.ltfile.LogicTableRE;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.enums.FileType;
import org.genevaers.repository.components.enums.LtRecordType;

public class LFAstNode extends ExtractBaseAST implements EmittableASTNode{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private LogicalFile lf;
    private String requestedName;

    public LFAstNode() {
        type = ASTFactory.Type.LF;
    }

    public LogicalFile getLogicalFile() {
        return lf;
    }

    public void setLogicalFile(LogicalFile lf) {
        this.lf = lf;
    }

    @Override
    public void emit() {
        ltEmitter.setFileId(lf.getID());
        // In here we emit the RE
        emitRE();

        // Then the children deal with the NVs and content
        emitChildNodes();
        // We'll need the ES or ET now
        emitEndOfSet();

    }

    private void emitRE() {
        LogicTableRE re = new LogicTableRE();
        re.setRecordType(LtRecordType.RE);
        LtFactoryHolder.getLtFunctionCodeFactory().setLogFileId(lf.getID());
        re.setFileId(lf.getID());
        PhysicalFile pf1 = lf.getPFIterator().next();
        if (pf1.getFileType() == FileType.TOKEN) {
            re.setFunctionCode("RETK");
        } else {
            if(pf1.getReadExitID() > 0) {
                re.setFunctionCode("REEX");
                re.setReadExitId(pf1.getReadExitID());
            } else {
                re.setFunctionCode("RENX");
            }
        }
        ltEmitter.setSuffixSeqNbr((short)0);
        ltEmitter.addToLogicTable(re);
    }

    private void emitEndOfSet() {
        LogicTableF0 end = new LogicTableF0();
        end.setRecordType(LtRecordType.F0);
        end.setFileId(lf.getID());
        ltEmitter.setSuffixSeqNbr((short)0);
        PhysicalFile pf1 = lf.getPFIterator().next();
        if (pf1.getFileType() == FileType.TOKEN) {
            end.setFunctionCode("ET");
        } else {
            end.setFunctionCode("ES");
        }
        end.setViewId(0);
        ltEmitter.addToLogicTable(end);
    }

    public void resolve(String lfPf) {
        String[] parts = lfPf.split("\\.");
        //Confirm LF
        requestedName = parts[0];
        lf = Repository.getLogicalFiles().get(requestedName);
        //And PF within it
        if(lf != null) {       
            PhysicalFile pf = lf.getPhysicalFile(parts[1]);
            if(pf != null) {
                PFAstNode pfn = (PFAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.PF);
                pfn.resolve(pf, parts[1]);
                pf.setRequired(true);
                addChildIfNotNull(pfn);
            }  else {
                ErrorAST err = (ErrorAST) ASTFactory.getNodeOfType(ASTFactory.Type.ERRORS);
                err.addError("Unknown physical file " + parts[1]);
                addChildIfNotNull(err);
            }
        } else {
            ErrorAST err = (ErrorAST) ASTFactory.getNodeOfType(ASTFactory.Type.ERRORS);
            err.addError("Unknown logical file " + requestedName);
            addChildIfNotNull(err);
        }
    }

    public String getName() {
        if(lf != null) {
            return lf.getName();
        } else {
            return requestedName;
        }
    }

}
