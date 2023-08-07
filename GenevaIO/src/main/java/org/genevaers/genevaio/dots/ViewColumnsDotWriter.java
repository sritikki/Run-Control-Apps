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

import java.util.Iterator;

import com.google.common.flogger.FluentLogger;

import org.apache.commons.text.StringEscapeUtils;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;

public class ViewColumnsDotWriter extends DotWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private StringBuilder vsDotString;

	public String getDotStringFromView(ViewNode view, Short viewSource) {
		logger.atFine().log("Build DOT string for columns of view %s source %d", view.getName(), viewSource);
		vsDotString = new StringBuilder();
		declareSubgraph(view, viewSource);

		if (view.getViewSource(viewSource).getExtractFilter() != null) {
			appendExtractFiler(view, viewSource);
		}

		appendColumns(view, viewSource);
		closeSubgraph();
		return vsDotString.toString();
	}

	private void closeSubgraph() {
		vsDotString.append("}");
	}

	private void appendColumns(ViewNode view, Short viewSource) {
		declareTable("COLS_" + viewSource);
		appendColumnHeader();
		appendColumnData(view, viewSource);
		closeTable();
	}

	private void appendColumnData(ViewNode view, Short viewSource) {
		Iterator<ViewColumn> ci = view.getColumnIterator();
		while (ci.hasNext()) {
			ViewColumn c = ci.next();
			appendColumnRow(c, viewSource);
		}
	}

	private void appendColumnRow(ViewColumn c, Short viewSource) {
		vsDotString.append("\n    <TR>\n");
		appendColumnString(c, viewSource);
		vsDotString.append("    </TR>");
	}

	private void appendColumnHeader() {
		vsDotString.append("\n    <TR>");
		vsDotString.append(getTableHeaderString());
		vsDotString.append("\n    </TR>");
	}

	private void appendExtractFiler(ViewNode view, Short viewSource) {
		// Makes a small table that holds the Extract Filter text
		// Declare table
		declareTable("EXTRF_" + viewSource);
		addFilterRow(view, viewSource);
		closeTable();
	}

	private void closeTable() {
		vsDotString.append("</TABLE>>]\n");
	}

	private void addFilterRow(ViewNode view, Short viewSource) {
		vsDotString.append("<TR>");
		addFilterTableData(view, viewSource);
		vsDotString.append("</TR>");
	}

	private void addFilterTableData(ViewNode view, Short viewSource) {
		vsDotString.append("<TD ALIGN=\"LEFT\" BGCOLOR=\"orchid\"");
		vsDotString.append(getExtractFilterToolTip(view.getViewSource(viewSource)) + ">Extract Filter");
		vsDotString.append("</TD>");
	}

	private void declareTable(String tableName) {
		vsDotString.append(tableName);
		vsDotString.append(" [label=<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">");
	}

	private void declareSubgraph(ViewNode view, Short viewSource) {
		vsDotString.append(addSubgraph("COLS_" + viewSource, view.getName() + " (" + view.getID() + ")"));
	}

	private String getExtractFilterToolTip(ViewSource viewSource) {
		String escapedHTML = StringEscapeUtils.escapeHtml4(viewSource.getExtractFilter());
		return " HREF=\"#\" TITLE=\""
				+ escapedHTML
				+ "\" ";
	}

	private void appendColumnString(ViewColumn c, Short viewSource) {
		String colour = getFormatColour(c.getDataType());
		appendColumnSource(c, viewSource, colour);
		appendColumnName(c, viewSource, colour);
		appendColumnPosition(c, colour);
		appendColumnLength(c, colour);
		appendColumnPlaces(c, colour);
		appendColumnDateCode(c, colour);
	}

	private void appendColumnSource(ViewColumn c, Short viewSource, String colour) {
		vsDotString.append("        <TD ALIGN=\"LEFT\" ");
		vsDotString.append("BGCOLOR=\"" + colour + "\" ");
		vsDotString.append(" PORT=\"" + "COL_"  + c.getComponentId() + "\" >");
		vsDotString.append(getSource(c, viewSource));
		vsDotString.append("</TD>\n");
	}

	private Object getSource(ViewColumn c, Short viewSource) {
		String src = "none";
		ViewColumnSource vcs = c.findFromSourcesByNumber(viewSource);
		if (vcs != null) {
			switch(vcs.getSourceType()) {
				case CONSTANT:
					src = vcs.getSrcValue();
					break;
				case EVENTLR:
					src = "field";
					break;
				case LOGICTEXT:
					src = "logic";
					break;
				case LOOKUP:
					src = "lookup";
					break;
				case NONE:
					break;
				default:
					break;
				
			}
		}
		return src;
	}

	private void appendColumnDateCode(ViewColumn c, String colour) {
		vsDotString.append("        <TD ALIGN=\"Right\" BGCOLOR=\"" + colour + "\">");
		vsDotString.append(c.getDateCode());
		vsDotString.append("</TD>\n");
	}

	private void appendColumnPlaces(ViewColumn c, String colour) {
		vsDotString.append("        <TD ALIGN=\"Right\" BGCOLOR=\"" + colour + "\">");
		if (c.getRounding() > 0 || c.getDecimalCount() > 0) {
			vsDotString.append( c.getRounding() + "/" + c.getDecimalCount());
		}
		vsDotString.append("</TD>\n");
	}

	private void appendColumnLength(ViewColumn c, String colour) {
		vsDotString.append("        <TD ALIGN=\"Right\" BGCOLOR=\"" + colour + "\">");
		vsDotString.append(c.getFieldLength());
		vsDotString.append("</TD>\n");
	}

	private void appendColumnPosition(ViewColumn c, String colour) {
		vsDotString.append("        <TD ALIGN=\"Right\" BGCOLOR=\"" + colour + "\">");
		vsDotString.append(c.getStartPosition());
		vsDotString.append("</TD>\n");
	}

	private void appendColumnName(ViewColumn c, Short viewSource, String colour) {
		vsDotString.append("        <TD ALIGN=\"LEFT\" ");
		vsDotString.append("BGCOLOR=\"" + colour + "\" ");
		vsDotString.append(getToolTip(c, viewSource));
		vsDotString.append(" >");
		vsDotString.append(c.getName());
		vsDotString.append("</TD>\n");
	}

	private String getToolTip(ViewColumn c, Short viewSource) {
		ViewColumnSource vcs = c.findFromSourcesByNumber(viewSource);
		if (vcs != null && vcs.getLogicText() != null) {
			String escapedHTML = StringEscapeUtils.escapeHtml4(c.findFromSourcesByNumber(viewSource).getLogicText());
			return " HREF=\"#\" TITLE=\"" + escapedHTML + "\" ";
		}
		return "";
	}

	private String getTypeColour(ViewColumn c, Short viewSource, String colour) {
		String clr = "BGCOLOR=\"";
		ViewColumnSource vcs = c.findFromSourcesByNumber(viewSource);
		if (vcs != null) {
			switch(vcs.getSourceType()) {
				case CONSTANT:
				clr += "orange\" ";
				break;
				case EVENTLR:
				clr += "orchid\" ";
				break;
				case LOGICTEXT:
					clr += "light green\" ";
					break;
				case LOOKUP:
				clr += "SkyBlue\" ";
				break;
				case NONE:
					break;
				default:
					break;
				
			}
		} else {
			clr += colour + "\"";
		}
		return clr;
	}

	private String getTableHeaderString() {
		String ss = "\n";
		ss += "        <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Source</TD>\n";
		ss += "        <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Column Name</TD>\n";
		ss += "        <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Position</TD>\n";
		ss += "        <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Length</TD>\n";
		ss += "        <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">R/DP</TD>\n";
		ss += "        <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Date Code</TD>";
		return ss;
	}

}
