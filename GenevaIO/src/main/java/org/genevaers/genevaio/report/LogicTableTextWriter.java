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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.genevaers.genevaio.fieldnodes.ComparisonState;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.MetadataNode;
import org.genevaers.genevaio.fieldnodes.NumericFieldNode;
import org.genevaers.genevaio.fieldnodes.StringFieldNode;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase.FieldNodeType;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.UserExit;
import org.genevaers.utilities.GersFile;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class LogicTableTextWriter {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	protected Map<String, Boolean> ignoreTheseDiffs = new HashMap<>();
	private static Map<Integer, ViewDetails> viewDetailsById = new TreeMap<>();
	private static Map<Integer, LookupDetails> lookupDetailsById = new TreeMap<>();
	private static Map<Integer, LrDetails> lrDetailsById = new TreeMap<>();

	private static int numDiffs;

	public static void writeFromRecordNodes( MetadataNode recordsRoot, String filename, String generated) {
		logger.atInfo().log("Write LT report to %s", filename);
		try(Writer fw = new GersFile().getWriter(filename)) {
			writeDetails(recordsRoot, fw, generated);
		} catch (IOException e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
		logger.atInfo().log("LT report written");
	}

	public static  void writeDetails( MetadataNode recordsRoot, Writer fw, String generated) throws IOException {
		writeHeader(generated, fw);
		writeContent(recordsRoot,fw);
		writeComparisonSummary(recordsRoot, fw);
	}
	
	private static void writeComparisonSummary(MetadataNode recordsRoot, Writer fw) throws IOException {
		if(recordsRoot.getName().equals("Compare")) {
			fw.write("\n\nComparison Results\n==================\n\n");
			fw.write(String.format("%-20s: %7d\n\n\n", "Number of diffs", numDiffs));
		}
	}

	private static void writeHeader(String generated, Writer fw) throws IOException {
		fw.write(String.format("LT Report: %s\n\n", generated));
	}


	private static void writeContent(MetadataNode recordsRoot, Writer fw) throws IOException {
		fw.write(String.format("\nRecord Level Reports\n"));
		fw.write(String.format("====================\n"));
        Iterator<FieldNodeBase> fi = recordsRoot.getChildren().iterator();
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
			writeRecord(n, fw);
        }
	}


	private static void writeRecord(FieldNodeBase r, Writer fw) throws IOException {
		fw.write("    Record:\n");
		Iterator<FieldNodeBase> fi = r.getChildren().iterator();
		while (fi.hasNext()) {
			FieldNodeBase n = (FieldNodeBase) fi.next();
			if(n.getFieldNodeType() == FieldNodeType.RECORDPART){
				writeArg(n, fw);
			} else {
				writeField(n, fw);
			}
		}
	}

	private static void writeArg(FieldNodeBase r, Writer fw) throws IOException {
		fw.write("    Arg:\n");
		Iterator<FieldNodeBase> fi = r.getChildren().iterator();
		while (fi.hasNext()) {
			FieldNodeBase n = (FieldNodeBase) fi.next();
			if(n.getFieldNodeType() == FieldNodeType.RECORDPART){
				writeRecord(n, fw);
			} else {
				writeField(n, fw);
			}
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
			fw.write(String.format("        %-25s: %s\n",f.getName(),((NumericFieldNode) f).getValueString()));
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
		if(f.getState() == ComparisonState.DIFF) {
			numDiffs++;
		}
	}
}
