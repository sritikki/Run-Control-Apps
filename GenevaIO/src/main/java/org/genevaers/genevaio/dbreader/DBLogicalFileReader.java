package org.genevaers.genevaio.dbreader;

import java.sql.Connection;

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


public class DBLogicalFileReader extends DBReaderBase{

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query = "select * from " + params.getSchema() +".LOGFILE "
        + " where LOGFILEID in (" + getIds(requiredLFs) + ") and ENVIRONID= " + params.getEnvironmentID() + ";";
        executeAndWriteToRepo(dbConnection, query);
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

}
