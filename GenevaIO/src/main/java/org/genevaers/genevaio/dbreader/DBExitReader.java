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
import org.genevaers.repository.components.UserExit;
import org.genevaers.repository.components.enums.ExitType;
import org.genevaers.repository.components.enums.ProgramType;

public class DBExitReader extends DBReaderBase {

    @Override
    public boolean addToRepo(DatabaseConnection dbConnection, DatabaseConnectionParams params) {
        requiredExits.remove(0);
        if(requiredExits.size() > 0) {
            String query = "select distinct "
            + "e.exitid, "
            + "e.name, "
            + "moduleid, "
            + "exittypecd, "
            + "programtypecd, "
            + "e.optimizeind "
            + "FROM " + params.getSchema() + ".exit e "
            + "where e.environid = " + params.getEnvironmenID() + " and e.exitid in(" + getIds(requiredExits) + ") ";

            executeAndWriteToRepo(dbConnection, query);
        }
        return hasErrors;
    }
    
    @Override
    protected void addComponentToRepo(ResultSet rs) throws SQLException {
        UserExit ue = new UserExit();
        ue.setComponentId(rs.getInt("EXITID")); 
        ue.setName(rs.getString("NAME"));
        ue.setExecutable(rs.getString("MODULEID"));
        ue.setProgramType(ProgramType.fromdbcode(rs.getString("PROGRAMTYPECD")));
        ue.setExitType(ExitType.fromdbcode(rs.getString("EXITTYPECD")));
        ue.setOptimizable(rs.getBoolean("OPTIMIZEIND"));
        Repository.getUserExits().add(ue, ue.getComponentId(), ue.getName());
    }
}
