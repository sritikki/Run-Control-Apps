package org.genevaers.genevaio.wbxml;

import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.google.common.flogger.FluentLogger;

public class GenerationParser extends RecordParser {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void parseRecord(XMLStreamReader reader) {
        switch (reader.getName().getLocalPart()) {
            default:
//                logger.atInfo().log(reader.getName().getLocalPart());
                break;
        }

    }

}
