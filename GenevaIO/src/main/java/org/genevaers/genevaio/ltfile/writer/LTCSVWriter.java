package org.genevaers.genevaio.ltfile.writer;

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

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.components.enums.LtRecordType;
import org.genevaers.utilities.GersFile;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;


public class LTCSVWriter {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private static Set<LtRecordType> headerMap = new HashSet<>();

	public static void write(LogicTable lt, String output) {
		logger.atInfo().log("Write LT CSV report to %s", output);
		try(Writer fw = new GersFile().getWriter(output)) {
			writeRecords(lt, fw);
		} catch (IOException e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
		logger.atInfo().log("%s LT report written", output);
	}

	private static void writeRecords(LogicTable lt, Writer fw) throws IOException {
		Iterator<LTRecord> lti = lt.getIterator();
		while(lti.hasNext()){
			LTRecord ltr = lti.next();
			writeHeaderForTypeIfNeeded((LTFileObject) ltr, ltr.getRecordType(), fw);
			writeRecordCSV((LTFileObject) ltr, fw);
		}
	}

	private static void writeHeaderForTypeIfNeeded(LTFileObject ltr, LtRecordType t, Writer fw) throws IOException {
		if(!headerMap.contains(t)) {
			ltr.writeCSVHeader(fw);
			fw.write("\n");
			headerMap.add(t);
		}
	}

	private static void writeRecordCSV(LTFileObject ltr, Writer fw) throws IOException {
		ltr.writeCSV(fw);
		fw.write("\n");
	}
}
