package org.genevaers.genevaio.dots;

import org.genevaers.repository.components.enums.DataType;

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




public class DotWriter {

	protected String addSubgraph(String clstr, String name) {
		return "\nsubgraph cluster_" + clstr + " { label=\"" + name + "\" node [shape=plaintext]\n";
	}
	
	protected String getFormatColour(DataType f) {
		switch (f)
		{
		case ALPHA:
			return "pink";
		case ALPHANUMERIC:
			return "pink";
		case ZONED:
			return "orange";
		case PACKED:
			return "yellow";
		case PSORT:
			return "gold";
		case BINARY:
			return "PaleGreen";
		case BSORT:
			return "LimeGreen";
		case BCD:
			return "SkyBlue";
		case MASKED:
			return "DeepSkyBlue";
		case  EDITED:
			return "purple1";
		case FLOAT:
			return "red";
		case GENEVANUMBER:
			return "red";
		default:
			return null;
		}
	}

}
