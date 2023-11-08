package org.genevaers.repository.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.genevaers.repository.components.enums.DateCode;
import org.junit.jupiter.api.Test;

public class NormalisedDatesTest {
    
	@Test
	public void testNormalisations() throws Exception {
		assertEquals("0000000000000000", NormalisedDate.get("123", DateCode.CYMD));
		assertEquals("2023110700000000", NormalisedDate.get("20231107", DateCode.CYMD));
		assertEquals("0023110700000000", NormalisedDate.get("231107", DateCode.YYMMDD));
		assertEquals("2023110700000000", NormalisedDate.get("11072023", DateCode.MMDDCCYY));
	}
}
