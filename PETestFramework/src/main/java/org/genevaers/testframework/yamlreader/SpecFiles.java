package org.genevaers.testframework.yamlreader;

import java.util.ArrayList;

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

public class SpecFiles {
    private String name;
    private String templateSetName;
    private List<String> specs;
    private List<TemplateSetEntry> jclTemplates = new ArrayList<>();
    private List<TemplateSetEntry> configTemplates = new ArrayList<>();
    private TemplateSet ts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateSetName() {
        return templateSetName;
    }

    public void setTemplateSetName(String templateSet) {
        this.templateSetName = templateSet;
    }

    public List<String> getSpecs() {
        return specs;
    }

    public void setSpecs(List<String> specs) {
        this.specs = specs;
    }

    public List<TemplateSetEntry> getJclTemplates() {
        return jclTemplates;
    }

    public void setJclTemplates(List<TemplateSetEntry> jclTemplates) {
        this.jclTemplates = jclTemplates;
    }

    public List<TemplateSetEntry> getConfigTemplates() {
        return configTemplates;
    }

    public void setConfigTemplates(List<TemplateSetEntry> configTemplates) {
        this.configTemplates = configTemplates;
    }

    public void addTemplateSet(TemplateSet ts) {
        this.ts = ts;
    }

    public void buildTemplateTypeLists() {
        for (TemplateSetEntry tse : ts.getTemplates()) {
            // applyTemplateSetEntryToSpec(tse, spec);
            switch (tse.getType().toUpperCase()) {
                case "JCL":
                    jclTemplates.add(tse); // We don't need the path in the template ... it is inherent
                    break;
                // case "COVERAGEJCL":
                // if (fmEnv.get("GENERATE_COVERAGE").equalsIgnoreCase("Y")) {
                // jclTemplates.add(tse);
                // }
                // break;
                case "CONFIG":
                    configTemplates.add(tse);
                    break;
                // case "COVERAGECONFIG":
                // if (fmEnv.get("GENERATE_COVERAGE").equalsIgnoreCase("Y")) {
                // configTemplates.add(tse);
                // }
                // break;
                // case "FLOWCONFIG":
                // if (fmEnv.get("GENERATE_VDPFLOW").equalsIgnoreCase("Y")) {
                // configTemplates.add(tse);
                // }
                // break;
                default:
                    break;
            }
        }
    }


}
