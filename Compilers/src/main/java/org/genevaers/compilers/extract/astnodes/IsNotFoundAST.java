package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;

public class IsNotFoundAST extends ExtractBaseAST implements EmittableASTNode{

    public IsNotFoundAST() {
        type = ASTFactory.Type.ISFOUND;
    }

    @Override
    public void emit() {
        LookupPathRefAST lkref = (LookupPathRefAST)getChildIterator().next();
        lkref.emitJoin(false);
    }

    @Override
    public void resolveGotos(Integer compT, Integer compF, Integer joinT, Integer joinF) {
        ExtractBaseAST source = (ExtractBaseAST) children.get(0);
        source.resolveGotos(compT, compF, compF, compT);
    }

}
