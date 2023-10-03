package FlowAnalyser;

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


import org.junit.jupiter.api.Test;


import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestFlowAnalyzer {
	
	private List<File> redFiles = new ArrayList<File>();
	private File vtable;
	private File lkupstable;
	private File lrs;
	private File lfs;
	private File pfs;
	private File exits;
	private File crTable;
	private File generation;
	private Path cwd;
	private File vcsv;
	private File xltcsv;
	private File jltcsv;

//	@Test
//	public void testRunControlAnalyserReadsVDP() throws Exception {
//		RunControlAnalyser fa = new RunControlAnalyser();
//		fa.readVDP("blah.vdp");
//		assertTrue(fa.getVDP().getGenerationRecordRecord().numberRecords > 0);
//	}
//
//	@Test
//	public void testCurrentWorkingDirectory() throws Exception {
//		String locroot = System.getProperty("user.dir");
//    	//locroot = locroot.replaceAll("^[Cc]:", "");
//    	locroot = locroot.replace("\\", "/");
////		cwd = Paths.get(locroot).resolve("VDPs/test");
//		cwd = Paths.get("C:/Users/IanCunningham/VDPs/test");
//		File html = cwd.resolve("GEBT.ETEST.OLDR.OLDP3.MR91.VDP.html").toFile();
//		html.delete();
//		makeDeleteVDPHTMLFiles(cwd);
//		makeDeleteCSVFiles(cwd);
//		makeDeleteRedFiles(cwd,1, 24);
//		File v1680 = cwd.resolve("views/v1680"+".dot").toFile();
//		v1680.delete();
//		File v1680SVG = cwd.resolve("views/v1680"+".dot.svg").toFile();
//		v1680SVG.delete();
//		FlowDriver driver = new FlowDriver();
//		driver.setCurrentWorkingDirectory("C:/Users/IanCunningham/VDPs/test");
//		driver.generateFromRunControlFilesIn("GEBT.ETEST.OLDR.OLDP3.MR91", true, true, "");
//		assertTrue(html.exists());
//		checkAllVDPHTMLFilesExist();
//		checkAllCSVFilesExist();
//		checkAllRedFilesExist(cwd,1, 24);
//		assertTrue(v1680.exists());
//		assertTrue(v1680SVG.exists());
//		
//	}


	private void checkAllCSVFilesExist() {
		// assertTrue(vtable.exists());
		// assertTrue(xltcsv.exists());
		// assertTrue(jltcsv.exists());	
	}

	private void makeDeleteCSVFiles(Path cwd2) {
		vcsv = cwd.resolve("GEBT.ETEST.OLDR.OLDP3.MR91.VDP.csv").toFile();
		vcsv.delete();
		xltcsv =cwd.resolve("GEBT.ETEST.OLDR.OLDP3.MR91.XLT.csv").toFile();
		xltcsv.delete();
		jltcsv = cwd.resolve("GEBT.ETEST.OLDR.OLDP3.MR91.JLT.csv").toFile();
		jltcsv.delete();
	}

	private void checkAllVDPHTMLFilesExist() {
		// assertTrue(vtable.exists());
		// assertTrue(lkupstable.exists());
		// assertTrue(lrs.exists());
		// assertTrue(lfs.exists());
		// assertTrue(pfs.exists());
		// assertTrue(exits.exists());
		// assertTrue(crTable.exists());
		// assertTrue(generation.exists());
	}

	private void makeDeleteVDPHTMLFiles(Path cwd) {
		vtable = cwd.resolve("ViewsTable.html").toFile();
		vtable.delete();
		lkupstable =cwd.resolve("LookupsTable.html").toFile();
		lkupstable.delete();
		lrs = cwd.resolve("LRs.html").toFile();
		lrs.delete();
		lfs = cwd.resolve("LFs.html").toFile();
		lfs.delete();
		pfs = cwd.resolve("PFs.html").toFile();
		pfs.delete();
		exits = cwd.resolve("Exits.html").toFile();
		exits.delete();
		crTable = cwd.resolve("CRTable.html").toFile();
		crTable.delete();
		generation = cwd.resolve("Generation.html").toFile();
		generation.delete();
	}

	private void checkAllRedFilesExist(Path cwd, int redStart, int redEnd) {
		for(File f : redFiles) {
//			assertTrue(f.exists());
		}
	}

	private void makeDeleteRedFiles(Path cwd, int redStart, int redEnd) {
		redFiles.clear();
		for(int i = redStart; i<redEnd; i++) {
			File redf = cwd.resolve("RED"+i+".dot").toFile();
			redf.delete();
			redFiles.add(redf);
			File redsvg = cwd.resolve("RED"+i+".dot.svg").toFile();
			redsvg.delete();
			redFiles.add(redsvg);
		}
	}

}
