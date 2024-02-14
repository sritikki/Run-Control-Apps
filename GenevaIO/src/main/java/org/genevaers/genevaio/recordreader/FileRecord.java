package org.genevaers.genevaio.recordreader;

import java.nio.ByteBuffer;

import com.google.common.flogger.FluentLogger;

public class FileRecord {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	public short length = 0;
	public ByteBuffer bytes = ByteBuffer.allocate(8 * 1024);
	public String name;

	public int bytesWritten() {
		return bytes.position();
	}

    public void dump() {
		logger.atFine().log("Record length %d", length);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<length; i++) {
			sb.append(String.format("%02X ", bytes.get(i)));
		}
		sb.append("\n");
		logger.atFine().log(sb.toString());
		bytes.rewind();
    }
}