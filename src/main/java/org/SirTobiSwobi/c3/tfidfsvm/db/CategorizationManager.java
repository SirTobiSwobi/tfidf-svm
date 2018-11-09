package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

public class CategorizationManager {
	protected AVLTree<Categorization> assignments;
	protected AVLTree<ArrayList<Categorization>> documentIndex;
	protected AVLTree<ArrayList<Categorization>> categoryIndex;
	protected ReferenceHub refHub;

	public CategorizationManager() {
		super();
		this.assignments = new AVLTree<Categorization>();
		this.documentIndex=new AVLTree<ArrayList<Categorization>>();
		this.categoryIndex=new AVLTree<ArrayList<Categorization>>();
	}
	
	public ReferenceHub getRefHub() {
		return refHub;
	}

	public void setRefHub(ReferenceHub refHub) {
		this.refHub = refHub;
	}
	
	public boolean containsCategorization(long id){
		return assignments.containsId(id);
	}
	
	public boolean containsCategorizationOf(long docId, long catId){
		boolean returnValue=false;
		Categorization[] allCategorizations = this.getCategorizationArray();
		for(int i=0; i<allCategorizations.length;i++){
			if(allCategorizations[i].getDocumentId()==docId&&allCategorizations[i].getCategoryId()==catId){
				returnValue=true;
			}
		}
		return returnValue;
	}
	
	private Categorization getCategorizationWithDocAndCat(long docId, long catId){
		Categorization czn=null;
		Categorization[] allCategorizations = this.getCategorizationArray();
		for(int i=0; i<allCategorizations.length;i++){
			if(allCategorizations[i].getDocumentId()==docId&&allCategorizations[i].getCategoryId()==catId){
				czn=allCategorizations[i];
			}
		}
		return czn;
	}
	
	public synchronized void setCategorization(Categorization czn){
		Categorization possibleExisting = getCategorizationWithDocAndCat(czn.getDocumentId(), czn.getCategoryId());
		if(possibleExisting!=null){
			if(czn.getProbability()>possibleExisting.getProbability()){
				possibleExisting.setProbability(czn.getProbability());
				ArrayList<Categorization> docIndex=documentIndex.getContent(czn.getDocumentId());
				for(int i=0; i<docIndex.size(); i++){
					if(docIndex.get(i).getId()==possibleExisting.getId()){
						docIndex.get(i).setProbability(czn.getProbability());
					}
				}
				ArrayList<Categorization> catIndex=categoryIndex.getContent(czn.getDocumentId());
				if(catIndex!=null){
					for(int i=0; i<catIndex.size(); i++){
						if(catIndex.get(i).getId()==possibleExisting.getId()){
							catIndex.get(i).setProbability(czn.getProbability());
						}
					}
				}
				
			}
		}else{
			this.assignments.setContent(czn, czn.getId());
			if(documentIndex.getSize()==0){
				ArrayList<Categorization> newList = new ArrayList<Categorization>();
				newList.add(czn);
				documentIndex.setContent(newList, czn.getDocumentId());
			}else if(documentIndex.containsId(czn.getDocumentId())){
				documentIndex.getContent(czn.getDocumentId()).add(czn);
			}else{
				ArrayList<Categorization> newList = new ArrayList<Categorization>();
				newList.add(czn);
				documentIndex.setContent(newList, czn.getDocumentId());
			}
			if(categoryIndex.getSize()==0){
				ArrayList<Categorization> newList = new ArrayList<Categorization>();
				newList.add(czn);
				categoryIndex.setContent(newList, czn.getCategoryId());
			}else if(categoryIndex.containsId(czn.getCategoryId())){
				categoryIndex.getContent(czn.getCategoryId()).add(czn);
			}else{
				ArrayList<Categorization> newList = new ArrayList<Categorization>();
				newList.add(czn);
				categoryIndex.setContent(newList, czn.getCategoryId());
			}
		}
	}
	
	public synchronized void addCategorizationWithoutId(long docId, long catId, double probability){
		long id = assignments.getMaxId()+1;
		Categorization czn = new Categorization(id, docId, catId, probability);
		this.setCategorization(czn);
	}
	
	public Categorization getCategorizationByAddress(long address){	
		return assignments.getContent(address);
	}
	
	public synchronized void deleteCategorization(long id){
		Categorization assignment = assignments.getContent(id);
		documentIndex.getContent(assignment.getDocumentId()).remove(assignment);
		if(documentIndex.getContent(assignment.getDocumentId()).isEmpty()){
			documentIndex.deleteNode(assignment.getDocumentId());
		}
		categoryIndex.getContent(assignment.getCategoryId()).remove(assignment);
		if(categoryIndex.getContent(assignment.getCategoryId()).isEmpty()){
			categoryIndex.deleteNode(assignment.getCategoryId());
		}
		assignments.deleteNode(id);
	}
	
	public Categorization[] getCategorizationArray(){
		ArrayList<Categorization> allContent = assignments.toArrayList();
		Categorization[] cznArray = allContent.toArray(new Categorization[0]);
		return cznArray;
	}
	
	public Categorization[] getDocumentCategorizations(long docId){
		ArrayList<Categorization> allContent = documentIndex.getContent(docId);
		Categorization[] cznArray;
		if(allContent!=null){
			cznArray = allContent.toArray(new Categorization[0]);
		}else {
			cznArray = new Categorization[0];
		}
		return cznArray;
	}
	
	public Categorization[] getCategoryCategorizations(long catId){
		ArrayList<Categorization> allContent = categoryIndex.getContent(catId);
		Categorization[] cznArray;
		if(allContent!=null){
			cznArray = allContent.toArray(new Categorization[0]);
		}else {
			cznArray = new Categorization[0];
		}
		
		return cznArray;
	}
	
	public long getSize(){
		return assignments.getSize();
	}
}
