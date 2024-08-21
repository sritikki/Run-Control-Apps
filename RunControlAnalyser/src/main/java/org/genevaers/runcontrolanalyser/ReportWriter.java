package org.genevaers.runcontrolanalyser;



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

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.genevaers.genevaio.dbreader.DBFoldersReader;
import org.genevaers.genevaio.dbreader.DBViewsReader;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolanalyser.configuration.RcaConfigration;
import org.genevaers.utilities.GersEnvironment;
import org.genevaers.utilities.GersFile;

import com.google.common.flogger.FluentLogger;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ReportWriter {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final static String REPORT_TEMPLATE = "RCARPT.ftl";

    private static final String LOCALROOT = "LOCALROOT";
	private  Configuration cfg;

    private int jltRecordsWritten;

    private int xltRecordsWritten;

    private int vdpRecordsWritten;

	public  void write(int numVDPDiffs, int numXLTDiffs, int numJLTDiffs){
		GersEnvironment.initialiseFromTheEnvironment();
		configureFreeMarker();
        Template template;
        try {
            String version = "unknown";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Properties properties = new Properties();
            InputStream resourceStream = loader.getResourceAsStream("application.properties");
			properties.load(resourceStream);
            version = properties.getProperty("build.version") + " (" + properties.getProperty("build.timestamp") + ")";
            template = cfg.getTemplate(REPORT_TEMPLATE);
            Map<String, Object> nodeMap = new HashMap<>();
            nodeMap.put("env", "stuff");
            nodeMap.put("parmsRead", RcaConfigration.getLinesRead());
            nodeMap.put("optsInEffect", RcaConfigration.getOptionsInEffect());
            nodeMap.put("rcaversion", readVersion());
            nodeMap.put("numVDPDiffs", numVDPDiffs);
            nodeMap.put("numXLTDiffs", numXLTDiffs);
            nodeMap.put("numJLTDiffs", numJLTDiffs);

            logger.atInfo().log(RcaConfigration.REPORT_DDNAME);
            generateTemplatedOutput(template, nodeMap, RcaConfigration.REPORT_DDNAME);
        } catch (IOException e) {
            logger.atSevere().log("Report Writer error %s",e.getMessage());
        }
		logger.atConfig().log("Report Generated");
	}


	private  void generateTemplatedOutput(Template template, Map<String, Object> nodeMap, String reportFileName) {
        try(Writer fw = new GersFile().getWriter(reportFileName)) {
	    	template.process(nodeMap, fw);
		} catch (IOException | TemplateException e) {
			logger.atSevere().log("Template generation failed %e", e.getMessage());;
		}
    }


    private  void configureFreeMarker() {
		cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}


    public void setNumJLTRecordsWritten(int numberOfRecords) {
        jltRecordsWritten = numberOfRecords;
    }


    public void setNumXLTRecordsWritten(int numberOfRecords) {
        xltRecordsWritten = numberOfRecords;
    }


    public void setNumVDPRecordsWritten(int numberOfRecords) {
        vdpRecordsWritten = numberOfRecords;
    }

	public String readVersion() {
		String version = "unknown";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
			properties.load(resourceStream);
            version = properties.getProperty("build.version") + " (" + properties.getProperty("build.timestamp") + ")";
		} catch (IOException e) {
            logger.atSevere().log("Cannot readVersion %s", e.getMessage());
		}
		return version;
	}


}
