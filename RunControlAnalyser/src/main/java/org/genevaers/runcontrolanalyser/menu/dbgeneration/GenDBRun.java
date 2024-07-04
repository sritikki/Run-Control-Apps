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


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.fusesource.jansi.Ansi.Color;
import org.genevaers.runcontrolanalyser.menu.RCAGenerationData;
import org.genevaers.utilities.menu.Menu;
import org.genevaers.utilities.menu.MenuItem;

import com.google.common.flogger.FluentLogger;

public class GenDBRun extends MenuItem{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    GenDBRun() {
        color = Color.RED;
        header = "Execute";
        prompt = "Run RCG";
        comment = "";
    }

    @Override
    public boolean doIt() {
        Path cwd = Paths.get("");
        Path rcDir = cwd.resolve(RCAGenerationData.getRcSet());
        rcDir.toFile().mkdirs();
        System.out.println("Write RC Set to " + rcDir.toString());
        buildMR91Parm(rcDir);
        RCAGenerationData.runRCG(rcDir);
        RCAGenerationData.generateFlow();
        //Run the RCG (via CommandLine)
        //Then run the RCA on the results
        Menu.promptedWrite("Enter to continue");
        return true;
    }

    private void buildMR91Parm(Path rcDir) {
        try (FileWriter fw = new FileWriter(rcDir.resolve("MR91Parm.cfg").toFile())) {
            fw.write("INPUT_TYPE=DB2\n");
            fw.write("OUTPUT_RUN_CONTROL_FILES=Y\n");
            fw.write("DB_SERVER="+RCAGenerationData.getDbServer()+"\n");
            fw.write("DB_DATABASE="+RCAGenerationData.getDbDatabase()+"\n");
            fw.write("DB_SCHEMA="+RCAGenerationData.getDbSchema()+"\n");
            fw.write("DB_PORT="+RCAGenerationData.getDbPort()+"\n");
            fw.write("DB_ENVIRONMENT_ID="+RCAGenerationData.getEnvId()+"\n");
            fw.write("DBVIEWS="+RCAGenerationData.getViewIds()+"\n");
            fw.close();
        } catch (IOException e) {
            logger.atSevere().log("buildMR91Parm failed\n%s", e.getMessage());
        }
    }


    
}
