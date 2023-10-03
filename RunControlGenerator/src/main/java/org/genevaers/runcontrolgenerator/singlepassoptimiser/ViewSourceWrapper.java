package org.genevaers.runcontrolgenerator.singlepassoptimiser;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008.
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

import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewSource;

public class ViewSourceWrapper implements Comparable<ViewSourceWrapper> {

    ViewSource vs;

    public ViewSourceWrapper(ViewSource vsIn) {
        vs = vsIn;
    }

    @Override
    public int compareTo(ViewSourceWrapper vsIn) {
        if(vs.getViewId() > vsIn.vs.getViewId()) {
            return 1;
        } else if(vs.getViewId() < vsIn.vs.getViewId()) {
            return -1;
        } else {
            if(vs.getSequenceNumber() > vsIn.vs.getSequenceNumber()) {
                return 1;              
            } else if(vs.getSequenceNumber() < vsIn.vs.getSequenceNumber()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public String getViewSourceString() {
        StringBuilder sb = new StringBuilder();
        sb.append(vs.getViewId() + ":" + vs.getSequenceNumber());
        return sb.toString();
    }

    public String getLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nView Source " + vs.getViewId() + ":" + vs.getSequenceNumber());
        sb.append("\nExtract Filter " + vs.getViewId() + ":" + vs.getSequenceNumber() + "\n");
        if(vs.getExtractFilter().length() == 0) {
            sb.append("None");
        } else {
            sb.append(vs.getExtractFilter());
        }
        Iterator<ViewColumnSource> vcsi = vs.getIteratorForColumnSourcesByNumber();
        while(vcsi.hasNext()) {
            ViewColumnSource vcs = vcsi.next();
            sb.append("\n\nView Column " + vcs.getColumnNumber() + " Logic\n" + vcs.getLogicText());
        }
        sb.append("\n\nExtract Output Logic " + vs.getViewId() + ":" + vs.getSequenceNumber());
        if(vs.getExtractOutputLogic().length() == 0) {
            sb.append("\nNone");
        } else {
            sb.append("\n" + vs.getExtractOutputLogic());
        }
        sb.append("\n-----------------------\n");
        return sb.toString();
    }

    public ViewSource getViewSource() {
        return vs;
    }

}
