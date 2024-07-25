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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.vdpfile.VDPFileObject;
import org.genevaers.utilities.GersConfigration;

public class RecordFileReaderWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	public static RecordFileReader getReader() {
		RecordFileReader rr = null;
		logger.atFine().log("Using ZosRecordReader");
		try {
			Class<?> rrc;
			if (GersConfigration.isZos()) {
				rrc = Class.forName("org.genevaers.genevaio.recordreader.ZosRecordReader");
			} else {
				rrc = Class.forName("org.genevaers.genevaio.recordreader.BinRecordReader");
			}
			Constructor[] constructors = rrc.getConstructors();
			rr = (RecordFileReader) constructors[0].newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.atSevere().log("getReader failed:\n %s", e.getMessage());
		}
		return rr;
	}

	public static RecordFileWriter getWriter() {
		RecordFileWriter rw = null;
		try {
			Class<?> rrc;
			if (GersConfigration.isZos()) {
				rrc = Class.forName("org.genevaers.genevaio.recordreader.ZosRecordWriter");
				RecordFileWriter.setSpacesEBCDIC();
			} else {
				rrc = Class.forName("org.genevaers.genevaio.recordreader.BinRecordWriter");
			}
			Constructor[] constructors = rrc.getConstructors();
			rw = (RecordFileWriter) constructors[0].newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.atSevere().log("getWriter failed:\n %s", e.getMessage());
		}
		VDPFileObject.setSpaces(RecordFileWriter.getSpaces());
		LTFileObject.setSpaces(RecordFileWriter.getSpaces());
		return rw;
	}

}
