package org.genevaers.runcontrolanalyser.menu.localrcset;

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


import java.nio.file.Path;
import java.nio.file.Paths;

import org.fusesource.jansi.Ansi.Color;
import org.genevaers.runcontrolanalyser.menu.RCAGenerationData;
import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuItem;

public class LocalRun extends MenuItem{


    LocalRun() {
        color = Color.RED;
        header = "Execute";
        prompt = "Run RCA";
        comment = "";
    }

    @Override
    public boolean doIt() {
        Path cwd = Paths.get("");
        Path rcDir = cwd.resolve(RCAGenerationData.getLocalRC());
        System.out.println("Write RC Set to " + rcDir.toString());
        RCAGenerationData.generateFlow();
        //Run the RCG (via CommandLine)
        //Then run the RCA on the results
        Menu.promptedWrite("Enter to continue");
        return true;
    }
   

}
