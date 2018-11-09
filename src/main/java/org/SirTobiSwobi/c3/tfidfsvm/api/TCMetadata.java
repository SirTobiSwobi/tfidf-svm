package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCMetadata {
	private String name;
	private String[] calls;
	private String algorithm;
	private String phases;
	private String outputFormat;
	private String configuration;
	private String configOptions;
	private String archetype;
	private String runType;
	private String debugExamples;
	
	public TCMetadata(){
		// Jackson deserialization
	}

	public TCMetadata(String name, String algorithm) {
		this.name = name;
		this.algorithm = algorithm;
	}
	
	
	
	public TCMetadata(String name, String[] calls, String algorithm, String phases, String outputFormat,
			String configuration, String configOptions, String archetype, String runType, String debugExamples) {
		this.name = name;
		this.calls = calls;
		this.algorithm = algorithm;
		this.phases = phases;
		this.outputFormat = outputFormat;
		this.configuration = configuration;
		this.configOptions = configOptions;
		this.archetype = archetype;
		this.runType = runType;
		this.debugExamples = debugExamples;
	}

	@JsonProperty
	public String getName() {
		return name;
	}
	
	@JsonProperty
	public String getAlgorithm() {
		return algorithm;
	}

	@JsonProperty
	public String[] getCalls() {
		return calls;
	}

	@JsonProperty
	public String getPhases() {
		return phases;
	}

	@JsonProperty
	public String getOutputFormat() {
		return outputFormat;
	}

	@JsonProperty
	public String getConfiguration() {
		return configuration;
	}

	@JsonProperty
	public String getConfigOptions() {
		return configOptions;
	}

	@JsonProperty
	public String getArchetype() {
		return archetype;
	}

	@JsonProperty
	public String getRunType() {
		return runType;
	}

	@JsonProperty
	public String getDebugExamples() {
		return debugExamples;
	}
	
	

	
}
