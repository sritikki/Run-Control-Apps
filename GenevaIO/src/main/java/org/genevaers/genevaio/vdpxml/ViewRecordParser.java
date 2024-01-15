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
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.enums.OutputMedia;
import org.genevaers.repository.components.enums.ViewStatus;
import org.genevaers.repository.components.enums.ViewType;

import difflib.StringUtills;

public class ViewRecordParser extends BaseParser {

	private ViewDefinition vd;
	private ViewNode vn;

	@Override
	public void addElement(String name, String text) {
		switch (name.toUpperCase()) {
			case "NAME":
				vd = new ViewDefinition();
				vd.setComponentId(componentID);
				vn = Repository.getViewNodeMakeIfDoesNotExist(vd);
				vd.setName(text);
				break;

			case "STATUS":
				vd.setStatus(ViewStatus.fromdbcode(text.trim()));
				break;

			case "TYPE":
				vd.setViewType(ViewType.fromdbcode(text.trim()));
				break;

			case "EXTRACTFILEPARTNBR":
				short efn = (short) Integer.parseInt(text.trim());
				vd.setExtractFileNumber(efn);
				break;

			case "OUTPUTMEDIACD":
				vd.setOutputMedia(OutputMedia.fromdbcode(text.trim()));
				break;

			case "OUTPUTLRID":
				vd.setOutputLrId(Integer.parseInt(text.trim()));
				break;

			case "LFPFASSOCID":
				RecordParserData.viewOutlfpf.put(componentID, Integer.parseInt(text.trim()));
				break;

			case "PAGESIZE":
				vd.setOutputPageSizeMax((short) Integer.parseInt(text.trim()));
				break;
			case "LINESIZE":
				vd.setOutputLineSizeMax((short) Integer.parseInt(text.trim()));
				break;
			case "ZEROSUPPRESSIND":
				vd.setZeroValueRecordSuppression(text.equals("0") ? false : true);
				break;
			case "EXTRACTMAXRECCNT":
				vd.setExtractMaxRecCount(Integer.parseInt(text.trim()));
				break;
			case "EXTRACTSUMMARYIND":
				vd.setExtractSummarized(text.equals("0") ? false : true);
				break;
			case "EXTRACTSUMMARYBUF":
				vd.setMaxExtractSummaryRecords(Integer.parseInt(text.trim()));
				break;
			case "OUTPUTMAXRECCNT":
				vd.setOutputMaxRecCount(Integer.parseInt(text.trim()));
				break;
			case "WRITEEXITID":
				vd.setWriteExitId(Integer.parseInt(text.trim()));
				break;
			case "WRITEEXITSTARTUP":
				vd.setWriteExitParams(text.trim());
				break;
			case "FORMATEXITID":
				vd.setFormatExitId(Integer.parseInt(text.trim()));
				break;
			case "FORMATEXITSTARTUP":
				vd.setFormatExitParams(text.trim());
				break;
			case "DELIMHEADERROWIND":
				vd.setGenerateDelimitedHeader(text.equals("0") ? false : true);
				break;
			case "FORMATFILTLOGIC":
				vn.setFormatFilterLogic(removeBRLineEndings(text));
				break;
			case "OWNERUSER":
				vd.setOwnerUser(text);
				break;

			default:
				// logger.atInfo().log(reader.getText());
				break;
		}
	}

    public void setContolRecord(int crid) {
		vd.setControlRecordId(crid);
    }
}
