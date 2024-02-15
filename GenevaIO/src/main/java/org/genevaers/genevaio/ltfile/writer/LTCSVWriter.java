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

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LTRecord;
import org.genevaers.genevaio.ltfile.LogicTable;
import org.genevaers.repository.components.enums.LtRecordType;
import org.genevaers.utilities.GersConfigration;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.ibm.jzos.ZFile;


public class LTCSVWriter {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private static Set<LtRecordType> headerMap = new HashSet<>();

	public static void write(LogicTable lt, String output) {
		ZFile dd;
		if (GersConfigration.isZos()) {
			try {
				logger.atInfo().log("Write LT CSV report to %s", output);
				dd = new ZFile("//DD:" + output, "w");
				writeTheLtDetailsToDnname(lt, dd);
				dd.close();
			} catch (IOException e) {
				logger.atSevere().log("Unable to create DDname %s", output);
			}
		} else {
			writeTheLtDetailsToFile(lt, output);
		}
		logger.atInfo().log("LT report written");
	}

	private static void writeRecords(LogicTable lt, Writer fw) throws IOException {
		Iterator<LTRecord> lti = lt.getIterator();
		while(lti.hasNext()){
			LTRecord ltr = lti.next();
			writeHeaderForTypeIfNeeded((LTFileObject) ltr, ltr.getRecordType(), fw);
			writeRecordCSV((LTFileObject) ltr, fw);
		}
	}

	private static void writeTheLtDetailsToFile(LogicTable lt, String output) {
		try (Writer fw = new FileWriter(output)){
			writeRecords(lt, fw);
		} catch (IOException e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
		}
	}

	private static void writeTheLtDetailsToDnname(LogicTable lt, ZFile dd) {
		logger.atFine().log("Stream details");
		try (Writer fw = new OutputStreamWriter(dd.getOutputStream(), "IBM-1047");) {
			writeRecords(lt, fw);
		}
		catch (Exception e) {
			logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);
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
