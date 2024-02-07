package org.genevaers.runcontrolgenerator.repositorybuilders;

import org.genevaers.genevaio.wbxml.WBXMLSaxIterator;
import org.genevaers.repository.data.InputReport;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class WBXMLBuilder extends XMLBuilder{
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public WBXMLBuilder(RunControlConfigration rcc) {
        super(rcc);
    }

    @Override
    protected void buildFromXML(InputReport ir) {
		WBXMLSaxIterator wbReader = new WBXMLSaxIterator();
		try {
			wbReader.setInputBuffer(inputBuffer);
			wbReader.addToRepsitory();
			ir.setGenerationID(wbReader.getGenerationID());
			retval = Status.OK;
		} catch (Exception e) {
			logger.atSevere().withStackTrace(StackSize.FULL).log("Repo build failed " + e.getMessage());
			retval = Status.ERROR;
		}
	}

    @Override
    protected String getXMLDirectory() {
        return rcc.getWBXMLDirectory();
    }


    
}
