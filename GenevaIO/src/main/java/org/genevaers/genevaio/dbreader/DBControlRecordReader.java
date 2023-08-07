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
import org.genevaers.repository.components.ControlRecord;

public class DBControlRecordReader extends DBReaderBase {

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query = "select distinct c.CONTROLRECID, "
            + "c.NAME, "
            + "FIRSTMONTH, "
            + "LOWVALUE, "
            + "HIGHVALUE, "
            + "c.COMMENTS "
            + "from " + params.getSchema() + ".CONTROLREC c INNER JOIN " + params.getSchema() + ".VIEW v "
            + "ON (v.CONTROLRECID = c.CONTROLRECID and c.ENVIRONID = v.ENVIRONID) "
            + "where viewid IN(" + viewIds
            + ") and c.ENVIRONID = " + params.getEnvironmenID() + ";";

            executeAndWriteToRepo(dbConnection, query);
            return hasErrors;
    }
    
    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        ControlRecord cr = new ControlRecord();
        cr.setComponentId(rs.getInt("CONTROLRECID"));
        cr.setName(rs.getString("NAME"));
        cr.setFirstFiscalMonth(rs.getShort("FIRSTMONTH"));
        cr.setBeginningPeriod(rs.getShort("LOWVALUE"));
        cr.setEndingPeriod(rs.getShort("HIGHVALUE"));
        cr.setDescription(getDefaultedString(rs.getString("COMMENTS"), ""));
        Repository.getControlRecords().add(cr, cr.getComponentId(), cr.getName());
    }
}
