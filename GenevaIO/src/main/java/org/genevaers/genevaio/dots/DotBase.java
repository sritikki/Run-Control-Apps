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


public abstract class DotBase {
	protected  String name;
	protected int id;
	protected String displayStr = "";
	protected String tip;
	protected String colour;
	protected String shape;
	
	public String getNodeString() {
	    String dot = getDOTID() 
	    		+ "[label=\"" + getDisplay()
	    		+ "\" shape="  + shape 
	    		+ " color="	   + colour
	    		+ " style=filled tooltip=\"" + tip
	       	    + "\"]\n";
	    return dot;
	}

	protected String getDisplay() {
		return (displayStr.length() > 0) ? displayStr : name;
	}

	abstract protected String  getDOTID();
}
