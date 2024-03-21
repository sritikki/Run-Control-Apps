package org.genevaers.runcontrolgenerator.workbenchinterface;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2023.
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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.fusesource.jansi.AnsiRenderer.Code;
import org.genevaers.compilers.extract.ExtractCompiler;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.genevaio.dataprovider.CompilerDataProvider;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.grammar.GenevaERSLexer;
import org.genevaers.grammar.GenevaERSParser;
import org.genevaers.grammar.GenevaERSParser.GoalContext;
import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.repository.components.enums.ColumnSourceType;
import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.JustifyId;
import org.genevaers.repository.components.enums.ViewType;
import org.genevaers.repository.data.CompilerMessage;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;


public class WorkbenchCompiler implements SyntaxChecker, DependencyAnalyser {

	protected WBCompilerType type;
	private GoalContext tree;
	private ExtractDependencyAnalyser dependencyAnalyser = new ExtractDependencyAnalyser();
	private ParseErrorListener errorListener;
	private CompilerDataProvider dataProvider;
	private int envId;
	private ViewType viewType;
	private ColumnData columnData;
	private LogicTable xlt;
	private static ViewNode currentView;
	private static ViewColumnSource vcs;
	private static ViewSource vs;

	public WorkbenchCompiler() {
	}

    public String greetings() {
        return "Greetings from the Run Control Generator.\nWatch this space.";
    }
    

    public void setViewDetails(int environmentId, int viewNum, ViewType type) {
        envId = environmentId;
		int viewID = viewNum;
		viewType = type;

    }

	public static void addView(ViewData vd) {
    	ViewDefinition vdef = new ViewDefinition();
        vdef.setComponentId(vd.getId());
        vdef.setName(vd.getName());
        vdef.setViewType(ViewType.values()[vd.getTypeValue()]);
        currentView = Repository.getViewNodeMakeIfDoesNotExist(vdef);
	}

	public static void addLR(LRData lrd) {
		LogicalRecord rcgLR = new LogicalRecord();
		rcgLR.setComponentId(lrd.getId());
		rcgLR.setName(lrd.getName());
		Repository.addLogicalRecord(rcgLR);
	}

	public static void addLRField(LRFieldData lrfd) {
		LRField lrf = new LRField();
		lrf.setComponentId(lrfd.getId());
		lrf.setDatatype(DataType.values()[lrfd.getDataTypeValue()]);
		lrf.setDateTimeFormat(DateCode.values()[lrfd.getDateCodeValue()]);
		lrf.setLength((short)lrfd.getLength());
		lrf.setLrID(lrfd.getLrId());
		lrf.setName(lrfd.getName());
		lrf.setNumDecimalPlaces((short)lrfd.getNumDecimals());
		lrf.setRounding((short)lrfd.getRounding());
		lrf.setSigned(lrfd.isSigned());
		int p = lrfd.getPosition();
		lrf.setStartPosition((short)p);
		Repository.addLRField(lrf);
	}

    public static void addColumn(ColumnData ci) {
    	ViewColumn vc = new ViewColumn();
        vc.setColumnNumber(1);
        vc.setComponentId(ci.getColumnId());
        vc.setDataType(DataType.values()[ci.getDataTypeValue()]);
        vc.setDateCode(DateCode.values()[ci.getDateCodeValue()]);
        vc.setDecimalCount((short)ci.getNumDecimalPlaces());
        vc.setExtractArea(ExtractArea.values()[ci.getExtractAreaValue()]);
        vc.setExtractAreaPosition((short)ci.getStartPosition());
        vc.setFieldLength((short)ci.getLength());
        vc.setFieldName(ci.getName());
        vc.setHeaderJustifyId(JustifyId.values()[ci.getAlignment()]);
        vc.setRounding((short)ci.getRounding());
        vc.setSigned(ci.isSigned());
        vc.setStartPosition((short)ci.getStartPosition());
        vc.setViewId(ci.getViewID());
        currentView.addViewColumn(vc);
    }

	public static void addViewSource(ViewSourceData vsd) {
    	vs = new ViewSource();
        vs.setComponentId(vsd.getId());
        vs.setViewId(vsd.getViewID());
        vs.setExtractFilter(vsd.getExtractFilter());
		vs.setExtractOutputLogic(vsd.getOutputLogic());
        vs.setSequenceNumber((short)vsd.getSequenceNumber());
        vs.setSourceLRID(vsd.getSourceLrId());
        currentView.addViewSource(vs);
	}

	public static void addViewColumnSource(ViewColumnSourceData vcsd) {
    	vcs = new ViewColumnSource();
        vcs.setColumnID(vcsd.getColumnId());
        vcs.setColumnNumber(vcsd.getColumnNumber());
        vcs.setComponentId(vcsd.getColumnId());
        vcs.setLogicText(vcsd.getLogicText());
        vcs.setSequenceNumber((short)vcsd.getSequenceNumber());
        vcs.setSourceType(ColumnSourceType.LOGICTEXT);
        vcs.setViewSourceId(vcsd.getViewSourceId());
        vcs.setViewId(vcsd.getViewID());
        vcs.setViewSrcLrId(vcsd.getViewSourceLrId());
        currentView.addViewColumnSource(vcs);
	}

	public void compileViewColumnSource() {
		try {
			syntaxCheckLogic(vcs.getLogicText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(errorListener.getErrors().size() == 0) {
			ExtractBaseAST.setCurrentColumnNumber((short)vcs.getColumnNumber());
			ExtractPhaseCompiler.buildViewColumnSourceAST(vcs);
			if(Repository.getCompilerErrors().size() == 0) {
				ExtractPhaseCompiler.buildTheExtractLogicTable();
				xlt = ExtractPhaseCompiler.getExtractLogicTable();
			}
		}
	}

	public void compileExtractFilter() {
		try {
			syntaxCheckLogic(vs.getExtractFilter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(errorListener.getErrors().size() == 0) {
			ExtractBaseAST.setCurrentColumnNumber((short)0);
			ExtractPhaseCompiler.buildViewSourceAST(vs);
			if(Repository.getCompilerErrors().size() == 0) {
				ExtractPhaseCompiler.buildTheExtractLogicTable();
				xlt = ExtractPhaseCompiler.getExtractLogicTable();
			}
		}
	}

	public void compileExtractOutput() {
		try {
			syntaxCheckLogic(vs.getExtractOutputLogic());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(errorListener.getErrors().size() == 0) {
			ExtractBaseAST.setCurrentColumnNumber((short)0);
			ExtractPhaseCompiler.buildViewOutputAST(vs);
			if(Repository.getCompilerErrors().size() == 0) {
				ExtractPhaseCompiler.buildTheExtractLogicTable();
				xlt = ExtractPhaseCompiler.getExtractLogicTable();
			}
		}
    }


	public void syntaxCheckLogic(String logicText) throws IOException {
        InputStream is = new ByteArrayInputStream(logicText.getBytes());
        GenevaERSLexer lexer = new GenevaERSLexer(CharStreams.fromStream(is));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GenevaERSParser parser = new GenevaERSParser(tokens);
        parser.removeErrorListeners(); // remove ConsoleErrorListener
        errorListener = new ParseErrorListener();
        parser.addErrorListener(errorListener); // add ours
        tree = parser.goal(); // parse
	}

	public LogicTable getXlt() {
		return xlt;
	}

	public boolean hasErrors() {
		return Repository.getCompilerErrors().size() > 0;
	}

	public List<String> getErrors() {
		List<String> errs = new ArrayList<String>();
		Iterator<CompilerMessage> ei = Repository.getCompilerErrors().iterator();
		while(ei.hasNext()) {
			CompilerMessage e = ei.next();
			errs.add(e.getDetail());
		}
		return errs;
	}

	public boolean hasWarnings() {
		return Repository.getWarnings().size() > 0;
	}

	public List<String> getWarnings() {
		List<String> warns = new ArrayList<String>();
		Iterator<CompilerMessage> wi = Repository.getWarnings().iterator();
		while(wi.hasNext()) {
			CompilerMessage w = wi.next();
			warns.add(w.getDetail());
		}
		return warns;
	}


	@Override
	public ParseTree getParseTree() {
		return tree;
	}

	public void setDataProvider(CompilerDataProvider dataFromHere) {
		dataProvider = dataFromHere;
		dependencyAnalyser.setDataProvider(dataFromHere);
	}

	@Override
	public void generateDependencies() {
		// Mr91 compiler will need to add the dependencies

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		dependencyAnalyser.setSycadaType(type);
        walker.walk(dependencyAnalyser, tree); // initiate walk of tree with listener		
	}

	@Override
	public boolean hasSyntaxErrors() {
		return errorListener.getErrors().size() > 0;
	}

	@Override
	public List<String> getSyntaxErrors() {
		return errorListener.getErrors();
	}

	@Override
	public int getNumberOfSyntaxWarningss() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasDataErrors() {
		return dependencyAnalyser.hasErrors();
	}

	@Override
	public List<String> getDataErrors() {
		return dependencyAnalyser.getErrors();
	}
	
	@Override
	public Stream<Integer> getFieldIDs() {
		return dependencyAnalyser.getFieldIDs();
	}

	public Stream<LookupRef> getLookupsStream() {
		return dependencyAnalyser.getLookupsStream();
	}

	public Set<Integer> getExitIDs() {
		return dependencyAnalyser.getExitIDs();
	}

	public Stream<Integer> getLFPFAssocIDs() {
		return dependencyAnalyser.getLFPFAssocIDs();
	}

	public void clearErrors() {
		dependencyAnalyser.getErrors().clear();
	}

	public static void reset() {
		Repository.clearAndInitialise();
        ExtractPhaseCompiler.reset();
        Repository.setGenerationTime(Calendar.getInstance().getTime());
		vcs = null;
		currentView = null;
	}

	public String getDependenciesAsString() {
		return dependencyAnalyser.getDependenciesAsString();
	}


	@Override
	public Map<Integer, List<Integer>> getLookupIDs() {
		throw new UnsupportedOperationException("Unimplemented method 'getLookupIDs'");
	}

	@Override
	public void getSourceLr(int lrid) {
		//Add the LR to the repo
		//Don't want to know about WB transfer objects here?

		//dataProvider.g
		dependencyAnalyser.preloadCacheFromLR(lrid);
	}

	public static String getVersion() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		String ver = "";
		try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
			properties.load(resourceStream);
			ver = "RCG" + ": " + properties.getProperty("build.version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ver;
	}

	public void addLR(LogicalRecord lr) {
		Repository.addLogicalRecord(lr);
	}

	public void addField(LRField f) {
		Repository.addLRField(f);
	}
}
