package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCEvaluation {
	private boolean includeImplicits;
	private double assignmentThreshold;
	private double microaveragePrecision, microaverageRecall, microaverageF1, macroaveragePrecision, macroaverageRecall, macroaverageF1;
	private int foldId;
	private TCEvalCategory[] categories;
	
	public TCEvaluation(){
		//Jackson deserialization
	}

	public TCEvaluation(boolean includeImplicits, double assignmentThreshold, double microaveragePrecision,
			double microaverageRecall, double microaverageF1, double macroaveragePrecision, double macroaverageRecall,
			double macroaverageF1, int foldId, TCEvalCategory[] categories) {
		super();
		this.includeImplicits = includeImplicits;
		this.assignmentThreshold = assignmentThreshold;
		this.microaveragePrecision = microaveragePrecision;
		this.microaverageRecall = microaverageRecall;
		this.microaverageF1 = microaverageF1;
		this.macroaveragePrecision = macroaveragePrecision;
		this.macroaverageRecall = macroaverageRecall;
		this.macroaverageF1 = macroaverageF1;
		this.foldId = foldId;
		this.categories = categories;
	}

	@JsonProperty
	public boolean isIncludeImplicits() {
		return includeImplicits;
	}

	@JsonProperty
	public double getAssignmentThreshold() {
		return assignmentThreshold;
	}

	@JsonProperty
	public double getMicroaveragePrecision() {
		return microaveragePrecision;
	}

	@JsonProperty
	public double getMicroaverageRecall() {
		return microaverageRecall;
	}

	@JsonProperty
	public double getMicroaverageF1() {
		return microaverageF1;
	}

	@JsonProperty
	public double getMacroaveragePrecision() {
		return macroaveragePrecision;
	}

	@JsonProperty
	public double getMacroaverageRecall() {
		return macroaverageRecall;
	}

	@JsonProperty
	public double getMacroaverageF1() {
		return macroaverageF1;
	}

	@JsonProperty
	public int getFoldId() {
		return foldId;
	}

	@JsonProperty
	public TCEvalCategory[] getCategories() {
		return categories;
	}
	
	
	
}
