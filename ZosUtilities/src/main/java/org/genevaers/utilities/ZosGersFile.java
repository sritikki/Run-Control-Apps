package org.genevaers.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import com.google.common.flogger.FluentLogger;
import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileException;

public class ZosGersFile extends GersFile{
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public Writer getWriter(String name) throws IOException {
        ZFile dd = new ZFile("//DD:" + name, "w");
		return new OutputStreamWriter(dd.getOutputStream(), "IBM-1047");
    }

    public Reader getReader(String name) {
		String ddname = "//DD:" + name;
		logger.atInfo().log("Read %s", ddname);
		try {
			return new BufferedReader(new InputStreamReader(new ZFile(ddname, "r").getInputStream()));
		} catch (ZFileException e) {
			logger.atSevere().log("Zos GersFile getReader failed %s", e.getMessage());
		}
		return null;
    }

    public boolean exists(String name) {
		boolean retval = false;
		String dd = "//DD:" + name;
		try {
			retval = ZFile.exists(dd);
		} catch (ZFileException e) {
			logger.atSevere().log("Zfile %s", e.getMessage());
		} 
		return retval;
    }

	

}
