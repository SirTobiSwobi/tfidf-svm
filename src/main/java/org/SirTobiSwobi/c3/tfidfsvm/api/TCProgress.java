package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCProgress {
	private long id;
	private String address;
	private double progress;
	
	public TCProgress(){
		//Jackson deserialization
	}

	public TCProgress(long id, String modelAddress, double progress) {
		super();
		this.id = id;
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
	
	@JsonProperty
	public long getId(){
		return id;
	}
	
	
}
