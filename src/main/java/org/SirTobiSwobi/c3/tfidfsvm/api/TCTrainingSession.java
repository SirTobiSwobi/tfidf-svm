package org.SirTobiSwobi.c3.tfidfsvm.api;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TCTrainingSession {
	private long modelId;
	private String timestamp;
	private String description;
	private long id;
	private TCEvaluation[] foldEvaluations;
	
	public TCTrainingSession(){
		//Jackson deserialization
	}

	public TCTrainingSession(long modelId, String timestamp, String description, long id,
			TCEvaluation[] foldEvaluations) {
		super();
		this.modelId = modelId;
		this.timestamp = timestamp;
		this.description = description;
		this.id = id;
		this.foldEvaluations = foldEvaluations;
	}

	@JsonProperty
	public long getModelId() {
		return modelId;
	}

	@JsonProperty
	public String getTimestamp() {
		return timestamp;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public TCEvaluation[] getFoldEvaluations() {
		return foldEvaluations;
	}
	
	

}
