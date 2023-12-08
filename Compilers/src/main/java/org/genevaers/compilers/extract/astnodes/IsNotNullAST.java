package org.genevaers.compilers.extract.astnodes;

import org.genevaers.genevaio.ltfile.LTRecord;
import com.google.common.flogger.FluentLogger;

public class IsNotNullAST extends IsNullAST {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private Integer goto1 = 0;
    private Integer goto2 = 0;


    public IsNotNullAST() {
        type = ASTFactory.Type.ISNULL;
    }

    @Override
    public void emit() {
        ExtractBaseAST child = (ExtractBaseAST) children.get(0);
        if(child != null) {
            emitFunctionCodeBasedOnDataSource(child);
        } else { 
            logger.atSevere().log("Is Spaces has no child node");
        }
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
        ExtractBaseAST source = (ExtractBaseAST) children.get(0);

        if (source != null) {
            ((LTRecord)ltfo).setGotoRow1(compF);
            ((LTRecord)ltfo).setGotoRow2(compT );
            source.resolveGotos(compF, compT, joinT, joinF);
        }
    }

}
