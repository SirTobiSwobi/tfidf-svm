package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCCategorization {
	private long id;
	private long documentId;
	private long categoryId;
	private double probability;
	private String explanation;
	
	public TCCategorization(){
		//Jackson deserialization;
	}

	public TCCategorization(long id, long documentId, long categoryId, double probability, String explanation) {
		super();
		this.id = id;
		this.documentId = documentId;
		this.categoryId = categoryId;
		this.probability = probability;
		this.explanation = explanation;
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
	@JsonProperty
	public double getProbability() {
		return probability;
	}
	@JsonProperty
	public String getExplanation() {
		return explanation;
	}	
}
