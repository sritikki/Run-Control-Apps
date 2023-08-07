package org.genevaers.runcontrolgenerator.repositorybuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
import com.google.common.flogger.StackSize;
import com.ibm.db2.jcc.DB2Connection;
import com.ibm.jzos.PdsDirectory;
import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileConstants;
import com.ibm.jzos.ZFileException;
import com.ibm.jzos.PdsDirectory.MemberInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.genevaers.genevaio.dbreader.DBReader;
import org.genevaers.genevaio.dbreader.DatabaseConnectionParams;
import org.genevaers.genevaio.dbreader.DatabaseConnection.DbType;
import org.genevaers.genevaio.wbxml.WBXMLSaxIterator;
import org.genevaers.runcontrolgenerator.InputType;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;
import org.genevaers.utilities.GenevaLog;

public class RepositoryBuilder {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	private RunControlConfigration rcc;
	private Status retval;

	public RepositoryBuilder(RunControlConfigration rcc) {
		this.rcc = rcc;
	}

	// Could flip this around and have types of RepoBuilder
	public Status run() {
		GenevaLog.logNow("RepositoryBuilder");
		retval = Status.OK;
		GenevaLog.writeHeader("Build the internal repository");
		if (rcc.getInputType().equals(InputType.WBXML.toString())) {
			logger.atInfo().log("Build repository from WB XML");
			buildRepoFromWBXML();
		} else if (rcc.getInputType().equals(InputType.DB2.toString())) {
			logger.atInfo().log("Build repository from DB2");
			buildRepoFromDB2();
		} else {
			logger.atSevere().log("Unknown Input Type %s", rcc.getInputType());
			retval = Status.ERROR;
		}
		return retval;
	}

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

	private void buildRepoFromWBXML() {
		logger.atFine().log("Read From %s", rcc.getWBXMLDirectory());

		// Here we need to know if we're on z/OS
		// Is so treat the WBXMLDirectory as a DDname to a PDS
		// Then again we could be running on USS?

		// We need to know if we are reading a PDS or not
		// or DDname input
		String os = System.getProperty("os.name");
		logger.atInfo().log("Operating System %s", os);
		if (os.startsWith("z")) {
			readFromDataSet();
		} else {
			readFromDirectory();
		}
	}

	private void readFromDataSet() {
		try {
			String ddname = "//DD:" + rcc.getWBXMLDirectory();
			ZFile dd = new ZFile(ddname, "r");
			// Problem here is that this will be a PDS and we need to iterate its memebers
			int type = dd.getDsorg();
			switch (type) {
				case ZFileConstants.DSORG_PDS_DIR:
				logger.atInfo().log("found PDS E");
					//Drop through
				case ZFileConstants.DSORG_PDSE: {
					logger.atInfo().log("found PDS");
					String pdsName = dd.getActualFilename();
					logger.atInfo().log("Actual filename " + pdsName);
					try {
						PdsDirectory pds = new PdsDirectory(ddname);
						Iterator pdsi = pds.iterator();
						while (pdsi.hasNext()) {
							MemberInfo mem = (MemberInfo) pdsi.next();
							String mname = mem.getName();
							String buildName = ddname + "(" + mname + ")";
							logger.atInfo().log("Build Repo from " + buildName);
							ZFile pdsmem = new ZFile(buildName, "r");
							buildFromXML(pdsmem.getInputStream());
							pdsmem.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;
				case ZFileConstants.DSORG_PDS_MEM:
				logger.atInfo().log("found PDS member");
					buildFromXML(dd.getInputStream());
					break;
				case ZFileConstants.DSORG_PS:
					logger.atInfo().log("found DSOR PS");
					buildFromXML(dd.getInputStream());
					break;
				default:
					logger.atSevere().log("Unhandled DSORG " + type);
			}
			dd.close();
		} catch (ZFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readFromDirectory() {
		Path xmlPath = Paths.get(rcc.getWBXMLDirectory());
		if (xmlPath.toFile().exists()) {
			WildcardFileFilter fileFilter = new WildcardFileFilter("*.xml");
			Collection<File> xmlFiles = FileUtils.listFiles(xmlPath.toFile(), fileFilter, TrueFileFilter.TRUE);

			for (File d : xmlFiles) {
				logger.atFine().log("Read %s", d.getName());
				try {
					buildFromXML(new FileInputStream(d));
				} catch (FileNotFoundException e) {
					logger.atSevere().withStackTrace(StackSize.FULL).log("Repo build failed " + e.getMessage());
					retval = Status.ERROR;
				}
			}
		} else {
			logger.atSevere().log("WBXML file %s not found", xmlPath.toString());
		}
	}

	private void buildFromXML(InputStream inputStream) {
		WBXMLSaxIterator wbReader = new WBXMLSaxIterator();
		try {
			wbReader.inputFrom(inputStream);
			wbReader.addToRepsitory();
		} catch (Exception e) {
			logger.atSevere().withStackTrace(StackSize.FULL).log("Repo build failed " + e.getMessage());
			retval = Status.ERROR;
		}
	}
}
