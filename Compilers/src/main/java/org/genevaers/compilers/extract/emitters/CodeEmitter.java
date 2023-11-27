package org.genevaers.compilers.extract.emitters;


import org.genevaers.genevaio.ltfile.Cookie;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.JustifyId;

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


/**
 * Base class for the different emitter types
 * Should this be an interface?
 * 
 * Holds a back reference to the LogicTableEmitter
 */
public class CodeEmitter {

    private static ViewSource currentViewSource;
    
    private  LogicTableEmitter ltEmitter;

    public void setLtEmitter(LogicTableEmitter ltEmitter) {
        this.ltEmitter = ltEmitter;
    }

    public LogicTableEmitter getLtEmitter() {
        return ltEmitter;
    }    

    protected void setDefaultArgValues(LogicTableArg arg1) {
        arg1.setDecimalCount((short) 0);
        arg1.setFieldContentId(DateCode.NONE);
        arg1.setFieldFormat(DataType.INVALID);
        arg1.setFieldId(0);
        //TODO the start pos is dependent on extract type
        arg1.setStartPosition((short) 0);
        arg1.setFieldLength((short) 0);
        arg1.setJustifyId(JustifyId.NONE);
        arg1.setValue(new Cookie(""));
        arg1.setPadding2("");  //This seems a little silly
    }
}
