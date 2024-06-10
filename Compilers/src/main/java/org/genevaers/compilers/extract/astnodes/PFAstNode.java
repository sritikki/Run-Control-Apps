package org.genevaers.compilers.extract.astnodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.genevaers.repository.components.LogicalFile;

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


import org.genevaers.repository.components.PhysicalFile;

public class PFAstNode extends ExtractBaseAST {

    private PhysicalFile pf;
    private String requestedName;

    public PFAstNode() {
        type = ASTFactory.Type.PF;
    }

    public PhysicalFile getPhysicalFile() {
        return pf;
    }

    public void setPhysicalFile(PhysicalFile pf) {
        this.pf = pf;
    }

    public void resolve(PhysicalFile pfin, String requested) {
        if(pfin == null) {
                requestedName = requested;
                addError("Unknown physical file " + requested);
        } else {
            pf = pfin;
        }
    }

    public String getName() {
        if(pf != null) {
            return pf.getName();
        } else {
            return requestedName;
        }
    }

    public int getPartitionID() {
        return pf != null ? pf.getComponentId() : 0;
    }
}
