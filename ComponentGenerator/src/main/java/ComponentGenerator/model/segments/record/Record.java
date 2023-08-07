package ComponentGenerator.model.segments.record;

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
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import ComponentGenerator.model.segments.record.fields.Field;

public class Record {

    private String recordName;
    private int recordId;
    private String componentName;
    private String description;
    private String recordType;
    private int recordLength;
    private String dsectName;
    private List<Field> fields = new ArrayList<>();

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String record_name) {
        this.recordName = record_name;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordID) {
        this.recordId = recordID;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDsectName() {
        return dsectName != null ? dsectName : recordName.toUpperCase();
    }

    public void setDsectName(String dsect_name) {
        this.dsectName = dsect_name;
    }

    public  List<String> getImports() {
        TreeSet<String> imports = new TreeSet<>();
        for(Field f : fields) {
            String imp = f.getImportString();
            if(imp != null)
                imports.add(imp);
        }
        return new ArrayList<>(imports);
    }
}
