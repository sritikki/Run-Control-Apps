package org.genevaers.genevaio.wbxml;

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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;

/**
 * This class will parse a LRField Record element into a
 * LRFieldTransfer object.
 */
public class LRFieldRecordParser extends RecordParser {

	private LRField lrField;
	private int currentLrId;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			int lrid;
			switch (part) {
				case "LRFIELDID":
					lrField = new LRField();
					lrField.setComponentId(Integer.parseInt(text));
					break;
				case "LOGRECID":
					lrid = Integer.parseInt(text);
					if(lrid != currentLrId) {
						currentLrId = lrid;
					}
					lrField.setLrID(lrid);
					break;
				case "NAME":
					lrField.setName(text.trim());
					Repository.addLRField(lrField);
					break;
				case "DBMSCOLNAME":
					lrField.setDbColName(text.trim());
					break;
				case "FIXEDSTARTPOS":
					short s = (short) Integer.parseInt(text);
					lrField.setStartPosition(s);
					break;
				case "ORDINALPOS":
					s = (short) Integer.parseInt(text);
					lrField.setOrdinalPosition(s);
					break;
				case "ORDINALOFFSET":
					s = (short) Integer.parseInt(text);
					lrField.setOrdinalOffset(s);
				default:
					break;
			}
		}
	}

}
