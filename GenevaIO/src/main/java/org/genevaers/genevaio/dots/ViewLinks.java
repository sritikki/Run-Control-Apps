package org.genevaers.genevaio.dots;

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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewLinks {
	private int view;
	private List<String> links = new ArrayList<String>();
	
	/*
	 * To avoid multiple parallel lines collect the end points in a unique map
	 * Then generate the link
	 * 
	 */
	public void addFieldLink(short viewSource, int src, int trg, boolean SK){
		String link = "SRCLR_" + viewSource + ":LRF_" + src + "_R -> ";
		if(trg==0) {
			link += "EXTRF_" + viewSource;
		} else {
			link += "COLS_" + viewSource + ":COL_" + trg;			
		}
		if(SK) {
			link += " [color = \"red\"]\n";
		} else {
			link += "\n";
		}
		if(links.contains(link) == false)
			links.add(link);
	}

	public Iterator<String> getIterator() {
		return links.iterator();
	}

	public void addFieldLookupKeyLink(short viewSource, int lrfieldID, String currentJoinNumber) {
		String link = "SRCLR_" + viewSource + ":LRF_" + lrfieldID + "_R -> ";
			link += "J" + currentJoinNumber + "_" + viewSource;
			link += " [color = \"green\"]\n";
		if(links.contains(link) == false) {
			links.add(link);
		}
	}

	public void addRefFieldLink(short viewSource, String joinNumber, int lrfieldID, int trg,	boolean SK, boolean inFilterList) {
		if(inFilterList) {
			String link = "J" + joinNumber + "_" + viewSource;
			link += ":LRF_" + lrfieldID+ "_R";
			link += " -> ";			
		
			if(trg==0) {
				link += "EXTRF_" + joinNumber;
			} else {
				link += "COLS_" + viewSource + ":COL_" + trg;			
			}
			if(SK) {
				link += " [color = \"red\"]\n";
			} else {
				link += " [color = \"green\"]\n";
			}
			if(links.contains(link) == false)
				links.add(link);
		}
	}

	public void addViewToPF(int viewID, int partitionID) {
		String link = "V_" + viewID + " -> " + "PF_" + partitionID;
		if(links.contains(link) == false) {
			links.add(link);
		}
	}

}
