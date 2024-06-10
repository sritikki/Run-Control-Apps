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
import java.util.List;

public abstract class DatabaseConnection {

    public enum DbType {
        DB2,
        POSTGRES,
        WBCONNECTION
    }

    public abstract void connect() throws SQLException;

    public abstract boolean isConnected();

    public abstract List<Integer> getExistingFolderIds(String folderIds) throws SQLException;

    public abstract List<Integer> getViewIdsFromFolderIds(String folderIds) throws SQLException;

    public abstract PreparedStatement prepareStatement(String query) throws SQLException ;

    public abstract ResultSet getResults(PreparedStatement ps) throws SQLException;

    public abstract Connection getConnection();

    public abstract void closeStatement(PreparedStatement ps) throws SQLException;

    public String getPlaceholders(int size) {
        StringBuilder builder = new StringBuilder();
        if (size > 1) {
            for (int i = 1; i < size; i++) {
                builder.append("?,");
            }
        }
        builder.append("?");
        return builder.toString();
    }

    public String getPlaceholders(String ids) {
        String[] pls = ids.split(",");
        return getPlaceholders(pls.length);
    }

}
