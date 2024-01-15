package org.genevaers.genevaio.vdpxml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

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

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;

public class ViewSourceRecordParser extends BaseParser {

	private ViewSource vs;
	private int lrlfAssocid;
	private int lfpfAssocid;

	private int viewid;

	@Override
	public void addElement(String name, String text) {
		switch (name) {
			case "VIEWSOURCEID":
				vs = new ViewSource();
				componentID = Integer.parseInt(text.trim());
				vs.setComponentId(componentID);
				break;
			case "SRCSEQNBR":
				short s = (short) Integer.parseInt(text.trim());
				vs.setSequenceNumber(s);
				ViewNode vn = Repository.getViews().get(viewid);
				vn.addViewSource(vs);
				break;
			case "INLRLFASSOCID":
				// This assoc id doesn't make any sense until later
				// when we parse the lflf associations
				// Need to preserve for a fixup later
				lrlfAssocid = Integer.parseInt(text.trim());
				RecordParserData.vs2lrlf.put(componentID, lrlfAssocid);
				break;
			case "VIEWID":
				viewid = Integer.parseInt(text.trim());
				vs.setViewId(viewid);
				break;
			case "EXTRACTFILTLOGIC":
				vs.setExtractFilter(removeBRLineEndings(text));
				break;

			// Save this too for later fix ups
			case "OUTLFPFASSOCID":
				lfpfAssocid = Integer.parseInt(text);
				RecordParserData.vs2lfpf.put(componentID, lfpfAssocid);
				break;
			case "WRITEEXITID":
				vs.setWriteExitId(Integer.parseInt(text.trim()));
				break;
			case "WRITEEXITPARM":
				vs.setWriteExitParams(text);
				break;
			case "EXTRACTOUTPUTLOGIC":
				vs.setExtractOutputLogic(removeBRLineEndings(text));
				break;
			default:
				break;
		}
	}
}
