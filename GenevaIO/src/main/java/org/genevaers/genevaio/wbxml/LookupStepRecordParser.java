package org.genevaers.genevaio.wbxml;

import java.util.List;

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
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.components.LookupPathStep;

/**
 * This class will parse a Join-Target Record element into a
 * LookupPathStepTransfer object.
 */
public class LookupStepRecordParser extends RecordParser {

	private LookupPathStep lookupStep;
	private int stepID;

	private LookupPath currenLookupPath;

	private int currentLookupId;

	@Override
	public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			switch (part) {
				case "LOOKUPSTEPID":
					//steps will already have been added via sourc keys
					lookupStep = new LookupPathStep();
					stepID = Integer.parseInt(text);
					List<LookupPathKey> stepKeys = lookupStepKeys.get(stepID);
					for(LookupPathKey k : stepKeys) {
						lookupStep.addKey(k);
					}
					lookupStep.setId(stepID);
				break;
				case "LOOKUPID":
					int lkid = Integer.parseInt(text);
					if (lkid != currentLookupId) {
						currenLookupPath = Repository.getLookups().get(lkid);
						currentLookupId = lkid;
					} 
					currenLookupPath.addStep(lookupStep);
					break;
				case "STEPSEQNBR":
					int stepNum = Integer.parseInt(text);
					lookupStep.setStepNum(stepNum);
					break;
				case "SRCLRID":
					lookupStep.setSourceLRid(Integer.parseInt(text));
					break;
				case "LRLFASSOCID":
					lookupStep.setLrLfId(Integer.parseInt(text));
					break;
				default:
					break;
			}
		}
	}
}
