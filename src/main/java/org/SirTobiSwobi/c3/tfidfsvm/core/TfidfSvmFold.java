package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.TrainingSession;
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;
import org.SirTobiSwobi.c3.tfidfsvm.db.Categorization;
import org.SirTobiSwobi.c3.tfidfsvm.core.Utilities;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategorizationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Evaluation;
import org.SirTobiSwobi.c3.tfidfsvm.db.Assignment;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import java.util.ArrayList;



public class TfidfSvmFold extends Fold {

	public TfidfSvmFold(ReferenceHub refHub, long[] trainingIds, long[] evaluationIds, int foldId, long modelId,
			TrainingSession trainingSession, Trainer trainer, long configId) {
		super(refHub, trainingIds, evaluationIds, foldId, modelId, trainingSession, trainer, configId);
	}
	
	public void run(){
		Model model=refHub.getModelManager().getModelByAddress(modelId);
		Configuration config = refHub.getConfigurationManager().getByAddress(configId);
		boolean includeImplicits = config.isIncludeImplicits(); 
		double assignmentThreshold = config.getAssignmentThreshold(); 
		
		String appendString="";
		
		TfidfFeatureExtractor fe = new TfidfFeatureExtractor(refHub, model);
		
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		//param.kernel_type = svm_parameter.RBF;
		param.kernel_type = svm_parameter.LINEAR;
		param.gamma = 1.0/(double)model.getControlledVocabulary().length;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		
		svm_problem problem = new svm_problem();
	    ArrayList<Double> y=new ArrayList<Double>();
	    ArrayList<svm_node[]> featureVectors= new ArrayList<svm_node[]>();
	    
	    for(int i=0; i<trainingIds.length; i++){
	    	appendString = "Fold "+foldId+" learned from document "+trainingIds[i]+": <br />";
			Assignment[] explicitAssignments=refHub.getTargetFunctionManager().getDocumentAssignments(trainingIds[i]);
			for(int j=0;j<explicitAssignments.length;j++){
				y.add((double)explicitAssignments[j].getCategoryId());
				double[] features = fe.getVector(explicitAssignments[j].getDocumentId());
				features = Utilities.normalizeVector(features);			
				featureVectors.add(LibSvmWrapper.buildSvmNodes(features));
				
				appendString+=" svm_nodes for document "+trainingIds[i];
				for(int k=0; k<featureVectors.get(featureVectors.size()-1).length;k++){
					appendString+="Index: "+featureVectors.get(featureVectors.size()-1)[k].index+" ";
					appendString+="Value: "+featureVectors.get(featureVectors.size()-1)[k].value+"<br />";
				}
				
			}
			if(includeImplicits){
				long[] implicitAssignments = refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(trainingIds[i]);
				if(implicitAssignments!=null){
					for(int j=0; j<implicitAssignments.length; j++){
						//appendString = appendString +" Implicitly belongs to category "+implicitAssignments[j]+", ";
						y.add((double)implicitAssignments[j]);
						double[] features = fe.getVector(trainingIds[i]);
						features = Utilities.normalizeVector(features);			
						featureVectors.add(LibSvmWrapper.buildSvmNodes(features));
						
					}
				}
			}
			model.appendToTrainingLog(appendString);
			model.incrementCompletedSteps();
	    }
	    double[] catAssignmentArray=new double[y.size()];
		svm_node[][] vectors = new svm_node[y.size()][];
		for(int i=0;i<y.size();i++){
			catAssignmentArray[i]=y.get(i);
			vectors[i]=featureVectors.get(i);
		}
		problem.y=catAssignmentArray;
		problem.x=vectors;
		problem.l=y.size();
		appendString=" Fold "+foldId+" problem size = "+problem.l+"<br/>Problem: <br/>";
		for(int i=0;i<problem.x.length;i++){
			for(int j=0;j<problem.x[i].length;j++){
				appendString+=" svm_node["+i+"]["+j+"]=(ind: "+problem.x[i][j].index+", val: "+problem.x[i][j].value+")<br />";
			}
			
		}
		appendString+=" cat assignments: <br />";
		for(int i=0;i<problem.y.length;i++){
			appendString+="y[i]="+problem.y[i]+"<br />";
		}
		
		model.appendToTrainingLog(appendString);
		
		try
		{
			svm_model svmModel= svm.svm_train(problem, param);
			CategorizationManager evalCznMan = new CategorizationManager();
			for(int i=0; i<evaluationIds.length; i++){
				appendString = "Fold "+foldId+" was evaluated using document "+evaluationIds[i]+" which: <br />";
				Assignment[] explicitAssignments=refHub.getTargetFunctionManager().getDocumentAssignments(evaluationIds[i]);
				for(int j=0;j<explicitAssignments.length;j++){
					//appendString = appendString +" Explicitly belongs to category "+explicitAssignments[j].getCategoryId()+", ";
				}
				long[] implicitAssignments = refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(evaluationIds[i]);
				if(implicitAssignments!=null){
					for(int j=0; j<implicitAssignments.length; j++){
						//appendString = appendString +" Implicitly belongs to category "+implicitAssignments[j]+", ";
					}
				}
				
				/*
				 * Here, the actual assignment should take place. This code can be replaced with it.
				 * Automated assignment: Vector of probabilities of one document belonging to a certain category. 
				 * (Document - Category - Probability)
				 */
				double[] features = fe.getVector(evaluationIds[i]);
				features = Utilities.normalizeVector(features);			
				svm_node[] vector = LibSvmWrapper.buildSvmNodes(features);
				double prediction = svm.svm_predict(svmModel, vector);
				appendString+=" Prediction for document "+evaluationIds[i]+": "+(long)prediction+" <br />";
				evalCznMan.addCategorizationWithoutId(evaluationIds[i], (long)prediction, 1.0);
				model.appendToTrainingLog(appendString);
				model.incrementCompletedSteps();
				
				if(includeImplicits){
					appendString="Fold "+foldId+" performing implicit categorizations. <br />";
					Categorization[] czns = evalCznMan.getCategorizationArray();
					for(int j=0; j<czns.length; j++){			
						long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(czns[j].getCategoryId(), SearchDirection.Ascending);
						appendString = appendString +"Performing "+implicitCategorizations.length+" implicit categorizations<br />";
						if(implicitCategorizations!=null){
							for(int k=0; k<implicitCategorizations.length; k++){
								appendString = appendString +("Explicit: "+czns[j].getCategoryId()+" <br />");
								appendString = appendString +("Implcit: "+implicitCategorizations[k]+" <br />");
								if(implicitCategorizations[k]!=czns[j].getCategoryId()){
									appendString = appendString +"Implicit addition: Document: "+evaluationIds[i]+" Category: "+implicitCategorizations[k]+" Probability: "+czns[j].getProbability()+"<br />";
									evalCznMan.addCategorizationWithoutId(evaluationIds[i], implicitCategorizations[k], czns[j].getProbability());
								}
							}
						}
					}
					model.appendToTrainingLog(appendString);
				}
			}
			appendString = "Fold "+foldId+" summarized categorizations: <br />";
			for(int i=0; i<evaluationIds.length; i++){
				Categorization[] czn = evalCznMan.getDocumentCategorizations(evaluationIds[i]);
				for(int j=0; j<czn.length;j++){
					appendString = appendString +"Document "+czn[j].getDocumentId()+" was assigned to category "+czn[j].getCategoryId()+
							" with probability "+czn[j].getProbability()+" <br />";
				}
			}
			model.appendToTrainingLog(appendString);
			String evalDescription = "Fold "+foldId;
			Assignment[] relevantAssignments=null;
			for(int i=0; i<evaluationIds.length; i++){
				relevantAssignments = Utilities.arrayUnionWithoutDuplicates(relevantAssignments, refHub.getTargetFunctionManager().getDocumentAssignments(evaluationIds[i])); 
			}
			Evaluation eval = new Evaluation(	relevantAssignments, 
												evalCznMan.getCategorizationArray(), 
												refHub.getCategoryManager().getCategoryArray(), 
												refHub.getCategoryManager().getRelationshipArray(), 
												refHub.getDocumentManager().getDocumentArray(),  
												evalDescription,
												includeImplicits, 
												assignmentThreshold,
												trainingSession,
												foldId);
			trainer.setSvmModelForFold(svmModel, foldId);
			trainer.selectBestEvaluation();
		}catch(ArrayIndexOutOfBoundsException e){
			
			System.out.println("Fold "+this.foldId+" failed due to an exception in the used library. Automated worst effectiveness.");
			e.printStackTrace();
			for(int i=0; i<evaluationIds.length; i++){
				model.incrementCompletedSteps();
			}
			CategorizationManager evalCznMan = new CategorizationManager();
			svm_model svmModel = new svm_model();
			String evalDescription = "Failed: Fold "+foldId;
			Assignment[] relevantAssignments=null;
			Evaluation eval = new Evaluation(	relevantAssignments, 
					evalCznMan.getCategorizationArray(), 
					refHub.getCategoryManager().getCategoryArray(), 
					refHub.getCategoryManager().getRelationshipArray(), 
					refHub.getDocumentManager().getDocumentArray(),  
					evalDescription,
					includeImplicits, 
					assignmentThreshold,
					trainingSession,
					foldId);
			trainer.setSvmModelForFold(svmModel, foldId);
			trainer.selectBestEvaluation();
			
		}
	}

}
