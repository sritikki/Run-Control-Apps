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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.genevaers.repository.components.ViewNode;

public abstract class DBReaderBase {

    protected boolean hasErrors = false;
    protected static String viewIds;
    protected static ViewNode currentViewNode;

    protected static Set<Integer> requiredLFs = new TreeSet<>();
    protected static Set<Integer> requiredPFs = new TreeSet<>();
    protected static Set<Integer> requiredLRs = new TreeSet<>();
    protected static Set<Integer> requiredExits = new TreeSet<>();
    protected static Set<Integer> lrlfAssociationIds = new TreeSet<>();
    protected static Set<Integer> lfpfAssociationIds = new TreeSet<>();

    protected void executeAndWriteToRepo(DatabaseConnection dbConnection, String query) {
        try {
            ResultSet rs = dbConnection.getResults(query);
            while(rs.next()) {
                addComponentToRepo(rs);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void executeAndWriteToRepo(Connection dbConnection, String query) {
        try {
            PreparedStatement ps = dbConnection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                addComponentToRepo(rs);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void addComponentToRepo(ResultSet rs) throws SQLException {
    }

    protected static String getDefaultedString(String rsValue, String defVal) {
        return rsValue == null ? defVal : rsValue;
    }
    


    abstract public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params);

    public boolean getHasErrors() {
        return hasErrors;
    }

    public static ViewNode getCurrentViewNode() {
        return currentViewNode;
    }

    protected String getIds(Set<Integer> inputSet) {
        String ids = "";
        inputSet.remove(0);
        if (inputSet.size() > 0) {
            Iterator<Integer> lri = inputSet.iterator();
            ids = lri.next().toString();
            while (lri.hasNext()) {
                ids += "," + lri.next().toString();
            }
        }
        return ids;
    }

}
