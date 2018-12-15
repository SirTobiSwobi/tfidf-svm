package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.Categorization;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;

import libsvm.svm;
import libsvm.svm_node;

public class CategorizationThread extends Thread {
	private ReferenceHub refHub;
	private Document document;

	public CategorizationThread(ReferenceHub refHub, long docId) {
		super();
		this.refHub = refHub;
		this.document = refHub.getDocumentManager().getByAddress(docId);
	}
	
	public void run(){
		/**
		 * One could simply inherit from this class and implement the performClassification method differently.
		 */
		performCategorization();
	}
	
	/**
	 * This is a dummy example classification that assigns all documents with even ids to categories with even ids. 
	 */
	private void performCategorization(){
		TfidfFeatureExtractor fe=new TfidfFeatureExtractor(refHub);
		double[] features = fe.getDimensionNormalizedVector(document.getId());
		String explanation="This document is considered to belong to category ";
		
		String featureString="Doc: "+document.getId();
		for(int i=0;i<features.length;i++){
			featureString+="("+i+", "+features[i]+") ";
		}
		//System.out.println(featureString);	//used for debugging within the log
		featureString="Doc: "+document.getId();
		for(int i=0;i<features.length;i++){
			featureString+="("+i+", "+Utilities.normalizeVector(features)[i]+") ";
		}
		//System.out.println(featureString);	//used for debugging within the log
		svm_node[] vector = LibSvmWrapper.buildSvmNodes(features);
		double prediction = svm.svm_predict(refHub.getActiveModel().getSvmModel(), vector);
		//System.out.println("Prediction: "+prediction);
		explanation += "\""+refHub.getCategoryManager().getByAddress((long)prediction).getLabel()+"\", "; 
		if(vector.length==1){
			explanation += " because none of the trained indicators are present in the document. Lack of indicator terms have been associated with this category ";
		}else{
			explanation+="because it contains multiple occurences of the terms ";
			for(int i=0;i<vector.length-1; i++){
				explanation+=refHub.getActiveModel().getControlledVocabulary()[vector[i].index].getTerm();
				if(i<vector.length-2){
					explanation+=", ";
				}else if(i==vector.length-1){
					explanation+=", and ";
				}else{
					explanation+=". ";
				}
			}
			explanation += " These terms have been identified as indicators for this category ";
		}
		
		explanation += "during training with a dataset containing "+refHub.getActiveModel().getTrainingSetSize()+" documents.";
		refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), (long)prediction, 1.0, explanation);
		if(refHub.getActiveModel().getConfiguration().isIncludeImplicits()){
			long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds((long)prediction, SearchDirection.Ascending);
			if(implicitCategorizations!=null){
				for(int l=0; l<implicitCategorizations.length; l++){
					if(implicitCategorizations[l]!=(long)prediction){
						if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), implicitCategorizations[l])){
							explanation = "The document is considered to belong to category \""+refHub.getCategoryManager().getByAddress(implicitCategorizations[l]).getLabel()+"\", ";
							explanation +=" because the category relationships imply that it belongs to this category.";
							refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), implicitCategorizations[l], 1.0, explanation);
						}else{
							Categorization czn = refHub.getCategorizationManager().getCategorizationWithDocAndCat(document.getId(), implicitCategorizations[l]);
							czn.setExplanation(czn.getExplanation()+" Additionally, the category relationships imply that it belongs to this category. ");
						}
					}				
				}
			}
		}
		
		
	}
}
