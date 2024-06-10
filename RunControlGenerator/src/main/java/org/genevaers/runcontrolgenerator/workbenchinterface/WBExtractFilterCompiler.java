package org.genevaers.runcontrolgenerator.workbenchinterface;

import java.io.IOException;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.data.ViewLogicDependency.LogicType;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2023.
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

public class WBExtractFilterCompiler extends WorkbenchCompiler  {

	public WBExtractFilterCompiler() {
		type = WBCompilerType.EXTRACT_FILTER;
    }

	@Override
	public void buildAST() {
		Repository.getDependencyCache().clearNamedEntries();
		Repository.getDependencyCache().setCurrenLogicType(LogicType.EXTRACT_RECORD_FILTER);
		Repository.getDependencyCache().setCurrentParentId(currentViewSource.getComponentId());
		ExtractBaseAST.setCurrentColumnNumber((short)0);
		ExtractPhaseCompiler.buildViewSourceAST(currentViewSource);
	}


}
