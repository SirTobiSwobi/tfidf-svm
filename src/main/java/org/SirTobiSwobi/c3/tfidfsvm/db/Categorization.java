package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Categorization extends Assignment{
	private double probability;

	public Categorization(long id, long documentId, long categoryId, double probability) {
		super(id, documentId, categoryId);
		this.probability = probability;
	}
	
	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String toString(){
		return ""+id+" "+documentId+" "+categoryId+" "+probability;
	}
}
