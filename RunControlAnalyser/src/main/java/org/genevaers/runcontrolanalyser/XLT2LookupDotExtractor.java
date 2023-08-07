package org.genevaers.runcontrolanalyser;

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


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.genevaers.genevaio.dots.LookupPathDot;
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.genevaio.ltfile.LogicTableF1;

public class XLT2LookupDotExtractor {

	private Map<Integer, LookupPathDot> joinDots = new HashMap<Integer, LookupPathDot>();

	public void extractLookups(LogicTable xlt) {
		Iterator<LTRecord> xi = xlt.getIterator();
		while(xi.hasNext()) {
			LTRecord ltr = xi.next();
			if(ltr.getFunctionCode().startsWith("JO")) {
				addLookupDotFromLogicRecord(ltr);
			}
		}
	}

	private void addLookupDotFromLogicRecord(LTRecord ltr) {
		LogicTableF1 join = (LogicTableF1)ltr;
		Integer num = Integer.valueOf(getArgValueString(join.getArg()));
		makeLookupDotIfNeeded(join, num);
	}

	private void makeLookupDotIfNeeded(LogicTableF1 join, Integer num) {
		LookupPathDot jd = joinDots.get(num);
		if(jd == null) {
			makeAndAddLookupDot(join, num);
		}
	}

	private void makeAndAddLookupDot(LogicTableF1 join, Integer num) {
		LookupPathDot newjd = new LookupPathDot();
		newjd.originalID = join.getColumnId();
		newjd.lfid = join.getArg().getLogfileId();
		newjd.lrid = join.getArg().getLrId();
		joinDots.put(num, newjd);
	}

	public int getNumberOfLookups() {
		return joinDots.size();
	}

	public void setLookupNames() {
		Iterator<Integer> jdi = joinDots.keySet().iterator();
		while(jdi.hasNext()) {
			Integer key = jdi.next();
			LookupPathDot jd = joinDots.get(key);
		}
		
	}

	public Iterator<Integer> getLookupsIterator() {
		return joinDots.keySet().iterator();		
	}

	public LookupPathDot getJoinDotForLookupNum(Integer jdnum) {
		return joinDots.get(jdnum);
	}

	private String getArgValueString(LogicTableArg arg) {
		return arg.getValue();
	}


}
