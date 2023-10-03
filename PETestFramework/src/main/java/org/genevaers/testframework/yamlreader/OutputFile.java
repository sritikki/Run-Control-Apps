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


public class OutputFile {
    private String ddname;
    private String filename;
    private String dsn;
    private String basedsn;
    private String workfile;
    private String space;
    private String primary;
    private String secondary;
    private String recfm;
    private String lrecl;
    private String blksize;
    private String compparm = "";
    private String dummy;
    private String comparable = "Y"; //default to yes
    private String startkey;
    private String stopkey;

    public String getDdname() {
        return ddname;
    }

    public void setDdname(String ddname) {
        this.ddname = ddname;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getRecfm() {
        return recfm;
    }

    public void setRecfm(String recfm) {
        this.recfm = recfm;
    }

    public String getLrecl() {
        return lrecl;
    }

    public void setLrecl(String lrecl) {
        this.lrecl = lrecl;
    }

    public String getBlksize() {
        return blksize;
    }

    public void setBlksize(String blksize) {
        this.blksize = blksize;
    }

    public String getDsn() {
        return dsn;
    }

    public void setDsn(String dsn) {
        this.dsn = dsn;
    }

    public String getBasedsn() {
        return basedsn;
    }

    public void setBasedsn(String basedsn) {
        this.basedsn = basedsn;
    }

    public String getWorkfile() {
        return workfile;
    }

    public void setWorkfile(String workfile) {
        this.workfile = workfile;
    }

    public String getCompparm() {
        return compparm;
    }

    public void setCompparm(String compparm) {
        this.compparm = compparm;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public String getComparable() {
        return comparable;
    }

    public void setComparable(String comparable) {
        this.comparable = comparable;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStartkey() {
        return startkey;
    }

    public void setStartkey(String startkey) {
        this.startkey = startkey;
    }

    public String getStopkey() {
        return stopkey;
    }

    public void setStopkey(String stopkey) {
        this.stopkey = stopkey;
    }

}
