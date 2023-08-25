package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.genevaio.ltfactory.LtFactoryHolder;
import org.genevaers.genevaio.ltfactory.LtFuncCodeFactory;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import com.google.common.flogger.FluentLogger;

public class IsSpacesAST extends ExtractBaseAST implements  EmittableASTNode{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private Integer goto1 = 0;
    private Integer goto2 = 0;

    protected LTFileObject ltfo;

    public IsSpacesAST() {
        type = ASTFactory.Type.ISSPACES;
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

    protected void emitFunctionCodeBasedOnDataSource(ExtractBaseAST child) {
        switch(child.getType()) {
            case LRFIELD:
                emitCSE(child);
            break;
            case LOOKUPFIELDREF:
                emitCSL(child);
            break;
            case COLUMNREF:
                emitCSX(child);
            break;
            case PRIORLRFIELD:
                emitCSP(child);
            break;
            default:
                logger.atSevere().log("Incorrect type for IsSpaces %s", child.getType());
            break;
        }
    }

    protected void emitCSP(ExtractBaseAST prior) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltfo = fcf.getCSP(((FieldReferenceAST)prior).getRef());
        addToLogicTableAndInitialiseGotos(ltfo);
    }

    protected void emitCSX(ExtractBaseAST col) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
            ltEmitter.addToLogicTable((LTRecord)fcf.getCSX(((ColumnAST)col).getViewColumn()));
    }

    protected void emitCSL(ExtractBaseAST lkp) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
            ltEmitter.addToLogicTable((LTRecord)fcf.getCSL(((LookupFieldRefAST)lkp).getRef()));
    }

    protected void emitCSE(ExtractBaseAST field) {
        LtFuncCodeFactory fcf = LtFactoryHolder.getLtFunctionCodeFactory();
        ltfo = fcf.getCSE(((FieldReferenceAST)field).getRef());
        addToLogicTableAndInitialiseGotos(ltfo);
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
                if (isNot) {
                    ((LTRecord)ltfo).setGotoRow1(compF);
                    ((LTRecord)ltfo).setGotoRow2(compT);
                    source.resolveGotos(compF, compT, joinT, joinF);
                }
                else {
                    ((LTRecord)ltfo).setGotoRow1(compT);
                    ((LTRecord)ltfo).setGotoRow2( compF );
                    source.resolveGotos(compT, compF, joinT, joinF);
               }
        }
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
