package org.genevaers.runcontrolanalyser.menu.dbgeneration;

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
import org.genevaers.utilities.menu.MenuSetting;

public class GenDBName extends MenuItem implements MenuSetting {

    GenDBName() {
        prompt = "Database Name";
        comment = "";
    }

    @Override
    public boolean doIt() {
        setValue(Menu.promptedRead(prompt));
        return true;
    }

    @Override
    public void setValue(String v) {
        RCAGenerationData.setDbDatabase(v);
    }

    @Override
    public String getValue() {
        return RCAGenerationData.getDbDatabase();
    }
    
}
