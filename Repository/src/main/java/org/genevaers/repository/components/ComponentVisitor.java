package org.genevaers.repository.components;

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


public interface ComponentVisitor {
	public void visit(ControlRecord ctrlRecord);
	public void visit(PhysicalFile pfRecord);
	public void visit(UserExit uerRecord);
	public void visit(LogicalRecord lr);
	public void visit(LRField lrf);
	public void visit(LRIndex lri);
	public void visit(LookupPath lp);
	public void visit(LookupPathStep lps);
	public void visit(LookupPathKey lpk);
	public void visit(ViewNode v);
	public void visit(ViewColumn vc);
	public void visit(ViewSortKey vsk);
	public void visit(ViewSource vs);
	public void visit(ViewColumnSource vcs);
}
