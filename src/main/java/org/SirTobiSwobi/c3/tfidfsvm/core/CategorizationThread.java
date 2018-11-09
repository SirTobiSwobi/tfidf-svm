package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;

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
		double[] vector = fe.getVector(document.getId());
		String featureString="Doc: "+document.getId();
		for(int i=0;i<vector.length;i++){
			featureString+="("+i+", "+vector[i]+") ";
		}
		//System.out.println(featureString);	//used for debugging within the log
		featureString="Doc: "+document.getId();
		for(int i=0;i<vector.length;i++){
			featureString+="("+i+", "+Utilities.normalizeVector(vector)[i]+") ";
		}
		//System.out.println(featureString);	//used for debugging within the log
		Category[] categories = refHub.getCategoryManager().getCategoryArray();
		for(int i=0;i<categories.length;i++){
			if(categories[i].getId()%2==document.getId()%2){ //if both, the document and the category ID are odd or even.
				if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), categories[i].getId())){ //only assign if not already there
					refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), categories[i].getId(), 0.8);
				}
				
				if(refHub.getActiveModel().getConfiguration().isIncludeImplicits()){ //if implicits are to be assigned
					long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(categories[i].getId(), SearchDirection.Ascending);
					if(implicitCategorizations!=null){
						for(int k=0; k<implicitCategorizations.length; k++){
							if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), implicitCategorizations[k])){
								refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), implicitCategorizations[k], 0.8);
							}
						}
					}
				}
			}
		}
	}
}
