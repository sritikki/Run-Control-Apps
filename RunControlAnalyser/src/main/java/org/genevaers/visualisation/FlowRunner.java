package org.genevaers.visualisation;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008.
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


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Comparator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.genevaers.utilities.CommandRunner;

public class FlowRunner {
    private static final String VDP_RECORDS = "VDPFlowRecords";
	static transient Logger logger = Logger.getLogger("com.ibm.safr.test.pm.FlowRunner");
	private Path configPath;
	private Path vdpRecordsPath;

	public void processVDP(String vdpAbs, String ref) {
		
		// We need to copy the vdp to a working directory as VDP1
		//Then is can be processed.
		
        Path src = Paths.get(vdpAbs);
        vdpRecordsPath = Paths.get(VDP_RECORDS + File.separator + ref);
        Path trpOutPath = Paths.get(vdpRecordsPath + File.separator + ref + File.separator + "out");
        Path trg = Paths.get( vdpRecordsPath + File.separator +"com.ibm.safr.VDP");
        Path trgXLT = Paths.get( vdpRecordsPath + File.separator +"XLT");
        Path srcXLT = src.getParent().resolve("MR91.XLT");
        Path trgConfig = Paths.get( vdpRecordsPath + File.separator +"VDPNPARM");
		
	    initLogger();
	    logger.info("Processing com.ibm.safr.VDP " + vdpAbs);
        String flowStr = "RunControlAnalyser.exe";
        CommandRunner cmd = new CommandRunner();

        try {
        	if(Files.exists(vdpRecordsPath)) {
        		Files.walk(vdpRecordsPath)
        	      .sorted(Comparator.reverseOrder())
        	      .map(Path::toFile)
        	      .forEach(File::delete);
        	}
    		Files.createDirectories(trpOutPath);
			Files.copy(src, trg, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(srcXLT, trgXLT, StandardCopyOption.REPLACE_EXISTING);
			if(configPath != null) {
				Files.copy(configPath, trgConfig, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.copy(Paths.get(VDP_RECORDS+ File.separator +"VDPNPARM"), trgConfig, StandardCopyOption.REPLACE_EXISTING);				
			}

			int rc = cmd.run(flowStr, vdpRecordsPath.toFile());
	        cmd.clear();
	        
	        convertDotsToSVGs(vdpRecordsPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	public void runBrowser(){
		CommandRunner cmdRunner = new CommandRunner();
		try {
			cmdRunner.run("firefox VDP1.dot.svg", vdpRecordsPath.toFile());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void convertDotsToSVGs(Path vdpRecordsPath) {
		//Iterate through all of the dot files and convert to svg
		logger.info("Processing All dot files in: " + vdpRecordsPath.toString());
		// want to find all of the xml files under the spec
		// use the Apache beast
		WildcardFileFilter fileFilter = new WildcardFileFilter("*.dot");
		Collection<File> dotFiles = FileUtils.listFiles(vdpRecordsPath.toFile(), fileFilter, TrueFileFilter.TRUE);
		
		for(File d : dotFiles) {
	        //Need to find an iterate for these ... but
	        GraphVizRunner gvr = new GraphVizRunner();
	        gvr.processDot(d);
		}
	}

	private void initLogger() {
		logger.setUseParentHandlers(false);
		Handler[] handlers = logger.getHandlers();
		if( handlers.length == 0) { 
		    Handler conHdlr = new ConsoleHandler();
		    conHdlr.setFormatter(new Formatter() {
		      public String format(LogRecord record) {
		        return record.getLevel() + "  :  "
		            + record.getSourceClassName() + " -:- "
		            + record.getSourceMethodName() + " -:- "
		            + record.getMessage() + "\n";
		      }
		    });
		    logger.addHandler(conHdlr);
		}
	}

	public void getConfigFrom(Path cfg) {
		configPath = cfg;
	}

	public String getRecordsDirectory() {
		return VDP_RECORDS;
	}

}
