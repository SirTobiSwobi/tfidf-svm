package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCSvmNode {
	private int index;
	private double value;
	
	public TCSvmNode(){
		//Jackson deserialization
	}

	public TCSvmNode(int index, double value) {
		super();
		this.index = index;
		this.value = value;
	}

	@JsonProperty
	public int getIndex() {
		return index;
	}

	@JsonProperty
	public double getValue() {
		return value;
	}
	
	
}
