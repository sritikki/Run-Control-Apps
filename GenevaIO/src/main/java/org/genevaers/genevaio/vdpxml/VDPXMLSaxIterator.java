package org.genevaers.genevaio.vdpxml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.genevaers.repository.data.InputReport;
import org.xml.sax.SAXException;

import com.google.common.flogger.FluentLogger;

public class VDPXMLSaxIterator {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private XMLStreamReader reader;
    private BufferedInputStream inputBuffer;

    private TreeMap<String, CatalogEntry> catalog = new TreeMap<>();

    private Object saxParserFactory;

    public void addToRepsitory() {
        initXMLFactoriesAndParse();
        dumpCatalog();
    }

    private void dumpCatalog() {
    }

    private void initXMLFactoriesAndParse() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        // https://rules.sonarsource.com/java/RSPEC-2755
        // prevent xxe
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            VDPHandler handler = new VDPHandler();
            saxParser.parse(inputBuffer, handler);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void readVDPXML() throws NumberFormatException, XMLStreamException {
        readTheRecords();
        fixupTheAssociations();
    }

    private void fixupTheAssociations() {
        RecordParserData.viewSourceFixups();
        RecordParserData.lookupfixups();
        RecordParserData.viewOutputFixups();
        RecordParserData.buildLFs();
    }

    private void readTheRecords() throws XMLStreamException {
        int eventType = reader.getEventType();
        while (reader.hasNext()) {

            eventType = reader.next();

            if (eventType == XMLEvent.START_ELEMENT) {
                // System.out.println(eventType);
                // System.out.println(reader.getName().getLocalPart());
                String elementName = reader.getName().getLocalPart();
                switch (elementName) {
                    case "SAFRVDP":
                        break;
                    case "JobInfo":
                        logger.atFine().log("Parsing Job Info");
                        parseRecordsForElementWithParser(elementName, new SafrvdpParser(), "Database");
                        break;
                    // case "Environment":
                    // logger.atFine().log("Environment");
                    // parseRecordsForElementWithParser(elementName, new EnvironmentParser());
                    // break;
                    case "ControlRecords":
                        logger.atFine().log("Parsing ControlRecords");
                        parseRecordsForElementWithParser(elementName, new CRRecordParser(), "ControlRecord");
                        break;
                    // case "View-Column":
                    // logger.atFine().log("Parsing View-Column");
                    // parseRecordsForElementWithParser(elementName, new ViewColumnRecordParser());
                    // break;
                    // case "View-Source":
                    // logger.atFine().log("Parsing View-Source");
                    // parseRecordsForElementWithParser(elementName, new ViewSourceRecordParser());
                    // break;
                    // case "View-Column-Source":
                    // logger.atFine().log("Parsing View-Column-Source");
                    // parseRecordsForElementWithParser(elementName, new
                    // ViewColumnSourceRecordParser());
                    // break;
                    // case "View-SortKey":
                    // logger.atFine().log("Parsing View-SortKey");
                    // parseRecordsForElementWithParser(elementName, new ViewSortKeyRecordParser());
                    // break;
                    // case "View-HeaderFooter":
                    // logger.atFine().log("Parsing View-HeaderFooter");
                    // parseRecordsForElementWithParser(elementName, new ViewHeaderFooterParser());
                    // break;
                    // case "Lookup":
                    // logger.atFine().log("Parsing Lookup");
                    // parseRecordsForElementWithParser(elementName, new LookupRecordParser());
                    // break;
                    // case "Lookup-Source-Key":
                    // logger.atFine().log("Parsing Lookup-Source-Key");
                    // parseRecordsForElementWithParser(elementName, new
                    // LookupSourceKeyRecordParser());
                    // break;
                    // case "Lookup-Step":
                    // logger.atFine().log("Parsing Lookup-Step");
                    // parseRecordsForElementWithParser(elementName, new LookupStepRecordParser());
                    // break;
                    // case "LogicalRecord":
                    // logger.atFine().log("Parsing LogicalRecord");
                    // parseRecordsForElementWithParser(elementName, new LRRecordParser());
                    // break;
                    // case "LRField":
                    // logger.atFine().log("Parsing LRField");
                    // parseRecordsForElementWithParser(elementName, new LRFieldRecordParser());
                    // break;
                    // case "LR-Field-Attribute":
                    // logger.atFine().log("Parsing LR-Field-Attribute");
                    // parseRecordsForElementWithParser(elementName, new
                    // LRFieldAttributeRecordParser());
                    // break;
                    // case "LR-Index":
                    // logger.atFine().log("Parsing LR-Index");
                    // parseRecordsForElementWithParser(elementName, new LRIndexRecordParser());
                    // break;
                    // case "LR-LF-Association":
                    // logger.atFine().log("Parsing LR-LF-Association");
                    // parseRecordsForElementWithParser(elementName, new LRLFAssocRecordParser());
                    // break;
                    // case "LR-IndexField":
                    // logger.atFine().log("Parsing LR-IndexFiel");
                    // parseRecordsForElementWithParser(elementName, new
                    // LRIndexFieldRecordParser());
                    // break;
                    // case "LogicalFile":
                    // logger.atFine().log("Parsing LogicalFile");
                    // parseRecordsForElementWithParser(elementName, new LogicalFileRecordParser());
                    // break;
                    // case "LF-PF-Association":
                    // logger.atFine().log("Parsing LF-PF-Association");
                    // parseRecordsForElementWithParser(elementName, new LFPFAssocRecordParser());
                    // break;
                    case "Partitions":
                        logger.atFine().log("Parsing PhysicalFile");
                        parseRecordsForElementWithParser(elementName, new PhysicalFileRecordParser(), "Partiton");
                        break;
                    // case "Exit":
                    // logger.atFine().log("Parsing Exit");
                    // parseRecordsForElementWithParser(elementName, new ExitRecordParser());
                    // break;
                    // case "ControlRecord":
                    // logger.atFine().log("Parsing ControlRecord");
                    // parseRecordsForElementWithParser(elementName, new CRRecordParser());
                    // break;
                }

            }

            if (eventType == XMLEvent.END_ELEMENT) {
                // System.out.println(reader.getName().getLocalPart());
                // if </staff>
                if (reader.getName().getLocalPart().equals("SAFRVDP")) {
                    logger.atInfo().log("All Done");
                }
            }

        }
    }

    private void parseRecordsForElementWithParser(String elementName, BaseParser rp, String recordType)
            throws XMLStreamException {
        addRecordsUsingParserForElementName(rp, elementName, recordType);
    }

    private void addRecordsUsingParserForElementName(BaseParser rp, String elementName, String recordType)
            throws XMLStreamException {
        boolean record = false;
        boolean notdone = true;
        int eventType = reader.getEventType();
        while (notdone && reader.hasNext()) {

            eventType = reader.next();

            if (eventType == XMLEvent.START_ELEMENT) {
                String name = reader.getName().getLocalPart();
                if (name.equals(recordType)) {
                    record = true;
                } else {
                    if (record) {
//                        rp.parseRecord(reader); // Calls derived class
                    }
                }
            }

            if (eventType == XMLEvent.ATTRIBUTE) {
                String attr = reader.getName().getLocalPart();
                int bang = 1;
            }

            if (eventType == XMLEvent.END_ELEMENT) {
                if (reader.getName().getLocalPart().equals(recordType)) {
                    rp.endRecord();
                    addCatalogEntry(elementName, rp);
                    record = false;
                }
                if (reader.getName().getLocalPart().equals(elementName)) {
                    notdone = false;
                }
            }
        }
    }

    private void addCatalogEntry(String type, BaseParser parser) {
        CatalogEntry ce = new CatalogEntry();
        ce.setType(type);
        ce.setId(parser.getComponentID());
        ce.setName(parser.getComponentName());
        ce.setCreated(parser.getCreated());
        ce.setLastMod(parser.getLastMod());

        catalog.put(type + parser.getComponentID(), ce);
    }

    public Collection<CatalogEntry> getCatalogEntries() {
        return catalog.values();
    }

    public void setInputBuffer(BufferedInputStream ib) {
        inputBuffer = ib;
    }

    public String getGenerationID() {
        return "VDP XML generation id to come";
    }

}
