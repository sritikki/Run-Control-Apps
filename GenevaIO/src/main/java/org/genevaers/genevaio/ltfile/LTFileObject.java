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

import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.recordreader.RecordFileReaderWriter;
import org.genevaers.genevaio.recordreader.RecordFileReaderWriter.FileRecord;

public abstract class LTFileObject {

	protected static String spaces = "";
	
	public abstract void readRecord(LTRecordReader reader, FileRecord rec) throws Exception;
   	public abstract void addRecordNodes(FieldNodeBase root, boolean compare);

	public abstract void writeCSV(FileWriter csvFile) throws IOException;
	public abstract void writeCSVHeader(FileWriter csvFile) throws IOException;
	public abstract void fillTheWriteBuffer(RecordFileReaderWriter rw);

	public static void setSpaces(String spaces) {
		LTFileObject.spaces = spaces;
	}

	protected Cookie cookieReader(int valueLength, LTRecordReader reader, FileRecord rec) throws Exception {
		Cookie c = new Cookie("");
        if(valueLength < 0) {
            byte[] bytes = new byte[256];
            rec.bytes.get(bytes, 0, 256);
            StringBuilder result = new StringBuilder();
            for (int i=0; i< Integer.BYTES; i++) {
                result.append(String.format("%02X", bytes[i]));
            }
            c.setIntegerData(result.toString());
          } else {
            rec.bytes.get(reader.getCleanStringBuffer(256), 0, 256);
            c.setIntegerData(reader.convertStringIfNeeded(reader.getStringBuffer(), 256).trim());
        }
           return c;
	}

	protected void cookieWriter(Cookie value, RecordFileReaderWriter readerWriter, FileRecord buffer) {
        buffer.bytes.putInt(value.length());
        if(value.length() < 0) {
            buffer.bytes.put(value.getBytes(), 0, 256);
        } else {
            buffer.bytes.put(readerWriter.convertOutputIfNeeded(value.getString()));
            buffer.bytes.put(spaces.getBytes(), 0, (256 - value.length()));
        }
	}

}
