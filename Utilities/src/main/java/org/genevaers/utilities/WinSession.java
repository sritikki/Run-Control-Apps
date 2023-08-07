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



import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WinSession extends FTPSession {

	public WinSession(String host, String userid, String password) {
		super(host);
	}

	public void getFile(String srcFile, File destFile) throws IOException {
		//pretend to be the FTP session
		//verify the output file exists?
		if(destFile.exists() == false)
		{
			logger.log(Level.SEVERE, "Dest file" + destFile.getName() + " not found");
		}
	}
	
	public void disconnect() throws IOException {
		// yeah right
	}

	public void setASCII() throws IOException {
		// yeah right
	}

	public void setBinary() throws IOException {
		// yeah right
	}
}
