package org.genevaers.compilers.extract;

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


import java.io.IOException;
import java.util.Iterator;

import com.google.common.flogger.FluentLogger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.extract.BuildGenevaASTVisitor.ExtractContext;
import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ErrorAST;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.grammar.GenevaERSLexer;
import org.genevaers.grammar.GenevaERSParser;
import org.genevaers.grammar.GenevaERSParser.GoalContext;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewSource;

public class ExtractCompiler {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private ParseErrorListener errorListener;
    private ViewColumnSource vcs;
    protected ViewSource vs;

    public ExtractCompiler() {
    }

    public void processLogicAndAddNodesToParent(ExtractBaseAST parent, String logic, BuildGenevaASTVisitor.ExtractContext exContext) throws IOException {
        CodePointCharStream stream = CharStreams.fromString(logic);
        ASTBase astTree = null;

        GenevaERSLexer lexer = new GenevaERSLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GenevaERSParser parser = new GenevaERSParser(tokens);
        parser.removeErrorListeners(); // remove ConsoleErrorListener
        errorListener = new ParseErrorListener();
        parser.addErrorListener(errorListener); // add ours
        GoalContext tree = parser.goal(); // parse
        if (parser.getNumberOfSyntaxErrors() == 0) {
            BuildGenevaASTVisitor astBuilder = new BuildGenevaASTVisitor(exContext);
            astBuilder.setParent(parent);
            if(exContext == ExtractContext.COLUMN) {
                astBuilder.setViewColumnSource(vcs);
            } else {
                astBuilder.setViewSource(vs);
            }
            astTree = astBuilder.visit(tree);
        } else {
            logger.atSevere().log("Syntax Errors detected");
            Iterator<String> ei = errorListener.getErrors().iterator();
            while(ei.hasNext()) {
                //Want to add these to an Error AST Node
                logger.atSevere().log(ei.next());
            }
            astTree = (ErrorAST)ASTFactory.getNodeOfType(ASTFactory.Type.ERRORS);
            ((ErrorAST)astTree).setErrors(errorListener.getErrors());
            ASTBase.addToErrorCount(errorListener.getErrors().size());
        }
        if(astTree != null) {
            ErrorAST errs = (ErrorAST) ((ExtractBaseAST) astTree).getFirstNodeOfType(ASTFactory.Type.ERRORS);
            if(errs != null) {
                logger.atSevere().log("Errors detected");
                Iterator<String> ei = errs.getErrors().iterator();
                while(ei.hasNext()) {
                    //Want to add these to an Error AST Node
                    logger.atSevere().log(ei.next());
                }
                ASTBase.addToErrorCount(errs.getErrors().size());
            }
        } else {
            logger.atInfo().log("Null AST Tree generated");
        }
        //parent.addChildIfNotNull(astTree);
    }

    public boolean hasErrors() {
        return errorListener.getErrors().size() > 0;
    }

    public void setViewColumnSource(ViewColumnSource viewColumnSource) {
        vcs = viewColumnSource;
    }

    public void setViewSource(ViewSource vs) {
        this.vs = vs;
    }
}
