package org.genevaers.repository.data;

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


public class ViewLogicDependency {

	public enum LogicType {
		INVALID(0), EXTRACT_RECORD_FILTER(1), EXTRACT_COLUMN_ASSIGNMENT(2), 
		FORMAT_COLUMN_CALCULATION(3), FORMAT_RECORD_FILTER(4), EXTRACT_RECORD_OUTPUT(5);	
	
		private int typeValue;

		LogicType(int typeValue) {
			this.typeValue = typeValue;
		}

		public int getTypeValue() {
			return typeValue;
		}
	}	

	/*
	 * Note, this class has no ID or name attributes, so those fields in the
	 * superclass hierarchy will have null values.
	 */

	private LogicType logicTextType; // LOGICTYPECD
	private Integer sequenceNo; // DEPENDID
	private Integer lookupPathId; // LOOKUPID
	private Integer lrFieldId; // LRFIELDID
	private Integer userExitRoutineId; // EXITID
	private Integer fileAssociationId; // LFPFASSOCID
	private Integer parentId;

	ViewLogicDependency(LogicType logicTextType,Integer sequenceNo,
			Integer lookupPathId, Integer lrFieldId, Integer userExitRoutineId,
			Integer fileAssociationId, int parentId) {
		this.logicTextType = logicTextType;
		this.sequenceNo = sequenceNo;
		this.lookupPathId = lookupPathId;
		this.lrFieldId = lrFieldId;
		this.userExitRoutineId = userExitRoutineId;
		this.fileAssociationId = fileAssociationId;
		this.parentId = parentId;
	}
	
	public LogicType getLogicTextType() {
		return logicTextType;
	}

	public void setLogicTextType(LogicType logicTextType) {
		this.logicTextType = logicTextType;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public Integer getLookupPathId() {
		return lookupPathId;
	}

	public void setLookupPathId(Integer lookupPathId) {
		this.lookupPathId = lookupPathId;
	}

	public Integer getLrFieldId() {
		return lrFieldId;
	}

	public void setLrFieldId(Integer lrFieldId) {
		this.lrFieldId = lrFieldId;
	}

	public Integer getUserExitRoutineId() {
		return userExitRoutineId;
	}

	public void setUserExitRoutineId(Integer userExitRoutineId) {
		this.userExitRoutineId = userExitRoutineId;
	}

	public Integer getFileAssociationId() {
		return fileAssociationId;
	}

	public void setFileAssociationId(Integer fileAssociationId) {
		this.fileAssociationId = fileAssociationId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getParentId() {
		return parentId;
	}
}
