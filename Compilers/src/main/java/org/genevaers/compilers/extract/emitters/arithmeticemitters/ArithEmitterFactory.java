package org.genevaers.compilers.extract.emitters.arithmeticemitters;

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


import java.util.EnumMap;

import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;

/**
 * Factory functions to get the left and right hand side Arithmetic Emitters
 * for an arithmetic expression.
 */
public class ArithEmitterFactory {

    private static ArithLHSAccumEmitter     lhsAccE = new ArithLHSAccumEmitter();
    private static ArithLHSColRefEmitter    lhsColRefE = new ArithLHSColRefEmitter();
    private static ArithLHSConstEmitter     lhsConstE = new ArithLHSConstEmitter();
    private static ArithLHSFieldEmitter     lhsFieldE = new ArithLHSFieldEmitter();
    private static ArithLHSLookupEmitter    lhsLookupE = new ArithLHSLookupEmitter();

    private static ArithRHSAccumEmitter     rhsAccE = new ArithRHSAccumEmitter();
    private static ArithRHSColRefEmitter    rhsColRefE = new ArithRHSColRefEmitter();
    private static ArithRHSConstEmitter     rhsConstE = new ArithRHSConstEmitter();
    private static ArithRHSFieldEmitter     rhsFieldE = new ArithRHSFieldEmitter();
    private static ArithRHSLookupEmitter    rhsLookupE = new ArithRHSLookupEmitter();

    private static EnumMap<ASTFactory.Type, ArithEmitter> lhsEmitters = new EnumMap<>(ASTFactory.Type.class); 
    private static EnumMap<ASTFactory.Type, ArithEmitter> rhsEmitters = new EnumMap<>(ASTFactory.Type.class); 

    private ArithEmitterFactory() {
    }

    public static void init() {
        if(lhsEmitters.isEmpty()) {
            lhsEmitters.put(ASTFactory.Type.LRFIELD, lhsFieldE);
            lhsEmitters.put(ASTFactory.Type.NUMATOM, lhsConstE);
            lhsEmitters.put(ASTFactory.Type.NUMACC, lhsAccE);

            rhsEmitters.put(ASTFactory.Type.LRFIELD, rhsFieldE);
            rhsEmitters.put(ASTFactory.Type.NUMATOM, rhsConstE);
            rhsEmitters.put(ASTFactory.Type.NUMACC, rhsAccE);
        }
    }

     public static ArithEmitter getLHSArithEmitter(ExtractBaseAST lhs) {
        return lhsEmitters.get(lhs.getType());
    }

    public static ArithEmitter getRHSArithEmitter(ExtractBaseAST rhs) {
        return rhsEmitters.get(rhs.getType());
    }
    
}
