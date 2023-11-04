package org.genevaers.compilers.extract.astnodes;

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


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFAAEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFACEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFAEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFALEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFAPEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFAXEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCAEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCCEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCLEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCPEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCXEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFEAEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFECEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFEEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFELEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFEPEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFEXEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLAEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLCEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLLEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLPEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLXEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFPAEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFPCEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFPEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFPLEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFPPEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFPXEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFXAEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFXCEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFXEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFXLEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFXPEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFXXEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.ComparisonEmitter;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.LtRecordType;

public class ExprComparisonAST extends ExtractBaseAST implements EmittableASTNode{

    private String op;
    private Integer goto1;
    private Integer goto2 = 0;

    private class ComparisonKey {
        ASTFactory.Type lhsType;
        ASTFactory.Type rhsType;

        public ComparisonKey(Type lhs, Type rhs) {
            lhsType = lhs;
            rhsType = rhs;
        }
        @Override
        public boolean equals(Object ck)
        {
            if (ck == this) {
                return true;
            }
     
            if (ck == null || ck.getClass() != getClass()) {
                return false;
            }
     
            ComparisonKey comp = (ComparisonKey) ck;
            return lhsType == comp.lhsType && rhsType == comp.rhsType;
        }
     
        @Override
        public int hashCode() {
            return Objects.hash(lhsType, rhsType);
        }
     
        @Override
        public String toString() {
            return "{" + lhsType + ", " + rhsType + "}";
        }    
    }

    Map<ComparisonKey, ComparisonEmitter> emitters = new HashMap<>();
    private LTFileObject ltfo;
    private ExtractBaseAST lhs;
    private ExtractBaseAST rhs;
    private DataType lhsCastTo;

    public ExprComparisonAST() {
        type = ASTFactory.Type.EXPRCOMP;
        //LHS Arith
        emitters.put(new ComparisonKey(ASTFactory.Type.CALCULATION, ASTFactory.Type.CALCULATION), new CFAAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.CALCULATION, ASTFactory.Type.NUMATOM), new CFACEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.CALCULATION, ASTFactory.Type.LRFIELD), new CFAEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.CALCULATION, ASTFactory.Type.LOOKUPFIELDREF), new CFALEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.CALCULATION, ASTFactory.Type.PRIORLRFIELD), new CFAPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.CALCULATION, ASTFactory.Type.COLUMNREF), new CFAXEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.BETWEENFUNC, ASTFactory.Type.CALCULATION), new CFAAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.BETWEENFUNC, ASTFactory.Type.NUMATOM), new CFACEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.BETWEENFUNC, ASTFactory.Type.LRFIELD), new CFAEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.BETWEENFUNC, ASTFactory.Type.LOOKUPFIELDREF), new CFALEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.BETWEENFUNC, ASTFactory.Type.PRIORLRFIELD), new CFAPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.BETWEENFUNC, ASTFactory.Type.COLUMNREF), new CFAXEmitter());

        //LHS Const
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.CALCULATION), new CFCAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.RUNDATE), new CFCCEmitter());  //CFCC
        emitters.put(new ComparisonKey(ASTFactory.Type.RUNDATE, ASTFactory.Type.STRINGATOM), new CFCCEmitter());  //CFCC
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.LRFIELD), new CFCEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.STRINGATOM, ASTFactory.Type.LRFIELD), new CFCEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.RUNDATE, ASTFactory.Type.LRFIELD), new CFCEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.DATEFUNC, ASTFactory.Type.LRFIELD), new CFCEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.LOOKUPFIELDREF), new CFCLEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.STRINGATOM, ASTFactory.Type.LOOKUPFIELDREF), new CFCLEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.PRIORLRFIELD), new CFCPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.STRINGATOM, ASTFactory.Type.PRIORLRFIELD), new CFCPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.COLUMNREF), new CFCXEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.STRINGATOM, ASTFactory.Type.COLUMNREF), new CFCXEmitter());

        //LHS Field
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.CALCULATION), new CFEAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.NUMATOM), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.STRINGATOM), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.RUNDATE), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.DATEFUNC), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.FISCALDATE), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.LRFIELD), new CFEEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.LOOKUPFIELDREF), new CFELEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.PRIORLRFIELD), new CFEPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.COLUMNREF), new CFEXEmitter());

        //LHS Lookup
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.CALCULATION), new CFLAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.STRINGATOM), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.NUMATOM), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.DATEFUNC), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.RUNDATE), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.LRFIELD), new CFLEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.LOOKUPFIELDREF), new CFLLEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.PRIORLRFIELD), new CFLPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.COLUMNREF), new CFLXEmitter());

        //LHS PRIOR
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.CALCULATION), new CFPAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.NUMATOM), new CFPCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.STRINGATOM), new CFPCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.RUNDATE), new CFPCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.LRFIELD), new CFPEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.LOOKUPFIELDREF), new CFPLEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.PRIORLRFIELD), new CFPPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.PRIORLRFIELD, ASTFactory.Type.COLUMNREF), new CFPXEmitter());

        //LHS ColumnRef
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.CALCULATION), new CFXAEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.NUMATOM), new CFXCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.STRINGATOM), new CFXCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.RUNDATE), new CFXCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.LRFIELD), new CFXEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.LOOKUPFIELDREF), new CFXLEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.PRIORLRFIELD), new CFXPEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.COLUMNREF, ASTFactory.Type.COLUMNREF), new CFXXEmitter());

    }

    @Override
    public void emit() {
        if(children.size() < 2) {
            int bang = 1;
        }
        ExtractBaseAST lhsin = (ExtractBaseAST) children.get(0);
        ExtractBaseAST rhsin = (ExtractBaseAST) children.get(1);
        ComparisonEmitter emitter = getComparisonEmitter(lhsin, rhsin); 
        if(emitter != null) {
            emitter.setLtEmitter(ltEmitter);
            ltfo = emitter.getLTEntry(op, lhs, rhs);
            applyComparisonRules(op, lhsin, rhsin);
            if(ltfo != null) {
                ltEmitter.addToLogicTable((LTRecord)ltfo);

                goto1 = ltEmitter.getNumberOfRecords();
                ((LTRecord)ltfo).setGotoRow1(goto2);
                ((LTRecord)ltfo).setGotoRow2( goto2 );
            }
        } else {
            int workToDo = 1;
        }
    }


    private ComparisonEmitter getComparisonEmitter( ExtractBaseAST lhsin, ExtractBaseAST rhsin) {
        //The real type can be under a cast.
        lhs = lhsin;
        rhs = rhsin;
        if(lhsin.getType() == Type.CAST){
            //add a decast to the node
            //note we need to change the formatID of the datasource
            CastAST cast = (CastAST) lhsin;
            lhs = (ExtractBaseAST) cast.decast();
            lhsCastTo = ((DataTypeAST)cast.getChildIterator().next()).getDatatype();
        }
        return emitters.get(new ComparisonKey(lhs.getType(), rhs.getType()));
    }

    public void setComparisonOperator(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
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
        if (isNot) {
            goto1 = compF;
            goto2 = compT;
        } else {
            ((LTRecord)ltfo).setGotoRow1(compT);
            ((LTRecord)ltfo).setGotoRow2(compF);
        }

        // resolve children
        ExtractBaseAST lhs = (ExtractBaseAST) children.get(0);
        ExtractBaseAST rhs = (ExtractBaseAST) children.get(1);
        if (lhs != null && rhs != null) {
            if (isNot) {
                lhs.resolveGotos(compT, compF, joinT, getEndOfLogic());
                rhs.resolveGotos(compT, compF, joinT, getEndOfLogic());
            } else {
                lhs.resolveGotos(compT, compF, joinT, joinF);
                rhs.resolveGotos(compT, compF, joinT, joinF);
            }
        }
    }

    private void applyComparisonRules(String op, ExtractBaseAST lhs, ExtractBaseAST rhs) {
        //if both sides have compatiible date codes strip date codes
        //if sides hava ALNUM and NUM flip ALNUM to Zoned
        if( lhsCastTo != null) {
            if(((LTRecord)ltfo).getRecordType() == LtRecordType.F1) {
                ((LogicTableF1)ltfo).getArg().setFieldFormat(lhsCastTo);
            }
        }
        FormattedASTNode frmtLhs = ((FormattedASTNode) lhs);
        FormattedASTNode frmtRhs = ((FormattedASTNode) rhs);
        if(((LTRecord)ltfo).getRecordType() == LtRecordType.F2) {
            if(frmtLhs.getDataType() == DataType.ALPHANUMERIC && frmtRhs.isNumeric()) {
                //flip LHS to Zoned
                ((LogicTableF2)ltfo).getArg1().setFieldFormat(DataType.ZONED);
            } else if(frmtLhs.isNumeric() && frmtRhs.getDataType() == DataType.ALPHANUMERIC) {
                ((LogicTableF2)ltfo).getArg2().setFieldFormat(DataType.ZONED);
                //flip RHS to Zoned
            }
        }
    }

}
