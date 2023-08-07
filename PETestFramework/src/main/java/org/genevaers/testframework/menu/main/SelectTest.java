package org.genevaers.testframework.menu.main;

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


import java.util.List;

import org.genevaers.testframework.TestRepository;
import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuItem;
import org.genevaers.utilities.menu.MenuSetting;

public class SelectTest extends MenuItem implements MenuSetting{

    String testName;
    private List<String> lines;

    public SelectTest() {
        prompt = "Test to run";
    }

    @Override
    public boolean doIt() {
        showTestsResults();
        setValue(Menu.promptedRead(prompt));
        nextMenu = "Main";
        return true;
    }

    //Base this list on the internal test repo
    //Not the overview.txt file
    private void showTestsResults() {
            List<String> selList = TestRepository.getSelectionList();
            for(String l : selList) {
                System.out.println(l);
            }
            System.out.println("\n\n");
    }

    @Override
    public void setValue(String v) {
        MainTestData.setTestToRun(TestRepository.getTest(Integer.parseInt(v)));
        MainTestData.setSpecToRun(null);
    }

    @Override
    public String getValue() {
        return MainTestData.getTestToRun() != null ? MainTestData.getTestToRun().getFullName() : "";
    }
    
}
