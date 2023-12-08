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


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.flogger.FluentLogger;

import org.genevaers.genevaio.ltfile.ArgHelper;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.LogicalRecord;

public class ViewDotFile {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private int viewID;
	private ViewLinks links = new ViewLinks();
	private Map<String, JoinDotWriter> joinWriters = new HashMap<>();
	private List<EventSetDot> eventSets = new ArrayList<>();

	public ViewDotFile(int id) {
		viewID = id;
	}

	private FileWriter fw;
	private boolean open = false;

	public void open(Path viewsp) throws IOException {
		logger.atFine().log("Open ViewDotFile for View %d", viewID);
		Path viewDot = viewsp.resolve("v"+viewID+".dot");
		fw = new FileWriter(viewDot.toFile());
		open = true;
	}

	public void write() throws IOException {
		open = false;
		writeEventSets();
		writeLinks();
		writeJoinLinks();
		fw.write("}\n");
		fw.close();
	}

	public void write(String str) throws IOException {
		fw.write(str);
	}

	public boolean isOpen() {
		return open;
	}
	
	public int getViewID() {
		return viewID;
	}

	public void writeHeader(String viewName) throws IOException {
		String header =  "digraph xml {\nrankdir=LR\n//Nodes\n"
		+ "graph [label=\"" + viewName + "(" + viewID + ")\\n\", labelloc=t, labeljust=center, fontname=Helvetica, fontsize=22];\n"
        + "labeljust=center; ranksep = \"3 equally\"\n";
		write(header);
	}

	//The Links are really part of an event set... 
	//but can be here too
	public ViewLinks getLinks() {
		return links;
	}

	public JoinDotWriter getJoinDotWriter(String joinNumber, short currentViewSource) {
		return joinWriters.get(joinNumber + "_" + currentViewSource);
	}
	
	public void writeLinks() throws IOException {
		Iterator<String> li = links.getIterator();
		while(li.hasNext())
		{
			fw.write(li.next());
		}
	}
	
	private void writeJoinLinks() throws IOException {
		for(JoinDotWriter jdw : joinWriters.values()) {
			String joinDot = jdw.getDotString();
			fw.write(joinDot);
		}
	}

	public void addEventSet(short viewSource, LogicalFile srclf, LogicalRecord srcLR) {
		EventSetDot es = new EventSetDot(viewSource);
		es.setView(viewID);
		es.setLF(srclf);
		es.setLR(srcLR);
		eventSets.add(es);
	}

	//The Join should be part of the Event set cluster?
	public JoinDotWriter addJoin(LogicTableF1 join, boolean joinDetailed, short currentViewSource) {
		logger.atInfo().log("Add Join %s to view %d", join.getArg().getValue().getString(), viewID);
		String val = join.getArg().getValue().getString();
		String joinPerSource = val + "_" + currentViewSource;

		JoinDotWriter jdw = joinWriters.get(joinPerSource);
		if(jdw == null) {
			jdw = new JoinDotWriter();
			jdw.setJoinRecord(join);
			jdw.setDetailed(joinDetailed);
			jdw.setSource(currentViewSource);
			joinWriters.put(joinPerSource, jdw);
			logger.atInfo().log("Add Join %s to DOT file", joinPerSource);
		}
		return jdw;
	}

	public void setRefLR(String joinNumber, LogicalRecord lr, short currentViewSource) {
		String jps = joinNumber + "_" + currentViewSource;
		JoinDotWriter jdw = joinWriters.get(jps);
		if(jdw == null) {
			logger.atSevere().log("setRefLR: Cannot find joinWriter for $s", jps);
		}else if(lr == null) {
			logger.atSevere().log("setRefLR: Attempt to assign null LR to Join Source $s", jps);
		} else {
			jdw.setRefLR(lr);
		}
	}

	public Iterator<EventSetDot> getEventSetIterator() {
		return eventSets.iterator();
	}


	private void writeEventSets() throws IOException {
		for (EventSetDot es : eventSets) {
			fw.write(es.getDotString());
		}
	}
}
