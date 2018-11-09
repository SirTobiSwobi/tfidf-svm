package org.SirTobiSwobi.c3.tfidfsvm.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCMetadata;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import static io.dropwizard.testing.FixtureHelpers.*;


public class TCMetadataTest {
	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		String a="example";
		String[] b={"GET /example","POST /example"};	
		
		final TCMetadata generated = new TCMetadata(a,b,a,a,a,a,a,a,a,"true");
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCMetadata.json"), TCMetadata.class));
		
		assertThat(MAPPER.writeValueAsString(generated)).isEqualTo(expected);
		
	}
	
}
