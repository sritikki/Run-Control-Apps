package org.genevaers.genevaio.wbxml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import com.google.common.flogger.FluentLogger;

public class GenerationParser extends RecordParser {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    
    @Override
    public void parseRecord(XMLStreamReader reader) throws XMLStreamException {
		String part = reader.getName().getLocalPart();
		int n = reader.next();
		if (n == XMLEvent.CHARACTERS) {
			String text = reader.getText();
			switch (part) {
				case "CREATEDTIMESTAMP":
                created = reader.getText();
					break;
				
				default:
					break;
			}
        }
    }


}
