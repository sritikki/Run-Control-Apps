package org.genevaers.utilities.menu;

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

public class MenuDriver {


    private boolean notdone = true;

    Map<String, Menu> menus = new HashMap<>();
    private static Menu currentMenu;

    public static void main(String[] args) {
        MenuDriver md = new MenuDriver();
        md.generate();
    }

    public void generate() {
        //Make this thing switched based on the OS
        //We only require it when this thing is run on a Windows command prompt
        //AnsiConsole.systemInstall();
        runMenus();
        Menu.clearScreen();
    }

    private void runMenus() {
        // menus.put(Menus.Main.toString(), new MainMenu());
        // currentMenu = menus.get(Menus.Main.toString());
        // //Need an enum or something to avoid name typos
        // menus.put(Menus.DBGen.toString(), new DBGenerationMenu());
        // menus.put(Menus.WBXMLGen.toString(), new XMLGenerationMenu());
        // menus.put(Menus.FTPFETCH.toString(), new FTPFetchMenu());
        // menus.put(Menus.LOCAL.toString(), new LocalRcMenu());

        while(currentMenu != null) {
            currentMenu.showMenu();
            currentMenu = menus.get(currentMenu.getNextMenu());
        }
    }

    public void setCurrentMenuByName(String name) {
        this.currentMenu = menus.get(name);
    }

    public void addMenu(String key, Menu m) {
        menus.put(key, m);
    }

    public boolean isNotDone() {
        return notdone;
    }

}
