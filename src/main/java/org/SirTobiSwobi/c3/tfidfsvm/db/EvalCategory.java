package org.SirTobiSwobi.c3.tfidfsvm.db;

public class EvalCategory extends Category{

	private long TP, FP, FN;
	private double precision, recall, f1;
	public EvalCategory(long id, String label, String description) {
		super(id, label, description);
		// TODO Auto-generated constructor stub
	}
	public long getTP() {
		return TP;
	}
	public void setTP(long tP) {
		TP = tP;
	}
	public long getFP() {
		return FP;
	}
	public void setFP(long fP) {
		FP = fP;
	}
	public long getFN() {
		return FN;
	}
	public void setFN(long fN) {
		FN = fN;
	}
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getRecall() {
		return recall;
	}
	public void setRecall(double recall) {
		this.recall = recall;
	}
	public double getF1() {
		return f1;
	}
	public void setF1(double f1) {
		this.f1 = f1;
	}
	
	

}
