package org.genevaers.genevaio.vdpxml;

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
public class LookupStepParser extends BaseParser {

	private LookupPathStep lookupStep;
	private LookupPath currenLookupPath;

	private int currentLookupId;
	private int stepNumber;
	private int sourceLrid;

	private boolean srcLR = false;

	@Override
	public void addElement(String name, String text) {
		switch (name.toUpperCase()) {
			case "LOGICALRECORDREF":
				if(srcLR) {
					lookupStep = new LookupPathStep();
					lookupStep.setStepNum(stepNumber);
					currenLookupPath = Repository.getLookups().get(currentLookupId);
					lookupStep.setSourceLRid(sourceLrid);
					currenLookupPath.addStep(lookupStep);
				}
				break;
			default:
				break;
		}
	}

    public void setLookupID(int id) {
		currentLookupId = id;
    }

    public void setStepNumber(int s) {
        stepNumber = s;
    }

	public void setLrRef(int lrid) {
		if(srcLR == false) {
			sourceLrid = lrid;
			srcLR = true;
		} else {
			srcLR = false;
			lookupStep.setTargetLRid(lrid);
		}
	}

}
