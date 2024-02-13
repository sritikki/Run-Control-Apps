package org.genevaers.genevaio.recordreader;

import java.nio.ByteBuffer;

public class FileRecord {
	public short length = 0;
	public ByteBuffer bytes = ByteBuffer.allocate(8 * 1024);
	public String name;

	public int bytesWritten() {
		return bytes.position();
	}
}