package org.genevaers.genevaio.vdpxml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.flogger.FluentLogger;

public class VDPHandler extends DefaultHandler{
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    
	private StringBuilder data;
	private BaseParser currentParser;
	private int currentLRID;
	private int currentIndexID;
	private int currentLookupID;
	private int currentViewID;

	private boolean srcLR;

	private ViewRecordParser currenctViewParser;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch(qName) {
			case "JobInfo":
			logger.atFine().log("Parsing Job Info");
			//parseRecordsForElementWithParser(elementName, new SafrvdpParser(), "Database");
			break;
			case "ControlRecord":
				logger.atFine().log("Parsing ControlRecords");
				currentParser = new CRRecordParser();
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				break;
			case "Partition":
				logger.atFine().log("Parsing Partitions");
				currentParser = new PhysicalFileRecordParser();
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				break;
			case "LogicalFile":
				logger.atFine().log("Logical Files");
				currentParser = new LogicalFileRecordParser();
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				break;
			case "LogicalRecord":
				logger.atFine().log("Logical Record");
				currentParser = new LRRecordParser();
				currentLRID = Integer.parseInt(attributes.getValue(0));
				currentParser.setComponentID(currentLRID);
				break;
			case "Field":
				logger.atFine().log("Logical Field");
				currentParser = new LRFieldRecordParser();
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				((LRFieldRecordParser)currentParser).setCurrentLrId(currentLRID);
				break;
			case "Index":
				logger.atFine().log("Index");
				currentParser = new LRIndexRecordParser();
				currentIndexID = Integer.parseInt(attributes.getValue(0));
				currentParser.setComponentID(currentIndexID);
				((LRIndexRecordParser)currentParser).setCurrentLrId(currentLRID);
				break;
			case "IndexFieldRef":
				logger.atFine().log("Index");
				currentParser = new LRIndexFieldRecordParser();
				((LRIndexFieldRecordParser)currentParser).setCurrentIndexId(currentIndexID);
				((LRIndexFieldRecordParser)currentParser).setSequenceNumber(Integer.parseInt(attributes.getValue(0)));
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				((LRIndexFieldRecordParser)currentParser).setCurrentLrId(currentLRID);
				break;
			case "Lookup":
				logger.atFine().log("Lookup");
				currentParser = new LookupRecordParser();
				currentLookupID = Integer.parseInt(attributes.getValue(0));
				currentParser.setComponentID(currentLookupID);
				break;
			case "Step":
				logger.atFine().log("Step");
				currentParser = new LookupStepParser();
				((LookupStepParser)currentParser).setLookupID(currentLookupID);
				((LookupStepParser)currentParser).setStepNumber(Integer.parseInt(attributes.getValue(0)));
				break;
			// case "LogicalRecordRef":
			// 	logger.atFine().log("LogicalRecordRef");
			// 	((LookupStepParser)currentParser).setLrRef(Integer.parseInt(attributes.getValue(0)));
			// 	break;
			case "KeyField":
				logger.atFine().log("KeyField");
				currentParser =new LookupSourceKeyParser();
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				((LookupSourceKeyParser)currentParser).setSequencNumber(Integer.parseInt(attributes.getValue(1)));
				((LookupSourceKeyParser)currentParser).setLookupID(currentLookupID);
				break;
			case "FieldRef":
				logger.atFine().log("FieldRef");
				((LookupSourceKeyParser)currentParser).setFieldId(Integer.parseInt(attributes.getValue(0)));
				break;
			case "View":
				logger.atFine().log("View");
				currentParser = new ViewRecordParser();
				currenctViewParser = (ViewRecordParser) currentParser;
				currentViewID = Integer.parseInt(attributes.getValue(0));
				currentParser.setComponentID(currentViewID);
				break;
			case "ControlRecordRef":
				logger.atFine().log("ControlRecordRef");
				currenctViewParser.setContolRecord(Integer.parseInt(attributes.getValue(0)));
				break;
			case "DataSource":
				logger.atFine().log("DataSource");
				currentParser = new ViewSourceRecordParser();
				currentParser.setComponentID(Integer.parseInt(attributes.getValue(0)));
				break;


		}		
		data = new StringBuilder();
	}

	private void setSourceLR(int int1) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setSourceLR'");
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (currentParser != null) {
			currentParser.addElement(qName, data.toString());
		}

		if (qName.equalsIgnoreCase("SAFRVDP")) {
			System.out.println("End of VDP");
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
