package org.genevaers.repository.jltviews;

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


import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupType;

import com.google.common.flogger.FluentLogger;

public class JLTViewMap<T> {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    Map<JLTViewKey, T> jltViews = new TreeMap<>();

    public T getOrAddJoinifAbsent(LookupType type, int lr, int join) {
        JLTViewKey key = new JLTViewKey(lr, type==LookupType.SKT ? true : false);
        return jltViews.computeIfAbsent(key, k -> (T)makeJTLView(type, lr, join));
    }

    public int getNumberOfJoins() {
        return jltViews.size();
    }

    public T getJLTView(int lr, boolean skt) {
        JLTViewKey key = new JLTViewKey(lr, skt);
        return jltViews.get(key);
    }

    public  Iterator<T> getIterator() {
        return jltViews.values().iterator();
    }

    private JLTView makeJTLView(LookupType type, int lr, int join) {
        JLTView jltv = null;
        switch (type) {
            case NORMAL:
                jltv = new ReferenceJoin(lr, join);
                jltv.setLookupType(LookupType.NORMAL);
                //jltv.setUniqueKey(UniqueKeys.getUniqueKey());
                break;
            case EXIT:
                jltv = new ExitJoin(lr, join);
                //jltv.setUniqueKey(UniqueKeys.getUniqueKey());
                break;
            case EXTERNAL:
                jltv = new ExternalJoin(lr, join);
                //jltv.setUniqueKey(UniqueKeys.getUniqueKey());
                break;
            case SKT:
                jltv = new ReferenceJoin(lr, join);
                jltv.setLookupType(LookupType.SKT);
                //jltv.setUniqueKey(UniqueKeys.getSktUniqueKey());
                break;
            default:
                logger.atSevere().log("JLTView tyoe %s not handled", type.toString());
        }
        return jltv;
    }

    public boolean hasEntries() {
        return jltViews.size() > 0;
    }


    public void log() {
        StringBuilder jltTable = new StringBuilder();
        jltTable.append(String.format("%7s %7s %7s %7s \n","LFid", "LRid",   "LKid", "key"));
        for(  Entry<JLTViewKey, T> jltes : jltViews.entrySet()) {
            JLTView jv = (JLTView) jltes.getValue();
            jltTable.append(String.format("%7d %7d %7s \n", jltes.getKey().getLogicalRecordId(), jv.getOrginalLookupId(), jv.getUniqueKey()));
            jltTable.append("     Ref to RED mapping\n");
            jltTable.append(String.format("     %7s %7s %3s %3s\n", "refID", "redID", "pos", "len"));
            Iterator<Entry<Integer, LRField>> f2ri = jv.getRefToRedIterator();
            while(f2ri.hasNext()) {
                Entry<Integer, LRField> f2r = f2ri.next();
                jltTable.append(String.format("     %7d %7d %3d %3d\n", f2r.getKey(), f2r.getValue().getComponentId(),f2r.getValue().getStartPosition(), f2r.getValue().getLength()));
            }
        }
        System.out.println(jltTable.toString());
    }

    public Set<Entry<JLTViewKey, T>> getEntries() {
        return jltViews.entrySet();
    }

}
