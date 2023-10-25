package org.genevaers.genevaio.html;

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


import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.join;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.script;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.genevaers.genevaio.fieldnodes.ComparisonState;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.FunctionCodeNode;
import org.genevaers.genevaio.fieldnodes.NumericFieldNode;
import org.genevaers.genevaio.fieldnodes.RecordNode;
import org.genevaers.genevaio.fieldnodes.StringFieldNode;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase.FieldNodeType;
import org.genevaers.genevaio.fieldnodes.MetadataNode;

import j2html.tags.DomContent;
import j2html.tags.UnescapedText;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.TdTag;
import j2html.tags.specialized.ThTag;
import j2html.tags.specialized.TrTag;

public class LTRecordsHTMLWriter extends HTMLRecordsWriter{
	
	@Override
	protected  DomContent recordTables(MetadataNode root) {
		return table(
				tbody(
						each(root.getChildren(), rec  -> getRows(rec))))
				.withClass("w3-table w3-striped w3-border");
	}

	private  UnescapedText getRows(FieldNodeBase rec) {
			return join(getHeaderRow(rec),
						getRow(rec));
	}


	protected  TrTag getRow(FieldNodeBase r) {
		preCheckAndChangeRowState(r);
		return tr( each(r.getChildren(), n -> ltrowEntry(n)) );
	}

	protected  DomContent ltrowEntry(FieldNodeBase n) {
		if(n.getFieldNodeType() == FieldNodeType.RECORDPART){
			return  each(n.getChildren(), d -> rowEntry(d));
		} else {
			return super.rowEntry(n);
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

	@Override
	protected TrTag getHeaderRow(FieldNodeBase fieldNodeBase) {
		return tr( each(fieldNodeBase.getChildren(), n -> ltheaderElement(n)) );
	}

	private DomContent ltheaderElement(FieldNodeBase n) {
		if(n.getFieldNodeType() == FieldNodeType.RECORDPART){
			return each(n.getChildren(), h -> headerElement(h));
		} else {
			return th(n.getName());
		}
	}

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
