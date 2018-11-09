package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCProgress {
	private String address;
	private double progress;
	
	public TCProgress(){
		//Jackson deserialization
	}

	public TCProgress(String modelAddress, double progress) {
		super();
		this.address = modelAddress;
		this.progress = progress;
	}

	@JsonProperty
	public String getModelAddress() {
		return address;
	}

	@JsonProperty
	public double getProgress() {
		return progress;
	}
	
	
}
