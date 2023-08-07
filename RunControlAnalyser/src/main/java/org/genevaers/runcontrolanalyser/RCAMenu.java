package org.genevaers.runcontrolanalyser;

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


import java.util.HashMap;
import java.util.Map;

import org.fusesource.jansi.AnsiConsole;
import org.genevaers.runcontrolanalyser.menu.Menus;
import org.genevaers.runcontrolanalyser.menu.RCAGenerationData;
import org.genevaers.runcontrolanalyser.menu.dbgeneration.DBGenerationMenu;
import org.genevaers.runcontrolanalyser.menu.ftpfetch.FTPFetchMenu;
import org.genevaers.runcontrolanalyser.menu.localrcset.LocalRcMenu;
import org.genevaers.runcontrolanalyser.menu.main.MainMenu;
import org.genevaers.runcontrolanalyser.menu.xmlgeneration.XMLGenerationMenu;
import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuDriver;

public class RCAMenu {


    private boolean notdone = true;
    static MenuDriver menuDriver = new MenuDriver();

    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        //Make this thing switched based on the OS
        //We only require it when this thing is run on a Windows command prompt
        //AnsiConsole.systemInstall();

        MainMenu mainMenu = new MainMenu();
        Menu.setHeader("Run Control Analyzer\nVersion: " + CommandLineHandler.readVersion());
        menuDriver.addMenu(Menus.Main.toString(), mainMenu);
        menuDriver.setCurrentMenuByName(Menus.Main.toString());
        //Need an enum or something to avoid name typos
        menuDriver.addMenu(Menus.DBGen.toString(), new DBGenerationMenu());
        menuDriver.addMenu(Menus.WBXMLGen.toString(), new XMLGenerationMenu());
        menuDriver.addMenu(Menus.FTPFETCH.toString(), new FTPFetchMenu());
        menuDriver.addMenu(Menus.LOCAL.toString(), new LocalRcMenu());
    
        menuDriver.generate();

    }

    public boolean isNotDone() {
        return notdone;
    }

}
