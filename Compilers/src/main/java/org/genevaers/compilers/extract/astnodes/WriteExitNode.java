package org.genevaers.compilers.extract.astnodes;

import org.genevaers.repository.Repository;

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


import org.genevaers.repository.components.UserExit;

public class WriteExitNode extends ExtractBaseAST {

    private UserExit ref;
    private String name; //The name in the logic text
    private String arg;

    public WriteExitNode() {
        type = ASTFactory.Type.WRITEEXIT;
    }

    public void resolveExit(String exitName) {
        name = exitName;
        UserExit exit = Repository.getUserExits().get(exitName);
        if(exit != null) {       
            ref = exit;
        } else {
            ErrorAST err = (ErrorAST) ASTFactory.getNodeOfType(ASTFactory.Type.ERRORS);
            err.addError("Unknown exit " + exitName);
            addChildIfNotNull(err);
        }
    }

    public void setArg(String text) {
        arg = text;
    }

    public String getArg() {
        return arg;
    }

    public int getExitID() {
        return ref != null ? ref.getComponentId() : 0;
    }

    public String getParams() {
        StringAtomAST sa =  (StringAtomAST) getFirstNodeOfType(ASTFactory.Type.STRINGATOM);
        return  sa != null ? sa.getValue() : "";
    }

}
