package org.genevaers.runcontrolgenerator;

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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.genevaio.ltfile.LTLogger;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.Repository;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;
import org.genevaers.runcontrolgenerator.compilers.FormatRecordsBuilder;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.repositorybuilders.RepositoryBuilder;
import org.genevaers.runcontrolgenerator.repositorybuilders.RepositoryBuilderFactory;
import org.genevaers.runcontrolgenerator.runcontrolwriter.RunControlWriter;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.LogicGroup;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.SinglePassOptimiser;
import org.genevaers.runcontrolgenerator.utility.Status;
import org.genevaers.utilities.GenevaLog;
import com.google.common.flogger.FluentLogger;

public class RunControlGenerator {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	//RunControlConfigration rcc;
	private FileWriter reportWriter;
	ReportWriter report = new ReportWriter();

	private Status status;

	private List<LogicGroup> logicGroups;

	private ASTBase xltRoot;

	private LogicTable extractLogicTable;
	private LogicTable joinLogicTable;

	public void runFromConfig() {
		GenevaLog.writeHeader("Run Control Generator");

		if(buildComponentRepositoryFromSelectedInput() != Status.ERROR) {
			Repository.fixupMaxHeaderLines();
			Repository.fixupPFDDNames();
			Repository.allLFsNotRequired();
			Repository.setGenerationTime(Calendar.getInstance().getTime());
			singlePassOptimise();
			runCompilers();
			writeRunControlFiles();
			report.write();
		} else {
			logger.atSevere().log("Failed to build the component repository. No run control files will be written");
		}
	}

	private void writeRunControlFiles() {
		if(status != Status.ERROR) {
			RunControlWriter rcw = new RunControlWriter();
			logger.atFine().log("Join Logic Table");
			logger.atFine().log(LTLogger.logRecords(joinLogicTable));
			logger.atFine().log("Extract Logic Table");
			logger.atFine().log(LTLogger.logRecords(extractLogicTable));
			rcw.setExtractLogicTable(extractLogicTable);
			rcw.setJoinLogicTable(joinLogicTable);
			status = rcw.run();
			report.setNumJLTRecordsWritten(joinLogicTable.getNumberOfRecords());
			report.setNumXLTRecordsWritten(extractLogicTable.getNumberOfRecords());
			report.setNumVDPRecordsWritten(rcw.getNumVDPRecordsWritten());
		} else {
			logger.atSevere().log("There were errors. No run control files will be written");
		}
	}

	/**
	 * Now that the single pass optimisation has been completed
	 * We need to form the tree of information to be enitted
	 * The top level of which is derived from the logic groups.
	 * The lower level as a result of compiling the logic text in the 
	 * view sources and view column sources.
	 * 
	 * We also need to compile the format phase filter and column calculations.
	 */
	private void runCompilers() {
		GenevaLog.logNow("runCompilers");
		if(status != Status.ERROR) {
			ExtractPhaseCompiler.run(logicGroups);
			extractLogicTable = ExtractPhaseCompiler.getExtractLogicTable();
			joinLogicTable = ExtractPhaseCompiler.getJoinLogicTable();
			FormatRecordsBuilder.run();
		} else {
			logger.atSevere().log("There were SPO errors. No compilation performed.");
		}
	}

	private void singlePassOptimise() {
		if(status != Status.ERROR) {
			SinglePassOptimiser spo = new SinglePassOptimiser();
			status = spo.run();
			logicGroups = spo.getLogicGroups();
			dumpLogicGroups();
		}
	}

	private void dumpLogicGroups() {
		StringBuilder sb = new StringBuilder();
		logicGroups.stream().forEach(lg -> lg.logData());
	}

	private Status buildComponentRepositoryFromSelectedInput() {
		RepositoryBuilder rb = RepositoryBuilderFactory.get();
		return rb != null ? rb.run() : Status.ERROR;
	}

	private void openReportFile() {
		try {
			reportWriter = new FileWriter(new File(RunControlConfigration.getReportFileName()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
