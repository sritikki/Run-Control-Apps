package org.genevaers.genevaio.vdpxml;

import java.util.Iterator;



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
import org.genevaers.repository.components.OutputFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.UserExit;
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.repository.components.enums.OutputMedia;
import org.genevaers.repository.components.enums.ViewStatus;
import org.genevaers.repository.components.enums.ViewType;
import org.xml.sax.Attributes;

public class ViewRecordParser extends BaseParser {

	private ViewDefinition vd;
	private ViewNode vn;
	private OutputFile outfile;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		switch (qName.toUpperCase()) {
			case "PARTITION":
				int id = Integer.parseInt(attributes.getValue("ID"));
				generateExtractOutputLogic(id);
				break;
			default:
				break;
		}
	}		

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

			case "FILENUMBER":
				short efn = (short) Integer.parseInt(text.trim());
				vd.setExtractFileNumber(efn);
				break;

			case "MEDIA":
				vd.setOutputMedia(OutputMedia.fromdbcode(text.trim()));
				break;

			case "OUTPUTLRID":
				vd.setOutputLrId(Integer.parseInt(text.trim()));
				break;

			case "LFPFASSOCID":
				RecordParserData.viewOutlfpf.put(componentID, Integer.parseInt(text.trim()));
				break;

			case "LINESPERPAGE":
				vd.setOutputPageSizeMax((short) Integer.parseInt(text.trim()));
				break;
			case "MAXCHARSPERLINE":
				vd.setOutputLineSizeMax((short) Integer.parseInt(text.trim()));
				break;
			case "ZEROSUPPRESSIND":
				vd.setZeroValueRecordSuppression(text.equals("0") ? false : true);
				break;
			case "EXTRACTMAXRECCNT":
				vd.setExtractMaxRecCount(Integer.parseInt(text.trim()));
				break;
			case "ENABLEAGGREGATION":
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

	private void generateExtractOutputLogic(int id) {
		String writeLogic = "";
		switch(vn.getViewDefinition().getViewType()) {
			case COPY:
				if(id > 0) {
					writeLogic = String.format("WRITE(SOURCE=INPUT, %s", getFileParm(vn, id));
				} else {
					writeLogic = "WRITE(SOURCE=INPUT,DEST=DEFAULT)";
				}
				break;
			case SUMMARY:
			case DETAIL:
				writeLogic = String.format("WRITE(SOURCE=VIEW,DEST=EXT=%03d", vn.getViewDefinition().getExtractFileNumber());
				writeLogic += getWriteParm(vn) + ")";
				break;
			case EXTRACT:
				if(vn.getOutputFile().getComponentId() > 0) {
					writeLogic = String.format("WRITE(SOURCE=DATA, %s", getFileParm(vn, id));
				} else {
					writeLogic = "WRITE(SOURCE=DATA,DEST=DEFAULT)";
				}
				break;
			default:
				//Error case
				break;
			
		}
		Iterator<ViewSource> vsi = vn.getViewSourceIterator();
		while (vsi.hasNext()) {
			ViewSource vs = vsi.next();
			vs.setExtractOutputLogic(writeLogic);
		}
	}

	private String getWriteParm(ViewNode vn) {
		String exitStr = "";
		int exitID = vn.getViewDefinition().getWriteExitId();
		if (exitID != 0) {
			UserExit ex = Repository.getUserExits().get(exitID);
			String wparms = vn.getViewDefinition().getWriteExitParams();
			if(wparms.length() > 0) {
				exitStr += String.format(",USEREXIT=({%s, \"%s\"})", ex.getName(), wparms);
			} else {
				exitStr += String.format(",USEREXIT=({%s})", ex.getName());
			}
		}
		return exitStr;
	}

	private String getFileParm(ViewNode vn, int partId) {
		String fileStr;
		PhysicalFile pf = Repository.getPhysicalFiles().get(partId);
		fileStr = "DEST=FILE={" + pf.getLogicalFilename() + "." + pf.getName() + "}" + getWriteParm(vn);
		return fileStr;
	}
	

}
