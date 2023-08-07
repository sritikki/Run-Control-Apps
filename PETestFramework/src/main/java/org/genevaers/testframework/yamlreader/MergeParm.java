package org.genevaers.testframework.yamlreader;

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

public class MergeParm {
    private List<MergeEntity> entities;
    private String breaklen;
    private String lrbuffer;
    private String extension;
    private String notfound;

    public List<MergeEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MergeEntity> entities) {
        this.entities = entities;
    }

    public String getBreaklen() {
        return breaklen;
    }

    public void setBreaklen(String breaklen) {
        this.breaklen = breaklen;
    }

    public String getLrbuffer() {
        return lrbuffer;
    }

    public void setLrbuffer(String lrbuffer) {
        this.lrbuffer = lrbuffer;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNotfound() {
        return notfound;
    }

    public void setNotfound(String notfound) {
        this.notfound = notfound;
    }


}
