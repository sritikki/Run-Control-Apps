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


import org.genevaers.testframework.menu.main.MainMenu;
import org.genevaers.utilities.GersEnvironment;
import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuDriver;

public class PETestMenu {

    private boolean notdone = true;
    static MenuDriver menuDriver = new MenuDriver();

    public static void show(TestDriver testDriver) {
        // Make this thing switched based on the OS
        // We only require it when this thing is run on a Windows command prompt
        // AnsiConsole.systemInstall();
        // Probably don't really want this here?
        TestDriver.processSpecList(); // Really just want the YAML reader to build the in memory specdata
        // Then we can select the test we want to run and from the specdata run the test
        // generating the templates etc as needed.
        // We do still want a command line interface that runs all tests though

        MainMenu mainMenu = new MainMenu();
        Menu.setHeader("Test Framework: running on " + GersEnvironment.get("OSNAME") + "\nVersion: "
                + "Get the version from somewhere");
        menuDriver.addMenu("Main", mainMenu);
        menuDriver.setCurrentMenuByName("Main");
        menuDriver.generate();
    }

    public boolean isNotDone() {
        return notdone;
    }

}
