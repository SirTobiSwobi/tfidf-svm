package org.SirTobiSwobi.c3.tfidfsvm.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCDocument;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import static io.dropwizard.testing.FixtureHelpers.*;


public class TCDocumentTest {
	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		
		
		final TCDocument generated = new TCDocument(0,"first label","first content","");
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCDocument.json"), TCDocument.class));
		
		assertThat(MAPPER.writeValueAsString(generated)).isEqualTo(expected);
		
	}
	
}
