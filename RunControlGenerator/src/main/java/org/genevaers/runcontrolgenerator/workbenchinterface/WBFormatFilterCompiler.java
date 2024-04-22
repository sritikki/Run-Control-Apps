package org.genevaers.runcontrolgenerator.workbenchinterface;

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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.genevaers.compilers.format.FormatCompiler;
import org.genevaers.compilers.format.astnodes.FormatASTFactory;
import org.genevaers.compilers.format.astnodes.FormatBaseAST;
import org.genevaers.compilers.format.astnodes.FormatFilterAST;
import org.genevaers.compilers.format.astnodes.FormatRoot;
import org.genevaers.compilers.format.astnodes.FormatView;
import org.genevaers.grammar.FormatFilterLexer;
import org.genevaers.grammar.FormatFilterParser;
import org.genevaers.grammar.FormatFilterParser.GoalContext;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.data.CompilerMessage;

public class WBFormatFilterCompiler implements SyntaxChecker {

	private GoalContext tree;
	private Set<Integer> columnRefs;

	@Override
	public ParseTree getParseTree() {
		return tree;
	}

	@Override
	public List<String> getSyntaxErrors() {
		List<String> errs = new ArrayList<String>();
		Iterator<CompilerMessage> ei = Repository.getCompilerErrors().iterator();
		while(ei.hasNext()) {
			CompilerMessage e = ei.next();
			errs.add(e.getDetail());
		}
		return errs;
	}

	@Override
	public int getNumberOfSyntaxWarningss() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasSyntaxErrors() {
		return Repository.getCompilerErrors().size() > 0;
	}

    public Set<Integer> getColumnRefs() {
		return columnRefs;
    }

	public String generateCalcStack(int viewId) {
		String cs;
		ViewNode vn = Repository.getViews().get(viewId);
		FormatCompiler fc = new FormatCompiler();
		try {
			FormatView fv = (FormatView)FormatASTFactory.getNodeOfType(FormatASTFactory.Type.FORMATVIEW);
			fv.addView(vn);
			FormatBaseAST ffast = fc.processLogic(vn.getFormatFilterLogic(), true);
			fv.addChildIfNotNull(ffast);
			if(fc.hasErrors()) {
				cs = "Syntax Errors found";
			} else {
				columnRefs = fc.getColumnRefs();
				((FormatFilterAST)ffast).setView(vn);
				fv.emit(true);
				cs = vn.getFormatFilterCalcStack().toString();
			}
		} catch (IOException e) {
			cs = "Unable to process column calculation";
		}
		return cs;
	}

}
