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
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFCLEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFECEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFEEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFELEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLCEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.CFLEEmitter;
import org.genevaers.compilers.extract.emitters.comparisonemitters.ComparisonEmitter;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableF1;
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
    private String lhsCastTo;

    public ExprComparisonAST() {
        type = ASTFactory.Type.EXPRCOMP;
        //Use static classes here?
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.LRFIELD), new CFCEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.STRINGATOM, ASTFactory.Type.LRFIELD), new CFCEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.NUMATOM, ASTFactory.Type.LOOKUPFIELDREF), new CFCLEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.STRINGATOM, ASTFactory.Type.LOOKUPFIELDREF), new CFCLEmitter());

        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.NUMATOM), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.RUNDATE), new CFECEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.LRFIELD), new CFEEEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LRFIELD, ASTFactory.Type.LOOKUPFIELDREF), new CFELEmitter());

        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.STRINGATOM), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.NUMATOM), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.DATEFUNC), new CFLCEmitter());
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.RUNDATE), new CFLCEmitter());

        
        emitters.put(new ComparisonKey(ASTFactory.Type.LOOKUPFIELDREF, ASTFactory.Type.LRFIELD), new CFLEEmitter());
    }

    @Override
    public void emit() {
        ExtractBaseAST lhsin = (ExtractBaseAST) children.get(0);
        ExtractBaseAST rhsin = (ExtractBaseAST) children.get(1);
        ComparisonEmitter emitter = getComparisonEmitter(lhsin, rhsin); 
        emitter.setLtEmitter(ltEmitter);
        ltfo = emitter.getLTEntry(op, lhs, rhs);
        applyComparisonRules(op, lhsin, rhsin);
        if(ltfo != null) {
            ltEmitter.addToLogicTable((LTRecord)ltfo);
            emitChildNodes();

            goto1 = ltEmitter.getNumberOfRecords();
            ((LTRecord)ltfo).setGotoRow1(goto2);
            ((LTRecord)ltfo).setGotoRow2( goto2 );
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
            switch(lhsCastTo) {
                case "<ZONED>":
                if(((LTRecord)ltfo).getRecordType() == LtRecordType.F1) {
                    ((LogicTableF1)ltfo).getArg().setFieldFormat(DataType.ZONED);
                }
                break;
                default:
                break;
            }
        }
    }

}
