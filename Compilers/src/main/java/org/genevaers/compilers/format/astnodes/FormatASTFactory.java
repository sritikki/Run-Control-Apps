package org.genevaers.compilers.format.astnodes;

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



public class FormatASTFactory {

    public enum Type {

        ERRORS("Errors"), 
        FORMATFILTERROOT("Format Filter"), 
        SELECTIF("SelectIF"), 
        FORMATROOT("Format Root"), 
        COLREF("Column Ref"), 
        NUMCONST("Number"), 
        SKIPIF("SkipId"), 
        OROP("Or"), 
        ANDOP("And"),
        NOTOP("Not"), 
        COLCALC("Column Calculation"), 
        FORMATVIEW("Format View"), 
        COLASSIGN("Assign"), 
        MULOP("Muliply"),
        DIVOP("Divide"),
        ADDOP("Add"),
        SUBOP("Subtract"), 
        GT(">"), 
        GE(">="), 
        EQ("="), 
        NE("<>"), 
        LE("<="), 
        LT("<"), 
        IF("IF");

        private String name;
        private Type(String n) {
            name = n;
        }
        @Override
        public String toString() {
            return name;
        }

    }

    public static FormatBaseAST getNodeOfType(Type t) {
        switch (t) {

            case ERRORS:
                return new FormatErrorAST();
            case FORMATFILTERROOT:
                return new FormatFilterAST();
            case SELECTIF:
                return new SelectIfAST();
            case FORMATROOT:
                return new FormatRoot();
            case COLREF:
                return new ColRef();
            case NUMCONST:
                return new NumConst();
            case SKIPIF:
                return new SkipIf();
            case OROP:
                return new OrOP();
            case ANDOP:
                return new AndOp();
            case COLCALC:
                return new ColumnCalculation();
            case FORMATVIEW:
                return new FormatView();
            case COLASSIGN:
                return new ColumnCalcAssignment();
            case MULOP:
                return new MulOp();
            case DIVOP:
                return new DivOp();
            case ADDOP:
                return new AddOp();
            case SUBOP:
                return new SubOp();
            case NOTOP:
                return new NotOP();
            case IF:
                return new FormatIF();
           
             default:
                return null;
        }
    }

    public static FormatBaseAST getComparisonNode(String op) {
        switch(op) {
            case ">":
            return new FormatGT();
            case ">=":
            return new FormatGE();
            case "=":
            return new FormatEQ();
            case "<>":
            return new FormatNE();
            case "<=":
            return new FormatLE();
            case "<":
            return new FormatLT();
            default:
            //Error message
            return null;
        }
    }

    public static FormatBaseAST getArithNode(String opStr) {
        switch(opStr) {
            case "*":
            return getNodeOfType(FormatASTFactory.Type.MULOP);
            case "/":
            return getNodeOfType(FormatASTFactory.Type.DIVOP);
            case "+":
            return getNodeOfType(FormatASTFactory.Type.ADDOP);
            case "-":
            return getNodeOfType(FormatASTFactory.Type.SUBOP);
            default:
            //This is an error 
            return null;
        }
    }



}
