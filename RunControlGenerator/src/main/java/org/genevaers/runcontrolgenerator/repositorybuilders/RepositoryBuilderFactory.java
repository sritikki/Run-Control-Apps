package org.genevaers.runcontrolgenerator.repositorybuilders;




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
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;

public class RepositoryBuilderFactory {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	public static RepositoryBuilder get(RunControlConfigration rcc) {

		switch (rcc.getInputType()) {
			case "WBXML":
				return new DB2Builder(rcc);
			case "VDPXML":
				return new VDPXMLBuilder(rcc);
			case "DB2":
				return new PostgresBuilder(rcc);
			case "POSTGRES":
				return new WBXMLBuilder(rcc);
			default:
				logger.atSevere().log("Unknown Input Type %s", rcc.getInputType());
				return null;
		}
	}

}
