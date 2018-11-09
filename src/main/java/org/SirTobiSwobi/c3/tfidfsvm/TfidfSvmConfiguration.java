package org.SirTobiSwobi.c3.tfidfsvm;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class TfidfSvmConfiguration extends Configuration {

	@NotEmpty
	private String name;
	@NotEmpty
	private String[] calls;
	@NotEmpty
	private String algorithm;
	@NotEmpty
	private String phases;
	@NotEmpty
	private String outputFormat;
	@NotEmpty
	private String configuration;
	@NotEmpty
	private String configOptions;
	@NotEmpty
	private String archetype;
	@NotEmpty
	private String runType;
	@NotEmpty
	private String debugExamples;
	@NotEmpty
	private String featureExtractionThreads;
	
	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
	

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	public String[] getCalls() {
		return calls;
	}

	@JsonProperty
	public void setCalls(String[] calls) {
		this.calls = calls;
	}

	@JsonProperty
	public String getAlgorithm() {
		return algorithm;
	}

	@JsonProperty
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	@JsonProperty
	public String getPhases() {
		return phases;
	}

	@JsonProperty
	public void setPhases(String phases) {
		this.phases = phases;
	}

	@JsonProperty
	public String getOutputFormat() {
		return outputFormat;
	}

	@JsonProperty
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	@JsonProperty
	public String getConfiguration() {
		return configuration;
	}

	@JsonProperty
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	@JsonProperty
	public String getConfigOptions() {
		return configOptions;
	}

	@JsonProperty
	public void setConfigOptions(String configOptions) {
		this.configOptions = configOptions;
	}

	@JsonProperty
	public String getArchetype() {
		return archetype;
	}

	@JsonProperty
	public void setArchetype(String archetype) {
		this.archetype = archetype;
	}
	
	@JsonProperty
	 public String getRunType() {
		return runType;
	}

	@JsonProperty
	public void setRunType(String runType) {
		this.runType = runType;
	}
	
	@JsonProperty
	public String getDebugExamples() {
		return debugExamples;
	}

	@JsonProperty
	public void setDebugExamples(String debugExamples) {
		this.debugExamples = debugExamples;
	}

	@JsonProperty("jerseyClient")
	    public JerseyClientConfiguration getJerseyClientConfiguration() {
	        return jerseyClientConfiguration;
	    }

	public String getFeatureExtractionThreads() {
		return featureExtractionThreads;
	}

	public void setFeatureExtractionThreads(String featureExtractionThreads) {
		this.featureExtractionThreads = featureExtractionThreads;
	}
	
	
}
