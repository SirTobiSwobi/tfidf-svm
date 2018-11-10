package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCVocabularyTripel {

	private int id;
	private String term;
	private int documentFrequency;
	private double sumDimensionSquares;
	public TCVocabularyTripel(int id, String term, int documentFrequency, double sumDimensionSquares) {
		super();
		this.id = id;
		this.term = term;
		this.documentFrequency = documentFrequency;
		this.sumDimensionSquares = sumDimensionSquares;
	}
	public TCVocabularyTripel(){
		//Jackson deserialization
	}
	@JsonProperty
	public int getId() {
		return id;
	}
	@JsonProperty
	public String getTerm() {
		return term;
	}
	@JsonProperty
	public int getDocumentFrequency() {
		return documentFrequency;
	}
	@JsonProperty
	public double getSumDimensionSquares() {
		return sumDimensionSquares;
	}
		
}
