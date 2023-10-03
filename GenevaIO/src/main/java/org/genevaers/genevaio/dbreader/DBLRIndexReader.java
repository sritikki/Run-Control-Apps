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
import org.genevaers.repository.components.LRIndex;

public class DBLRIndexReader extends DBReaderBase{

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query = "select distinct "
            + "i.lrindexid, "
            + "i.logrecid, "
            + "f.lrindexfldid, "
            + "f.fldseqnbr, "
            + "f.lrfieldid, "
            + "effdatestartfldid, "
            + "effdateendfldid "
            + "from " + params.getSchema() + ".lrindex i "
            + "inner join  " + params.getSchema() + ".lrindexfld f "
            + "on(f.environid = i.environid and f.lrindexid = i.lrindexid) "
            + "where i.ENVIRONID = " + params.getEnvironmenID() + " and i.logrecid in(" + getIds(requiredLRs) + ") "
            + "order by logrecid;";

        executeAndWriteToRepo(dbConnection, query);
    
        return hasErrors;
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        LRIndex lri = new LRIndex();
        lri.setComponentId(rs.getInt("LRINDEXID"));
        lri.setLrId(rs.getInt("LOGRECID"));
        lri.setKeyNumber(rs.getShort("FLDSEQNBR"));
        lri.setEffectiveDateStart(false);
        lri.setEffectiveDateEnd(false);
        if(rs.getInt("EFFDATESTARTFLDID") == 0 && rs.getInt("EFFDATEENDFLDID") == 0) {
            lri.setName("PK");
            lri.setFieldID(rs.getInt("LRFIELDID"));
        } else {
            if(rs.getInt("EFFDATESTARTFLDID") > 0) {
                lri.setFieldID(rs.getInt("EFFDATESTARTFLDID"));
                lri.setEffectiveDateStart(true);
            }
            if(rs.getInt("EFFDATEENDFLDID") > 0) {
                lri.setFieldID(rs.getInt("EFFDATEENDFLDID"));
                lri.setEffectiveDateEnd(true);
            }
        }
        Repository.addLRIndex(lri);
    }
    
}
