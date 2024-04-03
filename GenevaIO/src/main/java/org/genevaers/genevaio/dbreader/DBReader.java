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


import java.sql.SQLException;
import java.util.List;

import org.genevaers.repository.Repository;

public class DBReader {
 
    private List<Integer> viewIds;
    private DatabaseConnection dbConnection;
    private DatabaseConnectionParams params;
    private boolean hasErrors = false;
    DBViewsReader viewsReader = new DBViewsReader();
    DBViewColumnsReader viewColumnsReader = new DBViewColumnsReader();
    DBViewSourceReader viewSourceReader = new DBViewSourceReader();
    DBViewColumnSourceReader viewColumnSourceReader = new DBViewColumnSourceReader();
    DBSortKeyReader sortKeyReader = new DBSortKeyReader();
    DBControlRecordReader controlRecordReader = new DBControlRecordReader();
    DBLookupsReader lookupsReader = new DBLookupsReader();
    DBLogicalRecordReader logicalRecordReader = new DBLogicalRecordReader();
    DBFieldReader fieldReader = new DBFieldReader();
    DBLRIndexReader lrIndexReader = new DBLRIndexReader();
    DBLogicalFileReader logicalFileReader = new DBLogicalFileReader();
    DBPhysicalFileReader physicalFileReader = new DBPhysicalFileReader();
    DBExitReader exitReader = new DBExitReader();

    public List<Integer> getViewIds() {
        return viewIds;
    }

    public void addViewsToRepository(DatabaseConnectionParams pms) {
        params = pms;
        try {
            setConnectionType(params);
            dbConnection.connect();
            hasErrors = viewsReader.addToRepo(dbConnection, params);
            hasErrors |= viewSourceReader.addToRepo(dbConnection, params);
            hasErrors |= viewColumnsReader.addToRepo(dbConnection, params);
            hasErrors |= viewColumnSourceReader.addToRepo(dbConnection, params);
            hasErrors |= sortKeyReader.addToRepo(dbConnection, params);
            hasErrors |= controlRecordReader.addToRepo(dbConnection, params);
            hasErrors |= lookupsReader.addToRepo(dbConnection, params);
            hasErrors |= logicalRecordReader.addToRepo(dbConnection, params);
            hasErrors |= fieldReader.addToRepo(dbConnection, params);
            hasErrors |= lrIndexReader.addToRepo(dbConnection, params);
            hasErrors |= logicalFileReader.addToRepo(dbConnection, params);
            hasErrors |= physicalFileReader.addToRepo(dbConnection, params);
            hasErrors |= exitReader.addToRepo(dbConnection, params);
            } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** 
     * Funtion to aid the workbench compiler
     * Problem is that the view source may not have been saved
     * Therefore just want the LR?
     * 
     * WB will supply a DataProvider - which is just a wrapper for the database connection
     * 
     */
    // public boolean loadViewSource(int envID, int view, int sourceNumber) {

    // }

    private DatabaseConnection setConnectionType(DatabaseConnectionParams params) throws ClassNotFoundException {
        switch(params.getDbType()) {
            case DB2:
            dbConnection = new DB2Connection(params);
            break;
            case POSTGRES:
            dbConnection = new PostgresConnection(params);
            break;
        }
        return null;
    }

    public boolean hasErrors() {
        return hasErrors;
    }


    
}
