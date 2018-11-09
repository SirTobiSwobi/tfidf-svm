package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

public class ModelManager {
	private AVLTree<Model> models;
	private ReferenceHub refHub;
	private boolean trainingInProgress;
	
	public ModelManager() {
		super();
		this.models = new AVLTree<Model>();
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
		return models.getSize();
	}
	
	public Model getModelByAddress(long address){	
		return models.getContent(address);
	}
	
	public synchronized void setModel(Model model){
		models.setContent(model, model.getId());
	}
	
	public synchronized long addModelWithoutId(long configurationId){
		long id = models.getMaxId()+1;
		Model model = new Model(id, refHub.getConfigurationManager().getByAddress(configurationId), null,0);
		models.setContent(model, id);
		return id;
	}
	
	public synchronized void deleteModel(long id){
		models.deleteNode(id);
	}
	
	public Model[] getModelArray(){
		ArrayList<Model> allContent = models.toArrayList();
		Model[] relArray = allContent.toArray(new Model[0]);
		return relArray;
	}
	
	public boolean containsModel(long id){
		return models.containsId(id);
	}
	
	public String getModelHash(){
		byte[] contentHash = models.getContentHash();
		String result="";
		for(int i=0; i<contentHash.length;i++){
			result = result + Integer.toHexString(contentHash[i] & 255);
		}
		return result;
	}

	public boolean isTrainingInProgress() {
		return trainingInProgress;
	}

	public synchronized void setTrainingInProgress(boolean trainingInProgress) {
		this.trainingInProgress = trainingInProgress;
	}
	
	public long getMaxId(){
		return models.getMaxId();
	}
	
	
}
