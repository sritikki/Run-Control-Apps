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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.JustifyId;

import com.google.common.flogger.FluentLogger;

public class DBLookupsReader extends DBReaderBase{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private String lookups = "";

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        getSourceLookupIds(dbConnection, params);
        if(lookups.length() > 0) {
            logger.atInfo().log("Getting Lookups %s", lookups);
            String query = "SELECT 	distinct "
                + "l.LOOKUPID, "
                + "l.NAME, "
                + "l.SRCLRID as LKUPSRCLR, "
                + "DESTLRLFASSOCID, "
                + "s.STEPSEQNBR, "
                + "s.LOOKUPSTEPID, "
                + "s.SRCLRID as STEPSRCLR, "
                + "s.LRLFASSOCID as STEPLRLF, "
                + "k.KEYSEQNBR, "
                + "k.FLDTYPE, "
                + "K.LRFIELDID, "
                + "k.LRLFASSOCID as KEYLRLF, "
                + "k.VALUEFMTCD, "
                + "k.SIGNED, "
                + "k.VALUELEN, "
                + "k.DECIMALCNT, "
                + "k.FLDCONTENTCD, "
                + "k.ROUNDING, "
                + "k.JUSTIFYCD, "
                + "k.MASK, "
                + "k.SYMBOLICNAME, "
                + "k.VALUE, "
                + "t.LOGRECID, "
                + "t.LOGFILEID "
                + "FROM " + params.getSchema() + ".LOOKUP l "
                + "INNER JOIN " + params.getSchema() + ".LOOKUPSTEP s "
                + "ON(l.LOOKUPID = s.LOOKUPID AND l.environid = s.environid) "
                + "INNER JOIN " + params.getSchema() + ".LOOKUPSRCKEY k "
                + "ON(k.LOOKUPID = s.LOOKUPID AND s.LOOKUPSTEPID = k.LOOKUPSTEPID AND l.environid = k.environid) "
                + "INNER JOIN " + params.getSchema() + ".LRLFASSOC t "
                + "ON(s.LRLFASSOCID = t.LRLFASSOCID AND l.environid = k.environid) "
                + "where l.environid = " + params.getEnvironmenID() + " and l.lookupid in(" + lookups + ") "
                + "ORDER BY l.LOOKUPID, s.STEPSEQNBR, k.KEYSEQNBR; ";
            executeAndWriteToRepo(dbConnection, query);
            return hasErrors;
        } else {
            logger.atInfo().log("No Lookups required");
            return false;
        }
    }

    private void getSourceLookupIds(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query= "SELECT 	DISTINCT LOOKUPID FROM "
            + params.getSchema() + ".VIEWLOGICDEPEND d "
            + "WHERE d.ENVIRONID = " + params.getEnvironmenID() + " AND d.VIEWID in(" + viewIds + ") AND LOOKUPID > 0 "
            + "union "
            +"SELECT DISTINCT SORTTITLELOOKUPID as LOOKUPID FROM "
            + params.getSchema() + ".VIEWCOLUMNSOURCE s "
            + "WHERE s.ENVIRONID = " + params.getEnvironmenID() + " AND s.VIEWID in(" + viewIds + ") AND SORTTITLELOOKUPID > 0";
        
            Set<Integer> lookupIds = new TreeSet<>();
            try {
                ResultSet rs = dbConnection.getResults(query);
                while(rs.next()) {
                    lookupIds.add(rs.getInt("LOOKUPID"));
                }
                Iterator<Integer> li = lookupIds.iterator();
                if(li.hasNext()) {
                    lookups = li.next().toString();
                    while(li.hasNext()) {
                        lookups += "," + li.next().toString();
                    }
                } 
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        LookupPathKey lpKey = new LookupPathKey();
        lpKey.setComponentId(rs.getInt("LOOKUPID"));
        lpKey.setTargetlfid(rs.getInt("LOGFILEID"));
        requiredLFs.add(rs.getInt("LOGFILEID"));
        lpKey.setStepNumber(rs.getShort("STEPSEQNBR"));
        lpKey.setJoinName(rs.getString("NAME"));
        lpKey.setKeyNumber(rs.getShort("KEYSEQNBR"));
        lpKey.setTargetLrId(rs.getInt("LOGRECID"));
        requiredLRs.add(rs.getInt("LOGRECID"));
        lpKey.setSourceLrId(rs.getInt("LKUPSRCLR"));
        requiredLRs.add(rs.getInt("LKUPSRCLR"));
        lpKey.setFieldId(rs.getInt("LRFIELDID"));
        lpKey.setDatatype(DataType.fromdbcode(getDefaultedString(rs.getString("VALUEFMTCD"), "NONE")));
        lpKey.setSigned(rs.getBoolean("SIGNED"));
        lpKey.setStartPosition((short)0);
        lpKey.setFieldLength(rs.getShort("VALUELEN"));
        lpKey.setOrdinalPosition(rs.getShort("KEYSEQNBR"));
        lpKey.setDecimalCount(rs.getShort("DECIMALCNT"));
        lpKey.setRounding(rs.getShort("ROUNDING"));
        lpKey.setDateTimeFormat(DateCode.fromdbcode(getDefaultedString(rs.getString("FLDCONTENTCD"), "NONE")));
        lpKey.setJustification(JustifyId.fromdbcode(getDefaultedString(rs.getString("JUSTIFYCD"), "NONE")));
        lpKey.setMask(getDefaultedString(rs.getString("MASK"), ""));
        lpKey.setValueLength(rs.getShort("VALUELEN"));
        lpKey.setValue(getDefaultedString(rs.getString("VALUE"), ""));
        lpKey.setSymbolicName(getDefaultedString(rs.getString("SYMBOLICNAME"), ""));
        Repository.addLookupPathKey(lpKey);
    }
    
}
