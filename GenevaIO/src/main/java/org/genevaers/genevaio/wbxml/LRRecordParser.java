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
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.enums.LrStatus;

/**
 * This class will parse a LogicalRecord Record element into a
 * LogicalRecordTransfer object.
 */
public class LRRecordParser extends RecordParser {

	private LogicalRecord lr;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			switch (part) {
				case "LOGRECID":
					lr = new LogicalRecord();
					componentID = Integer.parseInt(text);
					lr.setComponentId(componentID);
					Repository.addLogicalRecord(lr);
					lr.setLookupExitID(0);
					break;
				case "NAME":
					componentName = text;
					lr.setName(componentName);
					break;
				case "LRSTATUSCD":
					lr.setStatus(LrStatus.fromdbcode(text));
					break;
				case "LOOKUPEXITID":
					lr.setLookupExitID(Integer.parseInt(text));
					break;
				case "LOOKUPEXITSTARTUP":
					lr.setLookupExitParams(text);
					break;
				case "CREATEDTIMESTAMP":
					created = text;
					break;
				case "LASTMODTIMESTAMP":
					lastMod = text;
					break;
				default:
					break;
			}
		}
	}
}
