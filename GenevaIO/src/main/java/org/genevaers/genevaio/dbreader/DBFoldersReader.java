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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.enums.OutputMedia;
import org.genevaers.repository.components.enums.ViewStatus;
import org.genevaers.repository.components.enums.ViewType;
import org.genevaers.utilities.GersConfigration;
import org.genevaers.utilities.IdsReader;

import com.google.common.flogger.FluentLogger;

public class DBFoldersReader extends DBReaderBase {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static Set<Integer> folderIds;

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        folderIds = IdsReader.getIdsFrom(GersConfigration.DBFLDRS);
        if(folderIds.size() > 0) {
            getViewIdsFromFolderIds(dbConnection, params);
        } else {
            logger.atInfo().log("No folders defined");            
        }
        return hasErrors;
    }



    private boolean verifyViewsExist(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        boolean allExist = true;
        String viewsQuery = "select VIEWID from " + params.getSchema() + ".view where ENVIRONID = "
                + params.getEnvironmentID() + " and VIEWID IN(" + params.getViewIds() + ")";
        try {
            PreparedStatement ps = dbConnection.prepareStatement(viewsQuery);
            ResultSet rs = dbConnection.getResults(ps);
            List<Integer> views = new ArrayList<>();
            while (rs.next()) {
                views.add(rs.getInt("VIEWID"));
            }
            allExist = checkEachInputIn(params.getViewIds(), views);
            dbConnection.closeStatement(ps);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return allExist;
    }

    private boolean checkEachInputIn(String input, List<Integer> views) {
        boolean allExist = true;
        List<String> inputs = Arrays.asList(input.split(","));
        Iterator<String> ii = inputs.iterator();
        while (ii.hasNext()) {
            String inputId = ii.next();
            if (views.contains(Integer.parseInt(inputId)) == false) {
                allExist = false;
                // Log folder id that is not found
                hasErrors = true;
            }
        }
        return allExist;
    }


    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
     }

    private void getViewIdsFromFolderIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        if(foldersExist(dbConnection, params)) {
            getFolderViewIds(dbConnection, params);
            params.setViewIds(viewIdsString); //update params for future queries
        } else {
            hasErrors = true;
        }
    }

    private static void getFolderViewIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        // try {
        //     List<Integer> views = dbConnection.getViewIdsFromFolderIds(params.getFolderIds());
        //     if (views.size() > 0) {
        //         Iterator<Integer> vi = views.iterator();
        //         viewIdsString = vi.next().toString();
        //         while (vi.hasNext()) {
        //             viewIdsString += "," + vi.next().toString();
        //         }
        //     }
        // } catch (SQLException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    private static boolean foldersExist(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        boolean allExist = true;
    
        List<Integer> fldrIds;
//         try {
// //            fldrIds = dbConnection.getExistingFolderIds(folderIds);
// //            Iterator<Integer> fi = fldrIds.iterator();
//             // while(fi.hasNext()) {
//             //     Integer fldrId = fi.next();
//             //     List<String> inputs = Arrays.asList(params.getFolderIds());
//             //     if(inputs.contains(fldrId.toString()) == false) {
//             //         allExist = false;
//             //         //Log folder id that is not found
//             //     }
//             // }
//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
        return allExist;
    }
    
    private void convertToIntergerSet(String viewIdsString) {
        String[] vids = viewIdsString.split(",");
        for(int i=0; i< vids.length; i++) {
            viewIds.add(Integer.valueOf(vids[i]));
        }
    }
}
