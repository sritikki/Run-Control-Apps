package ComponentGenerator.model.segments.record.fields;

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


public interface FieldGenerator {
    
    public String getFieldEntry();

    public String getGetAndSetEntry();

    public String getImportString();

    public String getReadEntry();

    public String getFieldNodeEntry(boolean prefix);

    public String getCsvEntry();

    public String getCsvHeaderEntry();

    public String getComponentEntry();

    public String getFillFromComponentEntry();

    public String getFillTheWriteBufferEntry();
    
    public String getDsectType();

}
