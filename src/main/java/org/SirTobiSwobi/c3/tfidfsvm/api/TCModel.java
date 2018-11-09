package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCModel {
	private long id;
	private long configurationId;
	private double progress;
	private String trainingLog;
	private TCConfiguration configuration;
	private TCVocabularyTripel[] controlledVocabulary;
	private int trainingSetSize;
	
	public TCModel(){
		//Jackson deserialization
	}

	public TCModel(long id, long configurationId, double progress, String trainingLog, TCConfiguration configuration, TCVocabularyTripel[] controlledVocabulary, int trainingSetSize) {
		super();
		this.id = id;
		this.configurationId = configurationId;
		this.progress = progress;
		this.trainingLog = trainingLog;
		this.configuration = configuration;
		this.controlledVocabulary=controlledVocabulary;
		this.trainingSetSize=trainingSetSize;
	}
	
	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public long getConfigurationId() {
		return configurationId;
	}

	@JsonProperty
	public double getProgress() {
		return progress;
	}

	@JsonProperty
	public String getTrainingLog() {
		return trainingLog;
	}

	@JsonProperty
	public TCConfiguration getConfiguration() {
		return configuration;
	}

	@JsonProperty
	public TCVocabularyTripel[] getControlledVocabulary() {
		return controlledVocabulary;
	}

	@JsonProperty
	public int getTrainingSetSize() {
		return trainingSetSize;
	}
	
	
	
}
