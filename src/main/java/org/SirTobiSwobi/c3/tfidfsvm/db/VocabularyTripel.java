package org.SirTobiSwobi.c3.tfidfsvm.db;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCVocabularyTripel;

public class VocabularyTripel {

	private int id;
	private String term;
	private int documentFrequency;
	private double sumDimensionSquares;
	
	public VocabularyTripel(int id, String term, int documentFrequency, double sumDimensionSquares) {
		super();
		this.id = id;
		this.term = term;
		this.documentFrequency = documentFrequency;
		this.sumDimensionSquares = sumDimensionSquares;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getDocumentFrequency() {
		return documentFrequency;
	}

	public void setDocumentFrequency(int documentFrequency) {
		this.documentFrequency = documentFrequency;
	}
	
	public static VocabularyTripel[] buildControlledVocabulary(TCVocabularyTripel[] tcControlledVocabulary){
		VocabularyTripel[] controlledVocabulary = new VocabularyTripel[tcControlledVocabulary.length];
		for(int i=0;i<controlledVocabulary.length;i++){
			controlledVocabulary[i]=new VocabularyTripel(tcControlledVocabulary[i].getId(), tcControlledVocabulary[i].getTerm(), 
					tcControlledVocabulary[i].getDocumentFrequency(), tcControlledVocabulary[i].getSumDimensionSquares());
		}
		return controlledVocabulary;
	}

	public double getSumDimensionSquares() {
		return sumDimensionSquares;
	}

	public void setSumDimensionSquares(double sumDimensionSquares) {
		this.sumDimensionSquares = sumDimensionSquares;
	}
	
	
	
}
