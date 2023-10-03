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

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTableWR;
import org.genevaers.genevaio.vdpfile.VDPFormatFile;
import org.genevaers.genevaio.vdpfile.VDPManagementRecords;
import org.genevaers.genevaio.vdpfile.ViewManagementData;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.ViewNode;

public class FlowDotFile {

	private FileWriter fw;
	private boolean open = false;
	private Map<Integer, PhysicalFileDotNode> pfs = new HashMap<Integer, PhysicalFileDotNode>();
	private Map<Integer, ExitDotNode> exits = new HashMap<Integer, ExitDotNode>();
	private Map<Integer, LogicalFileDotNode> lfs = new HashMap<Integer, LogicalFileDotNode>();
	private Map<Integer, ViewDotNode> views = new HashMap<Integer, ViewDotNode>();
	private List<String> links = new ArrayList<String>();
	private String name;

	public void open(Path viewsp) throws IOException {
		Path viewDot = viewsp.resolve(name);
		fw = new FileWriter(viewDot.toFile());
		open = true;
	}

	public void setName(String name) throws IOException {
		this.name = name;
	}

	public void close() throws IOException {
		open = false;
		fw.write("}");
		fw.close();
	}

	public void write(String str) throws IOException {
		fw.write(str);
	}

	public boolean isOpen() {
		return open;
	}

	public void writeHeader(String name) throws IOException {
		String header = "digraph xml {\nrankdir=LR\n//Nodes\n" + "graph [label=\"" + name
				+ "\\n\\n\\n\", labelloc=t, labeljust=center, fontname=Helvetica, fontsize=22];\n"
				+ "labeljust=center; splines=\"polyline\" \n";
		write(header);
	}

	public void addView(String name, Integer v) throws IOException {
		ViewDotNode vdn = new ViewDotNode(name, v);
		views.put(v, vdn);
	}

	public void addLogicalFile(LogicalFile lf) throws IOException {
		if (lf != null) {
			LogicalFileDotNode lfDot = new LogicalFileDotNode(lf.getName(), lf.getID());
			lfs.put(lf.getID(), lfDot);
			Iterator<PhysicalFile> pfi = lf.getPFIterator();
			while (pfi.hasNext()) {
				PhysicalFile pf = pfi.next();
				addPhysicalFile(pf);
				addPFLFlink(pf.getComponentId(), lf.getID());
			}
		} else {
			System.out.println("Null LF");
		}
	}

	public void addPhysicalFile(PhysicalFile pf) {
		PhysicalFileDotNode pfn = pfs.get(pf.getComponentId());
		if (pfn == null) {
			pfn = new PhysicalFileDotNode(pf.getName(), pf.getComponentId());
			pfs.put(pf.getComponentId(), pfn);
			if (pf.getReadExit() != null) {
				// Make a unique Exit node for this PF
				ExitDotNode en = new ExitDotNode(pf.getReadExit().getName(), pf.getComponentId());
				exits.put(pf.getComponentId(), en);
				String pathLink = "EX_" + pf.getComponentId() + " -> " + "PF_" + pf.getComponentId() + "\n";
				links.add(pathLink);
			}
		}
	}

	public void writePFs() throws IOException {
		for (PhysicalFileDotNode pfn : pfs.values()) {
			write(pfn.getNodeString());
		}

	}

	public void addLF2ViewLink(int lfid, int viewID) {
		String pathLink = "LF_" + lfid + " -> " + "V_" + viewID + "\n";
		links.add(pathLink);
	}

	public void addViewAndSource(int viewID, LogicalFile srclf) {
		ViewNode view = Repository.getViews().get(viewID);
		ViewDotNode vn = views.get(viewID);
		if (vn == null) {
			vn = new ViewDotNode(view.getName(), viewID);
			views.put(viewID, vn);
		}
		vn.addSourceLF(srclf);
	}

	public void writeLinks() throws IOException {
		for (String l : links) {
			fw.write(l);
		}
	}

	public void addPFLFlink(int pfid, int lfid) {
		String pathLink = "PF_" + pfid + " -> " + "LF_" + lfid + "\n";
		links.add(pathLink);
	}

	public void addWriteTarget(LTRecord xltr, VDPManagementRecords vmrs) {
		LogicTableWR wr = (LogicTableWR) xltr;
		PhysicalFile pf = Repository.getPhysicalFiles().get(wr.getOutputFileId());
		if (pf != null) {
			addPhysicalFile(pf);
		}
		if (wr.getOutputFileId() > 0) {
			String link = "V_" + wr.getViewId() + " -> " + "PF_" + wr.getOutputFileId() + "\n";
			if (links.contains(link) == false) {
				links.add(link);
			}
		} else {
			int extractNum = wr.getSuffixSeqNbr();
			PhysicalFileDotNode pfn = new PhysicalFileDotNode("Extract " + extractNum, extractNum);
			pfn.setExtract();
			pfs.put(extractNum, pfn);
			System.out.println("Extract Node");
			String link = "V_" + wr.getViewId() + " -> " + "PF_" + extractNum + "\n";
			if (links.contains(link) == false) {
				links.add(link);
			}
			ViewNode view = Repository.getViews().get(wr.getViewId());
			if (view.isFormat()) {
				ViewManagementData vmd = vmrs.getViewManagmentData(view.getViewDefinition().getComponentId());
				VDPFormatFile fof = vmd.getFormatFile();
				if(fof != null) {
					if(fof.getLfName().isEmpty()) {
						fof.setLfName(fof.getDdnameOutput());
					}
					System.out.println("Format view to " + fof.getLfName());
					LogicalFileDotNode lfn = new LogicalFileDotNode(fof.getLfName(), view.getID());
					lfs.put(view.getID(), lfn);
					String e2lf = "PF_" + extractNum + " -> " + lfn.getDOTID()+"\n";
					links.add(e2lf);
				}
			}
		}
	}

	public void writeLFs() throws IOException {
		for(LogicalFileDotNode lfn : lfs.values()) {
			write(lfn.getNodeString());
		}
	}

	public void writeExits() throws IOException {
		for( ExitDotNode exn : exits.values()) {
			write(exn.getNodeString());
		}
	}

	public void write() throws IOException {
		writeHeader("Flow");
		writeViews();
		writePFs();
		writeLFs();
		writeExits();
		writeLinks();
	}

	private void writeViews() throws IOException {
		for (ViewDotNode v : views.values()) {
			write(v.getNodeString());
		}
	}

	public void addLF2ViewLink(LogicalFile srclf, ViewNode view) {
		if(srclf != null) {
			String link = "LF_" + srclf.getID() + " -> " + "V_" + view.getID()+"\n";
			if(links.contains(link) == false) {
				links.add(link);
			}
		} else {
			
		}
	}

}
