package org.genevaers.repository;

import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LRIndex;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.enums.AccessMethod;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.DbmsRowFmtOptId;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.FieldDelimiter;
import org.genevaers.repository.components.enums.FileRecfm;
import org.genevaers.repository.components.enums.JustifyId;
import org.genevaers.repository.components.enums.RecordDelimiter;
import org.genevaers.repository.components.enums.SubtotalType;
import org.genevaers.repository.components.enums.TextDelimiter;

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


public class RepoHelper {

    private RepoHelper() {}

    public static boolean isNumeric(DataType frmt) {
        return frmt == DataType.ALPHANUMERIC ? false : true;
    }
    
    public static boolean isEffectiveDated(LRIndex ndx) {
        return ndx.isEffectiveDateEnd() || ndx.isEffectiveDateStart();
    }

    public static void copyFieldFormat(LRField src, LRField trg) {
        trg.setDatatype(src.getDatatype());
        trg.setDateTimeFormat(src.getDateTimeFormat());
        trg.setJustification(src.getJustification());
        trg.setStartPosition(src.getStartPosition());
        trg.setLength(src.getLength());
        trg.setMask(src.getMask());
        trg.setNumDecimalPlaces(src.getNumDecimalPlaces());
        trg.setRounding(src.getRounding());
        trg.setSigned(src.isSigned());
        trg.setDbColName(src.getDbColName());
    }

    public static void setEffDateBinaryField(LRField fld) {
        fld.setDatatype(DataType.BINARY);
        fld.setDateTimeFormat(DateCode.CYMD);
        fld.setLength((short)4);
        fld.setJustification(JustifyId.NONE);
        fld.setMask("");
        fld.setNumDecimalPlaces((short)0);
        fld.setOrdinalOffset((short)1);
        fld.setRounding((short)0);
        fld.setSigned(false);
        fld.setDbColName("");
    }

    public static void setField(LRField fld, DataType frmt, short sp, short len) {
        fld.setDatatype(frmt);
        fld.setDateTimeFormat(DateCode.NONE);
        fld.setJustification(JustifyId.NONE);
        fld.setStartPosition(sp);
        fld.setLength(len);
        fld.setMask("");
        fld.setNumDecimalPlaces((short)0);
        fld.setRounding((short)0);
        fld.setSigned(false);
        fld.setDbColName("");
    }

    public static void setViewColumnFromLRField(ViewColumn vc, LRField f) {
        vc.setDataType(f.getDatatype());
        vc.setDateCode(f.getDateTimeFormat());
        vc.setJustifyId(f.getJustification());
        vc.setStartPosition(f.getStartPosition());
        vc.setExtractAreaPosition(f.getStartPosition());
        vc.setFieldLength(f.getLength());
        vc.setReportMask(f.getMask());
        vc.setDecimalCount(f.getNumDecimalPlaces());
        vc.setRounding(f.getRounding());
        vc.setSigned(f.isSigned());
        vc.setFieldName(f.getName());

        vc.setSubtotalType(SubtotalType.NONE);
        vc.setExtractArea(ExtractArea.AREADATA);
        vc.setDetailPrefix("");
        vc.setSubtotalPrefix("");
        vc.setSubtotalMask("");
        vc.setDefaultValue("");
        vc.setHeaderLine1("");
        vc.setHeaderLine2("");
        vc.setHeaderLine3("");
        vc.setHeaderJustifyId(JustifyId.LEFT    );
    }

    public static void setViewAlnumColumn(ViewColumn vc, short start, short len, String name) {
        vc.setDataType(DataType.ALPHANUMERIC);
        vc.setDateCode(DateCode.NONE);
        vc.setJustifyId(JustifyId.LEFT);
        vc.setStartPosition(start);
        vc.setExtractAreaPosition(start);
        vc.setFieldLength(len);
        vc.setReportMask("");
        vc.setDecimalCount((short)0);
        vc.setRounding((short)0);
        vc.setSigned(false);
        vc.setFieldName(name);

        vc.setSubtotalType(SubtotalType.NONE);
        vc.setExtractArea(ExtractArea.AREADATA);
        vc.setDetailPrefix("");
        vc.setSubtotalPrefix("");
        vc.setSubtotalMask("");
        vc.setDefaultValue("");
        vc.setHeaderLine1("");
        vc.setHeaderLine2("");
        vc.setHeaderLine3("");
        vc.setHeaderJustifyId(JustifyId.LEFT);
    }

    public static void fillPF(PhysicalFile rehPF) {
        rehPF.setDatabase("");
        rehPF.setRecfm(FileRecfm.FB);
        rehPF.setAccessMethod(AccessMethod.SEQUENTIAL);
        rehPF.setDataSetName("");
        rehPF.setDatabaseRowFormat(DbmsRowFmtOptId.NONE);
        rehPF.setSqlText("");
        rehPF.setDatabaseConnection("");
        rehPF.setDatabaseTable("");
        rehPF.setInputDDName("");
        rehPF.setOutputDDName("");
        rehPF.setFieldDelimiter(FieldDelimiter.COMMA);
        rehPF.setReadExitIDParm("");
        rehPF.setRecordDelimiter(RecordDelimiter.CR);
        rehPF.setTextDelimiter(TextDelimiter.SINGLEQUOTE);        
    }
   
}
