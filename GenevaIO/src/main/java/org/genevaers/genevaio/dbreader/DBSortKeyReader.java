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
import org.genevaers.repository.components.ViewSortKey;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.SortBreakHeaderOption;
import org.genevaers.repository.components.enums.SortOrder;

public class DBSortKeyReader extends DBReaderBase {

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        String query = "select * from " + params.getSchema() + ".viewsortkey "
        + "where environid = " + params.getEnvironmenID() + " and viewid in(" + params.getViewIds()+ ");";

        executeAndWriteToRepo(dbConnection, query);
        return hasErrors;
    }

    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        ViewNode view = Repository.getViews().get(rs.getInt("VIEWID"));
        if(view != null) {
            ViewSortKey vsk = new ViewSortKey();
            vsk.setComponentId(rs.getInt("VIEWSORTKEYID"));
            vsk.setViewSortKeyId(rs.getInt("VIEWSORTKEYID"));
            vsk.setSequenceNumber(rs.getShort("KEYSEQNBR"));
            vsk.setSortorder(SortOrder.fromdbcode(rs.getString("SORTSEQCD")));
            //vsk.setSortBreakFooterOption(sortBreakFooterOption);
            //There is a sort display option here that is not available 
            //Set via the SORTKEYDISPLAYCD
            vsk.setSortBreakHeaderOption(SortBreakHeaderOption.values()[rs.getInt("PAGEBRKIND")]);
            //vsk.setDisplaySubtotalCount(subtotalCountInd);
            vsk.setLabel(getDefaultedString(rs.getString("SORTKEYLABEL"), ""));
            //vsk.setSortDisplay(sortKeyDispOptId);
            vsk.setRtdLrFieldId(rs.getInt("SORTTITLELRFIELDID"));
            //vsk.setRtdJoinId(rtdJoinId);
            vsk.setSortKeyDataType(DataType.fromdbcode(rs.getString("SKFLDFMTCD")));
            vsk.setSortKeySigned(rs.getBoolean("SKSIGNED"));
            vsk.setSkStartPosition(rs.getShort("SKSTARTPOS"));
            vsk.setSkFieldLength(rs.getShort("SKFLDLEN"));
            //vsk.setSkOrdinalPosition(skOrdinalPosition);
            vsk.setSkDecimalCount(rs.getShort("SKDECIMALCNT"));
            //vsk.setSkRounding(skRounding);
            vsk.setSortKeyDateTimeFormat(DateCode.fromdbcode(getDefaultedString(rs.getString("SKFLDCONTENTCD"), "NONE")));
            //vsk.setSkJustifyId(skJustifyId);
            // vsk.setSkOrdinalOffset(skOrdinalOffset);
            // vsk.setSktDataType(sktDataType);
            // vsk.setSktSigned(sktSignedInd);
            // vsk.setSktStartPosition(sktStartPosition);
            // vsk.setSktFieldLength(sktFieldLength);
            // vsk.setSktOrdinalPosition(sktOrdinalPosition);
            // vsk.setSktDecimalCount(sktDecimalCount);
            // vsk.setSktRounding(sktRounding);
            // vsk.setSktDateCode(sktDateCode);
            // vsk.setSktJustifyId(sktJustifyId);
            // vsk.setSktOrdinalOffset(sktOrdinalOffset);
            // vsk.setDescDataType(descDataType);
            // vsk.setDescSigned(descSignedInd);
            // vsk.setDescStartPosition(descStartPosition);
            // vsk.setDescFieldLength(descFieldLength);
            // vsk.setDescOrdinalPosition(descOrdinalPosition);
            // vsk.setDescDecimalCount(descDecimalCount);
            // vsk.setDescRounding(descRounding);
            // vsk.setDescDateCode(descDateCode);
            // vsk.setDescJustifyId(descJustifyId);
            // vsk.setDescOrdinalOffset(descOrdinalOffset);
            // vsk.setPerformBreakLogic(performBreakLogic);
            view.addViewSortKey(vsk);
            } else {
           //Can there ever be an else here... ?
           hasErrors = true;            
        }
    }
    
}
