package org.genevaers.utilities;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import com.google.common.flogger.FluentLogger;

public class WinIdsReader extends IdsReaderBase {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	public Set<Integer> readIds(String parmName) {
		Path parmPath = Paths.get(parmName);
		try (BufferedReader parmReader = new BufferedReader(new FileReader(parmPath.toFile()))) {
			logger.atInfo().log("Reading %s", parmName);
			parseLines(parmReader);
		} catch (IOException e) {
			result = IDS_RESULT.FAIL;
			logger.atInfo().log("Read failed. Cannot open %s\n%s", parmName, e.getMessage());
		}
		return ids;
	}


}
