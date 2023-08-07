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

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.repository.components.enums.DataType;

public class ArithDataTypeCheckerFactory {
    
    public static class ArithDateTypeChecker implements ArithDataTypeChecker {
        @Override
        public ArithResult verifyArithType(ExtractBaseAST type) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static class ArithMaskedTypeChecker implements ArithDataTypeChecker {
        @Override
        public ArithResult verifyArithType(ExtractBaseAST type) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static class ArithErrorTypeChecker implements ArithDataTypeChecker {
        @Override
        public ArithResult verifyArithType(ExtractBaseAST type) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static class ArithPassTypeChecker implements ArithDataTypeChecker {
        @Override
        public ArithResult verifyArithType(ExtractBaseAST type) {
            return ArithResult.ARITH_OK;
        }
    }

    private static ArithErrorTypeChecker ec = new ArithErrorTypeChecker();
    private static ArithPassTypeChecker pc = new ArithPassTypeChecker();
    private static ArithDateTypeChecker dc = new ArithDateTypeChecker();
    private static ArithMaskedTypeChecker mc = new ArithMaskedTypeChecker();
    static EnumMap<DataType, ArithDataTypeChecker> arithCheckers = new EnumMap<>(DataType.class);

    private ArithDataTypeCheckerFactory() {
    }
    
    public static void  init() {
        arithCheckers.put(DataType.ALPHANUMERIC, ec);
        arithCheckers.put(DataType.ALPHA, ec);
        arithCheckers.put(DataType.BCD, dc);
        arithCheckers.put(DataType.BINARY, dc);
        arithCheckers.put(DataType.BSORT, dc);
        arithCheckers.put(DataType.CONSTDATE, dc);
        arithCheckers.put(DataType.CONSTNUM, pc);
        arithCheckers.put(DataType.CONSTSTRING, ec);
        arithCheckers.put(DataType.EDITED, dc);
        arithCheckers.put(DataType.ZONED, dc);
        arithCheckers.put(DataType.FLOAT, dc);
        arithCheckers.put(DataType.GENEVANUMBER, pc);
        arithCheckers.put(DataType.PACKED, dc);
        arithCheckers.put(DataType.PSORT, dc);
        arithCheckers.put(DataType.MASKED, ec);
    }


    public static ArithDataTypeChecker getTypeChecker(DataType type) {
        return arithCheckers.get(type);
    }


}
