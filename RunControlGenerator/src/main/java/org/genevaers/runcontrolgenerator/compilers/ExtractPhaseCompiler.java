package org.genevaers.runcontrolgenerator.compilers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.genevaers.compilers.base.ASTBase;
import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.compilers.extract.ExtractColumnCompiler;
import org.genevaers.compilers.extract.ExtractFilterCompiler;
import org.genevaers.compilers.extract.ExtractOutputCompiler;
import org.genevaers.compilers.extract.astnodes.ASTFactory;
import org.genevaers.compilers.extract.astnodes.ExtractAST2Dot;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.ExtractFilterAST;
import org.genevaers.compilers.extract.astnodes.ExtractOutputAST;
import org.genevaers.compilers.extract.astnodes.LFAstNode;
import org.genevaers.compilers.extract.astnodes.PFAstNode;
import org.genevaers.compilers.extract.astnodes.ViewColumnSourceAstNode;
import org.genevaers.compilers.extract.astnodes.ViewSourceAstNode;
import org.genevaers.compilers.extract.emitters.LogicTableEmitter;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.LogicGroup;
import org.genevaers.runcontrolgenerator.singlepassoptimiser.ViewSourceWrapper;
import org.genevaers.runcontrolgenerator.utility.Status;

import com.google.common.flogger.FluentLogger;

public class ExtractPhaseCompiler {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	private static List<LogicGroup> logicGroups;   
	private static ExtractBaseAST extractRoot;
	private static ExtractBaseAST joinsRoot;
	private static LogicTableEmitter xltEmitter = new LogicTableEmitter();
	private static LogicTableEmitter jltEmitter = new LogicTableEmitter();
    
   	public ExtractPhaseCompiler(List<LogicGroup> lgs) {
		logicGroups = lgs;
    }

    public static void reset() {
        xltEmitter = new LogicTableEmitter();
        jltEmitter = new LogicTableEmitter();
    }

    public static Status run(List<LogicGroup> lgs) {
        Status status = Status.OK;
		logicGroups = lgs;
		ASTBase.clearErrorCount();
		buildTheAST();
		if(Repository.getCompilerErrors().size() == 0) {
			//once the AST is built we now have enough data
			//to generate the JLT
			//This has to be done before building the XLT
			//since the lookup numbers are changed when we generate the JLT
			buildTheLogicTables();
		} else{
			status = Status.ERROR;
		}
        return status;
	}

    public static void buildTheAST() {
		extractRoot = (ExtractBaseAST) ASTFactory.getNodeOfType(ASTFactory.Type.ERSROOT);
		Iterator<LogicGroup> lgi = logicGroups.iterator();
		while(lgi.hasNext()) {
			LogicGroup lg = lgi.next();
			addNodesFromLogicGroup(lg);
		}
		Repository.saveNumberOfExtractViews();
		writeXLTDotIfEnabled();
	}

    public static void buildViewColumnSourceAST(ViewColumnSource vcs) {
		extractRoot = (ExtractBaseAST) ASTFactory.getNodeOfType(ASTFactory.Type.ERSROOT);
		ViewSourceAstNode vsnode = (ViewSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWSOURCE);
		vsnode.setViewSource(Repository.getViews().get(vcs.getViewId()).getViewSource(vcs.getSequenceNumber()));
		extractRoot.addChildIfNotNull(vsnode);
		ViewColumnSourceAstNode vcsn = (ViewColumnSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWCOLUMNSOURCE);
		vcsn.setViewColumnSource(vcs);
		vsnode.addChildIfNotNull(vcsn);
		compileColumn(vcsn);		
		writeXLTDotIfEnabled();
	}

	private static void writeXLTDotIfEnabled() {
		if(RunControlConfigration.isXltDotEnabled()) {
			ExtractAST2Dot.setFilter(RunControlConfigration.getViewDots().length() >0 || RunControlConfigration.getColumnDots().length()>0);
			ExtractAST2Dot.setViews(RunControlConfigration.getViewDots().split(","));
			ExtractAST2Dot.setCols(RunControlConfigration.getColumnDots().split(","));
			ExtractAST2Dot.write(extractRoot, Paths.get("target/XLT.dot"));
		}
	}

	private static void addNodesFromLogicGroup(LogicGroup lg) {
		lg.getLfID();
		LFAstNode lfNode = (LFAstNode)ASTFactory.getNodeOfType(ASTFactory.Type.LF);
		lfNode.setLogicalFile(lg.getLogicalFile());
		if(RunControlConfigration.isPFDotEnabled()) {
			addPFNodes(lfNode);
		} else {
			extractRoot.addChildIfNotNull(lfNode);
		}
		addViewSourceNodesToLFNode(lfNode, lg);
	}

	private static void addViewSourceNodesToLFNode(LFAstNode lfNode, LogicGroup lg) {
		Iterator<ViewSourceWrapper> vsi = lg.getSortedViewSources().iterator();
		while(vsi.hasNext()) {
			ViewSourceWrapper vsw = vsi.next();
			ViewSourceAstNode vsnode = (ViewSourceAstNode) ASTFactory.getNodeOfType(ASTFactory.Type.VIEWSOURCE);
			ViewSource vs = vsw.getViewSource(); 
			logger.atInfo().log("Compile view %d source %d", vs.getViewId(), vs.getSequenceNumber());
			vsnode.setViewSource(vs);
			lfNode.addChildIfNotNull(vsnode);
			ExtractBaseAST.clearLastColumnWithAWrite();
			if(vsnode.hasExtractFilterText()) {
				addViewSourceNodes(vsnode);
			}
			addViewColumnSourceNodes(vsnode);
		}
	}

	private static void addViewSourceNodes(ViewSourceAstNode vsnode) {
		ExtractFilterAST ef = (ExtractFilterAST) ASTFactory.getNodeOfType(ASTFactory.Type.EXTRFILTER);
		vsnode.addChildIfNotNull(ef);
		ExtractFilterCompiler efc = new ExtractFilterCompiler();
		efc.setViewSource(vsnode.getViewSource());
		try {
			efc.processLogicAndAddNodes(ef);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addViewColumnSourceNodes(ViewSourceAstNode vsnode) {
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

	private static void resolveSortKeyTitleLookups(ViewColumnSource vcs) {
		if(vcs.getSortTitleLookupId() > 0) {
			logger.atInfo().log("Need Sort Title lookup id %d", vcs.getSortTitleLookupId());
			LookupPath lkup = Repository.getLookups().get(vcs.getSortTitleLookupId());
			LRField f = Repository.getFields().get(vcs.getSortTitleFieldId());
			Repository.getJoinViews().addSortKeyJLTViewField(lkup, f);
		}
	}

	private static void compileExtractOutputLogic(ViewSourceAstNode vsnode) {
		boolean runEOC = true;
		if(RunControlConfigration.getInputType().equalsIgnoreCase("VDPXML")) {
			if(noWriteStatementMissing(vsnode)) {
				runEOC = false; //There must have been a write in the last column
			}
		}

		//What if the extract output logic does not contain a write?
		if(runEOC) {
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
	}

	private static boolean noWriteStatementMissing(ViewSourceAstNode vsnode) {
		return (ExtractBaseAST.getLastColumnWithAWrite() == vsnode.getNumberOfColumns());
	}

	private static void compileColumn(ViewColumnSourceAstNode vcsn) {
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

	public static ExtractBaseAST getXltRoot() {
		return extractRoot;
	}

	public static void addPFNodes(LFAstNode lfNode) {
        Iterator<PhysicalFile> pfi = lfNode.getLogicalFile().getPFIterator();
        while(pfi.hasNext()) {
            PFAstNode pfn = (PFAstNode)ASTFactory.getNodeOfType(ASTFactory.Type.PF);
            pfn.setPhysicalFile(pfi.next());
            pfn.addChildIfNotNull(lfNode);
            extractRoot.addChildIfNotNull(pfn);
        }
    }

    public static void buildTheLogicTables() {
		if(RunControlConfigration.isEmitEnabled()) {
			buildTheJoinLogicTable();
			buildTheExtractLogicTable();
		}
	}

	private static void buildTheJoinLogicTable() {
		//To do this use a JLT generator
		//Which knows how to take the Repo data and make the beast
		//All of the required Join data should have been accumulated
		//Let's see what we have
		JLTTreeGenerator jltgen = new JLTTreeGenerator(jltEmitter);
		ExtractBaseAST.setLogicTableEmitter(jltEmitter);
		joinsRoot = jltgen.buildJoinViews();
		Repository.getJoinViews().logdata();
		writeJltDotIfEnabled();
		jltgen.emit();
	}

	private static void writeJltDotIfEnabled() {
		if(RunControlConfigration.isJltDotEnabled()) {
        	ExtractAST2Dot.write(joinsRoot, Paths.get("target/JLT.dot"));
		}
	}

	private static void buildTheExtractLogicTable() {
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

    public static LogicTable getExtractLogicTable() {
        return xltEmitter.getLogicTable();
    }

    public static LogicTable getJoinLogicTable() {
        return jltEmitter.getLogicTable();
    }

}
