package org.genevaers.runcontrolgenerator.compilers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

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


import com.google.common.flogger.FluentLogger;


import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.ExtractColumnCompiler;
import org.genevaers.compilers.extract.ExtractFilterCompiler;
import org.genevaers.compilers.extract.ExtractOutputCompiler;
import org.genevaers.compilers.extract.astnodes.ExtractAST2Dot;
import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.ExtractFilterAST;
import org.genevaers.compilers.extract.astnodes.ExtractOutputAST;
import org.genevaers.compilers.extract.astnodes.LFAstNode;
import org.genevaers.compilers.extract.astnodes.PFAstNode;
import org.genevaers.compilers.extract.astnodes.ViewColumnSourceAstNode;
import org.genevaers.compilers.extract.astnodes.ViewSourceAstNode;
import org.genevaers.compilers.extract.emitters.CodeEmitter;
import org.genevaers.compilers.extract.emitters.LogicTableEmitter;
import org.genevaers.compilers.format.FormatAST2Dot;
import org.genevaers.compilers.format.FormatCompiler;
import org.genevaers.compilers.format.astnodes.ColumnCalculation;
import org.genevaers.compilers.format.astnodes.FormatASTFactory;
import org.genevaers.compilers.format.astnodes.FormatBaseAST;
import org.genevaers.compilers.format.astnodes.FormatFilterAST;
import org.genevaers.compilers.format.astnodes.FormatView;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.LogicGroup;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.ViewSourceWrapper;
import org.genevaers.runcontrolgenerator.utility.Status;
import org.genevaers.utilities.GenevaLog;

public class RepositoryCompiler {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	private List<LogicGroup> logicGroups;
	private ExtractBaseAST extractRoot;
	private ExtractBaseAST joinsRoot;
	private RunControlConfigration rcc;
	private LogicTableEmitter xltEmitter = new LogicTableEmitter();
	private LogicTableEmitter jltEmitter = new LogicTableEmitter();
	private FormatBaseAST formatRoot;
	private FormatView currentFormatView;
	private Status returnStatus;

	public RepositoryCompiler(RunControlConfigration r) {
		rcc = r;
	}

	public Status run() {
		GenevaLog.writeHeader("Compile the repository logic");
		ASTBase.clearErrorCount();
		buildTheAST();
		//once the AST is built we now have enough data
		//to generate the JLT
		//This has to be done before building the XLT
		//since the lookup numbers are changed when we generate the JLT
		emitLogicTablesIfEnabled();
		buildFormatAST();
		emitFormatRecordsIfNeeded();
		return returnStatus;
	}

	private void emitFormatRecordsIfNeeded() {
		//Walk the format root and build the format records
		//where are the calc stacks to be defined?
		//In the view node or the view column node
		if(ASTBase.getErrorCount() > 0) {
			logger.atSevere().log("Number of format logic errors found: %d", ASTBase.getErrorCount());
			returnStatus = Status.ERROR;
		} else {
			if(formatRoot != null) {
				Iterator<ASTBase> fi = formatRoot.getChildIterator();
				while(fi.hasNext()) {
					FormatBaseAST fn = (FormatBaseAST)fi.next();
					fn.emit(true);
				}
			}
		}
	}

	private void buildFormatAST() {
		formatRoot = FormatASTFactory.getNodeOfType(FormatASTFactory.Type.FORMATROOT);
		Iterator<ViewNode> vi = Repository.getViews().getIterator();
		while(vi.hasNext()){
			ViewNode v = vi.next();
			if(v.getViewDefinition().getComponentId() == 4954) {
				int bang = 0;
			}
			if(v.isFormat()) {
				boolean addedToFormatRequired = true;
				currentFormatView = (FormatView)FormatASTFactory.getNodeOfType(FormatASTFactory.Type.FORMATVIEW);
				currentFormatView.addView(v);
				if(v.getFormatFilterLogic() != null && v.getFormatFilterLogic().length() > 0) {
					formatRoot.addChildIfNotNull(currentFormatView);
					compileFormatFilter(v);
					addedToFormatRequired = false;
				}
				Repository.getFormatViews().add(v, v.getID(), v.getName());
				addColumnCalculations(v, addedToFormatRequired);
			}
		}
		writeFormatAstIfEnabled();
	}

	private void writeFormatAstIfEnabled() {
		if(rcc.isFormatDotEnabled()) {
        	FormatAST2Dot.write(formatRoot, Paths.get("target/Format.dot"));
		}
	}

	private void addColumnCalculations(ViewNode v, boolean addedToFormatRequired) {
		Iterator<ViewColumn> vci = v.getColumnIterator();
		while(vci.hasNext()) {
			ViewColumn vc = vci.next();
			if(vc.getColumnCalculation() != null && vc.getColumnCalculation().length() > 0) {
				ColumnCalculation cc = (ColumnCalculation)FormatASTFactory.getNodeOfType(FormatASTFactory.Type.COLCALC);
				cc.addViewColumn(vc);
				currentFormatView.addChildIfNotNull(cc);
				if(addedToFormatRequired) {
					formatRoot.addChildIfNotNull(currentFormatView);
					addedToFormatRequired = false;
				}
				FormatCompiler fc = new FormatCompiler();
				try {
					cc.addChildIfNotNull(fc.processLogic(vc.getColumnCalculation(), false));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	private void compileFormatFilter(ViewNode view) {
		FormatCompiler ffc = new FormatCompiler();
		try {
			FormatFilterAST ff = (FormatFilterAST) ffc.processLogic(view.getFormatFilterLogic(), true);
			ff.setView(view);
			currentFormatView.addChildIfNotNull(ff);
			if(ffc.hasErrors()) {

			} else {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void emitLogicTablesIfEnabled() {
		if(rcc.isEmitEnabled()) {
			buildTheJoinLogicTable();
			buildTheExtractLogicTable();
		}
	}

	private void buildTheJoinLogicTable() {
		//To do this use a JLT generator
		//Which knows how to take the Repo data and make the beast
		//All of the required Join data should have been accumulated
		//Let's see what we have
		JLTTreeGenerator jltgen = new JLTTreeGenerator(jltEmitter);
		ExtractBaseAST.setLogicTableEmitter(jltEmitter);
		joinsRoot = jltgen.buildJoinViews();
		writeJltDotIfEnabled();
		jltgen.emit();
	}

	private void writeJltDotIfEnabled() {
		if(rcc.isJltDotEnabled()) {
        	ExtractAST2Dot.write(joinsRoot, Paths.get("target/JLT.dot"));
		}
	}

	private void buildTheExtractLogicTable() {
		//Walk the AST and add the entries to the XLT
		//We need to ensure there were no errors
		if(ASTBase.getErrorCount() == 0) {
			//Put an error count in the Base and check
			//We also need to extract the Join information at this time
			ExtractBaseAST.setLogicTableEmitter(xltEmitter);
			((EmittableASTNode)extractRoot).emit();
		} else {
			logger.atSevere().log("%d Errors detected. Logic Table will not be written.", ASTBase.getErrorCount());
			//walk the tree here and get the errors?
		}
	}

	public void setLogicGroups(List<LogicGroup> lgs) {
		logicGroups = lgs;
    }

    private void buildTheAST() {
		extractRoot = (ExtractBaseAST) ASTFactory.getNodeOfType(ASTFactory.Type.ERSROOT);
		Iterator<LogicGroup> lgi = logicGroups.iterator();
		while(lgi.hasNext()) {
			LogicGroup lg = lgi.next();
			addNodesFromLogicGroup(lg);
		}
		writeXLTDotIfEnabled();
	}

	private void writeXLTDotIfEnabled() {
		if(rcc.isXltDotEnabled()) {
			ExtractAST2Dot.setFilter(rcc.getViewDots().length() >0 || rcc.getColumnDots().length()>0);
			ExtractAST2Dot.setViews(rcc.getViewDots().split(","));
			ExtractAST2Dot.setCols(rcc.getColumnDots().split(","));
			ExtractAST2Dot.write(extractRoot, Paths.get("target/XLT.dot"));
		}
	}

	private void addNodesFromLogicGroup(LogicGroup lg) {
		lg.getLfID();
		LFAstNode lfNode = (LFAstNode)ASTFactory.getNodeOfType(ASTFactory.Type.LF);
		lfNode.setLogicalFile(lg.getLogicalFile());
		if(rcc.isPFDotEnabled()) {
			addPFNodes(lfNode);
		} else {
			extractRoot.addChildIfNotNull(lfNode);
		}
		addViewSourceNodesToLFNode(lfNode, lg);
	}

	private void addViewSourceNodesToLFNode(LFAstNode lfNode, LogicGroup lg) {
		Iterator<ViewSourceWrapper> vsi = lg.getSortedViewSources().iterator();
		while(vsi.hasNext()) {
			ViewSourceWrapper vsw = vsi.next();
			ViewSourceAstNode vsnode = (ViewSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWSOURCE);
			ViewSource vs = vsw.getViewSource(); 
			logger.atInfo().log("Compile view %d source %d", vs.getViewId(), vs.getSequenceNumber());
			vsnode.setViewSource(vs);
			lfNode.addChildIfNotNull(vsnode);
			if(vsnode.hasExtractFilterText()) {
				addViewSourceNodes(vsnode);
			}
			addViewColumnSourceNodes(vsnode);
		}
	}

	private void addViewSourceNodes(ViewSourceAstNode vsnode) {
		ExtractFilterAST ef = (ExtractFilterAST) ASTFactory.getNodeOfType(ASTFactory.Type.EXTRFILTER);
		vsnode.addChildIfNotNull(ef);
		ExtractFilterCompiler efc = new ExtractFilterCompiler();
		try {
			efc.processLogic(vsnode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addViewColumnSourceNodes(ViewSourceAstNode vsnode) {
		Iterator<ViewColumnSource> vcsi = vsnode.getViewSource().getIteratorForColumnSourcesByNumber();
		while(vcsi.hasNext()) {
			ViewColumnSource vcs = vcsi.next();
			ViewColumnSourceAstNode vcsn = (ViewColumnSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWCOLUMNSOURCE);
			vcsn.setViewColumnSource(vcs);
			vsnode.addChildIfNotNull(vcsn);
			compileColumn(vcsn);
			resolveSortKeyTitleLookups(vcs);
		}
		compileExtractOutputLogic(vsnode);
	}

	private void resolveSortKeyTitleLookups(ViewColumnSource vcs) {
		if(vcs.getSortTitleLookupId() > 0) {
			logger.atInfo().log("Need Sort Title lookup id %d", vcs.getSortTitleLookupId());
			LookupPath lkup = Repository.getLookups().get(vcs.getSortTitleLookupId());
			LRField f = Repository.getFields().get(vcs.getSortTitleFieldId());
			Repository.getJoinViews().addSortKeyJLTViewField(lkup, f);
		}
	}

	private void compileExtractOutputLogic(ViewSourceAstNode vsnode) {
		ExtractOutputAST eo = (ExtractOutputAST) ASTFactory.getNodeOfType(ASTFactory.Type.EXTRACTOUTPUT);
		vsnode.addChildIfNotNull(eo);
		ExtractOutputCompiler eoc = new ExtractOutputCompiler();
		eoc.setViewSource(vsnode.getViewSource());
		try {
			eoc.processLogicAndAddNodes(eo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void compileColumn(ViewColumnSourceAstNode vcsn) {
		logger.atFine().log("Compiling column %d", vcsn.getViewColumnSource().getColumnNumber());
		ExtractColumnCompiler ecc = new ExtractColumnCompiler();
		try {
			ecc.processLogicAndAddNodes(vcsn);
			
			if(ecc.hasErrors()) {
				logger.atSevere().log("%d Errors detected. Logic Table will not be written.", ASTBase.getErrorCount());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public ExtractBaseAST getXltRoot() {
		return extractRoot;
	}

	public void addPFNodes(LFAstNode lfNode) {
        Iterator<PhysicalFile> pfi = lfNode.getLogicalFile().getPFIterator();
        while(pfi.hasNext()) {
            PFAstNode pfn = (PFAstNode)ASTFactory.getNodeOfType(ASTFactory.Type.PF);
            pfn.setPhysicalFile(pfi.next());
            pfn.addChildIfNotNull(lfNode);
            extractRoot.addChildIfNotNull(pfn);
        }
    }

	public LogicTable getExtractLogicTable() {
		return xltEmitter.getLogicTable();
	}

    public Object getExtractFilter() {
        return null;
    }

	public LogicTable getJoinLogicTable() {
		return jltEmitter.getLogicTable();
	}

	public FormatBaseAST getFFRoot() {
		return formatRoot;
	}

}
