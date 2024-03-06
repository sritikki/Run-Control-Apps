package org.genevaers.genevaio.vdpxml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import com.google.common.flogger.FluentLogger;

public class SafrvdpParser extends BaseParser {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	@Override
	public void addElement(String name, String text) {
		switch (name) {
			case "CREATEDTIMESTAMP":
				created = text;
				break;

			default:
				break;
		}

	}

}
