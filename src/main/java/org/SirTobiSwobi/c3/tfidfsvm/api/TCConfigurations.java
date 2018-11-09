package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCConfigurations {

	private TCConfiguration[] configurations;

	public TCConfigurations(TCConfiguration[] configurations) {
		super();
		this.configurations = configurations;
	}

	public TCConfigurations() {
		//Jackson deserialization
	}

	@JsonProperty
	public TCConfiguration[] getConfigurations() {
		return configurations;
	}	
	
}
