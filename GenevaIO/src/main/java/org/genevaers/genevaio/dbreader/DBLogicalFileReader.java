package org.genevaers.genevaio.dbreader;

import java.sql.Connection;
import java.sql.PreparedStatement;

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


import java.sql.ResultSet;
import java.sql.SQLException;

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;

import com.google.common.flogger.FluentLogger;


public class DBLogicalFileReader extends DBReaderBase{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
		if(requiredLFs.size() > 0) {
			String query = "select * from " + params.getSchema() +".LOGFILE "
			+ " where LOGFILEID in (" + getIds(requiredLFs) + ") and ENVIRONID= " + params.getEnvironmentID() + ";";
			executeAndWriteToRepo(dbConnection, query);
		}
        return false;
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        LogicalFile lf = new LogicalFile();
        lf.setID(rs.getInt("LOGFILEID"));
        lf.setName(rs.getString("NAME"));
        Repository.getLogicalFiles().add(lf, lf.getID(), lf.getName());
    }    

    public boolean addLFtoRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params, int environmentID, int lfid) {
        String query = "select * from " + params.getSchema() +".LOGFILE "
        + " where LOGFILEID = " + lfid + " and ENVIRONID= " + params.getEnvironmentID() + ";";
        executeAndWriteToRepo(dbConnection, query);
        return false;
    }

    public boolean addToRepoByName(DatabaseConnection dbConnection, DatabaseConnectionParams params, String name) {
        String query = "select * from " + params.getSchema() +".LOGFILE "
        + " where NAME = '" + name + "' and ENVIRONID= " + params.getEnvironmentID() + ";";
        executeAndWriteToRepo(dbConnection, query);
        return false;
    }

	public Integer getLFPFAssocID(DatabaseConnection dbConnection, DatabaseConnectionParams params, String lfName, String pfName) {
		Integer result = null;
		try {
			String schema = params.getSchema();

			String selectString = "select lfpfassocid from " + schema + ".lfpfassoc a"
					+ " join " + schema + ".logfile l on l.environid=a.environid and l.logfileid = a.logfileid"
					+ " join " + schema + ".phyfile p on a.environid=p.environid and p.phyfileid = a.phyfileid"
					+ " where l.environid = ? and UPPER(l.name) = ? and UPPER(p.name) = ?";

			PreparedStatement pst = null;
			ResultSet rs = null;
			while (true) {
				try {
					pst = dbConnection.getConnection().prepareStatement(selectString);
					pst.setInt(1, Integer.valueOf(params.getEnvironmentID()));
					pst.setString(2, lfName.toUpperCase());
					pst.setString(3, pfName.toUpperCase());
					rs = pst.executeQuery();
					break;
				} catch (SQLException se) {
					if (dbConnection.getConnection().isClosed()) {
					} else {
						throw se;
					}
				}
			}

			if (rs.next()) {
				result = rs.getInt(1);
			} else {
				logger.atInfo()
						.log("No LogicalFile-PhysicalFile association found in Environment ["
								+ params.getEnvironmentID()
								+ "] with parentfileid "
								+ lfName
								+ "] and childpartitionid ["
								+ pfName + "]");
			}
			pst.close();
			rs.close();

		} catch (SQLException e) {
            logger.atInfo()
            .log("No LogicalFile-PhysicalFile association found in Environment ["
                    + params.getEnvironmentID()
                    + "] with parentfileid "
                    + lfName
                    + "] and childpartitionid ["
                    + pfName + "]");
		}
        return result;
	}
}
