package org.genevaers.testframework.sdsf;

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


import java.util.List;

import org.apache.commons.lang3.RegExUtils;

import com.ibm.zos.sdsf.core.ISFException;
import com.ibm.zos.sdsf.core.ISFRequestSettings;
import com.ibm.zos.sdsf.core.ISFStatus;
import com.ibm.zos.sdsf.core.ISFStatusRunner;

public class TestJobs {
    private final String prefix_;
    private boolean showSDSFMessages = false;
    List<ISFStatus> statObjList = null;
    /**
     * Constructor taking job name prefix.
     * 
     * @param prefix Job name prefix to process
     */
    public TestJobs(final String prefix) {
        this.prefix_ = prefix + "*";
    }

    /**
     * Runs the sample to obtain a list of jobs and list their properties.
     * <p>
     * The job name prefix is defined in the settings to limit the jobs
     * returned. The columns setting is established to limit the properties that
     * are retrieved. A sort is defined so the jobs will be sorted by jobname
     * and jobid.
     * <p>
     * A runner is obtained to access the SDSF ST panel and the list of jobs is
     * obtained. For each job returned, the properties are listed.
     */
    public void run() {

        // Create a request settings
        final ISFRequestSettings settings = new ISFRequestSettings();

        // Filter jobs by job name prefix
        settings.addISFPrefix(prefix_);
        settings.addISFOwner(System.getenv("USER"));

        // Limit the columns returned using the SDSF column (FLD) names
        // rather than the column titles. The interactive COLSHELP
        // command can be used to find the names for each panel.
        settings.addISFCols("jname jobid ownerid retcode status");

        // Sort by jobname and jobid
        settings.addISFSort("jname a jobid a");

        // Use this setting to improve performance since properties will not be
        // changed
        settings.addNoModify();

        // Get a runner used to access the SDSF ST panel
        final ISFStatusRunner runner = new ISFStatusRunner(settings);

        // Get the list of jobs
        try {
            statObjList = runner.exec();
        } catch (ISFException e) {
            // Perform error handling here @P1C
        } finally {
            // List any SDSF messages related to request
            if(showSDSFMessages) {
                runner.getRequestResults().printMessageList(System.out);
            }
        }
    }

    public void show() {
        // Process returned jobs
        if (statObjList != null) {
            for (ISFStatus statObj : statObjList) {
                System.out.println(statObj.toVerboseString());
            }
        }
    }

    public int getNumJobs() {
        return statObjList.size();
    }

    public int purge() {
        int purgeCount = 0;
        if (statObjList != null)  {
            purgeCount = statObjList.size();
            if(purgeCount > 0) {
                ISFStatus first = statObjList.get(0);
                if(purgeCount > 1) {
                    statObjList.remove(0);
                    first.purge(true, statObjList);
                } else {
                    first.purge(true);
                }
            }
        }
        return purgeCount;
    }

    public void setShowSDSFMessages(boolean showSDSFMessages) {
        this.showSDSFMessages = showSDSFMessages;
    }
}
