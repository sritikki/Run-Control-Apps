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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.genevaers.repository.components.LRIndex;
import org.genevaers.repository.components.LookupPathKey;

import com.google.common.flogger.FluentLogger;

// import com.ibm.safr.we.constants.DateFormat;
// import com.ibm.safr.we.data.transfer.SAFRTransfer;
// import com.ibm.safr.we.exceptions.SAFRValidationException;

/**
 * This abstract class represent a parser which converts Record XML
 * elements into component transfer objects. It defines one public method which
 * provides the algorithm for retrieving the elements, parsing them and
 * returning the transfer objects. Concrete subclasses must implement two
 * abstract methods which define component type-specific behavior.
 */
abstract public class RecordParser {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	protected int componentID;
	protected String componentName;
	protected String created;
	protected String lastMod;
	protected String elementName;
	protected static TreeMap<String, CatalogEntry> catalog;

	protected static Map<Integer, LRIndex> effdateStarts = new HashMap<>();
	protected static Map<Integer, LRIndex> effdateEnds = new HashMap<>();

	protected static Map<Integer, List<LookupPathKey>> lookupStepKeys = new HashMap<>();

	public RecordParser() {
	}

	public static String removeBRLineEndings(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("(?i)\\<BR\\/\\>", "");
	}

	public String getCreated() {
		return created;
	}

	public String getLastMod() {
		return lastMod;
	}

	public int getComponentID() {
		return componentID;
	}

	public void setComponentID(int componentID) {
		this.componentID = componentID;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	abstract public void parseRecord(XMLStreamReader reader) throws XMLStreamException;

	public void endRecord() {
		// override for special handling
	}

	public static void clearAndInitialise() {
		//protected static TreeMap<String, CatalogEntry> catalog;

		effdateStarts = new HashMap<>();
		effdateEnds = new HashMap<>();
	
		lookupStepKeys = new HashMap<>();	
		RecordParserData.clearAndInitialise();
	}

}
