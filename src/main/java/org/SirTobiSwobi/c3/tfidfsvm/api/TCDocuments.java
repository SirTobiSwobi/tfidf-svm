package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCDocuments {
	
	private TCDocument[] documents;

	public TCDocuments(){
		//Jackson deserialization
	}
	
	public TCDocuments(TCDocument[] documents) {
		super();
		this.documents = documents;
	}

	@JsonProperty
	public TCDocument[] getDocuments() {
		return documents;
	}
	
	
}
