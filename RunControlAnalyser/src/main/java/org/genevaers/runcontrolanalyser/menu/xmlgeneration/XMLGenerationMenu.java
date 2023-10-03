package org.genevaers.runcontrolanalyser.menu.xmlgeneration;

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

public class XMLGenerationMenu extends Menu {

    public XMLGenerationMenu() {
        menuItems.add(new XMLInput());
        menuItems.add(new XmlRCSetName());
        menuItems.add(new GenXmlRun());
        menuItems.add(new XMLReturn());
    }

    @Override
    public void showSettings(StringBuilder menu) {
        menu.append(YELLOW+"\nWBXML Generation Settings\n");
        for(MenuItem mi : menuItems) {
            if(mi instanceof MenuSetting) {
                Menu.addMenuSummaryItem(menu, mi.getPrompt(), ((MenuSetting)mi).getValue(), mi.getComment());
            }
        }
    }
}
