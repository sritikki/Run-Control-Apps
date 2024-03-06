package org.genevaers.runcontrolgenerator.singlepassoptimiser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

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

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.runcontrolgenerator.utility.Status;
import org.genevaers.utilities.GenevaLog;

public class SinglePassOptimiser {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	Map<Integer, Set<ViewSource>> pf2ViewSources = new TreeMap<>();

	Set<Integer> lfIDsUsed = new TreeSet<>();
	private Map<Integer, Set<ViewSource>> pf2VS;

	List<LogicGroup> logicGroups = new ArrayList<>();

	public SinglePassOptimiser() {
	}

	public Status run() {
        GenevaLog.writeHeader("Single Pass Optimization of view sources");
		Stream<ViewNode> vs = Repository.getViews().getValues().stream();
		vs.forEachOrdered(v -> viewSources(v));
		generateLogicGroups();
		//Tokens come first and orders by LF
		Collections.sort(logicGroups);
		return Status.OK;
	}

	private void generateLogicGroups() {
        logger.atFine().log("Generate Logic Groups");
		//copy so we can manipulat the map
		//Not sure if this is needed
		pf2VS = pf2ViewSources;
		while(pf2VS.size() > 0) {
			Iterator<Entry<Integer, Set<ViewSource>>> p2vsi = pf2VS.entrySet().iterator();
			Entry<Integer, Set<ViewSource>> pf2VSEntry = p2vsi.next();
			Set<ViewSource> viewSources = pf2VSEntry.getValue();
			logger.atFine().log("Look for View Source set to match for PF %s", pf2VSEntry.getKey());

			Set<Integer> pfIDset = removePfSetsReadByViewSourcs(viewSources);
			LogicGroup logicGroup = findLogicGroupsWithMatchingPFIDSet(viewSources);
			if (logicGroup != null)
			{
				logicGroup.addViewSources(viewSources); //This is silly since we just found them
				logicGroup.addPFIDSet(pfIDset);
			}
			else
			{
				//There was no Logic Group for this set of PFs
				//Make a new group
				logicGroup = new LogicGroup();
				logicGroup.setLfID(0);
				logicGroup.setPfIds(pfIDset);
				logicGroup.addViewSources(viewSources);
				logger.atFine().log("Made new Logic Group");
				logicGroups.add(logicGroup);
			}
		}
		matchLFsToLogicGroups();
	}

	private void matchLFsToLogicGroups() {
		//For each Logic Group is there a matching LF?
		//That is with a matching set of PFs
		//If not make a new LF
		logger.atFine().log("Match LFs to Logic Groups");
		Iterator<LogicGroup> lgi = logicGroups.iterator();
		int lgNum = 1;
		while(lgi.hasNext()) {
			LogicGroup lg = lgi.next();
			Set<Integer> lgpfids = lg.getPfIds();

			//Does this logic groups set of pfs match those of any of the source lfs?
			Iterator<Integer> lfui = lfIDsUsed.iterator();
			boolean noMatch = true;
			while(lfui.hasNext() && noMatch) {
				Integer lfid = lfui.next();
				LogicalFile lf = Repository.getLogicalFiles().get(lfid);
				Set<Integer> targetPfs = lf.getSetOfPFIDs();
				if(lgpfids.equals(targetPfs)) {
					noMatch = false;
					lg.setLfID(lfid);
					lg.setLf(lf);
					logger.atFine().log("Found matching PF set for Logic Group %d via LF %d", lgNum, lfid);
				}
			}
			if(noMatch) {
				createNewLFForLogicGroup(lg);
			}
			lgNum++;
		}
	}

	private void createNewLFForLogicGroup(LogicGroup lg) {
		// Get the next available logical file ID
		LogicalFile lf = new LogicalFile();
		lf.setID(Repository.getMaxFileID().getAsInt()+1);
		lf.setName("Auto Generated for LG ");
		logger.atFine().log("Create a new LF for Logic Group. LF ID %d", lf.getID());

		lg.setLfID(lf.getID());
		lg.setLf(lf);
		lg.getPfIds().forEach(pf -> lf.addPF(Repository.getPhysicalFiles().get(pf)));
		lg.updateViewSourcesToUseNewLF();
		Repository.getLogicalFiles().add(lf, lf.getID(), lf.getName());
	}

	private LogicGroup findLogicGroupsWithMatchingPFIDSet(Set<ViewSource> viewSources) {
		//This may be a case for a stream search/find
		return logicGroups.stream()
		.filter(lg -> lg.viewSources.equals(viewSources))
		.findFirst()
		.orElse(null);
	}

	private Set<Integer> removePfSetsReadByViewSourcs(Set<ViewSource> viewSources) {
		//Get the set of LFs read by these view sources
		viewSources.forEach(vs -> rememberSrcLF(vs));  //Used later
		Set<Integer> pfIDSet = new HashSet<>();
		Iterator<Entry<Integer, Set<ViewSource>>> pfsi = pf2VS.entrySet().iterator();

		while(pfsi.hasNext()) {
			Entry<Integer, Set<ViewSource>> pfVsSet = pfsi.next();
			Set<ViewSource> vsources = pfVsSet.getValue();
			if(vsources == viewSources) {
				logger.atFine().log("Found View Source set match for PF %s", pfVsSet.getKey());
				pfIDSet.add(pfVsSet.getKey());
			}
		}
		Iterator<Integer> pfidsi = pfIDSet.iterator();
		while(pfidsi.hasNext()) {
			Integer pfToRemove = pfidsi.next();
			pf2VS.remove(pfToRemove);
			logger.atFine().log("Removed PF %d from main set", pfToRemove);
		}
		return pfIDSet;
	}

	private void rememberSrcLF(ViewSource vs) {
		lfIDsUsed.add(vs.getSourceLFID());
	}

	private void viewSources(ViewNode v) {
		Iterator<ViewSource> vsi = v.getViewSourceIterator();
		StringBuilder sb = new StringBuilder();
		while(vsi.hasNext()) {
			ViewSource vs = vsi.next();
			sb.append("vs view:" + vs.getViewId() + " lf:" + vs.getSourceLFID() + "\nPFs ");
			buildPF2View(vs, sb);
			sb.append("\n");
		}
		logger.atFine().log("View Source\n%s", sb.toString());
		dumpPf2ViewSources();
	}

	private void dumpPf2ViewSources() {
		StringBuilder sb = new StringBuilder();
		Stream<Entry<Integer, Set<ViewSource>>> p2vStream = pf2ViewSources.entrySet().stream();
		p2vStream.forEachOrdered(p2v -> buildPf2ViewsTable(p2v, sb));
		logger.atFine().log("Pf to View Sources\n%s", sb.toString());
	}

	private void buildPf2ViewsTable(Entry<Integer, Set<ViewSource>> p2v, StringBuilder sb) {
		sb.append("PF " + p2v.getKey() + " Read by view sources\n");
		Iterator<ViewSource> vsi = p2v.getValue().iterator();
		while(vsi.hasNext()) {
			ViewSource vs = vsi.next();
			sb.append(vs.getViewId() + ":" + vs.getSourceLFID() + " ");
		}
	}

	private void buildPF2View(ViewSource vs, StringBuilder sb) {
		LogicalFile srclf = Repository.getLogicalFiles().get(vs.getSourceLFID());
		Stream<PhysicalFile> pfStream = srclf.getPFs().stream();
		pfStream.forEach(pf -> buildPF2ViewSources(pf, sb, vs));
	}

	private void buildPF2ViewSources(PhysicalFile pf, StringBuilder pfstr, ViewSource vs) {
		Set<ViewSource> viewSrcs = pf2ViewSources.get(pf.getComponentId());
		if(viewSrcs == null) {
			viewSrcs = new HashSet<>();
			pf2ViewSources.put(pf.getComponentId(), viewSrcs);
		}
		viewSrcs.add(vs);
		pfstr.append(pf.getComponentId() +" ");
	}

	public List<LogicGroup> getLogicGroups() {
		return logicGroups;
	}
}
