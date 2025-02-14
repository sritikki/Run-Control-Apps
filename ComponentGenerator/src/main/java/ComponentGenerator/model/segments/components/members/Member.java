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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/*
 * Make this a base type
 * Derived will be base on type
 * short integer string enum boolean map existing byte
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IntegerMember.class, name = "integer"),
        @JsonSubTypes.Type(value = ShortMember.class, name = "short"),
        @JsonSubTypes.Type(value = StringMember.class, name = "string"),
        @JsonSubTypes.Type(value = EnumMember.class, name = "enum"),
        @JsonSubTypes.Type(value = BooleanMember.class, name = "boolean"),
        @JsonSubTypes.Type(value = MapMember.class, name = "map"),
        @JsonSubTypes.Type(value = ExistingMember.class, name = "existing"),
        @JsonSubTypes.Type(value = ByteMember.class, name = "byte")
})
public abstract class Member implements MemberGenerator {

    protected String name;
    private String vdpSource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVdpSource() {
        return vdpSource;
    }

    public void setVdpSource(String vdpSource) {
        this.vdpSource = vdpSource;
    }

    public String getImportString() {
        return null;
    }

    protected String defaultFieldEntryForType(String t) {
        return "    private " + t + " " + name + ";";
    }

    protected String getAndSetEntryForType(String type) {
        StringBuilder gasEntry = new StringBuilder();
        String ucName = StringUtils.capitalize(name);
        gasEntry.append("    public " + type + " get" + ucName + "() {\n");
        gasEntry.append("        return " + name + ";\n    }\n\n");
        gasEntry.append("    public void set" + ucName + "(" + type + " " + name + ") {\n");
        gasEntry.append("        this." + name + " = " + name + ";\n    }\n");
        return gasEntry.toString();
    }

}
