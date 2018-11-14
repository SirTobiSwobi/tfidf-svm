package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmModel;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmNode;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmParameter;

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
	
	public static TCSvmNode[][] buildSupportVectors(svm_node[][] SV){
		if(SV==null){
			return null;
		}
		TCSvmNode[][] TCsvmNode = new TCSvmNode[SV.length][];
		for(int i=0;i<SV.length;i++){
				TCsvmNode[i] = new TCSvmNode[SV[i].length];
			for(int j=0; j<SV[i].length;j++){
				TCsvmNode[i][j]=new TCSvmNode(SV[i][j].index,SV[i][j].value);
			}
		}
		
		return TCsvmNode;
	}
	
	public static svm_node[][] buildSupportVectors(TCSvmNode[][] SV){
		svm_node[][] svmNode = new svm_node[SV.length][];
		for(int i=0;i<SV.length;i++){
			svmNode[i] = new svm_node[SV[i].length];
			for(int j=0; j<SV[i].length; j++){
				svmNode[i][j] = new svm_node();
				svmNode[i][j].index = SV[i][j].getIndex();
				svmNode[i][j].value = SV[i][j].getValue();
			}
		}
		return svmNode;
	}
	
	public static final String svm_type_table[] =
		{
			"c_svc","nu_svc","one_class","epsilon_svr","nu_svr",
		};

	public	static final String kernel_type_table[]=
		{
			"linear","polynomial","rbf","sigmoid","precomputed"
		};
	
	public static int getIdForSvmType(String type){
		int id=0;
		for(int i=0;i<svm_type_table.length;i++){
			if(type.equals(svm_type_table[i])){
				id=i;
			}
		}
		return id;
	}
	
	public static int getIdForKernelType(String type){
		int id=0;
		for(int i=0;i<kernel_type_table.length;i++){
			if(type.equals(kernel_type_table[i])){
				id=i;
			}
		}
		return id;
	}
	
	public static svm_parameter buildSvmParameter(TCSvmParameter tcParam){
		svm_parameter param = new svm_parameter();
		param.svm_type=getIdForSvmType(tcParam.getSvm_type());
		param.kernel_type=getIdForKernelType(tcParam.getKernel_type());
		param.degree=tcParam.getDegree();
		param.gamma=tcParam.getGamma();
		param.coef0=tcParam.getCoef0();
		param.cache_size=tcParam.getCache_size();
		param.eps=tcParam.getEps();
		param.C=tcParam.getC();
		param.nr_weight=tcParam.getNr_weight();
		param.weight_label=tcParam.getWeight_label();
		param.weight=tcParam.getWeight();
		param.nu=tcParam.getNu();
		param.p=tcParam.getP();
		param.shrinking=tcParam.getShrinking();
		param.probability=tcParam.getProbability_estimates();
		return param;
	}
	
	public static TCSvmParameter buildTcSvmParameter(svm_parameter param){
		TCSvmParameter tcParam = new TCSvmParameter(svm_type_table[param.svm_type],
				kernel_type_table[param.kernel_type],
				param.degree,
				param.gamma,
				param.coef0,
				param.cache_size,
				param.eps,
				param.nr_weight,
				param.weight_label,
				param.weight,
				param.C,
				param.nu,
				param.p,
				param.shrinking,
				param.probability
				);	
		return tcParam;
	}
	
	public static svm_model buildSvmModel(TCSvmModel tcModel){
		svm_model model = new svm_model();
		svm_parameter param = buildSvmParameter(tcModel.getParam());
		model.param = param;
		model.nr_class = tcModel.getNr_class();
		model.l = tcModel.getTotal_sv();
		model.SV = buildSupportVectors(tcModel.getSvm_node());
		model.sv_coef = tcModel.getSv_coef();
		model.rho = tcModel.getRho();
		model.probA = tcModel.getProbA();
		model.probB = tcModel.getProbB();
		model.label = tcModel.getLabel();
		model.nSV = tcModel.getNr_sv();
		return model;
	}
	
	public static TCSvmModel buildTcSvmModel(svm_model model){
		TCSvmParameter tcParam = buildTcSvmParameter(model.param);
		TCSvmModel tcModel = new TCSvmModel(tcParam,
				model.nr_class, 
				model.l,
				model.rho,
				model.label,
				model.probA,
				model.probB,
				model.nSV,
				model.sv_coef,
				buildSupportVectors(model.SV)
				);	
		return tcModel;
	}
}
