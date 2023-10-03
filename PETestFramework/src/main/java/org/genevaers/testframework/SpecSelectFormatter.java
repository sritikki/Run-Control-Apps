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


import org.genevaers.testframework.yamlreader.Spec;
import org.genevaers.utilities.menu.Menu;

public class SpecSelectFormatter {
    public static String format(Spec s, int specNum) {
        if(s.getResult().getMessage().startsWith("pass")) {
            return getFormatResultString(s, specNum, Menu.GREEN);
        } else if(s.getResult().getMessage().equals("unknown")) {
            return getFormatResultString(s, specNum, Menu.YELLOW);
        } else {
            return getFormatResultString(s, specNum, Menu.RED);
        }
    }

    private static String getFormatResultString(Spec s, int specNum, String resColour) {
        String catSpec = s.getCategory() + "/" + s.getName() + ": " + s.getDescription();
        return String.format("%-94s  %s%s%s", catSpec, resColour, s.getResult().getMessage(), Menu.RESET);

    }

    public static String formatWithNum(Spec s, int specNum) {
        if(s.getResult().getMessage().startsWith("pass")) {
            return getResultString(s, specNum, Menu.GREEN);
        } else if(s.getResult().getMessage().equals("unknown")) {
            return getResultString(s, specNum, Menu.YELLOW);
        } else {
            return getResultString(s, specNum, Menu.RED);
        }
    } 

    private static String getResultString(Spec s, int specNum, String resColour) {
        String specStr = String.format("%-4s ", + specNum);
        String catSpec = s.getCategory() + "/" + s.getName();
        return String.format("%-3s %-37s %-60s %s%s%s", specStr, catSpec, s.getDescription(), resColour, s.getResult().getMessage(), Menu.RESET);

    }
}
