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
import org.genevaers.repository.components.ControlRecord;

public class CRRecordParser extends RecordParser {

	private ControlRecord cr;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			switch (part) {
				case "CONTROLRECID":
					cr = new ControlRecord();
					componentID = Integer.parseInt(text);
					break;
				case "NAME":
					cr.setComponentId(componentID);
					cr.setName(text);
					Repository.getControlRecords().add(cr, componentID, text);
					break;
				case "FIRSTMONTH":
					short val = (short) Integer.parseInt(text);
					cr.setFirstFiscalMonth(val);
					break;
				case "LOWVALUE":
					val = (short) Integer.parseInt(text);
					cr.setBeginningPeriod(val);
					break;
				case "HIGHVALUE":
					val = (short) Integer.parseInt(text);
					cr.setEndingPeriod(val);
					break;
				case "COMMENTS":
					cr.setDescription(text);
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
