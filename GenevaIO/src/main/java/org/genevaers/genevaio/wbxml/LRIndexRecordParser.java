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
import org.genevaers.repository.components.LRIndex;

/**
 * This class will parse a LR-Index Record element into a
 * LRIndexTransfer object.
 */
public class LRIndexRecordParser extends RecordParser {

	private int indexID;
	private int lrid;
	private int effStartFld;
	private int effEndFld;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			switch (part) {
				case "LRINDEXID":
					indexID = Integer.parseInt(text);
					break;
				case "LOGRECID":
					lrid = Integer.parseInt(text);
					LRIndex pndx = new LRIndex();
					pndx.setComponentId(indexID);
					pndx.setEffectiveDateEnd(false);
					pndx.setEffectiveDateStart(false);
					pndx.setLrId(lrid);
					pndx.setKeyNumber((short)1); //This will be overwritten if needed
					Repository.addLRIndex(pndx);
					break;
				case "EFFDATESTARTFLDID":
					effStartFld = Integer.parseInt(text);
					if (effStartFld > 0) {
						LRIndex ndx = new LRIndex();
						ndx.setComponentId(indexID);
						ndx.setFieldID(effStartFld);
						ndx.setLrId(lrid);
						ndx.setEffectiveDateEnd(false);
						ndx.setEffectiveDateStart(true);
						ndx.setName("Start Eff Date");
						RecordParserData.effdateStarts.put(indexID, ndx);
					}
					break;
				case "EFFDATEENDFLDID":
					effEndFld = Integer.parseInt(text);
					if (effEndFld > 0) {
						LRIndex ndx = new LRIndex();
						ndx.setComponentId(indexID);
						ndx.setFieldID(effEndFld);
						ndx.setLrId(lrid);
						ndx.setEffectiveDateEnd(true);
						ndx.setEffectiveDateStart(false);
						ndx.setName("End Eff Date");
						RecordParserData.effdateEnds.put(indexID, ndx);
					}
					break;
				default:
					break;
			}
		}
	}
}
