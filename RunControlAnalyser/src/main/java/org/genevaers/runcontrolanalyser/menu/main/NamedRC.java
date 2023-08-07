package org.genevaers.runcontrolanalyser.menu.main;

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


import org.genevaers.runcontrolanalyser.menu.RCAGenerationData;
import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuItem;

public class NamedRC extends MenuItem{

    public NamedRC() {
        prompt = "Select a named RC set";
    }

    @Override
    public boolean doIt() {
        System.out.println("Select a named RC set");
        String rcs = chooseRcSetFromlist();
        if(rcs.length() > 0) {
            RCAGenerationData.openHtmlFor(rcs);
        }
        Menu.promptedWrite("Enter to continue");
        return true;
    }

    //This is common to the local rc set processing
    //But with a fixed directory
    private String chooseRcSetFromlist() {
        return chooseRcSetFromlist(System.getProperty("user.home") + "/.gersflows");
    }

    
}
