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
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.JustifyId;
import org.genevaers.repository.components.enums.SubtotalType;

public class ViewColumnRecordParser extends BaseParser {

	private ViewColumn vc;

	private int currentViewId = 0;

	private ViewNode currentViewNode;

	private int seqNum;

	@Override
	public void addElement(String name, String text) {
		switch (name.toUpperCase()) {
			case "NAME":
				currentViewNode = Repository.getViews().get(currentViewId);
				vc = currentViewNode.getColumnByID(componentID);
				break;
			case "AREA":
				vc = new ViewColumn();
				vc.setComponentId(componentID);
				vc.setName("");
				vc.setFieldName("");
				vc.setDetailPrefix("");
				vc.setSubtotalMask("");
				vc.setDateCode(DateCode.NONE);
				vc.setSubtotalType(SubtotalType.NONE);
				vc.setHeaderJustifyId(JustifyId.CENTER);
				currentViewNode = Repository.getViews().get(currentViewId);
				vc.setViewId(currentViewId);
				vc.setColumnNumber(seqNum);
				vc.setExtractArea(ExtractArea.fromdbcode(text.trim()));
				vc.setExtractAreaPosition((short) 1);
				vc.setStartPosition((short) 1);
				currentViewNode.addViewColumn(vc);
				break;
			case "DATATYPE":
				vc.setDataType(DataType.fromdbcode(text.trim()));
				if (vc.getDataType() == DataType.ALPHANUMERIC) {
					vc.setJustifyId(JustifyId.LEFT);
				} else {
					vc.setJustifyId(JustifyId.RIGHT);
				}
				break;
			case "SIGNEDDATA":
				vc.setSigned(text.equals("1") ? true : false);
				break;
			case "POSITION":
				short s = (short) Integer.parseInt(text.trim());
				vc.setStartPosition(s);
				vc.setExtractAreaPosition(s);
				break;
			case "LENGTH":
				s = (short) Integer.parseInt(text.trim());
				vc.setFieldLength(s);
				break;
			case "ORDINAL":
				s = (short) Integer.parseInt(text.trim());
				vc.setOrdinalPosition(s);
				break;
			case "DECIMALCNT":
				s = (short) Integer.parseInt(text.trim());
				vc.setDecimalCount(s);
				break;
			case "ROUNDING":
				s = (short) Integer.parseInt(text);
				vc.setRounding(s);
				break;
			case "FLDCONTENTCD":
				vc.setDateCode(DateCode.fromdbcode(text.trim()));
				break;
			case "ALIGNMENT":
				vc.setJustifyId(JustifyId.fromdbcode(text.trim()));
				break;
			case "DEFAULTVALUE":
				vc.setDefaultValue(text);
				break;
			case "VISIBLE":
				vc.setHidden(text.equals("1") ? false : true);
				break;
			case "SUBTOTALTYPECD":
				vc.setSubtotalType(SubtotalType.fromdbcode(text.trim()));
				break;
			case "SPACESBEFORECOLUMN":
				s = (short) Integer.parseInt(text.trim());
				vc.setSpacesBeforeColumn(s);
				break;
			case "EXTRAREAPOSITION":
				s = (short) Integer.parseInt(text);
				vc.setExtractAreaPosition(s);
				break;
			case "SUBTLABEL":
				vc.setSubtotalPrefix(text);
				break;
			case "MASK":
				vc.setReportMask(text);
				break;
			case "HEADERALIGNMENT":
				vc.setHeaderJustifyId(JustifyId.fromdbcode(text));
				break;
			case "HEADERLINE1":
				vc.setHeaderLine1(text);
				break;
			case "HEADERLINE2":
				vc.setHeaderLine2(text);
				break;
			case "HEADERLINE3":
				vc.setHeaderLine3(text);
				break;
			case "FORMATCALCLOGIC":
				vc.setColumnCalculation(removeBRLineEndings(text));
				break;
			default:
				break;
		}
	}

    public void setViewId(int vid) {
        currentViewId = vid;
    }

    public void setSequenceNumber(int s) {
		seqNum = s;
    }

}
