package org.genevaers.grammar;

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
import java.io.InputStream;
import java.util.Properties;
import com.google.common.flogger.FluentLogger;

public class Grammar  {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private Grammar() {
	}

	public static String getVersion() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		String ver = "";
		try (InputStream resourceStream = loader.getResourceAsStream("grammar.properties")) {
			properties.load(resourceStream);
			ver = properties.getProperty("library.name") + ":" + properties.getProperty("build.version");
		} catch (IOException e) {
			logger.atSevere().log("IO exception on grammar version\n%s", e.getMessage());
		}
		return ver;
	}

}
