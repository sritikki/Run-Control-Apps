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


import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.i;
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
import java.util.Iterator;

import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.NumericFieldNode;
import org.genevaers.genevaio.fieldnodes.RecordNode;
import org.genevaers.genevaio.fieldnodes.RecordTypeNode;
import org.genevaers.genevaio.fieldnodes.StringFieldNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;


import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.TableTag;
import j2html.tags.specialized.TdTag;
import j2html.tags.specialized.ThTag;
import j2html.tags.specialized.TrTag;

public class VDPRecordsHTMLWriter {
	
	private static String currentRecType = "";
	private static final String filename = "VDP.html";
	private static FileWriter fw;
	private static final String POPUP = "w3-modal-content w3-animate-zoom";
	static String toggleScript = "function toggleDiv(divname) {" +
			"var ele = document.getElementById(divname);" +
			"if (ele.style.display == \"none\") {" +
			"ele.style.display = \"block\";" +
			"}" +
			"else {" +
			"ele.style.display = \"none\";" +
			"}" +
			"}";

	public static void writeFromRecordNodes(Path cwd, RecordNode root) {

		File output = cwd.resolve(filename).toFile();
		try {
			
			fw = new FileWriter(output);
			fw.write(
					html(
							head(
									meta().withContent("text/html; charset=UTF-8"),
									link().withRel("stylesheet").withType("text/css").withHref("https://www.w3schools.com/w3css/4/w3.css"),
									link().withRel("stylesheet").withType("text/css").withHref("https://www.w3schools.com/lib/w3-colors-flat.css"),
									link().withRel("stylesheet").withType("text/css").withHref("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"),
									script(join(toggleScript)).withLang("Javascript")
							),
							body(
									bodyContent(root)
							)).withStyle("overflow-x: scroll").renderFormatted());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static DivTag bodyContent(RecordNode root) {
		return div(
						h2("VDP Records"),
						recordTables(root)
				).withClass("w3-container");
	}
	

	private static DomContent recordTables(RecordNode root) {
		return div(each(root.getChildren(), rec  -> getRecordTable(rec)) );
	}

	private static DivTag getRecordTable(FieldNodeBase rec) {
		String recType = ((RecordTypeNode)rec).getName();
		return div( h2(recType) ,
					getTableDetails(rec)
		);

	}

	private static DomContent getTableDetails(FieldNodeBase rec) {
		return table(
				tbody(
						getHeaderRow(rec.getChildren().get(0)),
						each(rec.getChildren(), r -> getRow(r))))
				.withClass("w3-table w3-striped w3-border");
	}

	private static TrTag getRow(FieldNodeBase r) {
		return tr( each(r.getChildren(), n -> rowEntry(n)) );
	}

	private static TdTag rowEntry(FieldNodeBase n) {
		switch(n.getType()) {
			case NUMBERFIELD:
				return td(Integer.toString( ((NumericFieldNode)n).getValue() ));
			case STRINGFIELD:
				return td(((StringFieldNode)n).getValue() );
			case RECORD:
			case RECORDTYPE:
			default:
				return td("Bad Value");
		}
	}

	private static TrTag getHeaderRow(FieldNodeBase fieldNodeBase) {
		return tr( each(fieldNodeBase.getChildren(), n -> headerElement(n)) );
	}

	private static ThTag headerElement(FieldNodeBase n) {
		return th(n.getName());
	}

	private static String getRecordTypeName(String typeStr) {
		String[] typeParts = typeStr.split("_");
		return "Record Type: " + typeParts[0];
	}

	public String getFileName() {
		return filename;
	}

}
