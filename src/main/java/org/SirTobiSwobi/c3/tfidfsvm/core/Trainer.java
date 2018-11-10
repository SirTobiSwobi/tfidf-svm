package org.SirTobiSwobi.c3.tfidfsvm.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.SirTobiSwobi.c3.tfidfsvm.db.AVLTree;
import org.SirTobiSwobi.c3.tfidfsvm.db.Assignment;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.Evaluation;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.SirTobiSwobi.c3.tfidfsvm.db.TermDocMap;
import org.SirTobiSwobi.c3.tfidfsvm.db.TrainingSession;
import org.SirTobiSwobi.c3.tfidfsvm.db.VocabularyTripel;

import libsvm.svm_model;

public class Trainer {
	private ReferenceHub refHub;
	private int openEvaluations, openFeatureExtractions, folds;
	private long trainingSessionId;
	private long modelId;
	private long configId;
	private int featureExtractionThreads;
	private ArrayList<Long> relevantDocIds;
	TrainingSession trainingSession;
	TermDocMap tdm;
	private svm_model[] svmModels;
	
	public Trainer(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
		featureExtractionThreads=2; //default to be overwritten in config yml.
		
	}
	
	public int getFeatureExtractionThreads() {
		return featureExtractionThreads;
	}

	public void setFeatureExtractionThreads(int featureExtractionThreads) {
		this.featureExtractionThreads = featureExtractionThreads;
	}


	private long[] computeTrainingIdsFromEvaluationIds(long[] allIds, long[] evaluationIds){
		long [] output = new long[allIds.length-evaluationIds.length];
		ArrayList<Long> allIdsList = new ArrayList<Long>();
		ArrayList<Long> evaluationIdsList = new ArrayList<Long>();
		for(int i=0;i<allIds.length;i++){
			allIdsList.add(allIds[i]);
		}
		for(int i=0;i<evaluationIds.length;i++){
			evaluationIdsList.add(evaluationIds[i]);
		}
		allIdsList.removeAll(evaluationIdsList);
		for(int i=0;i<output.length;i++){
			output[i]=allIdsList.get(i);
		}
		return output;
	}

	public synchronized void startTraining(long configId, long modelId){
		this.configId = configId;
		refHub.getModelManager().setTrainingInProgress(true);
		Configuration config = refHub.getConfigurationManager().getByAddress(configId);
		folds = config.getFolds();
		this.svmModels = new svm_model[folds];
		this.openEvaluations=folds;
		this.openFeatureExtractions=featureExtractionThreads;//Word counter threads 
		this.modelId=modelId; // There is always only one active training session per microservice. 
		Assignment[] assignments = refHub.getTargetFunctionManager().getAssignmentArray();
		relevantDocIds = new ArrayList<Long>();
		for(int i=0; i<assignments.length; i++){
			long id = assignments[i].getDocumentId();
			if(!relevantDocIds.contains(id)){
				relevantDocIds.add(id);
			}
		}
		
		
		/*
		Document[] allDocs=refHub.getDocumentManager().getDocumentArray();
		int overallSteps = allDocs.length*folds;
		refHub.getModelManager().getModelByAddress(modelId).setSteps(overallSteps);
		*/
		trainingSessionId = refHub.getEvaluationManager().addTrainingSessionWithoutId(modelId, "");
		trainingSession = refHub.getEvaluationManager().getTrainingSessionByAddress(trainingSessionId);
		/*
		long[] allIds = new long[allDocs.length];
		for(int i=0;i<allIds.length;i++){
			allIds[i]=allDocs[i].getId();
		}
		*/
		long[] allIds = new long[relevantDocIds.size()];
		int overallSteps = allIds.length*folds+allIds.length*2; //allIds*folds for n-fold cross validation. AllIds*2 for word counting and tfidf computation
		refHub.getModelManager().getModelByAddress(modelId).setSteps(overallSteps);
		
		for(int i=0;i<allIds.length;i++){
			allIds[i]=relevantDocIds.get(i);
		}
		
		tdm = new TermDocMap();
		
		for(int i=0; i<featureExtractionThreads;i++){
			int start=(allIds.length/featureExtractionThreads)*i;
			int end=((allIds.length/featureExtractionThreads)*(i+1));
			if(i==featureExtractionThreads-1){
				end=allIds.length;
			}
			long[] relevantIds = Arrays.copyOfRange(allIds, start, end);
			(new WordCounter(refHub,relevantIds,tdm,i, modelId, this)).start();
			
		}
		
		
		
	}
	
	public synchronized void awaitFeatureExtraction(){
		openFeatureExtractions--;
		if(openFeatureExtractions==0){
			tdm.computeDocumentFrequency();
			//logWordOccurences();	
			tdm.computeTfidf(refHub.getModelManager().getModelByAddress(modelId));
			refHub.getModelManager().getModelByAddress(modelId).setTrainingSetSize(tdm.getDocIds().size());
			ArrayList<Integer> controlledVocabIds = getControlledVocabularyIds();
			VocabularyTripel[] controlledVocabulary = new VocabularyTripel[controlledVocabIds.size()];
			for(int i=0;i<controlledVocabIds.size();i++){
				int vocabId = controlledVocabIds.get(i);
				String term = tdm.getTerms().get(vocabId);
				int docFreq = tdm.getDocumentFrequency().get(vocabId);
				double sumDimSq = tdm.getSumDimensionSquares().getContent(vocabId);
				controlledVocabulary[i]=new VocabularyTripel(i,term,docFreq, sumDimSq);
				refHub.getModelManager().getModelByAddress(modelId).appendToTrainingLog("Term: "+vocabId+": "+term+" docFreq: "+docFreq+" sumDimSq: "+sumDimSq+"<br>");
			}
			refHub.getModelManager().getModelByAddress(modelId).setControlledVocabulary(controlledVocabulary);
			performNFoldCrossValidation();
		}
	}
	
	private void logWordOccurences(){
		String appendString = "Document Frequencies:<br>";
		for(int i=0; i<tdm.getTerms().size(); i++){
			appendString+=" ("+tdm.getTerms().get(i);
			appendString+=", "+tdm.getDocumentFrequency().get(i)+") ";
		}
		refHub.getModelManager().getModelByAddress(modelId).appendToTrainingLog(appendString);
		Document[] docs = refHub.getDocumentManager().getDocumentArray();
		for(int i=0; i<docs.length;i++){
			long docId=docs[i].getId();
			AVLTree<Integer> values=tdm.getWordOccurences().getContent(docId);
			ArrayList<Long> termIds = values.getUsedIds();
			appendString = "Doc: "+docId+" terms: ";
			for(int l=0; l<termIds.size();l++){
				long termId = termIds.get(l);
				appendString+="("+termId+", "+tdm.getTerms().get((int)termId)+", "+values.getContent(termId)+") ";
			}
			appendString+="<br>";
			//refHub.getModelManager().getModelByAddress(modelId).appendToTrainingLog(appendString);
		}
	}
	
	private ArrayList<Integer> getControlledVocabularyIds(){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		int idsPerCat = refHub.getConfigurationManager().getByAddress(configId).getTopTermsPerCat();
		Category[] cats = refHub.getCategoryManager().getCategoryArray();
		
		for(int i=0;i<cats.length;i++){
			Assignment[] ass = refHub.getTargetFunctionManager().getCategoryAssignments(cats[i].getId());
			double[] maxTfidf = new double[idsPerCat];
			int[] maxIndex = new int[idsPerCat];
			for(int j=0;j<idsPerCat;j++){	
				for(int k=0; k<ass.length;k++){
					long docId = ass[k].getDocumentId();
					double maxDocTfidf=0.0;	//otherwise many documents can have no non-zero values in their feature vectors. So now the top of all docs + top N per Category
					int maxDocIndex=-1;
					AVLTree<Double> values=tdm.getTfidf().getContent(docId);
					ArrayList<Long> termIds = values.getUsedIds();
					for(int l=0; l<termIds.size();l++){
						long termId = termIds.get(l);
						if(values.getContent(termId)>maxTfidf[j]&&!Utilities.isIn(maxIndex, (int)termId)){
							maxTfidf[j]=values.getContent(termId);
							maxIndex[j] = (int) termId;
						}
						if(values.getContent(termId)>maxDocTfidf){
							maxDocTfidf=values.getContent(termId);
							maxDocIndex=(int) termId;
						}
					}
					if(!ids.contains(maxDocIndex)){
						ids.add(maxDocIndex);
					}
				}
				if(!ids.contains(maxIndex[j])){
					ids.add(maxIndex[j]);
				}
				
			}
			
			
		}
		return ids;
	}
	
	private void performNFoldCrossValidation(){
		long[] allIds = new long[relevantDocIds.size()];
		for(int i=0;i<allIds.length;i++){
			allIds[i]=relevantDocIds.get(i);
		}
		for(int i=0; i<folds;i++){
			int start=(allIds.length/folds)*i;
			int end=((allIds.length/folds)*(i+1));
			if(i==folds-1){
				end=allIds.length;
			}
			
			//long[] evaluationIds = Arrays.copyOfRange(allIds, start, end);
			long[] evaluationIds = computeModularEvaluationIds(allIds, folds, i);
			long[] trainingIds = computeTrainingIdsFromEvaluationIds(allIds,evaluationIds);
			
			(new TfidfSvmFold(refHub, trainingIds, evaluationIds, i, modelId, trainingSession, this, configId)).start();
		}	
	}
	
	private long[] computeModularEvaluationIds(long[] allIds, int folds, int fold){
		ArrayList<Long> relevantIds=new ArrayList<Long>();
		for(int i=0;i<allIds.length;i++){
			if(allIds[i]%folds==fold){
				relevantIds.add(allIds[i]);
			}
		}
		long[] evaluationIds = new long[relevantIds.size()];
		for(int i=0;i<evaluationIds.length; i++){
			evaluationIds[i]=relevantIds.get(i);
		}
		return evaluationIds;
	}
	
	public synchronized void selectBestEvaluation(){
		/*
		 * Implementing a semaphore so that the selection only takes place when all folds have been computed.
		 */
		openEvaluations--;
		if(openEvaluations==0){
			TrainingSession trainingSession = refHub.getEvaluationManager().getTrainingSessionByAddress(trainingSessionId);
			String appendString="";
			Model model = refHub.getModelManager().getModelByAddress(modelId);
			SelectionPolicy selectionPolicy = refHub.getConfigurationManager().getByAddress(configId).getSelectionPolicy();
			Evaluation[] evaluations = trainingSession.getEvaluationArray();	
			model.appendToTrainingLog("There are "+evaluations.length+" evaluations.");
			double maxValue=0.0;
			int maxId=0;
			for(int i=0; i<evaluations.length; i++){
				Evaluation eval = evaluations[i];
				appendString = " Evaluation: "+eval.getFoldId();
				appendString = appendString+" Microaverage Precision: "+eval.getMicroaveragePrecision()+" Microaverage Recall: "+eval.getMicroaverageRecall()+" Microaverage F1 "+eval.getMicroaverageF1();
				appendString = appendString+" Macroaverage Precision: "+eval.getMacroaveragePrecision()+" Macroaverage Recall: "+eval.getMacroaverageRecall()+" Macroaverage F1 "+eval.getMacroaverageF1(); 
				Category[] categories = refHub.getCategoryManager().getCategoryArray();
				for(int j=0; j<categories.length; j++){
					appendString = appendString + " Category: "+categories[j].getId()+
							" TP: "+eval.getTP(categories[j].getId())+" FP: "+eval.getFP(categories[j].getId())+
							" FN: "+eval.getFN(categories[j].getId())+
							" precision: "+eval.getPrecision(categories[j].getId())+" recall "+eval.getRecall(categories[j].getId())+
							" F1: "+eval.getF1(categories[j].getId());
				}
				model.appendToTrainingLog(appendString);
				if(selectionPolicy==SelectionPolicy.MicroaverageF1){
					if(eval.getMicroaverageF1()>maxValue){
						maxValue=eval.getMicroaverageF1();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MicroaveragePrecision){
					if(eval.getMicroaveragePrecision()>maxValue){
						maxValue=eval.getMicroaveragePrecision();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MicroaverageRecall){
					if(eval.getMicroaverageRecall()>maxValue){
						maxValue=eval.getMicroaverageRecall();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MacroaverageF1){
					if(eval.getMacroaverageF1()>maxValue){
						maxValue=eval.getMacroaverageF1();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MacroaveragePrecision){
					if(eval.getMacroaveragePrecision()>maxValue){
						maxValue=eval.getMacroaveragePrecision();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MacroaverageRecall){
					if(eval.getMacroaverageRecall()>maxValue){
						maxValue=eval.getMacroaverageRecall();
						maxId=i;
					}
				}
			}
			model.appendToTrainingLog(" Best evaluation following the "+selectionPolicy.toString()+" Policy is: "+evaluations[maxId].getFoldId());
			refHub.getModelManager().setTrainingInProgress(false);
		}
	}
	
	public void setSvmModelForFold(svm_model model, int fold){
		this.svmModels[fold]=model;
	}
}
