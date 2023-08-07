package org.genevaers.compilers;

import java.nio.file.Path;
import java.nio.file.Paths;

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

public class TestHelper {
    public static final String TEST_RESOURCES = "./src/test/resources";

	public static final String MR91DOT = "mr91.dot";
	private static Path resoucesPath = Paths.get(TEST_RESOURCES);

	public static Path getMR91dotPath() {
		return resoucesPath.resolve(MR91DOT);
	}

}
