package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCSvmParameter {
	private String svm_type;
	private String kernel_type;
	private int degree;
	private double gamma;
	private double coef0;
	private double cache_size;
	private double eps;
	private int nr_weight;
	private int[] weight_label;
	private double[] weight;
	private double c; //cost
	private double nu;
	private double p; //epsilon
	private int shrinking;
	private int probability_estimates;
	
	public TCSvmParameter(String svm_type, String kernel_type, int degree, double gamma, double coef0,
			double cache_size, double eps, int nr_weight, int[] weight_label, double[] weight, double c, double nu,
			double p, int shrinking, int probability_estimates) {
		super();
		this.svm_type = svm_type;
		this.kernel_type = kernel_type;
		this.degree = degree;
		this.gamma = gamma;
		this.coef0 = coef0;
		this.cache_size = cache_size;
		this.eps = eps;
		this.nr_weight = nr_weight;
		this.weight_label = weight_label;
		this.weight = weight;
		this.c = c;
		this.nu = nu;
		this.p = p;
		this.shrinking = shrinking;
		this.probability_estimates = probability_estimates;
	}

	public TCSvmParameter(){
		//Jackson deserialization
	}


	@JsonProperty
	public String getSvm_type() {
		return svm_type;
	}
	
	@JsonProperty
	public String getKernel_type() {
		return kernel_type;
	}
	
	@JsonProperty
	public int getDegree() {
		return degree;
	}
	
	@JsonProperty
	public double getGamma() {
		return gamma;
	}
	
	@JsonProperty
	public double getCoef0() {
		return coef0;
	}
	
	@JsonProperty
	public double getC() {
		return c;
	}
	
	@JsonProperty
	public double getNu() {
		return nu;
	}
	
	@JsonProperty
	public double getP() {
		return p;
	}
	
	@JsonProperty
	public int getShrinking() {
		return shrinking;
	}
	
	@JsonProperty
	public int getProbability_estimates() {
		return probability_estimates;
	}

	@JsonProperty
	public double getCache_size() {
		return cache_size;
	}

	@JsonProperty
	public double getEps() {
		return eps;
	}

	@JsonProperty
	public int getNr_weight() {
		return nr_weight;
	}

	@JsonProperty
	public int[] getWeight_label() {
		return weight_label;
	}

	@JsonProperty
	public double[] getWeight() {
		return weight;
	}
	
}
