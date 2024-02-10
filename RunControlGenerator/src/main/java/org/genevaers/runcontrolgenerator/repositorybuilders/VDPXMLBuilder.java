package org.genevaers.runcontrolgenerator.repositorybuilders;


import org.genevaers.genevaio.vdpxml.VDPXMLSaxIterator;
import org.genevaers.repository.data.InputReport;
import org.genevaers.runcontrolgenerator.configuration.RunControlConfigration;
import org.genevaers.runcontrolgenerator.utility.Status;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class VDPXMLBuilder extends XMLBuilder{
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public VDPXMLBuilder(RunControlConfigration rcc) {
        super(rcc);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void buildFromXML(InputReport ir) {
        VDPXMLSaxIterator vdpxmlReader = new VDPXMLSaxIterator();
		try {
            vdpxmlReader.setInputBuffer(inputBuffer);
            vdpxmlReader.addToRepository();
            ir.setGenerationID(vdpxmlReader.getGenerationID());
			retval = Status.OK;
		} catch (Exception e) {
			logger.atSevere().withStackTrace(StackSize.FULL).log("Repo build failed " + e.getMessage());
			retval = Status.ERROR;
		}
	}

    @Override
    protected String getXMLDirectory() {
        return "VDPXMLI";
    }
    
}
