package org.genevaers.genevaio.recordreader;

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
import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.vdpfile.VDPFileObject;
import org.genevaers.utilities.GersConfigration;

public class RecordFileReaderWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	public static RecordFileReader getReader() {
		RecordFileReader rr;
		if(GersConfigration.isZos()) {
			logger.atFine().log("Using ZosRecordReader");
			rr = new ZosRecordReader();
		} else {
			logger.atFine().log("Using BinRecordReader");
			rr = new BinRecordReader();
		}
		return rr;
	}

	public static RecordFileWriter getWriter() {
		RecordFileWriter rw;
		if(GersConfigration.isZos()) {
			logger.atFine().log("Using ZosRecordWriter");
			rw = new ZosRecordWriter();
			RecordFileWriter.setSpacesEBCDIC();
		} else {
			logger.atFine().log("Using BinRecordWriter");
			rw = new BinRecordWriter();
		}
		VDPFileObject.setSpaces(RecordFileWriter.getSpaces());
		LTFileObject.setSpaces(RecordFileWriter.getSpaces());
		return rw;
	}

}
