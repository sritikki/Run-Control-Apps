package org.genevaers.genevaio.vdpxml;

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
import org.genevaers.repository.components.UserExit;
import org.genevaers.repository.components.enums.ExitType;
import org.genevaers.repository.components.enums.ProgramType;
import org.xml.sax.Attributes;

/**
 * This class will parse a Procedure Record element into a
 * UserExitRoutineTransfer object.
 */
public class ExitRecordParser extends BaseParser {

	private UserExit userExit;

	public ExitRecordParser() {
	}

	public UserExit getUserExit() {
		return userExit;
	}

	@Override
	public void addElement(String name, String text) {
		switch (name) {
			case "EXITID":
				userExit = new UserExit();
				componentID = Integer.parseInt(text);
				userExit.setComponentId(componentID);
				break;
			case "NAME":
				userExit.setName(text);
				Repository.getUserExits().add(userExit, componentID, text);
				break;
			case "MODULEID":
				userExit.setExecutable(text);
				break;
			case "EXITTYPECD":
				userExit.setExitType(ExitType.fromdbcode(text.trim()));
				break;
			case "PROGRAMTYPECD":
				userExit.setProgramType(ProgramType.fromdbcode(text.trim()));
				break;
			case "OPTIMIZEIND":
				userExit.setOptimizable(text.equals("1") ? true : false);
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
