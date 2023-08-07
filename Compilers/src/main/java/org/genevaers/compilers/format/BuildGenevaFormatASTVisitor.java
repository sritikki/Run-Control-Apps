package org.genevaers.compilers.format;

import org.genevaers.compilers.format.astnodes.AndOp;
import org.genevaers.compilers.format.astnodes.ColRef;
import org.genevaers.compilers.format.astnodes.FormatASTFactory;
import org.genevaers.compilers.format.astnodes.FormatASTFactory.Type;
import org.genevaers.grammar.GenevaFormatBaseVisitor;
import org.genevaers.grammar.GenevaFormatParser;
import org.genevaers.grammar.GenevaFormatParser.CalcstmtContext;
import org.genevaers.grammar.GenevaFormatParser.ExprArithAddSubContext;
import org.genevaers.grammar.GenevaFormatParser.ExprArithMulDivContext;

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


import org.genevaers.compilers.format.astnodes.FormatBaseAST;
import org.genevaers.compilers.format.astnodes.NumConst;
import org.genevaers.compilers.format.astnodes.OrOP;
import org.genevaers.repository.Repository;

public class BuildGenevaFormatASTVisitor extends GenevaFormatBaseVisitor<FormatBaseAST>{

    private boolean fromFilter;

    BuildGenevaFormatASTVisitor(boolean fromFilter) {
        this.fromFilter = fromFilter;
    }

    @Override public FormatBaseAST visitGoal(GenevaFormatParser.GoalContext ctx) { 
        FormatBaseAST.resetOffset();
        FormatBaseAST froot;
        if(fromFilter) {
            froot = FormatASTFactory.getNodeOfType(Type.FORMATFILTERROOT);
            froot.addChildIfNotNull(visit(ctx.fstmt()));
        } else {
            froot = visit(ctx.fstmt());
        }
        shouldVisitNextChild(ctx.getRuleContext(), froot);
        return froot;
    }

    @Override public FormatBaseAST visitFstmt(GenevaFormatParser.FstmtContext ctx) { 
        int num = ctx.getChildCount();
        if (fromFilter) {
            FormatBaseAST filterNode = null;
            if (ctx.getChild(0) == ctx.SELECTIF()) {
                filterNode = FormatASTFactory.getNodeOfType(Type.SELECTIF);
            } else if (ctx.getChild(0) == ctx.SKIPIF()) {
                filterNode = FormatASTFactory.getNodeOfType(Type.SKIPIF);
            }
            if (num == 4 && filterNode != null) {
                filterNode.addChildIfNotNull(visit(ctx.getChild(2)));
            }
            NumConst trueCase = new NumConst();
            trueCase.setValue("1");
            filterNode.addChildIfNotNull(trueCase);
            NumConst falseCase = new NumConst();
            falseCase.setValue("0");
            filterNode.addChildIfNotNull(falseCase);
            return filterNode;
        } else {
            return visitChildren(ctx);
        }
    }

    @Override
    public FormatBaseAST visitCalcstmt(CalcstmtContext ctx) {
        switch(ctx.getChildCount()) {
            case 1:
            return visitChildren(ctx);

            case 3:
            FormatBaseAST assign = null;
            if(ctx.getChild(0).getText().equalsIgnoreCase("COLUMN")) {
                assign = FormatASTFactory.getNodeOfType(Type.COLASSIGN);
                assign.addChildIfNotNull(visitChildren(ctx));
            } else {
                //not sure what the else is here
            }
            return assign;

            case 5: // IF pred THEN stmnt ENDIF
            {
                if (ctx.getChild(0).getText().equalsIgnoreCase("IF")) {
                    FormatBaseAST ifNode = FormatASTFactory.getNodeOfType(FormatASTFactory.Type.IF);
                    ifNode.addChildIfNotNull(visit(ctx.getChild(1)));
                    ifNode.addChildIfNotNull(visit(ctx.getChild(3)));
                    return ifNode;
                } else {
                    //Error? Can this occur?
                    return null;
                }
            }
            case 7: // IF pred THEN stmnt ELSE stmnt ENDIF
            {
                if (ctx.getChild(0).getText().equalsIgnoreCase("IF")) {
                    FormatBaseAST ifNode = FormatASTFactory.getNodeOfType(FormatASTFactory.Type.IF);
                    ifNode.addChildIfNotNull(visit(ctx.getChild(1)));
                    ifNode.addChildIfNotNull(visit(ctx.getChild(3)));
                    ifNode.addChildIfNotNull(visit(ctx.getChild(5)));
                    return ifNode;
                }
                else {
                    //Error?
                    return null;
                }
            }
            default:
            return null;
        }
    }

    @Override public FormatBaseAST visitPredicate(GenevaFormatParser.PredicateContext ctx) { 
        return visitChildren(ctx); 
    }

    @Override public FormatBaseAST visitExprBoolOr(GenevaFormatParser.ExprBoolOrContext ctx) { 
        //Should only have  1 or 3 children and middle is termial OR
        if(ctx.getChildCount() == 3) {
            OrOP orOp = (OrOP) FormatASTFactory.getNodeOfType(FormatASTFactory.Type.OROP);
            orOp.addChildIfNotNull(visit(ctx.getChild(0)));
            orOp.addChildIfNotNull(visit(ctx.getChild(2)));
            return orOp;
        } else {
            return visitChildren(ctx); 
        }
    }

    @Override public FormatBaseAST visitExprBoolUnary(GenevaFormatParser.ExprBoolUnaryContext ctx) { 
        //If there are two children may be a NOT
        //So probably need an node here to manage
        return visitChildren(ctx); 
    }

    @Override
    public FormatBaseAST visitExprArithAddSub(ExprArithAddSubContext ctx) {
        FormatBaseAST arithNode = null;
        if(ctx.getChildCount() == 3) {
            arithNode = FormatASTFactory.getArithNode(ctx.getChild(1).getText());
            if(arithNode != null) {
                arithNode.addChildIfNotNull(visit(ctx.getChild(0)));
                arithNode.addChildIfNotNull(visit(ctx.getChild(2)));
            }
            return arithNode; 
        } else {
            return visitChildren(ctx); 
        }
    }

    @Override
    public FormatBaseAST visitExprArithMulDiv(ExprArithMulDivContext ctx) {
        FormatBaseAST arithNode = null;
        if(ctx.getChildCount() == 3) {
            arithNode = FormatASTFactory.getArithNode(ctx.getChild(1).getText());
            if(arithNode != null) {
                arithNode.addChildIfNotNull(visit(ctx.getChild(0)));
                arithNode.addChildIfNotNull(visit(ctx.getChild(2)));
            }
            return arithNode; 
        } else {
            return visitChildren(ctx); 
        }
    }

 
	@Override public FormatBaseAST visitExprBoolAnd(GenevaFormatParser.ExprBoolAndContext ctx) { 
        //Should only have 1 or 3 children and middle is termial AND
        //visit 0 and 2
        if(ctx.getChildCount() == 3) {
            AndOp andOp = (AndOp) FormatASTFactory.getNodeOfType(FormatASTFactory.Type.ANDOP);
            andOp.addChildIfNotNull(visit(ctx.getChild(0)));
            andOp.addChildIfNotNull(visit(ctx.getChild(2)));
            return andOp;
        } else {
            return visitChildren(ctx); 
        }
    }

    @Override public FormatBaseAST visitExprComp(GenevaFormatParser.ExprCompContext ctx) { 
        if(ctx.getChildCount() == 3) {
            FormatBaseAST comp = (FormatBaseAST) FormatASTFactory.getComparisonNode(ctx.getChild(1).getText());
            comp.addChildIfNotNull(visit(ctx.getChild(0)));
            comp.addChildIfNotNull(visit(ctx.getChild(2)));
            return comp;
        } else {
            return visitChildren(ctx); 
        }
    }


	@Override public FormatBaseAST visitStmtIfSelect(GenevaFormatParser.StmtIfSelectContext ctx) { 
        FormatBaseAST si = FormatASTFactory.getNodeOfType(FormatASTFactory.Type.SELECTIF);
        si.addChildIfNotNull(visitChildren(ctx));
        return si; 
    }

    @Override public FormatBaseAST visitStmtIfSkip(GenevaFormatParser.StmtIfSkipContext ctx) { 
        FormatBaseAST si = FormatASTFactory.getNodeOfType(FormatASTFactory.Type.SKIPIF);
        visitChildren(ctx); 
        return si;
    }

    @Override public FormatBaseAST visitExprArithUnary(GenevaFormatParser.ExprArithUnaryContext ctx) { 
        //This could have a leading minus sign
        //How do we manage that?
        //So probably need an node here to manage - or add and attribute to the child?
        //Does not make sense for a string
        //NOTE Parser does not tread -"fred" as an error
        return visitChildren(ctx); 
    }


	@Override public FormatBaseAST visitExprArithAtom(GenevaFormatParser.ExprArithAtomContext ctx) { 
        //if were here and the only child node is as terminal it must be a string const

        //Can also be brackets around the beast - strip off matching sets
        return visitChildren(ctx); 
    }


    @Override public FormatBaseAST visitColumnRef(GenevaFormatParser.ColumnRefContext ctx) { 
        ColRef cr = (ColRef) FormatASTFactory.getNodeOfType(Type.COLREF);
        String colText = ctx.getText();
        cr.setText(colText);
        return cr; 
    }

    @Override public FormatBaseAST visitNum(GenevaFormatParser.NumContext ctx) { 
        NumConst nc = (NumConst) FormatASTFactory.getNodeOfType(Type.NUMCONST);
        nc.setValue(ctx.getText());
        return nc; 
    }


}
