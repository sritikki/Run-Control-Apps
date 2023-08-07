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


public class MergeEntity {
    private String name;
    private String ddname;
    private String categories= "";
    private String from= "";
    private String to= "";
    private String type= "";
    private String prelen= "";
    private String keypos= "";
    private String keylen= "";
    private String timpos = "";
    private String efdpos = "";
    private String opt = "";
    private String dup = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDdname() {
        return ddname;
    }

    public void setDdname(String ddname) {
        this.ddname = ddname;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrelen() {
        return prelen;
    }

    public void setPrelen(String prelen) {
        this.prelen = prelen;
    }

    public String getKeypos() {
        return keypos;
    }

    public void setKeypos(String keypos) {
        this.keypos = keypos;
    }

    public String getKeylen() {
        return keylen;
    }

    public void setKeylen(String keylen) {
        this.keylen = keylen;
    }

    public String getTimpos() {
        return timpos;
    }

    public void setTimpos(String timpos) {
        this.timpos = timpos;
    }

    public String getEfdpos() {
        return efdpos;
    }

    public void setEfdpos(String efdpos) {
        this.efdpos = efdpos;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getDup() {
        return dup;
    }

    public void setDup(String dup) {
        this.dup = dup;
    }

}
