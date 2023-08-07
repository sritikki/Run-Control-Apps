package ComponentGenerator.model.segments.enums;

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


import java.util.ArrayList;
import java.util.List;

public class GenevaEnum {
    private String name;
    private String asmPrefix;
    private List<String> dbcodes = new ArrayList<>();
    private List<String> equates = new ArrayList<>();
    private String description;
    private String componentField;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsmPrefix() {
        return asmPrefix;
    }

    public void setAsmPrefix(String asmPrefix) {
        this.asmPrefix = asmPrefix;
    }

    public List<String> getDbcodes() {
        return dbcodes;
    }

    public void setDbcodes(List<String> dbcodes) {
        this.dbcodes = dbcodes;
    }

    public List<String> getEquates() {
        return equates;
    }

    public void setEquates(List<String> equates) {
        this.equates = equates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComponentField() {
        return componentField;
    }

    public void setComponentField(String componentField) {
        this.componentField = componentField;
    }

}
