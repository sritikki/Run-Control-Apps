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
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.ibm.jzos.ZFileException;

public class ZosRecordReader extends RecordFileReader {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    com.ibm.jzos.RecordReader reader = null;
	protected static String spaces = StringUtils.repeat("@", 1536); //!!! Don't change this length  !!!!!
	private FileRecord record = new FileRecord();

    @Override
    public void close() {
        try {
            logger.atInfo().log("Close dataset");
            reader.close();
        } catch (com.ibm.jzos.ZFileException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void readRecordsFrom(File file) throws IOException {
        String ddname = "//:DD" +file.toString();
        System.out.println("Open DD Name = " + ddname);
        reader = com.ibm.jzos.RecordReader.newReaderForDD(ddname);
}

    @Override
    public FileRecord readRecord() {
        try {
            reader.read(record.bytes.array());
        } catch (ZFileException e) {
            logger.atSevere().withCause(e).withStackTrace(StackSize.FULL);   
        } 
        return record;
    }

}
