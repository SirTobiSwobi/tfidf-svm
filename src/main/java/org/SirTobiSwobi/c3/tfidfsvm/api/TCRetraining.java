package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCRetraining {
	private boolean needsRetraining;
	
	public TCRetraining(){
		//Jackson deserialization
	}

	public TCRetraining(boolean needsRetraining) {
		super();
		this.needsRetraining = needsRetraining;
	}
	
	@JsonProperty
	public boolean isNeedsRetraining() {
		return needsRetraining;
	}
	
	
}
