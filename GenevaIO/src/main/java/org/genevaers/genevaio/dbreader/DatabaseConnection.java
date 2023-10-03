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
import java.util.List;

public interface DatabaseConnection {

    public enum DbType {
        DB2,
        POSTGRES
    }

    public void connect() throws SQLException;

    public boolean isConnected();

    public List<Integer> getExistingFolderIds(String folderIds) throws SQLException;

    public List<Integer> getViewIdsFromFolderIds(String folderIds) throws SQLException;

    public ResultSet getResults(String viewsQuery) throws SQLException;

}
