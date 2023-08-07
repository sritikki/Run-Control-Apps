package org.genevaers.genevaio.ltfile;

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
import java.nio.file.Path;
import java.util.Iterator;

import org.genevaers.genevaio.recordreader.RecordFileReaderWriter;


public class LTWriter {

	private RecordFileReaderWriter rr;

	public void write(LogicTable lt, Path output) throws IOException {
		rr = new RecordFileReaderWriter();
		rr.writeRecordsTo(output.toFile());
		writeRecords(lt);
	}

	private void writeRecords(LogicTable lt) throws IOException{
		Iterator<LTRecord> lti = lt.getIterator();
		while(lti.hasNext()){
			LTFileObject ltr = (LTFileObject) lti.next();
			ltr.fillTheWriteBuffer(rr);
			rr.writeAndClearTheRecord();
		}
	}

	public void close() throws IOException {
		rr.close();
	}

}
