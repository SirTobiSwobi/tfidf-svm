package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCSvmModel {
	private TCSvmParameter param;
	private int nr_class;
	private int total_sv;
	private double[] rho;
	private int[] label;
	private double[] probA;
	private double[] probB;
	private int[] nr_sv;
	private double[][] sv_coef;
	private TCSvmNode[][] svm_node;
	
	public TCSvmModel(){
		//Jackson deserialization
	}
	

	public TCSvmModel(TCSvmParameter param, int nr_class, int total_sv, double[] rho, int[] label, double[] probA,
			double[] probB, int[] nr_sv, double[][] sv_coef, TCSvmNode[][] svm_node) {
		super();
		this.param = param;
		this.nr_class = nr_class;
		this.total_sv = total_sv;
		this.rho = rho;
		this.label = label;
		this.probA = probA;
		this.probB = probB;
		this.nr_sv = nr_sv;
		this.sv_coef = sv_coef;
		this.svm_node = svm_node;
	}

	@JsonProperty
	public int getNr_class() {
		return nr_class;
	}

	@JsonProperty
	public int getTotal_sv() {
		return total_sv;
	}

	@JsonProperty
	public double[] getRho() {
		return rho;
	}

	@JsonProperty
	public int[] getLabel() {
		return label;
	}

	@JsonProperty
	public double[] getProbA() {
		return probA;
	}

	@JsonProperty
	public double[] getProbB() {
		return probB;
	}

	@JsonProperty
	public int[] getNr_sv() {
		return nr_sv;
	}

	@JsonProperty
	public double[][] getSv_coef() {
		return sv_coef;
	}

	@JsonProperty
	public TCSvmNode[][] getSvm_node() {
		return svm_node;
	}

	@JsonProperty
	public TCSvmParameter getParam() {
		return param;
	}
	
	

}
