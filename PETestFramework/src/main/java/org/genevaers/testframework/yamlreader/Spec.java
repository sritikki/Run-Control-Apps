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

public class Spec {
    
    private String name;
    private String title;
    private String description;
    private String category;
    private String cq;
    private String note;
    private boolean haspassviews;
    private List<GersTest> tests;
    private Result result = new Result();
    private SpecFiles parentSpecFiles;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public List<GersTest> getTests() {
        return tests;
    }
    public void setTests(List<GersTest> tests) {
        this.tests = tests;
    }
    public String getCq() {
        return cq;
    }
    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getPathName() {
        return category + "/" + name;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setHaspassviews(boolean haspassviews) {
        this.haspassviews = haspassviews;
    }

    public boolean isHaspassviews() {
        return haspassviews;
    }

    public void setParent(SpecFiles sf) {
        parentSpecFiles = sf;
    }

    public SpecFiles getParentSpecFiles() {
        return parentSpecFiles;
    }
   
}
