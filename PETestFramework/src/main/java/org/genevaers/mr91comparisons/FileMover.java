package org.genevaers.mr91comparisons;

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


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class FileMover {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    //Want the XML file name - minus the .xml
    //made dir of that name
    //move the xml file into that dir	private static final String RESULTS_PATH = "mr91Outputs";
	private static final String RESULTS_PATH = "mr91Outputs";
	private static final String LOCALROOT = "LOCALROOT";
	private static final String INPUTS = "mr91Inputs";

    public static void main(String[] args) {
        Path rootPath = Paths.get(TestEnvironment.get(LOCALROOT));
        Path inputsPath = rootPath.resolve(INPUTS);
        Path filesDir = inputsPath.resolve("mr91Comparisons/env153/views");
        Path files = filesDir.resolve("xmlnames.txt"); 
        
    try (FileReader fr = new FileReader(files.toFile())) {
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while ((line = reader.readLine()) != null) {
				logger.atInfo().log("Process %s", line);
                String name = line.substring(0, line.lastIndexOf(".xml"));
                Path nameDir = filesDir.resolve(name).resolve("WBXML");
                if(nameDir.toFile().exists()) {
                    appendToConfigFile(nameDir, line);
                } else {
                    nameDir.toFile().mkdirs();
                    Path source = filesDir.resolve(line);
                    if(source.toFile().exists()) {
                        Files.move(source, nameDir.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                    makeConfigFile(nameDir, line);
                }
            }
		} catch (Exception e) {
			//It does not have to exist DBRUN and be treated as an override
			logger.atSevere().withStackTrace(StackSize.SMALL).log(e.getMessage());
		}

    }    

    private static void appendToConfigFile(Path nameDir, String name) {
        logger.atInfo().log("Append to Run");
        Path mr91run = nameDir.resolve("MR91RUN.cfg");
        try(FileWriter config = new FileWriter(mr91run.toFile())) {
            config.write("# Auto generated " + nameDir.getFileName());
            config.write("\nSource: XML\n");
            config.write("#Source from XML(s)\n");
            config.write(name);
            config.write("\n#Output Option\n");
            config.write("GenerateXML\n");
            config.write("CopyXML\n");
        } catch (IOException e) {
			logger.atSevere().log(e.getMessage());
        }
    }

    public static void makeConfigFile(Path nameDir, String name) {
        //We need the basic XML config for MR91RUN.cfg 
        //which has in it the XML(s) to be processed
        Path mr91run = nameDir.resolve("MR91RUN.cfg");
        try(FileWriter config = new FileWriter(mr91run.toFile())) {
            config.write("# Auto generated " + nameDir.getFileName());
            config.write("\nSource: XML\n");
            config.write("#Source from XML(s)\n");
            config.write(name);
            config.write("\n#Output Option\n");
            config.write("GenerateXML\n");
            config.write("CopyXML\n");
        } catch (IOException e) {
			logger.atSevere().log(e.getMessage());
        }
    }
    
}
