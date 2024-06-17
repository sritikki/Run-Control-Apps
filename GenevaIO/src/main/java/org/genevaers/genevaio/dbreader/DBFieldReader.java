package org.genevaers.genevaio.dbreader;

import java.sql.Connection;

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
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.JustifyId;


public class DBFieldReader extends DBReaderBase{

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query = "select "
            + "f.LRFIELDID, "
            + "f.LOGRECID, "
            + "f.NAME, "
            + "DBMSCOLNAME, "
            + "FIXEDSTARTPOS, "
            + "ORDINALPOS, "
            + "ORDINALOFFSET, "
            + "FLDFMTCD, "
            + "SIGNEDIND, "
            + "MAXLEN, "
            + "DECIMALCNT, "
            + "ROUNDING, "
            + "FLDCONTENTCD, "
            + "JUSTIFYCD "
            + "from " + params.getSchema() + ".LRFIELD f "
            + "INNER JOIN " + params.getSchema() + ".LRFIELDATTR a "
            + "ON a.LRFIELDID = f.LRFIELDID and a.ENVIRONID = f.ENVIRONID "
            + "where f.ENVIRONID = ? and f.logrecid in(" + getPlaceholders(requiredLRs.size()) + ") ";
        
        executeAndWriteToRepo(dbConnection, query, params, requiredLRs);
        return hasErrors;
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        LRField lrf = new LRField();
        lrf.setComponentId(rs.getInt("LRFIELDID"));
        lrf.setLrID(rs.getInt("LOGRECID"));
        lrf.setName(rs.getString("NAME"));
        lrf.setStartPosition(rs.getShort("FIXEDSTARTPOS"));
        lrf.setOrdinalPosition(rs.getShort("ORDINALPOS"));
        lrf.setOrdinalOffset(rs.getShort("ORDINALOFFSET"));
        lrf.setDatatype(DataType.fromdbcode(rs.getString("FLDFMTCD")));
        lrf.setSigned(rs.getInt("SIGNEDIND") == 0 ? false : true);
        lrf.setLength(rs.getShort("MAXLEN"));
        lrf.setNumDecimalPlaces(rs.getShort("DECIMALCNT"));
        lrf.setRounding(rs.getShort("ROUNDING"));
        lrf.setDateTimeFormat(DateCode.fromdbcode(getDefaultedString(rs.getString("FLDCONTENTCD"), "NONE")));
        if (lrf.getDatatype() == DataType.ALPHANUMERIC) {
            lrf.setJustification(JustifyId.LEFT);
        } else {
            lrf.setJustification(JustifyId.RIGHT);
        }
        //lrf.setJustification(JustifyId.fromdbcode(getDefaultedString(rs.getString("JUSTIFYCD"), "NONE")));
        lrf.setMask("");  //These probably should not be here at all
        lrf.setDbColName("");
        Repository.addLRField(lrf);
    }

    public void addLRToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params, int environmentID, int sourceLR) {
                String query = "select "
                + "f.LRFIELDID, "
                + "f.LOGRECID, "
                + "f.NAME, "
                + "DBMSCOLNAME, "
                + "FIXEDSTARTPOS, "
                + "ORDINALPOS, "
                + "ORDINALOFFSET, "
                + "FLDFMTCD, "
                + "SIGNEDIND, "
                + "MAXLEN, "
                + "DECIMALCNT, "
                + "ROUNDING, "
                + "FLDCONTENTCD, "
                + "JUSTIFYCD "
                + "from " + params.getSchema() + ".LRFIELD f "
                + "INNER JOIN " + params.getSchema() + ".LRFIELDATTR a "
                + "ON a.LRFIELDID = f.LRFIELDID and a.ENVIRONID = f.ENVIRONID "
                + "where f.ENVIRONID = ? and f.logrecid = ?;";
                executeAndWriteToRepo(dbConnection, query, params, sourceLR);
    }
    
}
