package org.genevaers.genevaio.dbreader;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.enums.AccessMethod;
import org.genevaers.repository.components.enums.DbmsRowFmtOptId;
import org.genevaers.repository.components.enums.FieldDelimiter;
import org.genevaers.repository.components.enums.FileRecfm;
import org.genevaers.repository.components.enums.FileType;
import org.genevaers.repository.components.enums.RecordDelimiter;
import org.genevaers.repository.components.enums.TextDelimiter;


public class DBPhysicalFileReader extends DBReaderBase {
    Map<Integer, List<Integer>> lf2pf = new HashMap<>();

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        //get the required PFs via a query and add to the set
        updateRequiredPfs(dbConnection, params);
        //Then get the PFs
        String pfRecs = "select * from " + params.getSchema() + ".PHYFILE "
        + "where PHYFILEID in (" + getIds(requiredPFs) + ") and ENVIRONID= " + params.getEnvironmentID() + ";";
        executeAndWriteToRepo(dbConnection, pfRecs);
        //Note this relies on the LFs having been added first
        addPfsToLfs();
        return false;
    }

    private void updateRequiredPfs(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String pfsFromLfs = "select LOGFILEID, PHYFILEID from " + params.getSchema() + ".LFPFASSOC "
                + "where LOGFILEID in (" + getIds(requiredLFs) + ") and ENVIRONID=" + params.getEnvironmentID() + ";";
        try {
            ResultSet rs = dbConnection.getResults(pfsFromLfs);
            while (rs.next()) {
                requiredPFs.add(rs.getInt("PHYFILEID"));
                List<Integer> pfs = lf2pf.get(rs.getInt("LOGFILEID"));
                if(pfs == null) {
                    pfs = new ArrayList<>();
                    lf2pf.put(rs.getInt("LOGFILEID"), pfs);
                }
                pfs.add(rs.getInt("PHYFILEID"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        PhysicalFile pf = new PhysicalFile();
        pf.setComponentId(rs.getInt("PHYFILEID"));
        pf.setName(rs.getString("NAME"));
        pf.setFileType(FileType.fromdbcode(rs.getString("FILETYPECD")));
        pf.setAccessMethod(AccessMethod.fromdbcode(rs.getString("ACCESSMETHODCD")));
        int re = rs.getInt("READEXITID");
        pf.setReadExitID(re);
        if(re > 0) {
            requiredExits.add(re);
        }
        pf.setReadExitIDParm(getDefaultedString(rs.getString("READEXITSTARTUP"), ""));
        pf.setInputDDName(getDefaultedString(rs.getString("DDNAMEINPUT"), ""));
        pf.setDataSetName(getDefaultedString(rs.getString("DSN"), ""));
        pf.setMinimumLength(rs.getShort("MINRECLEN"));
        pf.setMaximumLength(rs.getShort("MAXRECLEN"));
        pf.setOutputDDName(getDefaultedString(rs.getString("DDNAMEOUTPUT"), ""));
        pf.setRecfm(FileRecfm.fromdbcode(getDefaultedString(rs.getString("RECFM"), "INVALID")));
        pf.setDatabaseConnection(getDefaultedString(rs.getString("DBMSSUBSYS"), ""));
        pf.setSqlText(getDefaultedString(rs.getString("DBMSSQL"), ""));
        pf.setDatabaseTable(getDefaultedString(rs.getString("DBMSTABLE"), ""));
        pf.setDatabaseRowFormat(DbmsRowFmtOptId.fromdbcode(getDefaultedString(rs.getString("DBMSROWFMTCD"), "NONE")));
        pf.setIncludeNulls(rs.getBoolean("DBMSINCLNULLSIND"));
		//Make sure these are not null
		pf.setExtractDDName("");
		pf.setDatabase("");
		pf.setFieldDelimiter(FieldDelimiter.FIXEDWIDTH);
		pf.setRecordDelimiter(RecordDelimiter.FIXED);
		pf.setTextDelimiter(TextDelimiter.DOUBLEQUOTE);
        Repository.addPhysicalFileOnly(pf);
    }

    private void addPfsToLfs() {
        for(Entry<Integer, List<Integer>> e : lf2pf.entrySet()) {
            LogicalFile lf = Repository.getLogicalFiles().get(e.getKey());
            Iterator<Integer> pfi = e.getValue().iterator();
            while(pfi.hasNext()) {
                PhysicalFile pf = Repository.getPhysicalFiles().get(pfi.next());
                if(pf.getLogicalFilename() == null) {
                    //Problem is this is not quite right for many to many
                    pf.setLogicalFilename(lf.getName());
                    pf.setLogicalFileId(lf.getID());
                }
                lf.addPF(pf);
            }
        }
    }

    public void addToRepoByName(DatabaseConnection databaseConnection, DatabaseConnectionParams params, String pfName) {
        //Then get the PFs
        String pfRecs = "select * from " + params.getSchema() + ".PHYFILE "
        + "where NAME = '" + pfName + "' and ENVIRONID= " + params.getEnvironmentID() + ";";
        executeAndWriteToRepo(databaseConnection, pfRecs);
     }
}
