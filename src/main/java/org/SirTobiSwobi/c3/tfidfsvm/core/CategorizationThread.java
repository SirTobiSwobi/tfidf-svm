package org.SirTobiSwobi.c3.tfidfsvm.core;

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
		refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), (long)prediction, 1.0);
		
		
	}
}
