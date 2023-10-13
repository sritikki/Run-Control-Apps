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
import org.genevaers.repository.components.LogicalRecord;

/**
 * This class will parse a LR-IndexField Record element into a
 * LRIndexFieldTransfer object.
 */
public class LRIndexFieldRecordParser extends RecordParser {

	private int sequenceNumber;
	private int fieldID;

	private LogicalRecord currentLR;

	private int ndxid;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			LRIndex ndx;
			switch (part) {
				case "LRINDEXID":
					ndxid = Integer.parseInt(text);
					break;
				case "FLDSEQNBR":
					sequenceNumber = Integer.parseInt(text);
					break;
				case "LRFIELDID":
					fieldID = Integer.parseInt(text);
					ndx = Repository.getIndexes().get(ndxid);
					if(ndx == null) {
						ndx = new LRIndex();
					}
					currentLR = Repository.getLogicalRecords().get(ndx.getLrId());
					ndx.setComponentId(ndxid);
					ndx.setFieldID(fieldID);
					ndx.setKeyNumber((short) sequenceNumber);
					ndx.setLrId(currentLR.getComponentId());
					ndx.setEffectiveDateEnd(false);
					ndx.setEffectiveDateStart(false);
					ndx.setName("Primary");
					currentLR.addToIndexBySeq(ndx);
					break;
				default:
					break;
			}
		}
	}

	@Override 
	public void endRecord() {
		//were there any effective date indexes?
		addEffStartIndex();
		addEffEndIndex();
	}

	private void addEffStartIndex() {
		LRIndex effStartNdx = effdateStarts.get(ndxid);
		if(effStartNdx != null) {
			effStartNdx.setKeyNumber((short) (currentLR.getValuesOfIndexBySeq().size() + 1));
			currentLR.addToIndexBySeq(effStartNdx);
		}		
	}

	private void addEffEndIndex() {
		LRIndex effEndNdx = effdateEnds.get(ndxid);
		if(effEndNdx != null) {
			effEndNdx.setKeyNumber((short) (currentLR.getValuesOfIndexBySeq().size() + 1));
			currentLR.addToIndexBySeq(effEndNdx);
		}
		
	}
}
