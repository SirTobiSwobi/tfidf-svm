package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.Date;

public class Evaluation {
	private ReferenceHub refHub;
	
	private boolean includeImplicits;
	private double assignmentThreshold;
	private double microaveragePrecision, microaverageRecall, microaverageF1, macroaveragePrecision, macroaverageRecall, macroaverageF1;
	private int foldId;
	private EvalCategory[] evalCategories;
	
	public Evaluation(Assignment[] tfAssignments, Categorization[] evalAssignments, Category[] categories, Relationship[] relationships, 
			Document[] documents, String description, boolean includeImplicits, double assignmentThreshold, TrainingSession trainingSession, int foldId){
		this.includeImplicits=includeImplicits;
		this.assignmentThreshold=assignmentThreshold;
		this.foldId = foldId;
		
		DocumentManager docMan = new DocumentManager();
		CategoryManager catMan = new CategoryManager(); 
		TargetFunctionManager evalMan = new TargetFunctionManager();
		ConfigurationManager confMan = new ConfigurationManager();
		ModelManager modMan = new ModelManager();
		CategorizationManager cznMan = new CategorizationManager();
		EvaluationManager evaluMan=null;
		Model activeModel = null;
		this.refHub = new ReferenceHub(catMan, docMan, evalMan, confMan, modMan, cznMan, evaluMan, activeModel); //Every evaluation has it's own reference Hub. Data from the evaluation is copied to it. 
		evalMan.setRefHub(refHub);
		cznMan.setRefHub(refHub);
		
		if(documents!=null){
			for(int i=0; i<documents.length; i++){
				docMan.setDocument(documents[i]);
			}
		}
		if(categories!=null){
			for(int i=0; i<categories.length; i++){
				EvalCategory evalCat = new EvalCategory(categories[i].getId(), categories[i].getLabel(), categories[i].getDescription());
				catMan.setCategory(evalCat);
			}
		}
		if(relationships!=null){
			for(int i=0; i<relationships.length; i++){
				catMan.setRelationship(relationships[i].getId(), relationships[i].getFrom().getId(), relationships[i].getTo().getId(), relationships[i].getType());
			}
		}
		if(tfAssignments!=null){
			for(int i=0; i<tfAssignments.length; i++){
				evalMan.setAssignment(tfAssignments[i].getId(), tfAssignments[i].getDocumentId(), tfAssignments[i].getCategoryId());
			}
		}
		if(evalAssignments!=null){
			for(int i=0; i<evalAssignments.length; i++){
				Categorization czn = new Categorization(evalAssignments[i].getId(), evalAssignments[i].getDocumentId(), 
						evalAssignments[i].getCategoryId(), evalAssignments[i].getProbability());
				cznMan.setCategorization(czn);
			}
		}
		countScores();
		calculateEffectiveness();
		calculateAverages();
		trainingSession.addFoldEvlaution(this);
		
	}
	
	private void countScores(){
		/**
		 * Calculating TP, FP, FN for all categories. 
		 * This is based on whether implicity and assignment thresholds are considered.
		 */
		//EvalCategory[] categories = (EvalCategory[]) refHub.getCategoryManager().getCategoryArray();
		Category[] catArray = refHub.getCategoryManager().getCategoryArray();
		EvalCategory[] categories = new EvalCategory[catArray.length];
		for(int i=0; i<catArray.length; i++){
			categories[i] = (EvalCategory) catArray[i];
		}
		this.evalCategories=categories;
		for(int i=0; i<categories.length; i++){
			EvalCategory cat = categories[i];
			Categorization[] czn = refHub.getCategorizationManager().getCategoryCategorizations(cat.getId());
			long[] assIds=null; //Document ids according to the target function!
			if(includeImplicits){
				assIds=refHub.getTargetFunctionManager().getImplicitDocIdsForCategory(cat.getId());
			}else{
				Assignment[] assignments=refHub.getTargetFunctionManager().getCategoryAssignments(cat.getId());
				if(assignments!=null){
					assIds = new long[assignments.length];
					for(int j=0; j<assignments.length;j++){
						assIds[j]=assignments[j].getDocumentId();
					}
				}
			}
			if(assIds==null||assIds.length==0||czn==null||czn.length==0){
				cat.setTP(0);
				if(!(assIds==null||assIds.length==0)){
					cat.setFN(assIds.length);
				}else{
					cat.setFN(0);
				}
				if(!(czn==null||czn.length==0)){
					cat.setFP(czn.length);
				}else{
					cat.setFP(0);
				}
			}else{
				long TP=0;
				long FP=czn.length;
				long FN=assIds.length;
				for(int j=0; j<czn.length;j++){
					for(int k=0; k<assIds.length;k++){
						if(czn[j].getDocumentId()==assIds[k]&&czn[j].getProbability()>this.assignmentThreshold){
							TP++;
							FP--;
							FN--;
						}
					}
				}
				cat.setTP(TP);
				cat.setFP(FP);
				cat.setFN(FN);
			}
		}	
		
	}
	
	private void calculateEffectiveness(){
		Category[] catArray = refHub.getCategoryManager().getCategoryArray();
		EvalCategory[] categories = new EvalCategory[catArray.length];
		for(int i=0; i<catArray.length; i++){
			categories[i] = (EvalCategory) catArray[i];
		}
		for(int i=0; i<categories.length; i++){
			EvalCategory cat = categories[i];
			double precision=0.0;
			if(cat.getTP()+cat.getFP()>0){			
				precision=(double)cat.getTP()/((double)cat.getTP()+(double)cat.getFP());
			}
			cat.setPrecision(precision);
			double recall=0.0;
			if(cat.getTP()+cat.getFN()>0){			
				recall=(double)cat.getTP()/((double)cat.getTP()+(double)cat.getFN());
			}
			cat.setRecall(recall);
			double f1=0.0;
			if(precision+recall>0.0){
				f1=(2*precision*recall)/(precision+recall);
			}
			cat.setF1(f1);
		}
	}
	
	private void calculateAverages(){
		long sumTP=0;
		long sumFP=0;
		long sumFN=0;
		double sumPrecision=0.0;
		double sumRecall = 0.0;
		double sumF1 = 0.0;
		Category[] catArray = refHub.getCategoryManager().getCategoryArray();
		EvalCategory[] categories = new EvalCategory[catArray.length];
		for(int i=0; i<catArray.length; i++){
			categories[i] = (EvalCategory) catArray[i];
		}
		for(int i=0; i<categories.length; i++){
			EvalCategory cat = categories[i];
			sumTP+=cat.getTP();
			sumFP+=cat.getFP();
			sumFN+=cat.getFN();
			sumPrecision+=cat.getPrecision();
			sumRecall+=cat.getRecall();
			sumF1+=cat.getF1();
		}
		if(sumTP+sumFP==0){
			this.microaveragePrecision=0.0;
		}else{
			this.microaveragePrecision=(double)sumTP/((double)sumTP+(double)sumFP);
		}
		if(sumTP+sumFN==0){
			this.microaverageRecall=0.0;
		}else{
			this.microaverageRecall=(double)sumTP/((double)sumTP+(double)sumFN);
		}
		if(microaveragePrecision+microaverageRecall==0.0){
			this.microaverageF1=0.0;
		}else{
			this.microaverageF1=(2*microaveragePrecision*microaverageRecall)/(microaveragePrecision+microaverageRecall);
		}		
		this.macroaveragePrecision=sumPrecision/(double)categories.length;
		this.macroaverageRecall=sumRecall/(double)categories.length;
		this.macroaverageF1=sumF1/(double)categories.length;
	}
	
	public double getPrecision(long catId){
		double precision = 0.0;
		EvalCategory cat = (EvalCategory) refHub.getCategoryManager().getByAddress(catId);
		if(cat!=null){
			precision = cat.getPrecision();
		}
		return precision;
	}
	
	public double getRecall(long catId){
		double recall = 0.0;
		EvalCategory cat = (EvalCategory) refHub.getCategoryManager().getByAddress(catId);
		if(cat!=null){
			recall = cat.getRecall();
		}
		return recall;
	}
	
	public double getF1(long catId){
		double f1 = 0.0;
		EvalCategory cat = (EvalCategory) refHub.getCategoryManager().getByAddress(catId);
		if(cat!=null){
			f1 = cat.getF1();
		}
		return f1;
	}
	
	public long getTP(long catId){
		long TP=0;
		EvalCategory cat = (EvalCategory) refHub.getCategoryManager().getByAddress(catId);
		if(cat!=null){
			TP = cat.getTP();
		}
		return TP;
	}
	
	public long getFP(long catId){
		long FP=0;
		EvalCategory cat = (EvalCategory) refHub.getCategoryManager().getByAddress(catId);
		if(cat!=null){
			FP = cat.getFP();
		}
		return FP;
	}
	
	public long getFN(long catId){
		long FN=0;
		EvalCategory cat = (EvalCategory) refHub.getCategoryManager().getByAddress(catId);
		if(cat!=null){
			FN = cat.getFN();
		}
		return FN;
	}

	public double getMicroaveragePrecision() {
		return microaveragePrecision;
	}

	public double getMicroaverageRecall() {
		return microaverageRecall;
	}

	public double getMicroaverageF1() {
		return microaverageF1;
	}

	public double getMacroaveragePrecision() {
		return macroaveragePrecision;
	}

	public double getMacroaverageRecall() {
		return macroaverageRecall;
	}

	public double getMacroaverageF1() {
		return macroaverageF1;
	}

	public int getFoldId() {
		return foldId;
	}

	public EvalCategory[] getEvalCategories() {
		return evalCategories;
	}

	public boolean isIncludeImplicits() {
		return includeImplicits;
	}

	public double getAssignmentThreshold() {
		return assignmentThreshold;
	}	
	
	
	
	
	
	
}
