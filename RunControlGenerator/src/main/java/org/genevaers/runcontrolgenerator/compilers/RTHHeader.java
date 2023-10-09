package org.genevaers.runcontrolgenerator.compilers;

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


import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.enums.OutputMedia;
import org.genevaers.repository.components.enums.ViewStatus;
import org.genevaers.repository.components.enums.ViewType;

public class RTHHeader extends REHHeader{

    private final String NAME ="Reference Title Header (RTH) data";
    private final String DDNAME = "REFRRTH";


    @Override
    public void addView(int viewNum) {
        
        ViewDefinition vd = new ViewDefinition();
        super.rehViewNum = viewNum;
        vd.setComponentId(viewNum);
        vd.setName(NAME);
        vd.setOutputMedia(OutputMedia.FILE);
        vd.setViewType(ViewType.EXTRACT);
        vd.setExtractSummarized(false);
        vd.setStatus(ViewStatus.ACTIVE);
        vd.setWriteExitParams("");
        vd.setFormatExitParams("");
        vn = Repository.getViewNodeMakeIfDoesNotExist(vd);

        makeHeaderLR();

        addColumns();

        makePF(viewNum);
    }


}
