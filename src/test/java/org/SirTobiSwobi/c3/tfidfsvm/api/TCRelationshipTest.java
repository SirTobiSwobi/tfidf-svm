package org.SirTobiSwobi.c3.tfidfsvm.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCRelationship;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TCRelationshipTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
	
	@Test
	public void serializesToJSON() throws Exception {
		
		
		final TCRelationship generated = new TCRelationship(1,0,4,"Sub");
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCRelationship.json"), TCRelationship.class));
		
		assertThat(MAPPER.writeValueAsString(generated)).isEqualTo(expected);
		
	}

	

}
