package org.genevaers.runcontrolgenerator.repositorybuilders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.genevaers.repository.Repository;
import org.genevaers.repository.data.InputReport;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.ibm.jzos.FileFactory;
import com.ibm.jzos.PdsDirectory;
import com.ibm.jzos.PdsDirectory.MemberInfo;
import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileConstants;
import com.ibm.jzos.ZFileException;

public abstract class XMLBuilder implements RepositoryBuilder{
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	protected BufferedInputStream inputBuffer;
	private Status retval = Status.OK;

    public XMLBuilder() {
    }

    @Override
    public Status run() {
		if (RunControlConfigration.isZos()) {
			readFromDataSet();
		} else {
			readFromDirectory();
		}
        return retval;
    }
    
	protected void readFromDataSet() {
		Status retval;
		try {
			String ddname = "//DD:" + RunControlConfigration.getWBXMLDirectory();
			ZFile dd = new ZFile(ddname, "r");
			// Problem here is that this will be a PDS and we need to iterate its memebers
			int type = dd.getDsorg();
			switch (type) {
				case ZFileConstants.DSORG_PDSE: 
					logger.atInfo().log("found PDS E");
					//Drop through
				case ZFileConstants.DSORG_PDS_DIR: {
					logger.atInfo().log("found PDS");
					String pdsName = dd.getActualFilename();
					logger.atInfo().log("Actual filename " + pdsName);
					try {
						PdsDirectory pds = new PdsDirectory(ddname);
						Iterator pdsi = pds.iterator();
						while (pdsi.hasNext()) {
							MemberInfo mem = (MemberInfo) pdsi.next();
							String mname = mem.getName();
							String buildName = ddname + "(" + mname + ")";
							logger.atInfo().log("Build Repo from " + buildName);
							inputBuffer = FileFactory.newBufferedInputStream(buildName);
							// ZFile pdsmem = new ZFile(buildName, "r");
							InputReport ir = new InputReport();
							ir.setDdName(ddname);
							ir.setMemberName(mname);
							buildFromXML(ir);
							// pdsmem.close();
							inputBuffer.close();
						}
					} catch (IOException e) {
						retval = Status.ERROR;
					}
				}
					break;
				case ZFileConstants.DSORG_PDS_MEM:
				case ZFileConstants.DSORG_PS:
				default:
					logger.atSevere().log("Unhandled DSORG " + type);
			}
			dd.close();
		} catch (ZFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract void buildFromXML(InputReport ir);
	protected abstract String getXMLDirectory();

	protected void readFromDirectory() {
		Path xmlPath = Paths.get(getXMLDirectory());
		if (xmlPath.toFile().exists()) {
			WildcardFileFilter fileFilter = new WildcardFileFilter("*.xml");
			Collection<File> xmlFiles = FileUtils.listFiles(xmlPath.toFile(), fileFilter, TrueFileFilter.TRUE);

			for (File d : xmlFiles) {
				logger.atFine().log("Read %s", d.getName());
				try {
					inputBuffer = new BufferedInputStream(new FileInputStream(d)); 
					InputReport ir = new InputReport();
					ir.setDdName(getXMLDirectory());
					ir.setMemberName(d.getName());
					buildFromXML(ir);
					Repository.addInputReport(ir);
				} catch (FileNotFoundException e) {
					logger.atSevere().withStackTrace(StackSize.FULL).log("Repo build failed " + e.getMessage());
					retval = Status.ERROR;
				}
			}
		} else {
			logger.atSevere().log("WBXML file %s not found", xmlPath.toString());
		}
	}

}
