package ComponentGenerator.model.segments.components.members;

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


import org.apache.commons.lang3.StringUtils;

public class BooleanMember extends Member {

    private static final String TYPE = "boolean";

    @Override
    public String getFieldEntry() {
        return defaultFieldEntryForType(TYPE);
    }

    @Override
    public String getGetAndSetEntry() {
        StringBuilder gasEntry = new StringBuilder();
        String ucName = StringUtils.capitalize(name);
        gasEntry.append("    public " + TYPE + " is" + ucName + "() {\n");
        gasEntry.append("        return " + name + ";\n    }\n\n");
        gasEntry.append("    public void set" + ucName + "(" + TYPE + " " + name + ") {\n");
        gasEntry.append("        this." + name + " = " + name + ";\n    }\n");
        return gasEntry.toString();
    }

}
