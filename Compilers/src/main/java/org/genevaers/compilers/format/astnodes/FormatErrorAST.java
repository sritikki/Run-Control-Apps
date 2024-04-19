package org.genevaers.compilers.format.astnodes;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
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


import java.util.List;

import org.genevaers.compilers.format.astnodes.FormatASTFactory.Type;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.repository.data.CompilerMessageSource;

public class FormatErrorAST extends FormatBaseAST{

    private String error;

    public FormatErrorAST() {
        type = Type.ERRORS;
    }

    public void setError(String err) {
        this.error = err;
        CompilerMessage errMessage = new CompilerMessage(currentViewSource.getViewId(), CompilerMessageSource.COLUMN_CALC,  0, 0, currentColumnNumber, err);
        Repository.addErrorMessage(errMessage);              
    }

    public String getError() {
        return error;
    }

}
