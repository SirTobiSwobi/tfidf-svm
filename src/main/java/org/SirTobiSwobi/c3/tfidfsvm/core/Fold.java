package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.Assignment;
import org.SirTobiSwobi.c3.tfidfsvm.db.Categorization;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategorizationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.Evaluation;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.Relationship;
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.SirTobiSwobi.c3.tfidfsvm.db.TrainingSession;

import ch.qos.logback.access.servlet.Util;

public class Fold extends Thread {
	protected ReferenceHub refHub;
	protected long[] trainingIds, evaluationIds;
	protected int foldId;
	protected long modelId, configId;
	protected TrainingSession trainingSession;
	protected Trainer trainer;
	
	public Fold(ReferenceHub refHub, long[] trainingIds, long[] evaluationIds, int foldId, long modelId, TrainingSession trainingSession, Trainer trainer, long configId) {
		super();
		this.refHub = refHub;
		this.trainingIds = trainingIds;
		this.evaluationIds = evaluationIds;
		this.foldId = foldId;
		this.modelId = modelId;
		this.trainingSession=trainingSession;
		this.trainer = trainer;
		this.configId = configId;
	}

	public void run(){
		Model model=refHub.getModelManager().getModelByAddress(modelId);
		Configuration config = refHub.getConfigurationManager().getByAddress(configId);
		boolean includeImplicits = config.isIncludeImplicits(); //ToDo: refactor this into the configuration!
		double assignmentThreshold = config.getAssignmentThreshold(); //ToDo: refactor this into the configuration!
		
		
		for(int i=0; i<trainingIds.length; i++){
			
			String appendString = "Fold "+foldId+" learned from document "+trainingIds[i]+": ";/*
			Assignment[] explicitAssignments=refHub.getTargetFunctionManager().getDocumentAssignments(trainingIds[i]);
			for(int j=0;j<explicitAssignments.length;j++){
				appendString = appendString +" Explicitly belongs to category "+explicitAssignments[j].getCategoryId()+", ";
			}
			long[] implicitAssignments = refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(trainingIds[i]);
			for(int j=0; j<implicitAssignments.length; j++){
				appendString = appendString +" Implicitly belongs to category "+implicitAssignments[j]+", ";
			}*/
			//model.appendToTrainingLog(appendString);
			model.incrementCompletedSteps();
			/*
			try{
				Thread.sleep(5000);
			}catch(InterruptedException e){
				//This is just use to simulate the actual training progress.
			}
			*/
			
		}
		CategorizationManager evalCznMan = new CategorizationManager();
		for(int i=0; i<evaluationIds.length; i++){
			String appendString = "Fold "+foldId+" was evaluated using document "+evaluationIds[i]+" which: ";
			Assignment[] explicitAssignments=refHub.getTargetFunctionManager().getDocumentAssignments(evaluationIds[i]);
			for(int j=0;j<explicitAssignments.length;j++){
				appendString = appendString +" Explicitly belongs to category "+explicitAssignments[j].getCategoryId()+", ";
			}
			long[] implicitAssignments = refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(evaluationIds[i]);
			if(implicitAssignments!=null){
				for(int j=0; j<implicitAssignments.length; j++){
					appendString = appendString +" Implicitly belongs to category "+implicitAssignments[j]+", ";
				}
			}
			
			/*
			 * Here, the actual assignment should take place. This code can be replaced with it.
			 * Automated assignment: Vector of probabilities of one document belonging to a certain category. 
			 * (Document - Category - Probability)
			 */
			
			/*
			*Random Assignment. This just randomly assigns documents to categories for debug and testing purposes.
			Category[] categories = refHub.getCategoryManager().getCategoryArray();
			int randId = (int)(Math.random()*(double)categories.length); //something more sophisticated should take place here ;-)
			double randProb = Math.random();
			evalCznMan.addCategorizationWithoutId(evaluationIds[i], categories[randId].getId(), randProb);
			appendString+="Randomly assigned document "+evaluationIds[i]+" to category "+categories[randId].getId()+
					" with probability "+randProb+". ";
			*/
			/*
			 * Fixed assignment to develop and debug evaluation.
			 */
			if(evaluationIds[i]%2==0){
				evalCznMan.addCategorizationWithoutId(evaluationIds[i], 2, 0.8, "");
				appendString+="Assigned document "+evaluationIds[i]+" to category 2 with probability 0.8. ";
			}else{
				evalCznMan.addCategorizationWithoutId(evaluationIds[i], 4, 0.7, "");
				appendString+="Assigned document "+evaluationIds[i]+" to category 4 with probability 0.7. ";
			}
			//model.appendToTrainingLog(appendString);
			model.incrementCompletedSteps();
			
			if(includeImplicits){
				appendString="Fold "+foldId+" performing implicit categorizations. ";
				Categorization[] czns = evalCznMan.getCategorizationArray();
				for(int j=0; j<czns.length; j++){			
					long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(czns[j].getCategoryId(), SearchDirection.Ascending);
					appendString = appendString +"Performing "+implicitCategorizations.length+" implicit categorizations";
					if(implicitCategorizations!=null){
						for(int k=0; k<implicitCategorizations.length; k++){
							appendString = appendString +("Explicit: "+czns[j].getCategoryId()+" ");
							appendString = appendString +("Implcit: "+implicitCategorizations[k]+" ");
							if(implicitCategorizations[k]!=czns[j].getCategoryId()){
								appendString = appendString +("Implicit addition: Document: "+evaluationIds[i]+" Category: "+implicitCategorizations[k]+" Probability: "+czns[j].getProbability());
								evalCznMan.addCategorizationWithoutId(evaluationIds[i], implicitCategorizations[k], czns[j].getProbability(), "");
							}
						}
					}
				}
				//model.appendToTrainingLog(appendString);
			}
			/*
			try{
				Thread.sleep(5000);
			}catch(InterruptedException e){
				//This is just use to simulate the actual training progress.
			}
			*/
			
		}
		
		String appendString = "Fold "+foldId+" summarized categorizations: ";
		for(int i=0; i<evaluationIds.length; i++){
			Categorization[] czn = evalCznMan.getDocumentCategorizations(evaluationIds[i]);
			for(int j=0; j<czn.length;j++){
				appendString = appendString +"Document "+czn[j].getDocumentId()+" was assigned to category "+czn[j].getCategoryId()+
						" with probability "+czn[j].getProbability();
			}
		}
		/*
		for(int i=0; i<evaluationIds.length; i++){
			Categorization[] czn=evalCznMan.getDocumentCategorizations(evaluationIds[i]);
			for(int j=0; j<czn.length;j++){
				appendString = appendString +"Document "+czn[j].getDocumentId()+" was assigned to category "+czn[j].getCategoryId()+
						" with probability "+czn[j].getProbability();
			}
			
		}
		*/
		//model.appendToTrainingLog(appendString);
		String evalDescription = "Fold "+foldId;
		
		
		/*
		 * Not all assignments can be taken into account per fold. Otherwise false negatives sky-rocket. If this fold doesn't know a document,
		 * it cannot be evaluated as if it knows it. 
		 */
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
		trainer.selectBestEvaluation();
		
		
	}

}
