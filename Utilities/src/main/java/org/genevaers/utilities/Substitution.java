package org.genevaers.utilities;

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


public class Substitution {
	
	private String replace;
	private String with;
	private Integer lineStart = null;
	private Integer lineStop = null;
	
	public Substitution(String replace, String with) {
		this.replace = replace;
		this.with = with;
	}

	public Substitution(String replace, String with, Integer lineStart, Integer lineStop) {
		this.replace = replace;
		this.with = with;
		this.lineStart = lineStart;
		this.lineStop = lineStop;
	}
	
	public String getReplace() {
		return replace;
	}

	public String getWith() {
		return with;
	}		

	public Integer getLineStart() {
		return lineStart;
	}

	public Integer getLineStop() {
		return lineStop;
	}
}
