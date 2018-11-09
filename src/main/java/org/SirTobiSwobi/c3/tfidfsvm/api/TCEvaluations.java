package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCEvaluations {
	private TCTrainingSession[] trainingSessions;
	
	public TCEvaluations(){
		//Jackson deserialization;
	}

	public TCEvaluations(TCTrainingSession[] trainingSessions) {
		super();
		this.trainingSessions = trainingSessions;
	}

	@JsonProperty
	public TCTrainingSession[] getTrainingSessions() {
		return trainingSessions;
	}

}
