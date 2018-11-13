package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCModelList {
	private TCProgress[] models;

	public TCModelList(TCProgress[] models) {
		super();
		this.models = models;
	}
	
	public TCModelList(){
		//Jackson deserialization
	}

	@JsonProperty
	public TCProgress[] getModels() {
		return models;
	}
	
}
