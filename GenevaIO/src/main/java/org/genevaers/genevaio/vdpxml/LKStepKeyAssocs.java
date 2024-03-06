package org.genevaers.genevaio.vdpxml;

import java.util.HashMap;
import java.util.Map;

public class LKStepKeyAssocs {
	private  Map <Integer, Integer> keyAssocs= new HashMap<>();

	public void add(int seqNum, int associd) {
		keyAssocs.put(seqNum, associd);
	}

	public int getAssocID(int seqNum) {
		return keyAssocs.get(seqNum);
	}
}