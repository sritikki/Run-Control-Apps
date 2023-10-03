package org.genevaers.testframework;

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


import java.nio.file.Path;

public class TestPaths {
    private Path root;
    private Path specDirPath;
    private Path templateSetDir;
    private Path jclPath;
    private Path configPath;
    private Path junitPath;
    private Path eventPath;

    public Path getRoot() {
        return root;
    }

    public void setRoot(Path root) {
        this.root = root;
    }

    public Path getSpecDirPath() {
        return specDirPath;
    }

    public void setSpecDirPath(Path specDirPath) {
        this.specDirPath = specDirPath;
    }

    public Path getTemplateSetDir() {
        return templateSetDir;
    }

    public void setTemplateSetDir(Path templateSetDir) {
        this.templateSetDir = templateSetDir;
    }

    public Path getJclPath() {
        return jclPath;
    }

    public void setJclPath(Path jclPath) {
        this.jclPath = jclPath;
    }

    public Path getConfigPath() {
        return configPath;
    }

    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    public Path getJunitPath() {
        return junitPath;
    }

    public void setJunitPath(Path junitPath) {
        this.junitPath = junitPath;
    }

    public Path getEventPath() {
        return eventPath;
    }

    public void setEventPath(Path eventPath) {
        this.eventPath = eventPath;
    }

}
