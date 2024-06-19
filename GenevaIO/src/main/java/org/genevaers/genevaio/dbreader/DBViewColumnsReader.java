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
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.JustifyId;
import org.genevaers.repository.components.enums.SubtotalType;

public class DBViewColumnsReader extends DBReaderBase{

    //Looks like we can extract the reader to a base class
    //and each reader implements an addCompenentToRepo function
    // Which could be generated?
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query = "select * from " + params.getSchema() + ".viewcolumn "
        + "where environid = ? and viewid in(" + getPlaceholders(viewIds.size())
        + ") order by columnnumber;";
        executeAndWriteToRepo(dbConnection, query, params, viewIds);
        return hasErrors;
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        ViewNode view = Repository.getViews().get(rs.getInt("VIEWID"));
        if(view != null) {
            ViewColumn vc = new ViewColumn();
            vc.setComponentId(rs.getInt("VIEWCOLUMNID"));
            vc.setViewId(rs.getInt("VIEWID"));
            vc.setColumnNumber(rs.getInt("COLUMNNUMBER"));
            vc.setDataType(DataType.fromdbcode(rs.getString("FLDFMTCD")));
            if(vc.getDataType() == DataType.ALPHANUMERIC) {
                vc.setJustifyId(JustifyId.LEFT);
            } else {
                vc.setJustifyId(JustifyId.RIGHT);
            }
            vc.setSigned(rs.getInt("SIGNEDIND") == 0 ? false : true);
            vc.setStartPosition(rs.getShort("STARTPOSITION"));
            vc.setFieldLength(rs.getShort("MAXLEN"));
            vc.setOrdinalPosition(rs.getShort("ORDINALPOSITION"));
            vc.setDecimalCount(rs.getShort("DECIMALCNT"));
            vc.setRounding(rs.getShort("ROUNDING"));
            vc.setDateCode(DateCode.fromdbcode(getDefaultedString(rs.getString("FLDCONTENTCD"), "NONE")));
            String jfy = rs.getString("JUSTIFYCD");
            if(jfy != null) {
                vc.setJustifyId(JustifyId.fromdbcode(jfy));
            }
            vc.setHidden(rs.getInt("VISIBLE") == 0 ? true : false);
            vc.setSubtotalType(SubtotalType.fromdbcode(getDefaultedString(rs.getString("SUBTOTALTYPECD"), "NONE")));
            vc.setSpacesBeforeColumn(rs.getShort("SPACESBEFORECOLUMN"));
            vc.setExtractArea(ExtractArea.fromdbcode(rs.getString("EXTRACTAREACD")));
            vc.setExtractAreaPosition(rs.getShort("EXTRAREAPOSITION"));
            vc.setSubtotalPrefix(getDefaultedString(rs.getString("SUBTLABEL"), ""));
            vc.setReportMask(getDefaultedString(rs.getString("RPTMASK"), ""));
            vc.setHeaderJustifyId(JustifyId.fromdbcode(getDefaultedString(rs.getString("HDRJUSTIFYCD"), "CNTER")));
            vc.setHeaderLine1(getDefaultedString(rs.getString("HDRLINE1"), ""));
            vc.setHeaderLine2(getDefaultedString(rs.getString("HDRLINE2"), ""));
            vc.setHeaderLine3(getDefaultedString(rs.getString("HDRLINE3"), ""));
            vc.setColumnCalculation(getDefaultedString(rs.getString("FORMATCALCLOGIC"), ""));

            vc.setName("Column Number " + vc.getColumnNumber());
            //Candidates for removal?
            vc.setFieldName("");
            vc.setDetailPrefix("");
            vc.setSubtotalMask("");
            vc.setDefaultValue("");
    
            view.addViewColumn(vc);
        } else {
            //Can there ever be an else here... ?
            hasErrors = true;
        }

    }

}
