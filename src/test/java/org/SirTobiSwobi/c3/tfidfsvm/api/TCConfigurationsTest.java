package org.SirTobiSwobi.c3.tfidfsvm.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfiguration;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfigurations;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TCConfigurationsTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		//new Configuration(long id, int folds, boolean includeImplicits, double assignmentThreshold, SelectionPolicy selectionPolicy) 
		ConfigurationManager confMan =  new ConfigurationManager();
		Configuration cfgn = new Configuration(1,2, true, 0.5,SelectionPolicy.MicroaverageF1, 5);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(3,3, true, 0.6,SelectionPolicy.MacroaverageF1, 10);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(5,5, true, 0.4,SelectionPolicy.MicroaverageRecall, 15);
		confMan.setConfiguration(cfgn);
		
		
		Configuration[] configurations = confMan.getConfigurationArray();
		TCConfiguration[] TCconfigurationArray = new TCConfiguration[configurations.length];
		for(int i=0; i<configurations.length;i++){
			Configuration conf = configurations[i];
			TCConfiguration TCconf = new TCConfiguration(conf.getId(),
					conf.getFolds(),
					conf.isIncludeImplicits(), 
					conf.getAssignmentThreshold(),
					conf.getSelectionPolicy().toString(),
					conf.getTopTermsPerCat());
			TCconfigurationArray[i]=TCconf;
		}
		TCConfigurations TCconfigurations = new TCConfigurations(TCconfigurationArray);
	
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCConfigurations.json"), TCConfigurations.class));
		
		assertThat(MAPPER.writeValueAsString(TCconfigurations)).isEqualTo(expected);
		
	}


}
