package org.genevaers.runcontrolgenerator.repositorybuilders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

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

import com.google.common.flogger.FluentLogger;

import org.genevaers.genevaio.dbreader.DBReader;
import org.genevaers.genevaio.dbreader.DatabaseConnectionParams;
import org.genevaers.genevaio.dbreader.DatabaseConnection.DbType;

import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;


public abstract class RepositoryBuilder {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	protected RunControlConfigration rcc;
	protected Status retval;

	public RepositoryBuilder(RunControlConfigration rcc) {
		this.rcc = rcc;
	}

	public abstract Status run();

	// public Status run() {
	// 	retval = Status.OK;
	// 	GenevaLog.writeHeader("Build the internal repository");
	// 	if (rcc.getInputType().equals(InputType.WBXML.toString())) {
	// 		logger.atInfo().log("Build repository from WB XML");
	// 		buildRepoFromWBXML();
	// 	} else if (rcc.getInputType().equals(InputType.VDPXML.toString())) {
	// 			logger.atInfo().log("Build repository from VDP XML");
	// 			buildRepoFromVDPXML();
	// 	 else if (rcc.getInputType().equals(InputType.DB2.toString())) {
	// 		logger.atInfo().log("Build repository from DB2");
	// 		buildRepoFromDB2();
	// 	} else if (rcc.getInputType().equals(InputType.POSTGRES.getName())) {
	// 		logger.atInfo().log("Build repository from Postgres");
	// 		buildRepoFromPostgres();
	// 	} else {
	// 		logger.atSevere().log("Unknown Input Type %s", rcc.getInputType());
	// 		retval = Status.ERROR;
	// 	}
	// 	return retval;
	// }

	private void buildRepoFromDB2() {
		DatabaseConnectionParams conParams = new DatabaseConnectionParams();
		conParams.setDatabase(rcc.getParm(RunControlConfigration.DB2_DATABASE));
		conParams.setDbType(DbType.DB2);
		conParams.setEnvironmenID(rcc.getParm(RunControlConfigration.DB2_ENVIRONMENT_ID));
		conParams.setPort(rcc.getParm(RunControlConfigration.DB2_PORT));
		conParams.setServer(rcc.getParm(RunControlConfigration.DB2_SERVER));
		conParams.setFolderIds(rcc.getParm(RunControlConfigration.DBFLDRS));
		conParams.setViewIds(rcc.getParm(RunControlConfigration.DBVIEWS));
		conParams.setSchema(rcc.getParm(RunControlConfigration.DB2_SCHEMA));
		conParams.setUsername(System.getenv("TSO_USERID"));
		conParams.setPassword(System.getenv("TSO_PASSWORD"));
		DBReader dbr = new DBReader();
		dbr.addViewsToRepository(conParams);
	}

	private void buildRepoFromPostgres() {
		DatabaseConnectionParams conParams = new DatabaseConnectionParams();
		conParams.setDatabase(rcc.getParm(RunControlConfigration.DB2_DATABASE));
		conParams.setDbType(DbType.POSTGRES);
		conParams.setEnvironmenID(rcc.getParm(RunControlConfigration.DB2_ENVIRONMENT_ID));
		conParams.setPort(rcc.getParm(RunControlConfigration.DB2_PORT));
		conParams.setServer(rcc.getParm(RunControlConfigration.DB2_SERVER));
		conParams.setFolderIds(rcc.getParm(RunControlConfigration.DBFLDRS));
		conParams.setViewIds(rcc.getParm(RunControlConfigration.DBVIEWS));
		conParams.setSchema(rcc.getParm(RunControlConfigration.DB2_SCHEMA));
		conParams.setUsername(System.getenv("PG_USERID"));
		conParams.setPassword(System.getenv("PG_PASSWORD"));
		DBReader dbr = new DBReader();
		dbr.addViewsToRepository(conParams);
	}

	// private void buildRepoFromWBXML() {
	// 	logger.atFine().log("Read From %s", rcc.getWBXMLDirectory());

	// 	// Here we need to know if we're on z/OS
	// 	// Is so treat the WBXMLDirectory as a DDname to a PDS
	// 	// Then again we could be running on USS?

	// 	// We need to know if we are reading a PDS or not
	// 	// or DDname input
	// 	String os = System.getProperty("os.name");
	// 	logger.atFine().log("Operating System %s", os);
	// 	if (os.startsWith("z")) {
	// 		readFromDataSet();
	// 	} else {
	// 		readFromDirectory();
	// 	}
	// }

}
