package org.genevaers.genevaio.html;

import org.genevaers.genevaio.fieldnodes.FieldNodeBase;

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


public class VDPRecordsHTMLWriter extends HTMLRecordsWriter {
	
	@Override
	public void setIgnores() {
		//Hide diffs we don't care about via map
		ignoreTheseDiffs.put("Generation_runDate", true); 
		ignoreTheseDiffs.put("Generation_date", true); 
		ignoreTheseDiffs.put("Generation_description", true); 
		ignoreTheseDiffs.put("Generation_time", true); 
		ignoreTheseDiffs.put("Control_Records_description", true); 
		ignoreTheseDiffs.put("Physical_Files_columnId", true); 
		ignoreTheseDiffs.put("Physical_Files_name", true); 
		ignoreTheseDiffs.put("Physical_Files_lfName", true); 
		ignoreTheseDiffs.put("Logical_Records_lrName", true); 
		ignoreTheseDiffs.put("LR_Fields_recordId", true); 
		ignoreTheseDiffs.put("LR_Fields_ordinalPosition", true); 
		ignoreTheseDiffs.put("Lookup_Paths_columnId", true); 
		ignoreTheseDiffs.put("LR_Indexes_columnId", true); 
		ignoreTheseDiffs.put("LR_Indexes_lrIndexName", true); 
		ignoreTheseDiffs.put("View_Definition_viewName", true); 
		ignoreTheseDiffs.put("View_Definition_outputLineSizeMax", true); 
		ignoreTheseDiffs.put("View_Definition_ownerUser", true); 
		ignoreTheseDiffs.put("View_Output_File_name", true); 
		ignoreTheseDiffs.put("View_Output_File_recordDelimId", true); 
		ignoreTheseDiffs.put("View_Output_File_allocRecfm", true); 
		ignoreTheseDiffs.put("View_Output_File_allocLrecl", true); 
		ignoreTheseDiffs.put("View_Output_File_lfName", true); 
		ignoreTheseDiffs.put("View_Output_File_ddnameOutput", true); 
		ignoreTheseDiffs.put("Columns_columnName", true); 
		ignoreTheseDiffs.put("Columns_fieldName", true); 
	}

	@Override
	protected String getDiffKey(FieldNodeBase n) {
		return n.getParent().getParent().getName() + "_" + n.getName();
	}

}
