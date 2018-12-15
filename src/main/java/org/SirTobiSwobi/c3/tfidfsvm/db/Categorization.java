package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Categorization extends Assignment{
	private double probability;
	private String explanation;

	public Categorization(long id, long documentId, long categoryId, double probability, String explanation) {
		super(id, documentId, categoryId);
		this.probability = probability;
		this.explanation = explanation;
	}
	
	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String toString(){
		return ""+id+" "+documentId+" "+categoryId+" "+probability+" "+explanation;
	}
}
