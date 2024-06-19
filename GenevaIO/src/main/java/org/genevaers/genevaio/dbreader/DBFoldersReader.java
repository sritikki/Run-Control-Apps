package org.genevaers.genevaio.dbreader;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetUtils.SetView;
import org.genevaers.utilities.GersConfigration;
import org.genevaers.utilities.IdsReader;

import com.google.common.flogger.FluentLogger;

public class DBFoldersReader extends DBReaderBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static Set<Integer> folderIds;
    private static Set<Integer> folderIdsFound = new HashSet<>();
    private static List<String> linesRead = new ArrayList<>();;

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        folderIds = IdsReader.getIdsFrom(GersConfigration.DBFLDRS);
        linesRead.addAll(IdsReader.getLinesRead());
        if(folderIds.size() > 0) {
            getViewIdsFromFolderIds(dbConnection, params);
        } else {
            logger.atInfo().log("No folders defined");            
        }
        return hasErrors;
    }



    private void getFolderViewIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String viewsQuery = "select VIEWID, VIEWFOLDERID from " + params.getSchema() + ".vfvassoc where ENVIRONID = ?"
                + " and VIEWFOLDERID IN(" + getPlaceholders(folderIds.size()) + ") ORDER BY VIEWID ASC, VIEWFOLDERID ASC ";
        try (PreparedStatement ps = dbConnection.prepareStatement(viewsQuery);) {
            int parmNum = 1;
            ps.setInt(parmNum++, params.getEnvironmentIdAsInt());
            Iterator<Integer> fi = folderIds.iterator();
            while (fi.hasNext()) {
                ps.setInt(parmNum++, fi.next());
            }
            ResultSet rs = dbConnection.getResults(ps);
            while (rs.next()) {
                viewIds.add(rs.getInt("VIEWID"));
                folderIdsFound.add(rs.getInt("VIEWFOLDERID"));
            }
        } catch (SQLException e) {
            logger.atSevere().log("getFolderViewIds failed %s", e.getMessage());
        }
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
    }

    private void getViewIdsFromFolderIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        getFolderViewIds(dbConnection, params);
        SetView<Integer> diff = SetUtils.difference(folderIds, folderIdsFound);
        if(diff.size() > 0) {
            hasErrors = true;
            logger.atSevere().log("Not all folders found");
            logMissingFolders(diff);
        } else {
            hasErrors = false;
        }
    }

    private void logMissingFolders(SetView<Integer> diff) {
        Iterator<Integer> di = diff.iterator();
        while (di.hasNext()) {
            logger.atSevere().log("folder %d missing", di.next());
        }
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public static List<String> getLinesRead() {
        if(linesRead.isEmpty()) {
            linesRead.add("<none>");
        }
        return linesRead;
    }

}
