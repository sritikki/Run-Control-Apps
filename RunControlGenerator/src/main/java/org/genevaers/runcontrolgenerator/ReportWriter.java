package org.genevaers.runcontrolgenerator;

import java.io.File;

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

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.utilities.GersEnvironment;

import com.google.common.flogger.FluentLogger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ReportWriter {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final static String REPORT_TEMPLATE = "MR91RPT.ftl";

    private static final String LOCALROOT = "LOCALROOT";
	private  Configuration cfg;

    private int jltRecordsWritten;

    private int xltRecordsWritten;

    private Object vdpRecordsWritten;

	public  void write(RunControlConfigration rcc){
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
            nodeMap.put("parmsRead", rcc.getLinesRead());
            nodeMap.put("optsInEffect", rcc.getOptionsInEffect());
            nodeMap.put("inputReports", Repository.getInputReports());
            nodeMap.put("vdpRecordsWritten", String.format("%,d", vdpRecordsWritten));
            nodeMap.put("xltRecordsWritten", String.format("%,d", xltRecordsWritten));
            nodeMap.put("jltRecordsWritten", String.format("%,d", jltRecordsWritten));
            nodeMap.put("views", Repository.getViews().getValues());
            logger.atInfo().log(rcc.getReportFileName());
            generateTemplatedOutput(template, nodeMap, rcc.getReportFileName());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		logger.atConfig().log("Report Generated");
	}


	private  void generateTemplatedOutput(Template template, Map<String, Object> nodeMap, String reportFileName) {
		try {
    		FileWriter cfgWriter = new FileWriter(reportFileName);
	    	template.process(nodeMap, cfgWriter);
            cfgWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

}
