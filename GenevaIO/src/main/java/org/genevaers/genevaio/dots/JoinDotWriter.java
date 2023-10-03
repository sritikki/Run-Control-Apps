package org.genevaers.genevaio.dots;

import com.google.common.flogger.FluentLogger;

import org.genevaers.genevaio.ltfile.ArgHelper;

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


import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.LookupPath;

public class JoinDotWriter extends DotWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private LogicTableF1 joinRec;
	private LogicalRecord logicalRecord;
	private boolean detailed = false;
	private short jsource;

	private StringBuilder jDot;

	public void setRefLR(LogicalRecord lr) {
		if(lr == null)
			logger.atSevere().log("JoinDotWriter Setting null Lr");
		logicalRecord = lr;
	}

	public String getDotString() {
		String rstr = "";
		if (joinRec != null) {
			LookupPath lkup = Repository.getLookups().get(joinRec.getColumnId());
			if (lkup != null) {
				logger.atFine().log("Build DOT string for lookup %s", lkup.getName());
				// Want to map lookup ID to Join ID
				// Lookup Gen record?
				String joinNumber = ArgHelper.getArgString(joinRec.getArg());
				String Jsource = "J" + joinNumber + "_" + jsource;
				// vdp.getLookupTargetSet().

				if (logicalRecord != null) {

					rstr = addSubgraph(Jsource + "_name", lkup.getName() + " (" + joinRec.getColumnId() + ")");
					rstr += addSubgraph(Jsource, "Join" + joinNumber);

					rstr += Jsource + "[";
					rstr += "label=<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">\n";
					rstr += "<TR><TD>" + logicalRecord.getName()
							+ "</TD></TR>\n";
					if (logicalRecord.getLookupExit() != null) {
						rstr += "<TR><TD>Exit</TD></TR>\n";
						rstr += "<TR><TD>" + logicalRecord.getLookupExit().getName() + "</TD></TR>\n";
					}
					rstr += "</TABLE>>]\n";

					LogicalRecordDotWriter lrdfw = new LogicalRecordDotWriter();
					if (logicalRecord.getLookupExit() == null) {
						String cluster = Jsource;
						if (detailed) {
							rstr += lrdfw.getDotStringFromLR(cluster, logicalRecord, false)
									+ "\n";
						}
					}
					rstr += "}\n";
					rstr += "}\n";
				} else {
					System.out.println("LR null");
				}
			} else {
				logger.atSevere().log("No lookup found");
			}
		} else {
			logger.atInfo().log("No join record");
		}
		return rstr;
	}
	
	public void setJoinRecord(LogicTableF1 join) {
		joinRec = join;		
	}

	public String getDotStringFromView() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDetailed(boolean b) {
		detailed  = b;
	}

	public void setSource(short currentViewSource) {
		jsource = currentViewSource;		
	}

}
