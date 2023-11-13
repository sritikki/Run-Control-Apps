package org.genevaers.compilers.extract;

import java.io.IOException;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.extract.astnodes.ExtractOutputAST;
import org.genevaers.compilers.extract.astnodes.ViewSourceAstNode;
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


public class ExtractOutputCompiler extends ExtractCompiler{

    public ExtractOutputCompiler() {
        super();
    }

    public void processLogicAndAddNodes(ExtractOutputAST eo) throws IOException {
        processLogicAndAddNodesToParent(eo, vs.getExtractOutputLogic(), BuildGenevaASTVisitor.ExtractContext.OUTPUT);
    }
    
}
