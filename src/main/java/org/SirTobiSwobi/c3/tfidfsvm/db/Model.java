package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Model {
	private long id;
	private long configurationId; //Storing what this model is based on.
	private double progress; //stores the training progress.
	private int steps;
	private int completed;
	private String trainingLog; //for API development purposed. Optional for actual model. Stores progress of the training.
	private Configuration configuration;
	private VocabularyTripel[] controlledVocabulary;
	private int trainingSetSize;
	
	public Model(long id, Configuration configuration, VocabularyTripel[] controlledVocabulary, int trainingSetSize) {
		super();
		this.id = id;
		this.configuration=configuration;
		this.progress = .0;
		this.trainingLog="";
		this.steps=0;
		this.completed=0;
		this.controlledVocabulary=controlledVocabulary;
		this.trainingSetSize=trainingSetSize;
		
	}
	
	public Model(long id, Configuration configuration, String trainingLog, VocabularyTripel[] controlledVocabulary, int trainingSetSize) {
		super();
		this.id = id;
		this.configuration=configuration;
		this.progress = 1.0; //only used for active Model when put there. Training progress is always completed.
		this.trainingLog=trainingLog;
		this.steps=10; //only used for active Model when put there. Training progress is always completed.
		this.completed=10; //only used for active Model when put there. Training progress is always completed.
		this.controlledVocabulary=controlledVocabulary;
		this.trainingSetSize=trainingSetSize;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(long configurationId) {
		this.configurationId = configurationId;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	public String toString(){
		return ""+id+" "+configurationId+" "+progress;
	}

	public String getTrainingLog() {
		return trainingLog;
	}

	public void setTrainingLog(String trainingLog) {
		this.trainingLog = trainingLog;
	}
	
	public synchronized void appendToTrainingLog(String appendString){
		this.trainingLog = this.trainingLog + appendString;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public synchronized void incrementCompletedSteps(){
		this.completed++;
		this.progress=(double)completed/(double)steps;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public VocabularyTripel[] getControlledVocabulary() {
		return controlledVocabulary;
	}

	public void setControlledVocabulary(VocabularyTripel[] controlledVocabulary) {
		this.controlledVocabulary = controlledVocabulary;
	}
	
	public int getTermId(String term){
		int id=-1;
		for(int i=0;i<controlledVocabulary.length;i++){
			if(term.equals(controlledVocabulary[i].getTerm())){
				id=i;
			}
		}
		return id;
	}

	public int getTrainingSetSize() {
		return trainingSetSize;
	}

	public void setTrainingSetSize(int trainingSetSize) {
		this.trainingSetSize = trainingSetSize;
	}
	
	
	
}
