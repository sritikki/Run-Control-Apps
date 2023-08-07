package org.genevaers.genevaio.dots;

import com.google.common.flogger.FluentLogger;

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


import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.ViewNode;

public class EventSetDot {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private short srcNumber;
	private LogicalFile lf;
	private LogicalRecord lr;
	private int viewID;

	EventSetDot(short num) {
		srcNumber = num;
	}

	public void setLF(LogicalFile srclf) {
		lf = srclf;		
	}

	public void setLR(LogicalRecord srcLR) {
		lr = srcLR;		
	}
	
	//An Event Set is made up of the Columns dot data
	//and the dot data from the LR fields of the source
	public String getDotString() {
		if(lr != null) {
			logger.atFine().log("Build DOT string for event set for LR %s source %d", lr.getName(), srcNumber);
		}
		String dot = getColumnsDot();
		LogicalRecordDotWriter lrdfw = new LogicalRecordDotWriter();
		String cluster = "SRCLR_" + srcNumber;
		if(lr != null) {
			dot += lrdfw.getDotStringFromLR(cluster, lr, false) + "\n";
		}
		return dot;
	}

	private String getColumnsDot() {
		ViewColumnsDotWriter vcdw = new ViewColumnsDotWriter();
		ViewNode view = Repository.getViews().get(viewID);
		return vcdw.getDotStringFromView(view,  srcNumber) + "\n";
	}

	public void setView(int v) {
		viewID = v;
	}

	public Integer getILFD() {
		if ( lf != null) { 
			return lf.getID();
		} else {
			return null;
		}
	}
}
