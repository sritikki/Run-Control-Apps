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


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.emitters.LogicTableEmitter;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewSource;

/**
 * The class represents a generic Extract AST Node.
 * Forces extended child nodes to implement an emit function
 * to populate the ltEmitter and its underlying logic table..
 * 
 * We should probably be have an interface 
 * then those nodes that do not emit eg. NumAtom do no need empty functions.
 *  
 */
public abstract class ExtractBaseAST extends ASTBase{

    protected static LogicTableEmitter ltEmitter;
    protected int endOfLogic = 0;

    protected static int currentAccumNumber = 0;
    protected static short currentColumnNumber = 0;
    protected Set<String> accumNames = new HashSet<>();
    protected static ViewSource currentViewSource;
    protected static ViewColumn currentViewColumn;
    protected static ViewColumnSource currentViewColumnSource;
    protected static int lastColumnWithAWrite = 0;

    protected ASTFactory.Type type = null;
    private boolean negative = false;


    ExtractBaseAST() {
        type = ASTFactory.Type.EBASE;
    }

    // We should do this via an emittable interface
    // since not all AST notes are emittable?
    //public abstract void emit();

    protected void emitChildNodes() {
        Iterator<ASTBase> ci = children.iterator();
        while(ci.hasNext()) {
            ASTBase n = ci.next();
            if(n instanceof EmittableASTNode) {
                EmittableASTNode c = (EmittableASTNode)n; 
                c.emit();
            }
        }
    }

    public static void setLogicTableEmitter(LogicTableEmitter ltEmitter) {
        ExtractBaseAST.ltEmitter = ltEmitter;
    }

    public void resolveGotosTop() {
        resolveGotos(null, null, null, null);
    }    
    
    public void setEndOfLogic(int endOfLogic) {
        this.endOfLogic = endOfLogic;
    }

    public Integer getEndOfLogic() {
        return endOfLogic;
    }

    public void resolveGotos(Integer comparisonT, Integer comparisonF, Integer joinT, Integer joinfF) {
        Iterator<ASTBase> ci = children.iterator();
        while(ci.hasNext()) {
            ExtractBaseAST c = (ExtractBaseAST) ci.next();
            c.resolveGotos(null, null, null, null);
        }
    }

    public static LogicTableEmitter getLtEmitter() {
        return ltEmitter;
    }

    public static short getCurrentColumnNumber() {
        return (short) currentViewColumnSource.getColumnNumber();
    }

    public static void setCurrentColumnNumber(short currentColumnNumber) {
        ExtractBaseAST.currentColumnNumber = currentColumnNumber;
    }

    public static void setCurrentAccumNumber(int currentAccumNumber) {
        ExtractBaseAST.currentAccumNumber = currentAccumNumber;
    }

    public ASTFactory.Type getType() {
        return type;
    }  
    
    public ExtractBaseAST getFirstNodeOfType(ASTFactory.Type t) {
        ExtractBaseAST leaf = null;
        Iterator<ASTBase> ci = children.iterator();
        while(leaf == null && ci.hasNext()) {
            leaf = (ExtractBaseAST) ci.next();
            if(leaf.getType() == t) {
                return leaf;
            } else {
                leaf = leaf.getFirstNodeOfType(t);
            }
        }
        return leaf;
    }

    public void setNegative() {
        negative = true;
    }

    public boolean isNegative() {
        return negative;
    }

    public static int getLastColumnWithAWrite() {
        return lastColumnWithAWrite;
    }

    public static void setLastColumnWithAWrite() {
        ExtractBaseAST.lastColumnWithAWrite = currentViewColumnSource.getColumnNumber();
    }

    public static void clearLastColumnWithAWrite() {
        ExtractBaseAST.lastColumnWithAWrite = 0;
    }

}
