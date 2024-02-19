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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.MetadataNode;
import org.genevaers.genevaio.fieldnodes.NumericFieldNode;
import org.genevaers.genevaio.fieldnodes.StringFieldNode;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.genevaio.recordreader.RecordFileReaderWriter;
import org.genevaers.genevaio.recordreader.RecordFileWriter;
import org.genevaers.utilities.GersConfigration;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.ibm.jzos.ZFile;

import org.genevaers.genevaio.fieldnodes.FieldNodeBase.FieldNodeType;

public class VDPTextWriter {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	private  FileWriter fw;
	private  final String POPUP = "w3-modal-content w3-animate-zoom";
	 String toggleScript = "function toggleDiv(divname) {" +
			"var ele = document.getElementById(divname);" +
			"if (ele.style.display == \"none\") {" +
			"ele.style.display = \"block\";" +
			"}" +
			"else {" +
			"ele.style.display = \"none\";" +
			"}" +
			"}";
	protected Map<String, Boolean> ignoreTheseDiffs = new HashMap<>();
	private  String title = "";

	// public void setIgnores() {};
	// protected abstract String getDiffKey(FieldNodeBase n);

	public void setTitle(String t) {
		title = t;
	}

	public static void writeFromRecordNodes( MetadataNode recordsRoot, String filename) {
		ZFile dd;
		if (GersConfigration.isZos()) {
			try {
				logger.atInfo().log("Write VDP report to %s", filename);
				dd = new ZFile("//DD:" + filename, "w");
				writeTheVDPDetailsToDnname(recordsRoot, dd);
				dd.close();
			} catch (IOException e) {
				logger.atSevere().log("Unable to create DDname %s", filename);
			}
		} else {
			writeTheLtDetailsToFile(recordsRoot, filename);
		}
		logger.atInfo().log("LT report written");
	}

	private static void writeTheLtDetailsToFile(MetadataNode recordsRoot, String ltPrint) {
		try (Writer out = new FileWriter(ltPrint + ".txt");) {
			writeDetails(recordsRoot, out);
		}
		catch (Exception e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
	}
	private static void writeTheVDPDetailsToDnname(MetadataNode recordsRoot, ZFile dd) throws IOException {
		logger.atFine().log("Stream details");
		try (Writer out = new OutputStreamWriter(dd.getOutputStream(), "IBM-1047");) {
			writeDetails(recordsRoot, out);
		}
		catch (Exception e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
	}

	public static  void writeDetails( MetadataNode recordsRoot, Writer fw) throws IOException {
			writeContent(recordsRoot,fw);
	}
	
	private static void writeContent(MetadataNode recordsRoot, Writer fw) throws IOException {
        Iterator<FieldNodeBase> fi = recordsRoot.getChildren().iterator();
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
            writeComponents(n, fw);
            //writeFields(child, n, fw);
        }
		fw.close();
	}
	private static void writeComponents(FieldNodeBase c, Writer fw) {
			if(c.getName().startsWith("View")) {
				writeView(c, fw);
			} else {
				writeComponent(c, fw);
		}
	}

	private static void writeComponent(FieldNodeBase c, Writer fw) {
		try {
			fw.write("~"+ c.getName() + "\n");
			writeComponentEntries(c, fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeComponentEntries(FieldNodeBase c, Writer fw) {
		Iterator<FieldNodeBase> fi = c.getChildren().iterator();
		while (fi.hasNext()) {
			FieldNodeBase n = (FieldNodeBase) fi.next();
			writeRecord(n, fw);
		}
	}

	
    private static void writeView(FieldNodeBase c, Writer fw) {
		try {
			fw.write("~View "+ c.getName().substring(4) + "\n");		
			Iterator<FieldNodeBase> fi = c.getChildren().iterator();
			while (fi.hasNext()) {
				FieldNodeBase n = (FieldNodeBase) fi.next();
				switch (n.getName()) {
					case "View_Definition":
						fw.write("  " + "~*View Definition\n");
						writeComponentEntries(n, fw);
						break;
					case "View_Output_File":
						fw.write("  " + "~*View Output File\n");
						writeComponentEntries(n, fw);
						break;
					case "Columns":
						fw.write("  " + "~*Columns\n");
						writeComponentEntries(n, fw);
						break;
					case "Sources":
						fw.write("  " + "~*Sources\n");
						writeComponentEntries(n, fw);
						break;
					case "Output_Logic":
						fw.write("  " + "~*Output Logic\n");
						writeComponentEntries(n, fw);
						break;
					case "Column_Sources":
						fw.write("  " + "~*Column Sources\n");
						writeComponentEntries(n, fw);
						break;
					case "Column_Logic":
						fw.write("  " + "~*Column Logic\n");
						writeComponentEntries(n, fw);
						break;
				
					default:
						fw.write("  " + "~*Other Stuff\n");
						writeComponentEntries(n, fw);
						break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

	private static void writeRecord(FieldNodeBase r, Writer fw) {
		try {
			fw.write("    Record:\n");
			Iterator<FieldNodeBase> fi = r.getChildren().iterator();
			while (fi.hasNext()) {
				FieldNodeBase n = (FieldNodeBase) fi.next();
				writeField(n, fw);
			}
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeField(FieldNodeBase f, Writer fw) {
		try {
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
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeFields(String from, FieldNodeBase next, Writer fw2) throws IOException {
        if (next != null ) {
            Iterator<FieldNodeBase> asti = next.getChildren().iterator();
            while (asti.hasNext()) {
                FieldNodeBase node = asti.next();
                // String child = dotNode(node);
                //     fw.write(from + " -> " + child);
                //     fw.write("\n");
                //     writeFields(child, node);
            }
        }
    }

}
