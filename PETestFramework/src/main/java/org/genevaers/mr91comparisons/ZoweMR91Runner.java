package org.genevaers.mr91comparisons;

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


import java.nio.file.Path;

import org.genevaers.utilities.CommandRunner;

public class ZoweMR91Runner implements MR91Runner{
    // An MR91 Runner will need to setup the MR91PARM
    // Maybe Input datasets eg  WBXMLI
    // JCL to run
    // Use Freemarker to help with the JCL generation?
    // run it and get back the spool or job status

    // Working directory to be set too? Or can we write/read via relative paths?

    // Use a generic GEBT.IAN.MR91 as a base address
    // create a JCL 


    private String baseHLQ;

    /**
     * All of these can be set 
     * Path to a WBXMLI directory
     * a DBVIEWS file - or a list of Integers
     * an MR91.JCL file (or should this be generated)
     * a MR91PARM file - again generated?
     */
    private Path mr91DataPath;

    public void setBaseHLQ(String hlq) {
        baseHLQ = hlq;
    }

    public Path getMr91DataPath() {
        return mr91DataPath;
    }

    public void setMr91DataPath(Path mr91Data) {
        this.mr91DataPath = mr91Data;
    }

    @Override
    public void runFrom(Path runDir) {
        // TODO Auto-generated method stub
        
    }
  
}
