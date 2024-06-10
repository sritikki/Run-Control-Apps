package org.genevaers.genevaio.report;

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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.MetadataNode;
import org.genevaers.genevaio.fieldnodes.NumericFieldNode;
import org.genevaers.genevaio.fieldnodes.StringFieldNode;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase.FieldNodeType;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.UserExit;
import org.genevaers.utilities.GersConfigration;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.ibm.jzos.ZFile;

public class VDPTextWriter {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	protected Map<String, Boolean> ignoreTheseDiffs = new HashMap<>();
	private static Map<Integer, ViewDetails> viewDetailsById = new TreeMap<>();
	private static Map<Integer, LookupDetails> lookupDetailsById = new TreeMap<>();
	private static Map<Integer, LrDetails> lrDetailsById = new TreeMap<>();

	public static void writeFromRecordNodes( MetadataNode recordsRoot, String filename, String generated) {
		ZFile dd;
		if (GersConfigration.isZos()) {
			try {
				logger.atInfo().log("Write VDP report to %s", filename);
				dd = new ZFile("//DD:" + filename, "w");
				writeTheVDPDetailsToDnname(recordsRoot, dd, generated);
				dd.close();
			} catch (IOException e) {
				logger.atSevere().log("Unable to create DDname %s", filename);
			}
		} else {
			writeTheLtDetailsToFile(recordsRoot, filename, generated);
		}
		logger.atInfo().log("LT report written");
	}

	private static void writeTheLtDetailsToFile(MetadataNode recordsRoot, String ltPrint, String generated) {
		try (Writer out = new FileWriter(ltPrint);) {
			writeDetails(recordsRoot, out, generated);
		}
		catch (Exception e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
	}
	private static void writeTheVDPDetailsToDnname(MetadataNode recordsRoot, ZFile dd, String generated) throws IOException {
		logger.atFine().log("Stream details");
		try (Writer out = new OutputStreamWriter(dd.getOutputStream(), "IBM-1047");) {
			writeDetails(recordsRoot, out, generated);
		}
		catch (Exception e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
	}

	public static  void writeDetails( MetadataNode recordsRoot, Writer fw, String generated) throws IOException {
		writeHeader(generated, fw);
		writeSummary(recordsRoot,fw);
		writeViewSummaries(recordsRoot, fw);
		writeLookupSummaries(recordsRoot, fw);
		writeLRSummaries(recordsRoot, fw);
		writeExitSummaries(recordsRoot, fw);
		writeLfSummaries(recordsRoot, fw);
		writePfSummaries(recordsRoot, fw);
		writeContent(recordsRoot,fw);
	}
	
	private static void writeHeader(String generated, Writer fw) throws IOException {
		fw.write(String.format("VDP Report: %s\n\n", generated));
	}

	private static void writePfSummaries(MetadataNode recordsRoot, Writer fw) throws IOException {
		fw.write("\nPhysical Files\n");
		fw.write("==================\n");
		fw.write(String.format("%7s %-48s %-7s %8s %8s %7s %-48s\n", "ID", "Name", "Type", "InputDD", "OutputDD", "ExitID", "Parm"));
		fw.write(StringUtils.repeat('-', 64)+"\n");
		Iterator<PhysicalFile> pfi = Repository.getPhysicalFiles().getIterator();
		while (pfi.hasNext()) {
			PhysicalFile pf = pfi.next();
			fw.write(String.format("%7s %-48s %-7s %8s %8s %7d %-48s\n",pf.getComponentId(), pf.getName(), pf.getFileType(), pf.getInputDDName(), pf.getOutputDDName(), pf.getReadExitID(), pf.getReadExitIDParm()));
		}
	}

	private static void writeLfSummaries(MetadataNode recordsRoot, Writer fw) throws IOException {
		fw.write("\nLogical Files\n");
		fw.write("==================\n");
		fw.write(String.format("%7s %-48s %-7s\n", "ID", "Name", "Num PFs"));
		fw.write(StringUtils.repeat('-', 64)+"\n");
		Iterator<LogicalFile> lfi = Repository.getLogicalFiles().getIterator();
		while (lfi.hasNext()) {
			LogicalFile lf = lfi.next();
			fw.write(String.format("%7s %-48s %-7d\n",lf.getID(), lf.getName(), lf.getNumberOfPFs()));
		}
	}

	private static void writeExitSummaries(MetadataNode recordsRoot, Writer fw) throws IOException {
		fw.write("\nUser Exit Routines\n");
		fw.write("==================\n");
		fw.write(String.format("%7s %-48s %-6s %-9s %8s %10s \n", "ID", "Name", "Type", "Optimized", "Language", "Executable"));
		fw.write(StringUtils.repeat('-', 93)+"\n");
		Iterator<UserExit> ei = Repository.getUserExits().getIterator();
		while (ei.hasNext()) {
			UserExit e = ei.next();
			fw.write(String.format("%7s %-48s %-6s %-9s %8s %10s \n",e.getComponentId(), e.getName(), e.getExitType().toString(), e.isOptimizable(), e.getProgramType(), e.getExecutable()));
		}
	}

	private static void writeLRSummaries(MetadataNode recordsRoot, Writer fw) throws IOException {
		Iterator<LrDetails> lrds = lrDetailsById.values().iterator();
		fw.write("\nLogical Record Summaries\n");
		fw.write("========================\n");
		fw.write(String.format("%7s %-48s %-9s %7s %7s %-48s\n", "ID", "Name", "NumFields", "KeyLen", "ExitId", "Parms"));
		fw.write(StringUtils.repeat('-', 88)+"\n");
		while (lrds.hasNext()) {
			LrDetails lrd = lrds.next();
			fw.write(String.format("%7d %-48s %9s %7d %7d %-48s\n", lrd.id, lrd.name, lrd.numberOfFields, lrd.keyLen, lrd.lookupExitId, lrd.exitParms));
		}
	}

	private static void writeLookupSummaries(MetadataNode recordsRoot, Writer fw) throws IOException {
		Iterator<LookupDetails> lkds = lookupDetailsById.values().iterator();
		fw.write("\nLookup Path Summaries\n");
		fw.write("=====================\n");
		fw.write(String.format("%7s %-48s %-5s %7s %7s %7s\n", "ID", "Name", "Steps", "SrcLR", "TrgLR", "TrgLF"));
		fw.write(StringUtils.repeat('-', 86)+"\n");
		while (lkds.hasNext()) {
			LookupDetails lkd = lkds.next();
			fw.write(String.format("%7d %-48s %5s %7d %7d %7d\n", lkd.id, lkd.name, lkd.numberOfSteps, lkd.sourceLR, lkd.targetLR, lkd.targetLF));
		}
	}

	private static void writeViewSummaries(MetadataNode recordsRoot, Writer fw) throws IOException {
		Iterator<ViewDetails> vds = viewDetailsById.values().iterator();
		fw.write("View Summaries\n");
		fw.write("==============\n");
		fw.write(String.format("%7s %-48s %-8s %-4s %s\n", "ID", "Name", "Type", "Cols", "Sources"));
		fw.write(StringUtils.repeat('-', 78)+"\n");
		while (vds.hasNext()) {
			ViewDetails vd = vds.next();
			fw.write(String.format("%7d %-48s %-8s %4d %7d\n", vd.id, vd.name, vd.viewType, vd.numberOfColumns, vd.numberOfSources));
		}
	}

	private static void writeSummary(MetadataNode recordsRoot, Writer fw) throws IOException {
        Iterator<FieldNodeBase> fi = recordsRoot.getChildren().iterator();
		fw.write("Summary\n=======\n\n");
		fw.write(String.format("%-20s: %7s\n", "Component", "Count"));
		fw.write(StringUtils.repeat('=', 29)+"\n");
		int numViews = 0;
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
			if(n.getFieldNodeType() != FieldNodeType.VIEW) {
				fw.write(String.format("%-20s: %7d\n", n.getName(), n.getChildren().size()));
				if(n.getName().equals("Lookup_Paths")) {
					collectLookupDetails(n, fw);
				} else if(n.getName().equals("Logical_Records")) {
					collectLrDetails(n, fw);
				} else if(n.getName().equals("LR_Fields")) {
					//collectLrFieldDetails(n, fw);
				}

			} else {
				numViews++;
				collectViewDetails(n, fw);
			}
		}
		fw.write(String.format("%-20s: %7d\n\n\n", "Views", numViews));
	}

	private static void collectLrDetails(FieldNodeBase n, Writer fw) {
        Iterator<FieldNodeBase> fi = n.getChildren().iterator();
		int currentID = 0 ;
		LrDetails lrds = null;
        while (fi.hasNext()) {
            FieldNodeBase lk = (FieldNodeBase) fi.next();
			int id = ((NumericFieldNode)(lk.getChildrenByName("recordId"))).getValue();
			if(id != currentID) {
				lrds = new LrDetails();
				lrds.id = id;
				lrds.name = ((StringFieldNode)(lk.getChildrenByName("lrName"))).getValue();
				lrDetailsById.put(lrds.id, lrds);
				lrds.numberOfFields = Repository.getLogicalRecords().get(id).getValuesOfFieldsByID().size();
				lrds.keyLen = Repository.getLrKeyLen(id);
				lrds.lookupExitId = ((NumericFieldNode)(lk.getChildrenByName("exitPgmId"))).getValue(); 
				lrds.exitParms = ((StringFieldNode)(lk.getChildrenByName("exitStartup"))).getValue();
			}
		}
	}

	private static void collectLookupDetails(FieldNodeBase n, Writer fw) {
        Iterator<FieldNodeBase> fi = n.getChildren().iterator();
		int currentID = 0 ;
		int stepNum = 0;
		LookupDetails lkds = null;
        while (fi.hasNext()) {
            FieldNodeBase lk = (FieldNodeBase) fi.next();
			int id = ((NumericFieldNode)(lk.getChildrenByName("recordId"))).getValue();
			if(id != currentID) {
				lkds = new LookupDetails();
				lkds.id = id;
				lkds.name = ((StringFieldNode)(lk.getChildrenByName("joinName"))).getValue();
				lookupDetailsById.put(lkds.id, lkds);
				lkds.sourceLR = ((NumericFieldNode)(lk.getChildrenByName("sourceLrId"))).getValue();
				currentID = id;
			}
			lkds.numberOfSteps = ((NumericFieldNode)(lk.getChildrenByName("sequenceNbr"))).getValue();
			lkds.targetLF = ((NumericFieldNode)(lk.getChildrenByName("inputFileId"))).getValue();
			lkds.targetLR = ((NumericFieldNode)(lk.getChildrenByName("targetLrId"))).getValue();
		}
	}

	private static void collectViewDetails(FieldNodeBase v, Writer fw) throws IOException {
        Iterator<FieldNodeBase> fi = v.getChildren().iterator();
		ViewDetails vds = new ViewDetails();
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
			if(n.getName().equals("View_Definition")) {
				FieldNodeBase rec = n.getChildren().get(0);
				vds.name = ((StringFieldNode)(rec.getChildrenByName("viewName"))).getValue();
				vds.id = ((NumericFieldNode)(rec.getChildrenByName("recordId"))).getValue();
				vds.viewType = ((StringFieldNode)(rec.getChildrenByName("viewType"))).getValue();
			} else if(n.getName().equals("Columns")) {
				vds.numberOfColumns = n.getChildren().size();
			} else if(n.getName().equals("Sources")) {
				vds.numberOfSources = n.getChildren().size();
			}		
		}
		viewDetailsById.put(vds.id, vds);
	}

	private static void writeComponentSummary(MetadataNode recordsRoot, Writer fw) throws IOException {
        Iterator<FieldNodeBase> fi = recordsRoot.getChildren().iterator();
		fw.write("Summary\n=======\n\n");
		fw.write(String.format("%-20s: %7s\n", "Component", "Count"));
		fw.write(StringUtils.repeat('=', 29)+"\n");
		int numViews = 0;
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
			if(n.getFieldNodeType() != FieldNodeType.VIEW) {
				fw.write(String.format("%-20s: %7d\n", n.getName(), n.getChildren().size()));
			} else {
				numViews++;
			}
		}
		fw.write(String.format("%-20s: %7d\n\n\n", "Views", numViews));
	}


	private static void writeContent(MetadataNode recordsRoot, Writer fw) throws IOException {
		fw.write(String.format("\nRecord Level Reports\n"));
	fw.write(String.format("====================\n"));
        Iterator<FieldNodeBase> fi = recordsRoot.getChildren().iterator();
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
            writeComponents(n, fw);
            //writeFields(child, n, fw);
        }
		fw.close();
	}
	private static void writeComponents(FieldNodeBase c, Writer fw) throws IOException {
			if(c.getFieldNodeType() == FieldNodeType.VIEW) {
				writeView(c, fw);
			} else {
				writeComponent(c, fw);
		}
	}

	private static void writeComponent(FieldNodeBase c, Writer fw) throws IOException {
		fw.write(String.format("\n~%s (%s)\n",c.getName(), ((NumericFieldNode)c.getChildren().get(0).getChildrenByName("recordType")).getValue()));
		writeComponentEntries(c, fw);
	}

	private static void writeComponentEntries(FieldNodeBase c, Writer fw) throws IOException {
		Iterator<FieldNodeBase> fi = c.getChildren().iterator();
		while (fi.hasNext()) {
			FieldNodeBase n = (FieldNodeBase) fi.next();
			writeRecord(n, fw);
		}
	}

	
    private static void writeView(FieldNodeBase c, Writer fw) throws IOException {
		fw.write("\n~View "+ c.getName().substring(4) + "\n");		
		Iterator<FieldNodeBase> fi = c.getChildren().iterator();
		while (fi.hasNext()) {
			FieldNodeBase n = (FieldNodeBase) fi.next();
			fw.write(String.format("\n    ~*%s (%s)\n",n.getName().replace('_', ' '), ((NumericFieldNode)n.getChildren().get(0).getChildrenByName("recordType")).getValue()));
			writeComponentEntries(n, fw);
		}
	}

	private static void writeRecord(FieldNodeBase r, Writer fw) throws IOException {
		fw.write("    Record:\n");
		Iterator<FieldNodeBase> fi = r.getChildren().iterator();
		while (fi.hasNext()) {
			FieldNodeBase n = (FieldNodeBase) fi.next();
			writeField(n, fw);
		}
	}

	private static void writeField(FieldNodeBase f, Writer fw) throws IOException {
		switch(f.getFieldNodeType()) {
			case FUNCCODE:
				break;
			case METADATA:
				break;
			case NOCOMPONENT:
				break;
			case NUMBERFIELD:
			fw.write(String.format("        %-25s: %d\n",f.getName(),((NumericFieldNode) f).getValue( )));
			break;
			case RECORD:
				break;
			case RECORDPART:
				break;
			case ROOT:
				break;
			case STRINGFIELD:
			fw.write(String.format("        %-25s: %s\n",f.getName(),((StringFieldNode) f).getValue( )));
				break;
			case VIEW:
				break;
			default:
				break;

		}
	}
}
