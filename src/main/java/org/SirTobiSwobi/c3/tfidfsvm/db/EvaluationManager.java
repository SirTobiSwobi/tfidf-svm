package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

public class EvaluationManager {
	private AVLTree<TrainingSession> trainingSessions;
	private ReferenceHub refHub;
	private boolean trainingInProgress;
	
	public EvaluationManager() {
		super();
		this.trainingSessions = new AVLTree<TrainingSession>();
		this.refHub = null;
		this.trainingInProgress=false;
	}

	public ReferenceHub getRefHub() {
		return refHub;
	}

	public void setRefHub(ReferenceHub refHub) {
		this.refHub = refHub;
	}
	
	public long getSize(){
		return trainingSessions.getSize();
	}
	
	public TrainingSession getTrainingSessionByAddress(long address){	
		return trainingSessions.getContent(address);
	}
	
	public synchronized void setTrainingSession(TrainingSession model){
		trainingSessions.setContent(model, model.getId());
	}
	
	public synchronized long addTrainingSessionWithoutId(long modelId, String description){
		long id = trainingSessions.getMaxId()+1;
		TrainingSession trainingSession = new TrainingSession(id, modelId, description);
		trainingSessions.setContent(trainingSession, id);
		return id;
	}
	
	public synchronized void deleteTrainingSession(long id){
		trainingSessions.deleteNode(id);
	}
	
	public TrainingSession[] getTrainingSessionArray(){
		ArrayList<TrainingSession> allContent = trainingSessions.toArrayList();
		TrainingSession[] relArray = allContent.toArray(new TrainingSession[0]);
		return relArray;
	}
	
	public boolean containsTrainingSession(long id){
		return trainingSessions.containsId(id);
	}

	public boolean isTrainingInProgress() {
		return trainingInProgress;
	}

	public synchronized void setTrainingInProgress(boolean trainingInProgress) {
		this.trainingInProgress = trainingInProgress;
	}

	
}
