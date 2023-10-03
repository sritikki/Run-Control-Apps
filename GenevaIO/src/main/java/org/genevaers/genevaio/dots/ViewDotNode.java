package org.genevaers.genevaio.dots;

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
import java.util.Map;

import org.genevaers.repository.components.LogicalFile;

public class ViewDotNode extends DotBase {
	private Map<Integer, LogicalFileDotNode> lfs = new HashMap<Integer, LogicalFileDotNode>();

	public ViewDotNode(String name, int id) {
		super.id = id;
		super.name = name;
		shape = "oval";
		colour = "PaleGreen";
	}

	@Override
	protected String getDOTID() {
		return "V_" + id;
	}

	@Override
	public String getNodeString() {
	    String dot = getDOTID() 
	    		+ "[label=\"" + getDisplay()
	    		+ "\" URL=\"views/v" + id + ".dot.svg"
	    		+ "\" shape="  + shape 
	    		+ " color="	   + colour
	    		+ " style=filled tooltip=\"" + tip
	       	    + "\"]\n";
	    return dot;
	}

	public void addSourceLF(LogicalFile srclf) {
		if(srclf != null) {
			LogicalFileDotNode lfdn = lfs.get(srclf.getID());
			if(lfdn == null) {
				lfdn = new LogicalFileDotNode(srclf.getName(), srclf.getID());
				lfs.put(srclf.getID(), lfdn);
			}
		} else {
			System.out.println("srclf null");
		}
	}
}
