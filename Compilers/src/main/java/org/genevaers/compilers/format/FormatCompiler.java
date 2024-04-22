package org.genevaers.compilers.format;

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


import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.extract.ParseErrorListener;
import org.genevaers.compilers.format.astnodes.FormatASTFactory;
import org.genevaers.compilers.format.astnodes.FormatBaseAST;
import org.genevaers.compilers.format.astnodes.FormatErrorAST;
import org.genevaers.grammar.GenevaFormatLexer;
import org.genevaers.grammar.GenevaFormatParser;
import org.genevaers.grammar.GenevaFormatParser.GoalContext;
import com.google.common.flogger.FluentLogger;

public class FormatCompiler {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private ParseErrorListener errorListener;
	private Set<Integer> columnRefs;

    public FormatCompiler() {
    }
    
    public FormatBaseAST processLogic(String text, boolean fromFilter) throws IOException {
        CodePointCharStream stream = CharStreams.fromString(text);
        FormatBaseAST astTree = null;

        logger.atInfo().log("Format Logic to be processed: " + text);

        GenevaFormatLexer lexer = new GenevaFormatLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GenevaFormatParser parser = new GenevaFormatParser(tokens);
        parser.removeErrorListeners(); // remove ConsoleErrorListener
        errorListener = new ParseErrorListener();
        parser.addErrorListener(errorListener); // add ours
        GoalContext tree = parser.goal(); // parse
        if (parser.getNumberOfSyntaxErrors() == 0) {
            BuildGenevaFormatASTVisitor astBuilder = new BuildGenevaFormatASTVisitor(fromFilter);
            astTree = astBuilder.visit(tree);
            columnRefs = astBuilder.getColumnRefs();
        } else {
            logger.atSevere().log("Syntax Errors detected");
            Iterator<String> ei = errorListener.getErrors().iterator();
            while(ei.hasNext()) {
                //Want to add these to an Error AST Node
                String err = ei.next();
                logger.atSevere().log(err);
                astTree = (FormatBaseAST)FormatASTFactory.getNodeOfType(FormatASTFactory.Type.ERRORS);
                ((FormatErrorAST)astTree).setError(err, fromFilter);
                ASTBase.addToErrorCount(errorListener.getErrors().size());
            }
        }
        return astTree;
    }

    public boolean hasErrors() {
        return errorListener.getErrors().size() > 0;
    }

    public Set<Integer> getColumnRefs() {
        return columnRefs;
    }
}
