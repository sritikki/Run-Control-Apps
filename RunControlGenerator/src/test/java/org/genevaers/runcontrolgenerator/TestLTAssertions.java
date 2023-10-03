package org.genevaers.runcontrolgenerator;

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


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;

public class TestLTAssertions {
    
    public static void assertFuncCodesStartingAt(int start, String[] values, LogicTable xlt) {
        int pos = start;
        LTRecord func = null;
        for(int i=0; i<values.length; i++) {
            func = xlt.getFromPosition(pos++);
            assertEquals(values[i], func.getFunctionCode());
        }
    }

    public static void assertFunctionCodesAndGotos(int start, String[] expected, int expectedGotos[][], LogicTable xlt) {
        assertFuncCodesStartingAt(start, expected, xlt);
        assertGotos(expectedGotos, xlt);
    }
    
    public static void assertGotos(int[][] expectedGotos, LogicTable xlt) {
        for (int i = 0; i < expectedGotos.length; i++) {
            int[] gotos = expectedGotos[i];
            if(gotos.length > 0) {
                LTRecord func = xlt.getFromPosition(gotos[0]);
                assertEquals(Integer.valueOf(gotos[1]), func.getGotoRow1());
                assertEquals(Integer.valueOf(gotos[2]), func.getGotoRow2());
            }
        }
    }
}
