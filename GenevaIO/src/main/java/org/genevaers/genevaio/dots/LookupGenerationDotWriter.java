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


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.parameter.DateTimeFormat;

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.ltfile.LogicTableF1;
import org.genevaers.genevaio.ltfile.LogicTableF2;
import org.genevaers.genevaio.ltfile.LogicTableNV;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.enums.DateCode;

public class LookupGenerationDotWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	/**
	 *
	 */
	private static final String SRCLR_PORT = "SRCLR_";
	private LogicTable jlt;
	private int redCheck = 9000001;
	private REDDotFile rdf;
	private boolean psuedoLRNeeded = false;
	private String currentRED;
	private int currentRedID;
	private int currentJoinID;
	private int currentRedFieldID;
	private LogicalRecord redLR;
	private boolean writingRED;
	private LogicalRecord srcLR;

	private Map<Integer, String> lr2REDCluster = new TreeMap<Integer, String>();

	// Join -> LF -> LR -> RED Cluster
	// Join -> LF is one to one
	// LF -> LR is usually one to one
	// LR -> RED Cluster is one to one

	// So make a Join to Cluster?

	private List<String> links = new ArrayList<String>();
	private int currentLRID;

	public LookupGenerationDotWriter(LogicTable j) {

		jlt = j;
	}

	public void writeREDGenerations(Path cwd) throws IOException {
		logger.atFine().log("writeREDGenerations");
		// Here we need to read the jlt and for each NV we write its dot
		Iterator<LTRecord> ji = jlt.getIterator();
		writingRED = false;
		while(ji.hasNext()) {
			LTRecord jtr = ji.next();
			if(jtr.getFunctionCode().equals("NV")) {
				makeDotIfNotReference(cwd, (LogicTableNV)jtr);
				psuedoLRNeeded = true;
				writingRED = true;
			}
			else if(writingRED) {
				addTargetData(jtr);
			}
		}
	}

	public Iterator<String> getLRToREDClusterIterator() {
		return lr2REDCluster.values().iterator();
	}

	public String getCluster(int trgLRid) {
		String c = lr2REDCluster.get(trgLRid);
		return c != null ? c : ""; 
	}


	private void addTargetData(LTRecord jtr) throws IOException {
		// a made up LR derived from the DT targets
		//Or it is in the VDP? it is - add from there
		//Then we make the links base on the field ids in the DTs
		if(psuedoLRNeeded ) { 
			//The issue here is that the RED LR in the repo does not have the key fields
			//So we make our own psudo LR that is what the JLT process writes to.
			LogicalRecord lr = Repository.getLogicalRecords().get(currentRedID);

			redLR = new LogicalRecord();
			redLR.setName(currentRED);
			redLR.setComponentId(currentRedID);
			currentRedFieldID = 1;
			psuedoLRNeeded = false;
		}
		if(jtr.getFunctionCode().startsWith("DT")) {
			if(jtr.getFunctionCode().endsWith("E")) {
				LogicTableF2 dte = (LogicTableF2)jtr;
				String srcPort = "LRF_" + dte.getArg1().getFieldId();
				LRField redField =  new LRField();
				redField.setComponentId(currentRedFieldID++);
				LRField lfr = Repository.getFields().get(dte.getArg1().getFieldId());
				redField.setName("From " + lfr.getName());
				redField.setStartPosition(dte.getArg2().getStartPosition());
				redField.setLength(dte.getArg2().getFieldLength());
		        redField.setDatatype(dte.getArg2().getFieldFormat());
				redField.setDateTimeFormat(DateCode.NONE);;
				redLR.addToFieldsByID(redField);
				addFieldLink(dte.getArg1().getFieldId(), redField.getComponentId());
			}
			else if(jtr.getFunctionCode().endsWith("C")) {
				LogicTableF1 dtc = (LogicTableF1)jtr;
				LRField redField =  new LRField();
				redField.setComponentId(currentRedFieldID);
				
				redField.setName("REDF" + currentRedFieldID++);
				redField.setStartPosition(dtc.getArg().getStartPosition());
				redField.setLength(dtc.getArg().getFieldLength());
		        redField.setDatatype(dtc.getArg().getFieldFormat());
				redField.setDateTimeFormat(DateCode.NONE);;
				redLR.addToFieldsByID(redField);
			}
		} else if (jtr.getFunctionCode().startsWith("WR")) {
			if(rdf.isOpen()) {
				addTargetLRToRDF();
				writeLinks();
				rdf.write("\n}\n");
				rdf.close();
				writingRED = false;
			}
		}
	}

	private void writeLinks() throws IOException {
		for( String l : links) {
			rdf.write(l);
		}
		links.clear();
	}

	private void addFieldLink(int src, int trg) throws IOException {
		if(srcLR != null)  {
			String link = SRCLR_PORT+ srcLR.getComponentId() + ":LRF_" + src + "_R -> SRCLR_" + currentRedID +":LRF_" + trg +"_L\n";
			links.add(link);
		} else {
			logger.atWarning().log("Null srcLR");
		}
	}

	private void addTargetLRToRDF() throws IOException {
		LogicalRecordDotWriter lrdfw = new LogicalRecordDotWriter(); 
		String display = currentRED; // + lr.getName();
		lrdfw.setDisplayString(display);
		String cluster = SRCLR_PORT + currentRedID;
		if(redLR != null) {
			String redDot = lrdfw.getDotStringFromLR(cluster, redLR, true) ;
			rdf.write(redDot);
			lr2REDCluster.put(currentLRID, redDot);
		} else {
			logger.atSevere().log("Unable to find LR for ID %d", currentRedID);
		}
	}

	private void  makeDotIfNotReference(Path cwd, LTFileObject jtr) throws IOException {
		LogicTableNV nvr = (LogicTableNV) jtr;
		currentLRID = nvr.getSourceLrId();
		currentRedID  = nvr.getViewId();
		if(nvr.getSourceLrId() != 0) {
			rdf = makeREDDotFile(cwd);
			writingRED = true;
			addSourceLRToRDF(nvr.getSourceLrId(), rdf);
		}
	}

	private void addSourceLRToRDF(int sourceLRID, REDDotFile rdf) throws IOException {
		LogicalRecordDotWriter lrdfw = new LogicalRecordDotWriter();
		srcLR = Repository.getLogicalRecords().get(sourceLRID);
		if(srcLR != null) {
			String cluster = SRCLR_PORT + srcLR.getComponentId();
			String lrDot = lrdfw.getDotStringFromLR(cluster, srcLR, true) ;
			rdf.write(lrDot);
		} else {
			logger.atWarning().log("addSourceLRToRDF srcLR Null");
		}
	}

	private REDDotFile makeREDDotFile(Path cwd) throws IOException {
		REDDotFile rdf = new REDDotFile(currentRedID);
		rdf.open(cwd); //want the cwd here
		logger.atFine().log("Opened %s", rdf.getRedDot().getName());
		writeHeader(currentRedID, rdf);
		return rdf;
	}

	private void writeHeader(int redID, REDDotFile rdf) throws IOException {
		currentRED = "RED" + redID;
		String header = "digraph xml {\nrankdir=LR\n//Nodes\n"
				+ "graph [label=\""
				+ currentRED
				+ "\\n\\n\\n\", labelloc=t, labeljust=center, fontname=Helvetica, fontsize=22];\n"
				+ "labeljust=center; splines=\"polyline\"\n";

		rdf.write(header);
	}

}
