package org.genevaers.genevaio.wbxml;

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

import com.google.common.flogger.FluentLogger;

import difflib.StringUtills;

public class ViewRecordParser extends RecordParser {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private boolean enabled;
	private ViewDefinition vd;
	private ViewNode vn;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			switch (part) {
				case "VIEWID":
					String id = reader.getText();
					componentID = Integer.parseInt(id);
					vd = new ViewDefinition();
					vd.setComponentId(componentID);
					if(Repository.isViewEnabled(componentID)) {
						vn = Repository.getViewNodeMakeIfDoesNotExist(vd);
						enabled = true;
					} else {
						enabled = false;
						logger.atInfo().log("View %d not enabled", componentID);
					}
					break;

				case "NAME":
					vd.setName(text);
					break;

				case "VIEWSTATUSCD":
					vd.setStatus(ViewStatus.fromdbcode(text.trim()));
					break;

				case "VIEWTYPECD":
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
					vd.setOutputPageSizeMax((short)Integer.parseInt(text.trim()));
					break;
				case "LINESIZE":
					vd.setOutputLineSizeMax((short)Integer.parseInt(text.trim()));
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
				case "CONTROLRECID":
					vd.setControlRecordId(Integer.parseInt(text.trim()));
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
					if(enabled) {
						vn.setFormatFilterLogic(removeBRLineEndings(text));
					}
					break;
				case "CREATEDUSERID":
				case "LASTMODUSERID": //Last will overwrite the created if set
					vd.setOwnerUser(text);
					break;
				
				default:
					// logger.atInfo().log(reader.getText());
					break;
			}
		}

	}
}
