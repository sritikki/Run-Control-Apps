package org.genevaers.genevaio.dots;

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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class REDDotFile extends DOTFile{

	private int redID;
	private FileWriter fw;
	private File redDot;
	private boolean open = false;

	public REDDotFile(int id) {
		redID = id;
	}

	public void open(Path cwd) throws IOException {
		redDot = cwd.resolve("RED"+redID+".dot").toFile();
		fw = new FileWriter(redDot);
		open = true;
	}

	public void close() throws IOException {
		open = false;
		fw.close();
	}

	public void write(String str) throws IOException {
		fw.write(str);
	}

	public boolean isOpen() {
		return open;
	}

	public File getRedDot() {
		return redDot;
	}

}
