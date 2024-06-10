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



public class DatabaseConnectionParams {
    // What does a connection need

    DatabaseConnection.DbType dbType;
    String schema;
    String environmentID;
    String username;
    String password;
    String port;
    String server;
    String database;
    String folderIds = "";
    String viewIds = "";

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getEnvironmentID() {
        return environmentID;
    }

    public int getEnvironmentIdAsInt() {
        return Integer.valueOf(environmentID);
    }

    public void setEnvironmentID(String environmentID) {
        this.environmentID = environmentID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public DatabaseConnection.DbType getDbType() {
        return dbType;
    }

    public void setDbType(DatabaseConnection.DbType dbType) {
        this.dbType = dbType;
    }

    public String getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(String folderIds) {
        this.folderIds = folderIds;
    }

    public String getViewIds() {
        return viewIds;
    }

    public void setViewIds(String viewIds) {
        this.viewIds = viewIds;
    }

    
}
