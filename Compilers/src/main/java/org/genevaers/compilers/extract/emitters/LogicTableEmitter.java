package org.genevaers.compilers.extract.emitters;

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


import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableGeneration;
import org.genevaers.repository.components.enums.LtRecordType;

/**
 * This class manages the logic table and wraps the add to table function
 * Ensuring we have the fields set as we need.
 */
public class LogicTableEmitter {

    private LogicTable logicTable = new LogicTable();
    
    private int viewId;
    private int columnID;
    private int fileId;
    private short suffixSeqNbr;

    private int hdCounter;
    private int calcCounter;
    private int ccCounter;
    private int nameCounter;
    private int nameF1Counter;
    private int nameF2Counter;
    private int nameValueCounter;
    private int reCounter;
    private int nvCounter;
    private int f0Counter;
    private int f1Counter;
    private int f2Counter;
    private int wrCounter;


    public void addToLogicTable(LTRecord rec) {
        if (rec != null) { //Should never really be null but handy during development testing
            if(rec.getViewId() == 0)
                rec.setViewId(viewId);
            if(rec.getFileId() == 0)
                rec.setFileId(fileId);
            rec.setRowNbr(logicTable.getNumberOfRecords());
            rec.setSuffixSeqNbr(suffixSeqNbr);

            incrementTypeCounter(rec);
            logicTable.add(rec);
        }
    }

    private void incrementTypeCounter(LTRecord rec) {
        switch(rec.getRecordType()) {
            case HD:
            hdCounter++;
            break;
            case CALC:
            calcCounter++;
            break;
            case CC:
            ccCounter++;
            break;
            case NAME:
            nameCounter++;
            break;
            case NAMEF1:
            nameF1Counter++;
            break;
            case NAMEF2:
            nameF2Counter++;
            break;
            case NAMEVALUE:
            nameValueCounter++;
            break;
            case RE:
            reCounter++;
            break;
            case NV:
            nvCounter++;
            break;
            case F0:
            f0Counter++;
            break;
            case F1:
            f1Counter++;
            break;
            case F2:
            f2Counter++;
            break;
            case WR:
            wrCounter++;
            break;
            default:
            break;
        }
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public LogicTableGeneration getGenerationRecord() {
        return (LogicTableGeneration) logicTable.getFromPosition(0);
    }

    public int getNumberOfRecords() {
        return logicTable.getNumberOfRecords();
    }

    public LogicTable getLogicTable() {
        return logicTable;
    }

    public void updateGenerationCounters() {
        LogicTableGeneration genRec = getGenerationRecord();
        genRec.setCcCnt(ccCounter);
        genRec.setCalcCnt(calcCounter);
        genRec.setF0Cnt(f0Counter);
        genRec.setF1Cnt(f1Counter);
        genRec.setF2Cnt(f2Counter);
        genRec.setHdCnt(hdCounter);
        genRec.setNvCnt(nvCounter);
        genRec.setNameCnt(nameCounter);
        genRec.setNamef1Cnt(nameF1Counter);
        genRec.setNamef2Cnt(nameF2Counter);
        genRec.setNamevalueCnt(nameValueCounter);
        genRec.setReCnt(reCounter);
        genRec.setWrCnt(wrCounter);
    }

    public short getSuffixSeqNbr() {
        return suffixSeqNbr;
    }

    public void setSuffixSeqNbr(short suffixSeqNbr) {
        this.suffixSeqNbr = suffixSeqNbr;
    }

}
