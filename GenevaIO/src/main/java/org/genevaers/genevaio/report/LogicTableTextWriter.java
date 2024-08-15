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
import java.util.Iterator;

import org.genevaers.genevaio.fieldnodes.ComparisonState;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.MetadataNode;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase.FieldNodeType;
import org.genevaers.genevaio.fieldnodes.FunctionCodeNode;
import com.google.common.flogger.FluentLogger;

public class LogicTableTextWriter extends TextRecordWriter {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	public LogicTableTextWriter() {
		setIgnores();
	}

	@Override
	public void writeDetails( MetadataNode recordsRoot, Writer fw, String generated) throws IOException {
		writeHeader(generated, fw);
		writeContent(recordsRoot,fw);
		writeComparisonSummary(recordsRoot, fw);
	}
	
	private void writeComparisonSummary(MetadataNode recordsRoot, Writer fw) throws IOException {
		if(recordsRoot.getName().equals("Compare")) {
			fw.write("\n\nComparison Results\n==================\n\n");
			fw.write(String.format("%-20s: %7d\n\n\n", "Number of diffs", numDiffs));
		}
	}

	private void writeHeader(String generated, Writer fw) throws IOException {
		fw.write(String.format("LT Report: %s\n\n", generated));
	}


	private void writeContent(MetadataNode recordsRoot, Writer fw) throws IOException {
		fw.write(String.format("\nRecord Level Reports\n"));
		fw.write(String.format("====================\n"));
        Iterator<FieldNodeBase> fi = recordsRoot.getChildren().iterator();
        while (fi.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) fi.next();
			preCheckAndChangeRowState(n);
			writeRecord(n, fw);
        }
	}


	private void writeRecord(FieldNodeBase r, Writer fw) throws IOException {
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

	private void writeArg(FieldNodeBase r, Writer fw) throws IOException {
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

	@Override
	public void setIgnores() {
		ignoreTheseDiffs.put("GEN_fileId", true); 
		ignoreTheseDiffs.put("GEN_timeHh", true); 
		ignoreTheseDiffs.put("GEN_timeMm", true); 
		ignoreTheseDiffs.put("GEN_timeSs", true); 
		ignoreTheseDiffs.put("GEN_desc", true); 
		ignoreTheseDiffs.put("GEN_padding", true); 
		ignoreTheseDiffs.put("HD_fileId", true); 
		ignoreTheseDiffs.put("HD_time", true); 
		ignoreTheseDiffs.put("sourceSeqNbr", true); 
		ignoreTheseDiffs.put("GOTO_viewId", true); 
		ignoreTheseDiffs.put("DTC_lrId", true); 
		ignoreTheseDiffs.put("JOIN_fieldId", true); 
		ignoreTheseDiffs.put("JOIN_fieldFormat", true); 
		ignoreTheseDiffs.put("JOIN_startPosition", true); 
		ignoreTheseDiffs.put("JOIN_ordinalPosition", true); 
		ignoreTheseDiffs.put("JOIN_justifyId", true); 
		ignoreTheseDiffs.put("LKE_ordinalPosition", true); 
		ignoreTheseDiffs.put("DTL_ordinalPosition", true); 
		ignoreTheseDiffs.put("CFLC_ordinalPosition", true); 
	}


	protected  void preCheckAndChangeRowState(FieldNodeBase r) {
		boolean updateRowState = true;
		for( FieldNodeBase n : r.getChildren()) {
			if(n.getFieldNodeType() == FieldNodeType.RECORDPART) {
				preCheckAndChangeRowState(n);
			} else {
				if(n.getState() == ComparisonState.DIFF) {
					if(ignoreTheseDiffs.get(getDiffKey(n)) != null) {
						n.setState(ComparisonState.IGNORED);
					} else {
						updateRowState = false;
					}
				}
			}
		}
		if(updateRowState) {
			r.setState(ComparisonState.INSTANCE);
		}
	}

	@Override
	protected String getDiffKey(FieldNodeBase n) {
		if(n.getName().equals("sourceSeqNbr")) {
			return n.getName();
		} else {
			if(n.getParent().getFieldNodeType() == FieldNodeType.RECORDPART) {
				return ((FunctionCodeNode)n.getParent().getParent()).getFunctionCode() + "_" + n.getName();
			} else {
				return ((FunctionCodeNode)n.getParent()).getFunctionCode() + "_" + n.getName();
			}
		}
	}
}
