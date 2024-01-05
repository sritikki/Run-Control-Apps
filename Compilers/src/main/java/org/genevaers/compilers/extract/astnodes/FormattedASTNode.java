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


import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.DataType;

public abstract class FormattedASTNode extends ExtractBaseAST {

    DataType overriddenDataType = DataType.INVALID;
    DateCode overriddenDateCode = null;
    boolean overriddenSigned;

    public abstract DataType getDataType();
    public abstract DateCode getDateCode();
    public abstract String getMessageName();
    
    // Implementing classes must manage the mapping to the Enum types above
    public void overrideDataType(DataType overFormat) {
        overriddenDataType = overFormat;
    }

    public DataType getOverriddenDataType() {
        return overriddenDataType;
    }

    public void overrideDateCode(DateCode overContent) {
        overriddenDateCode = overContent;
    }

    public DateCode getOverriddenDateCode() {
        return overriddenDateCode;
    }

    public boolean isSigned() {
        return overriddenSigned;
    }

    public void setSigned(boolean overSigned) {
        overriddenSigned = overSigned;
    }

    public boolean isNumeric() {
        return getDataType().ordinal() > DataType.ALPHA.ordinal() && getDataType().ordinal() < DataType.CONSTSTRING.ordinal();
    }
    
}
