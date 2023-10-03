package org.genevaers.testframework;

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


import org.genevaers.testframework.yamlreader.GersTest;
import org.genevaers.utilities.menu.Menu;


public class TestSelectFormatter {
    public final int TESTNUM = 5;
    public final int TESTNAME = 8;
    public final int TESTDESC = 75;

    public static String format(GersTest t, int testNum) {
        if(t.getResult().getMessage().startsWith("pass")) {
            return getResultString(t, testNum, Menu.GREEN);
        } else if(t.getResult().getMessage().equals("unknown")) {
            return getResultString(t, testNum, Menu.YELLOW);
        } else {
            return getResultString(t, testNum, Menu.RED);
        }
    }

    private static String getResultString(GersTest t, int testNum, String resColour) {
        return String.format("    %-4s  %-8s  %-75s %s%s%s", + testNum, t.getName(), t.getDescription(), resColour,t.getResult().getMessage(),Menu.RESET);
    } 
}
