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

import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LogicalRecord;

public class LogicalRecordDotWriter extends DotWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	private String dispStr = "";
	private StringBuilder lrDotString;

	//Three names we care about here
	// the cluster/subgraph name
	//the label that is displayed on the table
	//and the name of the table. Which is what preceded the port number for a link

	public String getDotStringFromLR(String cluster , LogicalRecord lr, boolean portLeft) {
		logger.atFine().log("Build DOT string for LR %s", lr.getName());
		lrDotString = new StringBuilder();
		declareSubgraph(cluster, getDisplayName(lr));
		declareTable(cluster);
		appendTableHeader();
	    appendLRFields(lr);
		closeTable();
		closeSubgraph();
		return lrDotString.toString();
	}


	private void closeSubgraph() {
		lrDotString.append("}");
	}

	private void appendLRFields(LogicalRecord lr) {
		//std::sort(m_childNodes.begin(), m_childNodes.end(), viewFixPosSort);
		Iterator<LRField> fi = lr.getIteratorForFieldsByID();
		while(fi.hasNext()) {
			LRField f = fi.next();
			appendField(f);
		}
	}


	private void appendField(LRField f) {
		lrDotString.append("    <TR BGCOLOR=\"" + getColour(f) + "\">");
		appendFieldString(f);
		lrDotString.append("    </TR>\n");
	}


	private void appendTableHeader() {
		lrDotString.append("    <TR>");
		lrDotString.append(getTableHeaderString());
		lrDotString.append("    </TR>\n");
	}


	private void declareTable(String cluster) {
		lrDotString.append(cluster);
		lrDotString.append(" [label=<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">\n");
	}


	private void declareSubgraph(String cluster, String displayName) {
		lrDotString.append(addSubgraph(cluster, displayName));
	}


	private String getDisplayName(LogicalRecord lr) {
		return dispStr.length() > 0 ? dispStr : lr.getName() + " (" + lr.getComponentId() + ")";
	}


	private void appendFieldString(LRField f) {
		String colour = getColour(f);
		appendFieldId(f, colour);
		appendFieldName(f, colour);
		appendPosition(f,colour);
		appendLength(f,colour);
		appendPlaces(f, colour);
		appendDateCode(f, colour);
	}

	private void appendFieldName(LRField f, String colour) {
		lrDotString.append("        <TD ALIGN=\"LEFT\" BGCOLOR=\"" + colour + "\">");
		lrDotString.append(f.getName());
		lrDotString.append("</TD>\n");
	}

	private void appendDateCode(LRField f, String colour) {
		lrDotString.append("        <TD ALIGN=\"Right\" BGCOLOR=\"" + colour + "\"");
		lrDotString.append(" PORT=\"" + "LRF_"  + f.getComponentId() + "_R\" >");
		lrDotString.append(f.getDateTimeFormat().toString());
		lrDotString.append("</TD>\n");
	}

	private void appendPlaces(LRField f, String colour) {
		lrDotString.append("        <TD ALIGN=\"Right\" BGCOLOR=\"" + colour + "\">");
		if (f.getRounding() > 0 || f.getNumDecimalPlaces() > 0) {
			lrDotString.append( f.getRounding() + "/" + f.getNumDecimalPlaces());
		}
		lrDotString.append("</TD>\n");
	}

	private void appendLength(LRField f, String colour) {
		lrDotString.append("        <TD ALIGN=\"RIGHT\" BGCOLOR=\"" + colour + "\">");
		lrDotString.append(f.getLength());
		lrDotString.append("</TD>\n");
	}

	private void appendPosition(LRField f, String colour) {
		lrDotString.append("        <TD ALIGN=\"RIGHT\" BGCOLOR=\"" + colour + "\">");
		lrDotString.append(f.getStartPosition());
		lrDotString.append("</TD>\n");
	}

	private void appendFieldId(LRField f, String colour) {
		lrDotString.append("\n        <TD ALIGN=\"LEFT\" BGCOLOR=\"" + colour + "\"");
		lrDotString.append(" PORT=\"" + "LRF_"  + f.getComponentId() + "_L\" >");
		lrDotString.append(f.getComponentId());
		lrDotString.append("</TD>\n");
	}

	private String getColour(LRField f) {
		if(f.getDatatype() != null) {
			switch (f.getDatatype())
			{
			case ALPHANUMERIC:
				return "pink";
			case ALPHA:
				return "pink";
			case ZONED:
				return "orange";
			case PACKED:
				return "yellow";
			case PSORT:
				return "gold";
			case BINARY:
				return "PaleGreen";
			case BSORT:
				return "LimeGreen";
			case BCD:
				return "SkyBlue";
			case MASKED:
				return "DeepSkyBlue";
			case EDITED:
				return "purple1";
			case FLOAT:
				return "red";
			case GENEVANUMBER:
				return "red";
			default:
				return null;
			}
		} else {
			return "";
		}
	}

	private String getTableHeaderString()
	{
		String ss = "\n";
		ss += "       <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">ID</TD>\n";
        ss += "       <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Field Name</TD>\n";
        ss += "       <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Poisiton</TD>\n";
        ss += "       <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Length</TD>\n";
        ss += "       <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">R/DP</TD>\n";
        ss += "       <TD ALIGN=\"LEFT\" BGCOLOR=\"white\">Date Code</TD>\n";
		return ss;
	}


	public void setDisplayString(String display) {
		dispStr = display;		
	}
	

	private void closeTable() {
		lrDotString.append("</TABLE>>]\n");
	}

}
