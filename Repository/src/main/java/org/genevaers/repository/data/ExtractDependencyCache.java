package org.genevaers.repository.data;

import java.util.HashSet;
import java.util.Iterator;

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


import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupPath;

public class ExtractDependencyCache {

	private Map<String, Integer> pfsByName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private Map<String, Integer> exitsByName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private Map<String, Integer> procsByName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private Map<String, Integer> fieldsByName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private Map<String, LookupRef> lookupsByName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private Set<Integer> writeExits = new HashSet<>();

	public void clear() {
		pfsByName.clear();
		exitsByName.clear();
		procsByName.clear();
		fieldsByName.clear();
		lookupsByName.clear();
	}

	public Integer addLookupIfAbsent(String name, LookupPath lk) {
		LookupRef localLkref = lookupsByName.computeIfAbsent(name, s -> getLookupRefByName(s,lk.getID()));
		return localLkref != null ? localLkref.getId() : null;
	}

	private LookupRef getLookupRefByName(String name, int id) {
		LookupRef lkref = null;
		//Get all of the lookup target fields here
				lkref = new LookupRef();
				lkref.setId(id);
				lkref.setName(name);
				Iterator<Entry<String, Integer>> lkfsi = lkref.getLookupFieldsByName().entrySet().iterator();
				while(lkfsi.hasNext()) {
					Entry<String, Integer> lkf = lkfsi.next();
					if(!lkf.getKey().equals("Lookup_ID")) {
						lkref.getLookupFieldsByName().put(lkf.getKey(), lkf.getValue());
					}
				}
		return lkref;
	}

	public Integer addNamedField(String name, int id) {
		return fieldsByName.putIfAbsent(name, Integer.valueOf(id));

	}

	public Integer getNamedField(String fieldName) {
		return fieldsByName.get(fieldName);
	}

	public Integer addNamedLookupField(String lkname, LRField fld){
		LookupRef localLkref = lookupsByName.computeIfAbsent(lkname, s -> getLookupRefByName(s, 0));
		Integer r = localLkref.getLookupFieldsByName().computeIfAbsent(fld.getName(), s -> getLkField(localLkref, fld));
		return r;
	}

	public Integer getNamedLookupField(String lkname, String field){
		return lookupsByName.get(lkname).getField(field);
	}
	private Integer getLkField(LookupRef lkref, LRField fld) {
		return fld.getComponentId();
	}


	//Need to manage the overlap... name and procdure to same exit
	public Integer addIfAbsent(String name, int id) {
		return exitsByName.putIfAbsent(name, id);
	}
	
	private Integer setExitID(String name, int id) {
		return id;
	}

	public Integer getProcedure(String name) {
		return procsByName.computeIfAbsent(name, s -> getProcedureID(s));
	}
	
	private Integer getProcedureID(String name) {
		return 0;
	}

	public Integer setNamedLfPfAssoc(String fullName, int id) { 
		return pfsByName.putIfAbsent(fullName, id);
	}

	public Integer getPfAssocID(String fullName) {
		return pfsByName.get(fullName);
	}

	public void setLRFieldNameIds(Map<String, Integer> fieldsFromLr) {
		fieldsByName = fieldsFromLr;
	}

	public boolean needsFieldNames() {
		return fieldsByName.isEmpty();
	}

	public void addExitID(int componentId) {
		writeExits.add(componentId);
	}

	public Set<Integer> getExitIDs() {		
		return writeExits;
	}

}
