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

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;

public class DBViewSourceReader extends DBReaderBase{

    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
 
        String query = "select "
            + "VIEWSOURCEID, "
            + "v.INLRLFASSOCID, "
            + "VIEWID, "
            + "LOGRECID, "
            + "i.LOGFILEID as lfin, "
            + "SRCSEQNBR, "
            + "EXTRACTFILTLOGIC, "
            + "EXTRACTOUTPUTLOGIC, "
            + "WRITEEXITID, "
            + "WRITEEXITPARM, "
            + "o.LOGFILEID as lfout, "
            + "PHYFILEID "
            + "from " + params.getSchema() + ".VIEWSOURCE v "
            + "LEFT JOIN " + params.getSchema() + ".LRLFASSOC i "
            + "ON v.INLRLFASSOCID = i.LRLFASSOCID and v.ENVIRONID = i.ENVIRONID "
            + "LEFT JOIN " + params.getSchema() + ".LFPFASSOC o "
            + "ON v.OUTLFPFASSOCID = o.LFPFASSOCID and v.ENVIRONID = o.ENVIRONID "
            + "where v.ENVIRONID = " + params.getEnvironmenID() + " and VIEWID in(" + params.getViewIds() + "); ";
    
            executeAndWriteToRepo(dbConnection, query);

            return hasErrors;
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        ViewNode view = Repository.getViews().get(rs.getInt("VIEWID"));
        if(view != null) {
            ViewSource vs = new ViewSource();
            vs.setComponentId(rs.getInt("VIEWSOURCEID"));
            vs.setSequenceNumber(rs.getShort("SRCSEQNBR"));
            vs.setSourceLFID(rs.getInt("LFIN"));
            requiredLFs.add(rs.getInt("LFIN"));
            vs.setViewId(rs.getInt("VIEWID"));
            vs.setSourceLRID(rs.getInt("LOGRECID"));
            requiredLRs.add(rs.getInt("LOGRECID"));
            vs.setExtractFilter(rs.getString("EXTRACTFILTLOGIC"));
            vs.setExtractOutputLogic(rs.getString("EXTRACTOUTPUTLOGIC"));
            vs.setWriteExitId(rs.getInt("WRITEEXITID"));
            requiredExits.add(rs.getInt("WRITEEXITID"));
            vs.setWriteExitParams(rs.getString("WRITEEXITPARM"));
            vs.setOutputLFID(rs.getInt("LFOUT"));
            requiredLFs.add(rs.getInt("LFOUT"));
            vs.setOutputPFID(rs.getInt("PHYFILEID"));
            requiredPFs.add(rs.getInt("PHYFILEID"));
            view.addViewSource(vs);
        } else {
            //Log this
            hasErrors = true;
        }
    }
    
}
