package org.genevaers.compilers.extract.astnodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.data.NormalisedDate;

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


public class DateFunc extends FormattedASTNode implements GenevaERSValue{

    private String value;
    private String dateCodeStr;
    private DateCode dateCode;

    public DateFunc() {
        type = ASTFactory.Type.DATEFUNC;
    }

    public String getValue() {
        return value;
    }

    public void resolve(String dateStr, String format) {
        //TODO We need to think about this some more 
        //Check the string matches the format etc
        value = dateStr.replace("\"", "");
        dateCodeStr = format.replace("\"", "");
        dateCode = DateCode.fromValue(format);
    }

    @Override
    public String getValueString() {
        return value;
    }

    @Override
    public DataType getDataType() {
        return overriddenDataType != DataType.INVALID ? overriddenDataType : DataType.ALPHANUMERIC;
    }

    @Override
    public DateCode getDateCode() {
        return (overriddenDateCode != null) ? overriddenDateCode : dateCode;
    }

    public String getDateCodeStr() {
        return dateCodeStr;
    }

    public String getNormalisedDate() {
        return NormalisedDate.get(value, getDateCode());
    }
}
