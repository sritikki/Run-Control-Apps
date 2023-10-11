package org.genevaers.runcontrolgenerator.compilers;

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


import java.util.Iterator;

import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ColumnAST;
import org.genevaers.compilers.extract.astnodes.ColumnAssignmentASTNode;
import org.genevaers.compilers.extract.astnodes.EndOfSetASTNode;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.FieldReferenceAST;
import org.genevaers.compilers.extract.astnodes.LFAstNode;
import org.genevaers.compilers.extract.astnodes.RecordCountAST;
import org.genevaers.compilers.extract.astnodes.StringAtomAST;
import org.genevaers.compilers.extract.astnodes.ViewColumnSourceAstNode;
import org.genevaers.compilers.extract.astnodes.ViewSourceAstNode;
import org.genevaers.compilers.extract.astnodes.WriteASTNode;
import org.genevaers.compilers.extract.astnodes.WriteExtractNode;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.repository.components.enums.ColumnSourceType;
import org.genevaers.repository.jltviews.JLTView;

public class JoinViewGenerator {

    public LFAstNode makeLFNode(int lfid) {
		LFAstNode lfNode = (LFAstNode)ASTFactory.getNodeOfType(ASTFactory.Type.LF);
		lfNode.setLogicalFile(Repository.getLogicalFiles().get(lfid));
        return lfNode;
    }

    public ViewSourceAstNode addViewToLFNode(JLTView jv, LFAstNode lfNode, int joinNumber) {
        jv.buildREDLRs(joinNumber);
        ViewNode vn = jv.buildRefViewForLF(lfNode.getLogicalFile().getID());
        ViewSourceAstNode vsnode = (ViewSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWSOURCE);
        ViewSource vs = vn.getViewSource((short)1); //There will be only one
        vsnode.setViewSource(vs);
        lfNode.addChildIfNotNull(vsnode);
        addViewColumnSourceNodes(vsnode);
        addWriteNode(vsnode, jv.getDdNum());
        return vsnode;
    }

    public void addEndofSet(ViewSourceAstNode vsnode) {
        EndOfSetASTNode eos = (EndOfSetASTNode) ASTFactory.getNodeOfType(ASTFactory.Type.EOS);
        eos.setLfID(vsnode.getViewSource().getSourceLFID());
        vsnode.addChildIfNotNull(eos);
    }

    private void addWriteNode(ViewSourceAstNode vsnode, int ddnum) {
        WriteASTNode wrNode = (WriteASTNode) ASTFactory.getNodeOfType(ASTFactory.Type.WRITE);
        wrNode.setViewSource(vsnode.getViewSource());
        wrNode.setFileNumber(ddnum);
        vsnode.addChildIfNotNull(wrNode);
        WriteExtractNode wen =  (WriteExtractNode) ASTFactory.getNodeOfType(ASTFactory.Type.WRITEEXTRACT);
        wen.setFileNumber(ddnum);
        wrNode.addChildIfNotNull(wen);
    }

    void addRecordCountIncrement(ViewSourceAstNode vsnode) {
        vsnode.addChildIfNotNull((RecordCountAST) ASTFactory.getNodeOfType(ASTFactory.Type.RECORD_COUNT));
    }

    private void addViewColumnSourceNodes(ViewSourceAstNode vsnode) {
      
        /* the source side of these assignmends should be from the original LR... not the genLR 
        * we need to use reverse mappling from the gen LR field to the orginal LR field.
        */
		Iterator<ViewColumnSource> vcsi = vsnode.getViewSource().getIteratorForColumnSourcesByNumber();
		while(vcsi.hasNext()) {
			ViewColumnSource vcs = vcsi.next();
			ViewColumnSourceAstNode vcsn = (ViewColumnSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWCOLUMNSOURCE);
			vcsn.setViewColumnSource(vcs);
			vsnode.addChildIfNotNull(vcsn);
			vcsn.addChildIfNotNull(addColumnAssignment(vcs));
		}
    }

    public ExtractBaseAST addColumnAssignment(ViewColumnSource vcs) { 
        ColumnAssignmentASTNode casnode = (ColumnAssignmentASTNode) ASTFactory.getNodeOfType(ASTFactory.Type.COLUMNASSIGNMENT);
        ViewNode view = Repository.getViews().get(vcs.getViewId());
        ColumnAST colNode = (ColumnAST)ASTFactory.getColumnNode(view.getColumnNumber(vcs.getColumnNumber()));
        if(vcs.getSourceType() == ColumnSourceType.EVENTLR) {
            addFieldRef(casnode, vcs);
        } else if (vcs.getSourceType() == ColumnSourceType.CONSTANT) {
            addConstant(casnode, vcs);
        }
        casnode.addChildIfNotNull(colNode);
        return casnode;
    }

    private void addConstant(ColumnAssignmentASTNode casnode, ViewColumnSource vcs) {
        StringAtomAST sa =  (StringAtomAST) ASTFactory.getNodeOfType(ASTFactory.Type.STRINGATOM);
        sa.setValue(vcs.getSrcValue());
        casnode.addChildIfNotNull(sa);
    }

    private void addFieldRef(ColumnAssignmentASTNode casnode, ViewColumnSource vcs) {
        FieldReferenceAST fieldRef = (FieldReferenceAST) ASTFactory.getNodeOfType(ASTFactory.Type.LRFIELD);
        fieldRef.setRef(Repository.getFields().get(vcs.getViewSrcLrFieldId())); 
        casnode.addChildIfNotNull(fieldRef);
    }

}
