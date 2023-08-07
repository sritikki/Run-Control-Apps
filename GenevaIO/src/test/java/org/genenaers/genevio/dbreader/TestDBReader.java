package org.genenaers.genevio.dbreader;

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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.SQLException;

import org.genevaers.genevaio.dbreader.DBReader;
import org.genevaers.repository.Repository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// These tests will require a password to connect to the database
// And so will in require an environment variable to be set with the required password
// And a live database connection.
// So tests to be run manually not as part of the build.
// Then we do not rely on a database as part of the Maven install

public class TestDBReader {


    // This test relies on the following environment variables being set
    // TSO_USERID and TSO_PASSWORD (only set this temporarly with a shell)
    //
    // We also added the db2 jars in main/resources to the local repo
    //  mvn install:install-file -Dfile=db2jcc4.jar -DgroupId=com.ibm -DartifactId=db2jcc4 -Dversion=4 -Dpackaging=jar
    //  mvn install:install-file -Dfile=db2jcc_license_cu.jar -DgroupId=com.ibm -DartifactId=db2jcc_license_cu -Dversion=4 -Dpackaging=jar
    //  mvn install:install-file -Dfile=db2jcc_license_cisuz.jar -DgroupId=com.ibm -DartifactId=db2jcc_license_cisuz -Dversion=4 -Dpackaging=jar

    @Test @Disabled
    public void ReadFolderDB2() throws SQLException, ClassNotFoundException {
        DBReader dbReader = new DBReader();
        dbReader.addViewsToRepository(DBTestHelper.getDB2Params("1599", ""));
        assertFalse(dbReader.hasErrors());
        assertEquals(2, Repository.getViews().size());
    }
    
    @Test @Disabled
    public void ReadViewsDB2() throws SQLException, ClassNotFoundException {
        DBReader dbReader = new DBReader();
        dbReader.addViewsToRepository(DBTestHelper.getDB2Params("", "9403,11373,12078"));
        assertFalse(dbReader.hasErrors());
        assertEquals(3, Repository.getViews().size());
        assertEquals(2, Repository.getViews().get(9403).getNumberOfViewSources());
        assertEquals(1506, Repository.getViews().get(9403).getViewSource((short)1).getSourceLFID());
        assertEquals(9751, Repository.getViews().get(11373).getViewSource((short)1).getOutputPFID());
        assertEquals(4, Repository.getViews().get(9403).getNumberOfColumns());
        assertEquals(2, Repository.getViews().get(9403).getColumnNumber(1).getValuesOfSourcesByNumber().size());
        assertEquals(1, Repository.getViews().get(9403).getNumberOfSortKeys());
        assertEquals(1, Repository.getControlRecords().size());
        assertEquals(2, Repository.getLookups().size());
        assertEquals(6, Repository.getLogicalRecords().size());
        assertEquals(75, Repository.getFields().size());
        assertEquals(1, Repository.getIndexes().size());
        assertEquals(6, Repository.getLogicalFiles().size());
        assertEquals(14, Repository.getPhysicalFiles().size());
        assertEquals(9, Repository.getLogicalFiles().get(217).getNumberOfPFs());
        assertEquals(2, Repository.getUserExits().size());

        //Something wrong here
//        assertEquals(2, repo.getLookup(2495).getNumberOfSteps());
    }
    
    @Test @Disabled
    public void ReadViewsPostgres() throws SQLException, ClassNotFoundException {
        DBReader dbReader = new DBReader();
        dbReader.addViewsToRepository(DBTestHelper.getPostgresParams("", "11037"));
        assertFalse(dbReader.hasErrors());
        assertEquals(1, Repository.getViews().size());
    }
    
    @Test @Disabled
    public void ReadOneColDB2() throws SQLException, ClassNotFoundException {
        DBReader dbReader = new DBReader();
        dbReader.addViewsToRepository(DBTestHelper.getDB2Params("", "9956"));
        assertFalse(dbReader.hasErrors());
        assertEquals(1, Repository.getViews().size());
    }
}
