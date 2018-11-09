package org.SirTobiSwobi.c3.tfidfsvm.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategory;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TCCategoryTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		
		
		final TCCategory generated = new TCCategory(0,"first category","first description");
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCCategory.json"), TCCategory.class));
		
		assertThat(MAPPER.writeValueAsString(generated)).isEqualTo(expected);
		
	}

}
