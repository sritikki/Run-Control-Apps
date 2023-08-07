package org.genevaers.runcontrolanalyser.menu.ftpfetch;

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


import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuItem;
import org.genevaers.utilities.menu.MenuSetting;

public class FTPFetchMenu extends Menu {

    public FTPFetchMenu() {
        menuItems.add(new FTPServer());
        menuItems.add(new FTPhlq());
        menuItems.add(new FTPRCSetName());
        menuItems.add(new FTPRun());
        menuItems.add(new FTPReturn());
    }

    @Override
    public void showSettings(StringBuilder menu) {
        if(System.getenv("TSO_USERID") == null || System.getenv("TSO_USERID").isEmpty()) {
            menu.append(RED+"TSO_USERID not set!"+RESET+ "\n");
        }
        if(System.getenv("TSO_PASSWORD") == null || System.getenv("TSO_PASSWORD").isEmpty()) {
            menu.append(RED+"TSO_PASSWORD not set!"+RESET+ "\n");
        }
        menu.append(YELLOW+"\nDatabase Generation Settings\n");
        menu.append(YELLOW+"\nFTP Settings\n");
        for(MenuItem mi : menuItems) {
            if(mi instanceof MenuSetting) {
                Menu.addMenuSummaryItem(menu, mi.getPrompt(), ((MenuSetting)mi).getValue(), mi.getComment());
            }
        }
    }
}
