package org.genevaers.repository.jltviews;

import java.util.Map;
import java.util.TreeMap;

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


public class UniqueKeys {
    private static int base = 1;
    private static int sktbase = 1;

    static Map<String, UniqueKeyData> keysMap = new TreeMap<String, UniqueKeyData>();

    public static UniqueKeyData getOrMakeUniuUniqueKeyData(String key, int oldJoin) {
        UniqueKeyData ukd = keysMap.computeIfAbsent(key, k->makeUKDEntry(oldJoin));
        return ukd;
    }

    private static UniqueKeyData makeUKDEntry(int oldJoin) {
        return new UniqueKeyData(oldJoin, base++);
    }

    public static void reset() {
        base = 1;
        sktbase = 1;
        keysMap.clear();
    }


}
