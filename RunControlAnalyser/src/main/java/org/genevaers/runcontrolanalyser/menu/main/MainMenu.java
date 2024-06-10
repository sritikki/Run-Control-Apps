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


import org.genevaers.utilities.menu.Menu;

public class MainMenu extends Menu{

    public MainMenu() {
        menuItems.add(new GenerateLocalDB()); 
        menuItems.add(new GenerateLocalXML()); 
        menuItems.add(new FtpRcSet());
        menuItems.add(new LocalRcSet());
        menuItems.add(new OpenFlows());
        menuItems.add(new ShuffleOff());
    }

    @Override
    public void showSettings(StringBuilder menuStr) {
        // TODO Auto-generated method stub
        
    }

}
