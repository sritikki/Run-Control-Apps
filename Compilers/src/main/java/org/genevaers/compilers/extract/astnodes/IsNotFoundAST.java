package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import com.google.common.flogger.FluentLogger;

public class IsNotFoundAST extends ExtractBaseAST implements EmittableASTNode{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private Integer goto1 = 0;
    private Integer goto2 = 0;

    protected LTFileObject ltfo;


    public IsNotFoundAST() {
        type = ASTFactory.Type.ISFOUND;
    }

    @Override
    public void emit() {
    }

    public void setGoto1(Integer goto1) {
        this.goto1 = goto1;
    }

    public void setGoto2(Integer goto2) {
        this.goto2 = goto2;
        ((LTRecord)ltfo).setGotoRow2(goto2);
    }

    public Integer getGoto1() {
        return goto1;
    }

    public Integer getGoto2() {
        return goto2;
    }

    @Override
    public void resolveGotos(Integer compT, Integer compF, Integer joinT, Integer joinF) {
        // ExtractBaseAST source = (ExtractBaseAST) children.get(0);

        // if (source != null) {
        //         if (isNot) {
        //             ((LTRecord)ltfo).setGotoRow1(compF);
        //             ((LTRecord)ltfo).setGotoRow2(compT);
        //             source.resolveGotos(compF, compT, joinT, joinF);
        //         }
        //         else {
        //             ((LTRecord)ltfo).setGotoRow1(compT);
        //             ((LTRecord)ltfo).setGotoRow2( compF );
        //             source.resolveGotos(compT, compF, joinT, joinF);
        //        }
        // }
    }

    protected void addToLogicTableAndInitialiseGotos(LTFileObject ltfo) {
        if(ltfo != null) {
            ltEmitter.addToLogicTable((LTRecord)ltfo);

            goto1 = ltEmitter.getNumberOfRecords();
            ((LTRecord)ltfo).setGotoRow1(goto1);
            ((LTRecord)ltfo).setGotoRow2( goto2 );
        }
    }

}
