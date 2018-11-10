package org.SirTobiSwobi.c3.tfidfsvm.core;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class LibSvmWrapper {
	
	public static svm_node newSvmNode(int index, double value){
		svm_node node = new svm_node();
		node.index=index;
		node.value=value;
		return node;
	}
	
	public static svm_node[] buildSvmNodes(double[] values){
		//svm_node[] nodes = new svm_node[values.length+1];
		//svm_node[] nodes = new svm_node[values.length];
		int nonZeroValues=1;	//starting with 1 for the -1 entry at the end.
		for(int i=0;i<values.length;i++){
			if(values[i]>0.0){
				nonZeroValues++;
			}
		}
		svm_node[] nodes = new svm_node[nonZeroValues];
		int nonZeroIndex=0;
		for(int i=0;i<values.length;i++){
			if(values[i]>0.0){
				nodes[nonZeroIndex]=newSvmNode(i,values[i]);
				nonZeroIndex++;
			}		
		}
		nodes[nonZeroValues-1]=newSvmNode(-1,0.0);
		return nodes;
	}
	
	public static svm_model buildModelForCategory(svm_node[][] nodes){
		// Build Parameters
	    svm_parameter param = new svm_parameter();
	    param.svm_type    = svm_parameter.ONE_CLASS;
	    param.kernel_type = svm_parameter.RBF;
	   /* 
	    param.gamma       = 1;
	    param.nu          = 0.5;
	    param.cache_size  = 1000;
		*/
	    // Build Problem
	    svm_problem problem = new svm_problem();
	    problem.x = nodes;
	    problem.l = nodes.length;
	    problem.y = prepareY(nodes.length);

	    // Build Model
	    return svm.svm_train(problem, param);
	}
	
	private static double[] prepareY(int size) {
	    double[] y = new double[size];

	    for (int i=0; i < size; i++)
	        y[i] = 1;

	    return y;
	}
	
	public static double predictCategoryLikelyness(svm_model categoryModel, svm_node[] vector){
	    double[] scores = new double[2];
	    double result = svm.svm_predict_values(categoryModel, vector, scores);

	    return scores[0];
	}
	
	public static double predict(svm_model categoryModel, svm_node[] vector){
		return svm.svm_predict(categoryModel, vector);
	}
}
