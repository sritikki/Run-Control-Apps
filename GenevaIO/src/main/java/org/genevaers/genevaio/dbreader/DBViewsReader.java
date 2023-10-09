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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.enums.OutputMedia;
import org.genevaers.repository.components.enums.ViewStatus;
import org.genevaers.repository.components.enums.ViewType;

public class DBViewsReader extends DBReaderBase {

    String queryBase = "SELECT "
    + "v.ENVIRONID, "
    + "VIEWID, "
    + "NAME, "
    + "EFFDATE, "
    + "VIEWSTATUSCD, "
    + "VIEWTYPECD, "
    + "EXTRACTFILEPARTNBR, "
    + "OUTPUTMEDIACD, "
    + "OUTPUTLRID, "
    + "PAGESIZE, "
    + "LINESIZE, "
    + "ZEROSUPPRESSIND, "
    + "EXTRACTMAXRECCNT, "
    + "EXTRACTSUMMARYIND, "
    + "EXTRACTSUMMARYBUF, "
    + "OUTPUTMAXRECCNT, "
    + "CONTROLRECID, "
    + "WRITEEXITID, "
    + "WRITEEXITSTARTUP, "
    + "FORMATEXITID, "
    + "FORMATEXITSTARTUP, "
    + "FILEFLDDELIMCD, "
    + "FILESTRDELIMCD, "
    + "DELIMHEADERROWIND, "
    + "v.LFPFASSOCID, "
    + "a.PHYFILEID, "
    + "FORMATFILTLOGIC, "
    + "v.CREATEDUSERID, "
    + "v.LASTMODUSERID ";


    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        if(params.getFolderIds().length() > 0) {
            getViewIdsFromFolderIds(dbConnection, params);
        }else if(params.getViewIds().length() > 0) {
            viewIds = params.getViewIds();
            //verify that all views in list exist
            verifyViewsExist(dbConnection, params);
        }
        if(hasErrors == false) {
            addViewsToRepo(dbConnection, params);
        }
        return hasErrors;
    }


    private boolean verifyViewsExist(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        boolean allExist = true;
        String viewsQuery = "select VIEWID from " + params.getSchema() + ".view where ENVIRONID = "
                + params.getEnvironmenID() + " and VIEWID IN(" + params.getViewIds() + ")";
        try {
            ResultSet rs = dbConnection.getResults(viewsQuery);
            List<Integer> views = new ArrayList<>();
            while (rs.next()) {
                views.add(rs.getInt("VIEWID"));
            }
            allExist = checkEachInputIn(params.getViewIds(), views);
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


    private void addViewsToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String viewsQuery = queryBase + " from " + params.getSchema() + ".View v " 
        + "LEFT JOIN " + params.getSchema() + ".LFPFASSOC a ON(v.LFPFASSOCID = a.LFPFASSOCID and v.environid = a.environid) "
        + "where viewid IN(" + viewIds + ") and v.environid = " + params.getEnvironmenID() + ";";

        executeAndWriteToRepo(dbConnection, viewsQuery);
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        //Have to be able to generate this bit...
        ViewDefinition vd = new ViewDefinition();
        vd.setComponentId(rs.getInt("VIEWID"));
        vd.setName(rs.getString("NAME"));
        vd.setStatus(ViewStatus.fromdbcode(rs.getString("VIEWSTATUSCD")));
        // + "OUTPUTLRID, "
        // + "PAGESIZE, "
        // + "LINESIZE, "
        // + "FILEFLDDELIMCD, "
        // + "FILESTRDELIMCD, "
        // + "DELIMHEADERROWIND, "
        // + "FORMATFILTLOGIC, "
        vd.setViewType(ViewType.fromdbcode(rs.getString("VIEWTYPECD")));
        vd.setOutputMedia(OutputMedia.fromdbcode(rs.getString("OUTPUTMEDIACD")));
        vd.setExtractFileNumber((short)rs.getInt("EXTRACTFILEPARTNBR"));
        vd.setMaxExtractSummaryRecords(rs.getInt("EXTRACTSUMMARYBUF"));
        vd.setOutputLrId(rs.getInt("OUTPUTLRID"));
        vd.setDefaultOutputFileId(rs.getInt("PHYFILEID"));
        // this one can be dropped vd.setOutputDestinationId(outputDestinationId);
        // not sure whatvd.setDetailed(outputDetailInd);
        vd.setZeroValueRecordSuppression(rs.getBoolean("ZEROSUPPRESSIND"));
        vd.setGenerateDelimitedHeader(rs.getBoolean("DELIMHEADERROWIND"));
        vd.setExtractMaxRecCount(rs.getInt("EXTRACTMAXRECCNT"));
        vd.setOutputMaxRecCount(rs.getInt("OUTPUTMAXRECCNT"));
        //vd.setProcessAsofDate(processAsofDate);
        //vd.setLookupAsofDate(lookupAsofDate);
        //vd.setFillErrorValue(fillErrorValue);
        //vd.setFillTruncationValue(fillTruncationValue);
        vd.setExtractSummarized(rs.getBoolean("EXTRACTSUMMARYIND"));
        vd.setWriteExitId(rs.getInt("WRITEEXITID"));
        vd.setWriteExitParams(getDefaultedString(rs.getString("WRITEEXITSTARTUP"), ""));
        vd.setFormatExitId(rs.getInt("FORMATEXITID"));
        vd.setFormatExitParams(getDefaultedString(rs.getString("FORMATEXITSTARTUP"), ""));
        vd.setControlRecordId(rs.getInt("CONTROLRECID"));

        //These probably should not be here
        vd.setProcessAsofDate("");
		vd.setLookupAsofDate("");
        vd.setOwnerUser(rs.getString("CREATEDUSERID"));
        vd.setOwnerUser(rs.getString("LASTMODUSERID"));

        currentViewNode = Repository.getViewNodeMakeIfDoesNotExist(vd);
        currentViewNode.setFormatFilterLogic(rs.getString("FORMATFILTLOGIC"));
    }

    private void getViewIdsFromFolderIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        if(foldersExist(dbConnection, params)) {
            getFolderViewIds(dbConnection, params);
            params.setViewIds(viewIds); //update params for future queries
        } else {
            hasErrors = true;
        }
    }

    private static void getFolderViewIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        try {
            List<Integer> views = dbConnection.getViewIdsFromFolderIds(params.getFolderIds());
            if (views.size() > 0) {
                Iterator<Integer> vi = views.iterator();
                viewIds = vi.next().toString();
                while (vi.hasNext()) {
                    viewIds += "," + vi.next().toString();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    private static boolean foldersExist(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        boolean allExist = true;
    
        List<Integer> fldrIds;
        try {
            fldrIds = dbConnection.getExistingFolderIds(params.getFolderIds());
            Iterator<Integer> fi = fldrIds.iterator();
            while(fi.hasNext()) {
                Integer fldrId = fi.next();
                List<String> inputs = Arrays.asList(params.getFolderIds());
                if(inputs.contains(fldrId.toString()) == false) {
                    allExist = false;
                    //Log folder id that is not found
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return allExist;
    }
    
}
