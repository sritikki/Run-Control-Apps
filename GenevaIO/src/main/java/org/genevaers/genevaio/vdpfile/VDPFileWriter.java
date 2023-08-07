package org.genevaers.genevaio.vdpfile;

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

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.genevaers.genevaio.recordreader.RecordFileReaderWriter;
import org.genevaers.genevaio.vdpfile.record.VDPRecord;
import org.genevaers.repository.Repository;
import org.genevaers.repository.calculationstack.CalcStack;
import org.genevaers.repository.components.ControlRecord;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LRIndex;
import org.genevaers.repository.components.LogicalFile;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.components.LookupPathStep;
import org.genevaers.repository.components.OutputFile;
import org.genevaers.repository.components.PhysicalFile;
import org.genevaers.repository.components.ReportFooter;
import org.genevaers.repository.components.ReportHeader;
import org.genevaers.repository.components.UserExit;
import org.genevaers.repository.components.ViewColumn;
import org.genevaers.repository.components.ViewColumnSource;
import org.genevaers.repository.components.ViewDefinition;
import org.genevaers.repository.components.ViewNode;
import org.genevaers.repository.components.ViewSortKey;
import org.genevaers.repository.components.ViewSource;
import org.genevaers.repository.components.enums.AccessMethod;
import org.genevaers.repository.components.enums.FieldDelimiter;
import org.genevaers.repository.components.enums.FileRecfm;
import org.genevaers.repository.components.enums.FileType;
import org.genevaers.repository.components.enums.RecordDelimiter;
import org.genevaers.repository.components.enums.TextDelimiter;
import org.genevaers.repository.jltviews.JoinViewsManager.JoinTargetEntry;

import com.google.common.flogger.FluentLogger;

public class VDPFileWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private File vdpFile;
	private RecordFileReaderWriter VDPWriter;
	private int numrecords;
	private VDPManagementRecords vdpMgmtRecs;

	public VDPFileWriter() {
		// empty
	}

	// We should make an interface for a VDP Writer...
	// And then we have a bunch...
	// Dot file so we can draw the tree
	// We probably should have had the HMTL via this?
	public int writeVDPFrom(VDPManagementRecords vmrs) throws Exception {

		vdpMgmtRecs = vmrs;
		int numBytesWritten = 0;
		writeTheRecords();
		return numBytesWritten;
	}

	public void open(String name) {
		vdpFile = new File(name);
	}

	private void writeTheRecords() throws Exception {
		VDPWriter = new RecordFileReaderWriter();
		VDPWriter.writeRecordsTo(vdpFile);

		// We really need to walk the list of items to get the generation information
		// first
		// Another set of visitors?
		writeGenerationRecord();
		writeFormatViewsRecord();
		writeControlRecords();
		writePhysicalFileRecords();
		writeExitRecords();
		writeLogicalRecords();
		writeLRFields();
		writeLRIndexes();
		writeLookupPaths();
		writeLookupTargetSet();
		writeLookupGenMap();
		//I don't think this is ever used by MR95
		writeExtractOutputFile();
		writeExtractRecordFile();
		writeViews();
	}

	private void writeViews() {
		Iterator<ViewNode> vi = Repository.getViews().getIterator();
		while (vi.hasNext()) {
			writeView(vi.next());
		}
	}

	private void writeView(ViewNode view) {
		writeViewDefinition(view.getViewDefinition());
		ViewManagementData vmd = vdpMgmtRecs.getViewManagmentData(view.getViewDefinition().getComponentId());
		// if(vmd.getFormatFile() == null) {
		// 	makeViewFormatRecord(view);  //This is a silly name for the view output record (basically its DDname)
		// }
		writeTheOutputFile(view);
		writeFormatFilterLogic(view);
		writeFormatFilterStack(view);
		writeExtractTargetSet(vmd.getExtractTargetSet());
		writeViewColumns(view);
		writeViewSortKeys(view);
		writeViewSources(view);
		writeViewColumnSources(view);
		writeHeaders(view);
		writeFooters(view);
	}

	private void writeHeaders(ViewNode view) {
		Iterator<ReportHeader> rhi = view.getHeadersIterator();
		short s = 1;
		while(rhi.hasNext()) {
			VDPHeader vh = new VDPHeader();
			vh.fillFromComponent(rhi.next());
			vh.setViewId(view.getID());
			vh.setSequenceNbr(s++);
			vh.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeFooters(ViewNode view) {
		Iterator<ReportFooter> rhi = view.getFootersIterator();
		short s = 1;
		while(rhi.hasNext()) {
			VDPFooter vh = new VDPFooter();
			vh.fillFromComponent(rhi.next());
			vh.setViewId(view.getID());
			vh.setSequenceNbr(s++);
			vh.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void makeViewFormatRecord(ViewNode view) {

			OutputFile vof = view.getOutputFile();
			vof.setComponentId(view.getID());
			String ddname = String.format("F%07d", view.getID());
			vof.setOutputDDName(ddname);
			vof.setName("MR91 generated for " + ddname);
	}

	private void writeTheOutputFile(ViewNode view) {
		//Output files are not required for all views
		VDPFormatFile ff = vdpMgmtRecs.getViewManagmentData(view.getID()).getFormatFile();
		if(ff == null) {
			ff = new VDPFormatFile();
			if(view.getOutputFile().getOutputDDName().isEmpty()) {
				makeViewFormatRecord(view);
			}
			ff.fillFromComponent(view.getOutputFile());
			ff.setViewId(view.getID());

			ff.setRecordType(VDPRecord.VDP_FORMAT_OUTPUT_FILE);
			ff.setViewId(view.getID());
			ff.setSequenceNbr((short) 1);
			ff.setRecordId(view.getID());
			ff.setInputFileId(0);

			//Keep the writer happy
			ff.setAllocFileType(FileType.DISK);
			ff.setFieldDelimId(FieldDelimiter.INVALID);
			ff.setRecordDelimId(RecordDelimiter.INVALID);
			ff.setTextDelimId(TextDelimiter.INVALID);
			ff.setAccessMethodId(AccessMethod.SEQUENTIAL);
			ff.setAllocLrecl((short) 400);
			ff.setAllocRecfm(FileRecfm.FB);
		}

		ff.fillTheWriteBuffer(VDPWriter);
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeFormatFilterLogic(ViewNode view) {
		String ffl = view.getFormatFilterLogic();
		if (ffl != null && ffl.length() > 0) {
			VDPFormatFilterLogic vffl = new VDPFormatFilterLogic();

			vffl.setRecordType(VDPRecord.VDP_FORMAT_FILTER_LOGIC);
			vffl.setViewId(view.getID());
			vffl.setSequenceNbr((short) 1);
			vffl.setRecordId(view.getID());
			vffl.setInputFileId(0);
			vffl.setLogicLength((short) ffl.length());
			vffl.setRecLen((short) (26 + ffl.length()));
			vffl.setLogic(ffl);
			vffl.fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeFormatFilterStack(ViewNode view) {
		CalcStack ffcs = view.getFormatFilterCalcStack();
		if (ffcs != null) {
			VDPFormatFilterStack vffs = new VDPFormatFilterStack();

			vffs.setRecordType(VDPRecord.VDP_FORMAT_FILTER_STACK);
			vffs.setViewId(view.getID());
			vffs.setSequenceNbr((short) 1);
			vffs.setRecordId(view.getID());
			vffs.setInputFileId(0);

			for (int i = 0; i<16; i += 1) {
				vffs.getStackHeaderPrefix().add((byte)0);
			}
			vffs.setFunctionCode("CALC");
			for (int i = 0; i<40; i += 1) {
				vffs.getStackHeaderSuffix().add((byte)0);
			}

			ffcs.fill(vffs.getStack());
			vffs.setRecLen((short) (60 + ffcs.getStackLength()));
			vffs.setStackLength(ffcs.getStackLength());

			vffs.fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeViewSources(ViewNode view) {
		Iterator<ViewSource> vsi = view.getViewSourceIterator();
		while (vsi.hasNext()) {
			writeViewSource(vsi.next());
		}
	}

	private void writeViewSource(ViewSource vs) {
		VDPViewSource vvs = new VDPViewSource();
		vvs.fillFromComponent(vs);
		vvs.fillTheWriteBuffer(VDPWriter);
		VDPWriter.writeAndClearTheRecord();
		writeExtractFilter(vs);
		writeOutputLogic(vs);
	}

	private void writeOutputLogic(ViewSource vs) {
		if (vs.getExtractOutputLogic() != null) {
			String eol = vs.getExtractOutputLogic();
			VDPExtractOutputLogic veol = new VDPExtractOutputLogic();

			veol.setRecordType(VDPRecord.VDP_OUTPUT_LOGIC);
			veol.setViewId(vs.getViewId());
			veol.setSequenceNbr(vs.getSequenceNumber());
			veol.setRecordId(vs.getComponentId());
			veol.setInputFileId(vs.getComponentId());
			veol.setLogicLength((short) eol.length());
			veol.setRecLen((short) (26 + eol.length()));
			veol.setLogic(eol);
			veol.fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeExtractFilter(ViewSource vs) {
		if (vs.getExtractFilter() != null && vs.getExtractFilter().length() > 0) {
			String ef = vs.getExtractFilter();
			VDPExtractFilter vef = new VDPExtractFilter();

			vef.setRecordType(VDPRecord.VDP_EXTRACT_FILTER);
			vef.setViewId(vs.getViewId());
			vef.setSequenceNbr(vs.getSequenceNumber());
			vef.setRecordId(vs.getComponentId());
			vef.setInputFileId(vs.getComponentId());
			vef.setLogicLength((short) ef.length());
			vef.setRecLen((short) (26 + ef.length()));
			vef.setLogic(ef);
			vef.fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeViewSortKeys(ViewNode view) {
		Iterator<ViewSortKey> vski = view.getSortKeyIterator();
		while (vski.hasNext()) {
			writeViewSortKey(vski.next());
		}
	}

	private void writeViewSortKey(ViewSortKey vsk) {
		VDPViewSortKey vvsk = new VDPViewSortKey();
		vvsk.fillFromComponent(vsk);
		vvsk.fillTheWriteBuffer(VDPWriter);
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeViewColumns(ViewNode view) {
		Iterator<ViewColumn> ci = view.getColumnIterator();
		while (ci.hasNext()) {
			writeViewColumn(ci.next());
		}
	}

	private void writeViewColumn(ViewColumn col) {
		VDPViewColumn vvc = new VDPViewColumn();
		vvc.fillFromComponent(col);
		vvc.fillTheWriteBuffer(VDPWriter);
		VDPWriter.writeAndClearTheRecord();
		writeColumnCalculation(col);
	}

	private void writeColumnCalculation(ViewColumn col) {
		String ccl = col.getColumnCalculation();
		if (ccl != null && ccl.length() > 0) {
			VDPColumnCalculationLogic vccl = new VDPColumnCalculationLogic();

			vccl.setRecordType(VDPRecord.VDP_COLUMN_CALCULATION);
			vccl.setViewId(col.getViewId());
			vccl.setSequenceNbr((short) 1); // If there is more than 8K we ar in trouble
			vccl.setColumnId(col.getComponentId());
			vccl.setInputFileId(col.getComponentId());
			vccl.setLogicLength((short) ccl.length());
			vccl.setRecLen((short) (26 + ccl.length()));
			vccl.setLogic(ccl);
			vccl.fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();

			writeColumnCalculationStack(col);
		}
	}

	private void writeColumnCalculationStack(ViewColumn col) {
		VDPColumnCalculationStack vccs = new VDPColumnCalculationStack();

		vccs.setRecordType(VDPRecord.VDP_COLUMN_CALCULATION_LT);
		vccs.setViewId(col.getViewId());
		vccs.setSequenceNbr((short) 1);
		vccs.setColumnId(col.getComponentId());
		vccs.setRecordId(col.getComponentId());
		vccs.setInputFileId(0);

		for (int i = 0; i<16; i += 1) {
			vccs.getStackHeaderPrefix().add((byte)0);
		}
		vccs.setFunctionCode("CALC");
		for (int i = 0; i<40; i += 1) {
			vccs.getStackHeaderSuffix().add((byte)0);
		}
		CalcStack colCalcStack = col.getColumnCalculationStack();
		colCalcStack.fill(vccs.getStack());
		vccs.setRecLen((short) (60 + colCalcStack.getStackLength()));
		vccs.setStackLength(colCalcStack.getStackLength());

		vccs.fillTheWriteBuffer(VDPWriter);
		VDPWriter.setLengthFromPosition(); //Need this for the variable length records
		VDPWriter.writeAndClearTheRecord();
	}

	/**
	 * My initial approach was to write the ViewColumnSources when we wrote the
	 * column. This leads to great badness. Since the record types are out of order.
	 * When a subsequent read is done we get into a situation of having a view
	 * column source without a view source. Hence they are written on their own.
	 * 
	 * @param view
	 */
	private void writeViewColumnSources(ViewNode view) {
		Iterator<ViewColumn> ci = view.getColumnIterator();
		while (ci.hasNext()) {
			ViewColumn col = ci.next();
			Iterator<ViewColumnSource> vcsi = col.getIteratorForSourcesByNumber();
			while (vcsi.hasNext()) {
				writeViewColumnSource(col.getColumnNumber(), vcsi.next());
			}
		}
	}

	private void writeViewColumnSource(int colNumber, ViewColumnSource vcs) {
		VDPViewColumnSource vvcs = new VDPViewColumnSource();
		vvcs.fillFromComponent(vcs);
		vvcs.fillTheWriteBuffer(VDPWriter);
		VDPWriter.writeAndClearTheRecord();
		writeColumnLogic(colNumber, vcs);
	}

	private void writeColumnLogic(int colNumber, ViewColumnSource vcs) {
		String cl = vcs.getLogicText();
		VDPColumnLogic vcl = new VDPColumnLogic();

		vcl.setRecordType(VDPRecord.VDP_COLUMN_LOGIC);
		vcl.setViewId(vcs.getViewId());
		vcl.setColumnId(colNumber);
		vcl.setSequenceNbr(vcs.getSequenceNumber());
		vcl.setRecordId(vcs.getComponentId());
		vcl.setInputFileId(vcs.getComponentId());
		vcl.setLogicLength((short) cl.length());
		vcl.setRecLen((short) (26 + cl.length()));
		vcl.setLogic(cl);
		vcl.fillTheWriteBuffer(VDPWriter);
		VDPWriter.setLengthFromPosition(); //Need this for the variable length records
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeExtractTargetSet(VDPExtractTargetSet extractTargets) {
		if (extractTargets != null) {
			extractTargets.fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();
		}
	}

		private void writeViewDefinition(ViewDefinition viewDefinition) {
		VDPViewDefinition vvd = new VDPViewDefinition();
		vvd.fillFromComponent(viewDefinition);
		vvd.setOutputPageSizeMax((short)66);
		vvd.setOutputLineSizeMax((short)250);
		vvd.fillTheWriteBuffer(VDPWriter);
		//set some defaults
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeExtractRecordFile() {
		if (vdpMgmtRecs.getExtractRecordFile() != null) {
			vdpMgmtRecs.fillExtractFileNumbersFrom(Repository.getExtractFileNubers());
			vdpMgmtRecs.getExtractRecordFile().fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition();
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeExtractOutputFile() {
		VDPExtractFile eof = vdpMgmtRecs.getExtractOutputFile();
		if(eof != null) {
			eof.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeLookupTargetSet() {
		Iterator<JoinTargetEntry> jti = Repository.getJoinViews().getJoinTargetsIterator();
		VDPLookupPathTargetSet lpts = new VDPLookupPathTargetSet();
		lpts.setRecordId(650);
		List<VDPLookupPathTargetEntry> refViews = lpts.getRefViews();
		while(jti.hasNext()) {
			JoinTargetEntry jt = jti.next();
			VDPLookupPathTargetEntry lpte = new VDPLookupPathTargetEntry();
			lpte.setGrefJoinid(jt.lfid);
			lpte.setRefFlag(jt.flag==1);
			lpte.setGrefEntry(jt.lfName);
			lpte.setGrefEntlen(jt.lfName.length());
			refViews.add(lpte);
		}
		if(refViews.size() > 0) {
			lpts.setRefCount(refViews.size());
			lpts.fillTheWriteBuffer(VDPWriter);
		} else if (vdpMgmtRecs.getLookupPathTargetSet() != null) {
			vdpMgmtRecs.getLookupPathTargetSet().fillTheWriteBuffer(VDPWriter);
		}
		VDPWriter.setLengthFromPosition(); //Need this for the variable length records
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeLookupGenMap() {
		if (vdpMgmtRecs.getLookupPathGenerationMap() != null) {
			vdpMgmtRecs.getLookupPathGenerationMap().fillTheWriteBuffer(VDPWriter);
			VDPWriter.setLengthFromPosition(); //Need this for the variable length records
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeLookupPaths() {
		Iterator<LookupPath> lpi = Repository.getLookups().getIterator();
		while (lpi.hasNext()) {
			LookupPath lp = lpi.next();
			Iterator<LookupPathStep> lpsi = lp.getStepIterator();
			while (lpsi.hasNext()) {
				LookupPathStep lps = lpsi.next();
				Iterator<LookupPathKey> lpki = lps.getKeyIterator();
				while (lpki.hasNext()) {
					VDPLookupPathKey vlpk = new VDPLookupPathKey();
					LookupPathKey lpk = lpki.next();
					lpk.setJoinName(lp.getName());
					vlpk.fillFromComponent(lpk);
					vlpk.fillTheWriteBuffer(VDPWriter);
					VDPWriter.writeAndClearTheRecord();
				}
			}
		}
	}

	private void writeLRIndexes() {
		Iterator<LRIndex> lrii = Repository.getIndexes().getIterator();
		while (lrii.hasNext()) {
			VDPLRIndex vi = new VDPLRIndex();
			vi.fillFromComponent(lrii.next());
			vi.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeLRFields() {
		Iterator<LRField> lrfi = Repository.getFields().getIterator();
		while (lrfi.hasNext()) {
			VDPLRField vlrf = new VDPLRField();
			vlrf.fillFromComponent(lrfi.next());
			vlrf.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeLogicalRecords() {
		Iterator<LogicalRecord> lri = Repository.getLogicalRecords().getIterator();
		while (lri.hasNext()) {
			VDPLogicalRecord vlr = new VDPLogicalRecord();
			vlr.fillFromComponent(lri.next());
			vlr.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writeGenerationRecord() {
		VDPGenerationRecord gen = vdpMgmtRecs.getViewGeneration();
		gen.setAsciiInd(true);
		gen.setVersionInfo((short)13);
		gen.setLrFieldCount(Repository.getFields().size());
		gen.fillTheWriteBuffer(VDPWriter);
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeFormatViewsRecord() {
		//Need to create a default empty record
		//VDP always has one in C++
		if (vdpMgmtRecs.getFormatViews() == null) {
			vdpMgmtRecs.makeFormatViewRecord();
		}
		vdpMgmtRecs.getFormatViews().fillTheWriteBuffer(VDPWriter);
		VDPWriter.setLengthFromPosition(); //Need this for the variable length records
		VDPWriter.writeAndClearTheRecord();
	}

	private void writeControlRecords() {
		Iterator<ControlRecord> cri = Repository.getControlRecords().getIterator();
		while (cri.hasNext()) {
			VDPControlRecord cr = new VDPControlRecord();
			cr.fillFromComponent((ControlRecord)cri.next());
			cr.setEffectiveDate("00000000");
			cr.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	private void writePhysicalFileRecords() {
		// The VDP has an inflated or de normalised view of PFs.
		// So we need to write from the perspective of LFs
		Iterator<LogicalFile> lfi = Repository.getLogicalFiles().getIterator();
		while (lfi.hasNext()) {
			LogicalFile lf = lfi.next();
			Iterator<PhysicalFile> pfi = lf.getPFIterator();
			short seqNum = 1;
			while (pfi.hasNext()) {
				PhysicalFile pf = pfi.next();
				VDPPhysicalFile vdppf = new VDPPhysicalFile();
				if(pf.isRequired()) {
					vdppf.fillFromComponent(pf);
					vdppf.setSequenceNbr(seqNum++);
					vdppf.fillTheWriteBuffer(VDPWriter);
					VDPWriter.writeAndClearTheRecord();
				} else {
					logger.atInfo().log("PF %s %d not required", pf.getName(), pf.getComponentId());
				}
			}
		}
	}

	private void writeExitRecords() {
		Iterator<UserExit> exi = Repository.getUserExits().getIterator();
		while (exi.hasNext()) {
			VDPExit ue = new VDPExit();
			ue.fillFromComponent(exi.next());
			ue.fillTheWriteBuffer(VDPWriter);
			VDPWriter.writeAndClearTheRecord();
		}
	}

	public void close() {
		VDPWriter.close();
	}

}
