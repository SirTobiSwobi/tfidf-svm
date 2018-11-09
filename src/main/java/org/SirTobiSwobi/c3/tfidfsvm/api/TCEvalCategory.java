package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCEvalCategory {
	private long id;
	private String label;
	private String description;
	private long TP, FP, FN;
	private double precision, recall, f1;
	
	public TCEvalCategory(){
		//Jackson deserialization
	}

	public TCEvalCategory(long id, String label, String description, long tP, long fP, long fN, double precision,
			double recall, double f1) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
		TP = tP;
		FP = fP;
		FN = fN;
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public String getLabel() {
		return label;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

	@JsonProperty
	public long getTP() {
		return TP;
	}

	@JsonProperty
	public long getFP() {
		return FP;
	}

	@JsonProperty
	public long getFN() {
		return FN;
	}

	@JsonProperty
	public double getPrecision() {
		return precision;
	}

	@JsonProperty
	public double getRecall() {
		return recall;
	}

	@JsonProperty
	public double getF1() {
		return f1;
	}
	
	
}
