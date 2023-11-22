package org.genevaers.compilers.extract.astnodes;

import java.io.IOException;

import org.genevaers.compilers.base.EmittableASTNode;


public class BooleanNotAST extends ExtractBaseAST implements EmittableASTNode{

    public BooleanNotAST() {
        type = ASTFactory.Type.BOOLNOT;
    }

    @Override
    public void resolveGotos(Integer compT, Integer compF, Integer joinT, Integer joinF) {
        ((ExtractBaseAST)getChildIterator().next()).resolveGotos(compF, compT, compT, compF);
    }

    @Override
    public void emit() {
        emitChildNodes();
    }


}
