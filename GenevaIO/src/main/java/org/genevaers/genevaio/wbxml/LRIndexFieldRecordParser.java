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
			LRIndex existingIndex;
			switch (part) {
				case "LRINDEXID":
					ndxid = Integer.parseInt(text);
					break;
				case "FLDSEQNBR":
					sequenceNumber = Integer.parseInt(text);
					break;
				case "LRFIELDID":
					fieldID = Integer.parseInt(text);
					existingIndex = Repository.getIndexes().get(ndxid);
					if(existingIndex == null) {
						existingIndex = new LRIndex();
						//This should not happen!!!
					}
					currentLR = Repository.getLogicalRecords().get(existingIndex.getLrId());
					LRIndex ndxToBeUpdated;
					if(existingIndex.getKeyNumber() == sequenceNumber) {
						ndxToBeUpdated = existingIndex;
					} else {
						ndxToBeUpdated = new LRIndex();
					}
					ndxToBeUpdated.setComponentId(ndxid);
					ndxToBeUpdated.setFieldID(fieldID);
					ndxToBeUpdated.setKeyNumber((short) sequenceNumber);
					ndxToBeUpdated.setLrId(currentLR.getComponentId());
					ndxToBeUpdated.setEffectiveDateEnd(false);
					ndxToBeUpdated.setEffectiveDateStart(false);
					ndxToBeUpdated.setName("Primary");
					currentLR.addToIndexBySeq(ndxToBeUpdated);
					break;
				default:
					break;
			}
		}
	}

	@Override 
	public void endRecord() {
	}
}
