package ComponentGenerator.model;

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


import com.google.common.flogger.FluentLogger;

import ComponentGenerator.model.segments.components.Component;
import ComponentGenerator.model.segments.components.members.Member;

public class ComponentWalker {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    
    /**
     * Build an import list
     * A field declaration list
     * A getter and setter list (pairs)
     * 
     * Would this be made easier if the Members objects we made from a factory
     * based on Types? Save all the ifs?
     * But can Jackson make different types? How?
     * 
     */
    public void buildEntryStrings(Component comp) {
        logger.atInfo().log("Processing Component %s", comp.getComponentName());
        for(Member m : comp.getMembers()) {
            logger.atInfo().log("Member %s", m.getName());
        }

    }
}
