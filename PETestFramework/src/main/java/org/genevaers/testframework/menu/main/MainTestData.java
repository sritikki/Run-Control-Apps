package org.genevaers.testframework.menu.main;

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


import org.genevaers.testframework.yamlreader.GersTest;
import org.genevaers.testframework.yamlreader.Spec;

public class MainTestData {

    private static String  testOrSpecToRun;
    private static GersTest testToRun;
    private static Spec specToRun;

    public static String getTestOrSpecToRun() {
        return testOrSpecToRun;
    }

    public static void setTestOrSpecToRun(String tors) {
        testOrSpecToRun = tors;
    }

    public static void setTestToRun(GersTest t) {
        testToRun = t;
    }

    public static GersTest getTestToRun() {
        return testToRun;
    }

    public static void setSpecToRun(Spec s) {
        specToRun = s;
    }

    public static Spec getSpecToRun() {
        return specToRun;
    }
    
}
