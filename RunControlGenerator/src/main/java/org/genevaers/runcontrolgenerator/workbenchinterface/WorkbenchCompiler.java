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
import java.sql.Connection;
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
import org.genevaers.compilers.extract.BuildGenevaASTVisitor;
import org.genevaers.genevaio.dbreader.DatabaseConnectionParams;
import org.genevaers.genevaio.dbreader.LazyDBReader;
import org.genevaers.genevaio.dbreader.WBConnection;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.grammar.GenevaERSLexer;
import org.genevaers.grammar.GenevaERSParser;
import org.genevaers.grammar.GenevaERSParser.GoalContext;
import org.genevaers.repository.Repository;
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
import org.genevaers.repository.data.LookupRef;
import org.genevaers.repository.data.ViewLogicDependency.LogicType;
import org.genevaers.runcontrolgenerator.compilers.ExtractPhaseCompiler;


public abstract class WorkbenchCompiler implements SyntaxChecker, DependencyAnalyser {

	private static DatabaseConnectionParams params = new DatabaseConnectionParams();
	protected WBCompilerType type;
	private GoalContext tree;
	protected ParseErrorListener errorListener;
	private static LazyDBReader dataProvider = new LazyDBReader();;
	private int envId;
	private ViewType viewType;
	private ColumnData columnData;
	private static ViewNode currentView;
	protected static ViewColumnSource currentViewColumnSource;
	protected static ViewSource currentViewSource;
	private static int environmentID;
	private static int sourceLR;
	private static int sourceLF;

	//Common setup functions
    public static void setSQLConnection(Connection c) {
		dataProvider.setDatabaseConnection(new WBConnection(c));
		dataProvider.setParams(params);
		BuildGenevaASTVisitor.setDataProvider(dataProvider);
    }

	public static void setSchema(String schema) {
		params.setSchema(schema);
	}

    public void setViewDetails(int environmentId, int viewNum, ViewType type) {
        envId = environmentId;
		int viewID = viewNum;
		viewType = type;
    }

	public static void setEnvironment(int i) {
		environmentID = i;
		params.setEnvironmentID(Integer.toString(i));
		dataProvider.setEnvironmentID(i);
	}

    public static void setSourceLRID(int i) {
        sourceLR = i;
		dataProvider.loadLR(environmentID, sourceLR);
    }

    public static void setSourceLFID(int id) {
        sourceLF = id;
    }

	public static void addView(ViewData vd) {
    	ViewDefinition vdef = new ViewDefinition();
        vdef.setComponentId(vd.getId());
        vdef.setName(vd.getName());
        vdef.setViewType(ViewType.values()[vd.getTypeValue()]);
        currentView = Repository.getViewNodeMakeIfDoesNotExist(vdef);
	}

    public static void addColumn(ColumnData ci) {
    	ViewColumn vc = new ViewColumn();
        vc.setColumnNumber(ci.getColumnNumber());
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
    	currentViewSource = new ViewSource();
        currentViewSource.setComponentId(vsd.getId());
        currentViewSource.setViewId(vsd.getViewID());
        currentViewSource.setExtractFilter(vsd.getExtractFilter());
		currentViewSource.setExtractOutputLogic(vsd.getOutputLogic());
        currentViewSource.setSequenceNumber((short)vsd.getSequenceNumber());
        currentViewSource.setSourceLRID(vsd.getSourceLrId());
        currentView.addViewSource(currentViewSource);
		Repository.getDependencyCache().setCurrentParentId(currentViewSource.getComponentId());
	}

	public static void addViewColumnSource(ViewColumnSourceData vcsd) {
    	currentViewColumnSource = new ViewColumnSource();
        currentViewColumnSource.setColumnID(vcsd.getColumnId());
        currentViewColumnSource.setColumnNumber(vcsd.getColumnNumber());
        currentViewColumnSource.setComponentId(vcsd.getColumnId());
        currentViewColumnSource.setLogicText(vcsd.getLogicText());
        currentViewColumnSource.setSequenceNumber((short)vcsd.getSequenceNumber());
        currentViewColumnSource.setSourceType(ColumnSourceType.LOGICTEXT);
        currentViewColumnSource.setViewSourceId(vcsd.getViewSourceId());
        currentViewColumnSource.setViewId(vcsd.getViewID());
        currentViewColumnSource.setViewSrcLrId(vcsd.getViewSourceLrId());
        currentView.addViewColumnSource(currentViewColumnSource);
		Repository.getDependencyCache().setCurrentParentId(currentViewColumnSource.getComponentId());
	}

	public void run() {
		Repository.getDependencyCache().clearNamedEntries();
		buildAST();
		buildTheExtractTableIfThereAreNoErrors();
	}

	public abstract void buildAST();

	public void syntaxCheckLogic(String logicText){
        InputStream is = new ByteArrayInputStream(logicText.getBytes());
        GenevaERSLexer lexer;
		try {
			lexer = new GenevaERSLexer(CharStreams.fromStream(is));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			GenevaERSParser parser = new GenevaERSParser(tokens);
			parser.removeErrorListeners(); // remove ConsoleErrorListener
			errorListener = new ParseErrorListener();
			parser.addErrorListener(errorListener); // add ours
			tree = parser.goal(); // parse
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void buildTheExtractTableIfThereAreNoErrors() {
		if(Repository.getCompilerErrors().size() == 0) {
			ExtractPhaseCompiler.buildTheJoinLogicTable();
			ExtractPhaseCompiler.buildTheExtractLogicTable();
		}
	}

	public static LogicTable getXlt() {
		return ExtractPhaseCompiler.getExtractLogicTable();
	}

	public static boolean hasErrors() {
		return Repository.getCompilerErrors().size() > 0;
	}

	public static List<String> getErrors() {
		List<String> errs = new ArrayList<String>();
		Iterator<CompilerMessage> ei = Repository.getCompilerErrors().iterator();
		while(ei.hasNext()) {
			CompilerMessage e = ei.next();
			errs.add(e.getDetail());
		}
		return errs;
	}

	public static boolean hasWarnings() {
		return Repository.getWarnings().size() > 0;
	}

	public static List<String> getWarnings() {
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
	public Stream<Integer> getFieldIDs() {
		return Repository.getDependencyCache().getFieldIDs();
	}

	public Stream<LookupRef> getLookupsStream() {
		return Repository.getDependencyCache().getLookupsStream();
	}

	public Set<Integer> getExitIDs() {
		return Repository.getDependencyCache().getExitIDs();
	}

	public Stream<Integer> getLFPFAssocIDs() {
		return Repository.getDependencyCache().getLFPFAssocIDs();
	}

	public void clearErrors() {
	}

	public static void reset() {
		Repository.clearAndInitialise();
        ExtractPhaseCompiler.reset();
        Repository.setGenerationTime(Calendar.getInstance().getTime());
		currentViewColumnSource = null;
		currentView = null;
	}

	public String getDependenciesAsString() {
		return "";
	}


	@Override
	public Map<Integer, List<Integer>> getLookupIDs() {
		throw new UnsupportedOperationException("Unimplemented method 'getLookupIDs'");
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

	public static void setDataProvider(LazyDBReader dp) {
		dataProvider = dp;
	}

}
