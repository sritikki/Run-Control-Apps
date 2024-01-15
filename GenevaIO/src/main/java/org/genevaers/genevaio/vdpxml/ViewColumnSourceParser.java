package org.genevaers.genevaio.vdpxml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.genevaers.repository.Repository;

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

import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.enums.ColumnSourceType;

public class ViewColumnSourceParser extends BaseParser {

	private ViewColumnSource vcs;

	@Override
	public void addElement(String name, String text) {
		switch (name) {
			case "VIEWCOLUMNSOURCEID":
				vcs = new ViewColumnSource();
				vcs.setComponentId(Integer.parseInt(text));
				break;
			case "VIEWSOURCEID":
				vcs.setViewSourceId(Integer.parseInt(text));
				break;
			case "VIEWCOLUMNID":
				vcs.setColumnID(Integer.parseInt(text));
				break;
			case "VIEWID":
				int viewID = Integer.parseInt(text);
				vcs.setViewId(viewID);
				Repository.getViews().get(viewID).addViewColumnSource(vcs);
				break;
			case "SOURCETYPEID":
				vcs.setSourceType(ColumnSourceType.values()[Integer.parseInt(text.trim())]);
				break;
			case "CONSTVAL":
				vcs.setSrcValue(text);
				break;
			case "LOOKUPID":
				vcs.setSrcJoinId(Integer.parseInt(text));
				break;
			case "LRFIELDID":
				vcs.setViewSrcLrFieldId(Integer.parseInt(text));
				break;
			case "EXTRACTCALCLOGIC":
				vcs.setLogicText(removeBRLineEndings(text));
				break;
			case "SORTTITLELOOKUPID":
				vcs.setSortTitleLookupId(Integer.parseInt(text));
				break;
			case "SORTTITLELRFIELDID":
				vcs.setSortTitleFieldId(Integer.parseInt(text));
				break;
			default:
				break;
		}
	}
}
