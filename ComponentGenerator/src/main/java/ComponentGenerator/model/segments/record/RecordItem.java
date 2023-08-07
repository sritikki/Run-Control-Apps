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


import ComponentGenerator.model.Item;

public class RecordItem extends Item {

    private Boolean recordPart = false;

    private RecordItem recordItem;
    private Record record;

    public Boolean getRecordPart() {
        return recordPart;
    }

    public void setRecordPart(Boolean recordPart) {
        this.recordPart = recordPart;
    }

    public RecordItem getRecordItem() {
        return recordItem;
    }

    public void setRecordItem(RecordItem recordItem) {
        this.recordItem = recordItem;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

}
