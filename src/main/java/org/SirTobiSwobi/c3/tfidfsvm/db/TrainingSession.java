package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;
import java.util.Date;

public class TrainingSession {
	private ArrayList<Evaluation> foldEvaluations;
	private long modelId;
	private String timestamp;
	private String description;
	private long id;
	
	public TrainingSession(long id, long modelId,  String description) {
		super();
		this.modelId = modelId;
		this.id = id;
		this.foldEvaluations = new ArrayList<Evaluation>();
		Date date = new Date();
		this.timestamp = date.toString();
		this.description = description;
	}
	public long getModelId() {
		return modelId;
	}
	public void setModelId(long modelId) {
		this.modelId = modelId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void addFoldEvlaution(Evaluation evaluation){
		this.foldEvaluations.add(evaluation);
	}
	public ArrayList<Evaluation> getFoldEvaluations(){
		return foldEvaluations;
	}
	public Evaluation getFoldEvaluation(int foldId){
		Evaluation eval=null;
		for(int i=0;i<foldEvaluations.size();i++){
			if(foldId == foldEvaluations.get(i).getFoldId()){
				eval=foldEvaluations.get(i);
			}
		}
		
		return eval;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Evaluation[] getEvaluationArray(){
		Evaluation[] evalArray = foldEvaluations.toArray(new Evaluation[0]);
		return evalArray;
	}
	
	
	
}
