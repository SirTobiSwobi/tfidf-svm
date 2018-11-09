package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCAssignment {
	private long id;
	private long documentId;
	private long categoryId;
	public TCAssignment() {
		//Jackson deserialization
	}
	public TCAssignment(long id, long documentId, long categoryId) {
		super();
		this.id = id;
		this.documentId = documentId;
		this.categoryId = categoryId;
	}
	@JsonProperty
	public long getId() {
		return id;
	}
	@JsonProperty
	public long getDocumentId() {
		return documentId;
	}
	@JsonProperty
	public long getCategoryId() {
		return categoryId;
	}
	
	
	
	
}
