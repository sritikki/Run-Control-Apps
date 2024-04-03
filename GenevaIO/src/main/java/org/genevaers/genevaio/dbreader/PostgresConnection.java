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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PostgresConnection implements DatabaseConnection {

    private DatabaseConnectionParams params;
    private Connection con;

    public PostgresConnection(DatabaseConnectionParams params) {
        this.params = params;
    }

    @Override
    public void connect() throws SQLException {
        String url = "jdbc:postgresql://" 
        + params.getServer() +":" + params.getPort() + "/" + params.getDatabase()
        + "?user=" + params.getUsername() 
        + "&password=" +params.getPassword() 
        + "&ssl=false"
        + "&currentSchema=" + params.getSchema();
        con  = DriverManager.getConnection(url);
    }
    
    @Override
    public boolean isConnected() {
        boolean retval = false;
        try {
            retval =  con.isValid(10);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return retval;
    }

    @Override
    public List<Integer> getExistingFolderIds(String folderIds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Integer> getViewIdsFromFolderIds(String folderIds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet getResults(String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        return ps.executeQuery();
    }

    @Override
    public Connection getConnection() {
        return con;
    }

}
