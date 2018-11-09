package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

public class CategoryManager {
	
	private AVLTree<Category> categories;
	private AVLTree<Relationship> relationships;
	private AVLTree<ArrayList<Relationship>> fromIndex;
	private AVLTree<ArrayList<Relationship>> toIndex;

	public CategoryManager(){
		this.categories = new AVLTree<Category>();
		this.relationships = new AVLTree<Relationship>();
		this.fromIndex = new AVLTree<ArrayList<Relationship>>();
		this.toIndex = new AVLTree<ArrayList<Relationship>>();
	}
	
	public long getSize(){
		return categories.getSize();
	}
	
	public Category getByAddress(long address){	
		return categories.getContent(address);
	}
	
	public void setCategory(Category category){
		categories.setContent(category, category.getId());
	}

	public synchronized void addCategoryWithoutId(String label, String description){
		long id = categories.getMaxId()+1;
		Category cat = new Category(id,label,description);
		categories.setContent(cat,id);
	}
	
	public synchronized void deleteCategory(long id){
		if(this.relationships.getSize()>0){ //Otherwise null pointer exceptions can occur if there are categories but no relationships.
			long toDelete = categories.getContent(id).getId();
			deleteAllRelationshipsContaining(toDelete);
		}		
		categories.deleteNode(id);
	}
	
	public Category[] getCategoryArray(){
		ArrayList<Category> allContent = categories.toArrayList();
		Category[] catArray = allContent.toArray(new Category[0]);
		return catArray;
	}
	
	public synchronized void setRelationship(long id, long fromId, long toId, RelationshipType type){
		assert(categories.containsId(fromId)&&categories.containsId(toId));
		Relationship relationship = new Relationship(id, categories.getContent(fromId), categories.getContent(toId), type);
		relationships.setContent(relationship, id);
		if(fromIndex.getSize()==0){
			ArrayList<Relationship> newList = new ArrayList<Relationship>();
			newList.add(relationship);
			fromIndex.setContent(newList, fromId);
		}else if(fromIndex.containsId(fromId)){
			fromIndex.getContent(fromId).add(relationship);
		}else{
			ArrayList<Relationship> newList = new ArrayList<Relationship>();
			newList.add(relationship);
			fromIndex.setContent(newList, fromId);
		}
		if(toIndex.getSize()==0){
			ArrayList<Relationship> newList = new ArrayList<Relationship>();
			newList.add(relationship);
			toIndex.setContent(newList, toId);
		}else if(toIndex.containsId(toId)&&toIndex.getSize()>0){
			toIndex.getContent(toId).add(relationship);
		}else{
			ArrayList<Relationship> newList = new ArrayList<Relationship>();
			newList.add(relationship);
			toIndex.setContent(newList, toId);
		}
	};
	
	public synchronized void deleteRelationship(long id){
		Relationship rel = relationships.getContent(id);
		fromIndex.getContent(rel.getFrom().getId()).remove(rel);
		if(fromIndex.getContent(rel.getFrom().getId()).isEmpty()){
			fromIndex.deleteNode(rel.getFrom().getId());
		}
		toIndex.getContent(rel.getTo().getId()).remove(rel);
		if(toIndex.getContent(rel.getTo().getId()).isEmpty()){
			toIndex.deleteNode(rel.getTo().getId());
		}
		relationships.deleteNode(id);
	}
	
	public Relationship getRelationshipByAddress(long relationshipAddress){
		
		return relationships.getContent(relationshipAddress);
	}
	
	public synchronized void addRelatonshipWithoutId(long fromId, long toId, RelationshipType type){
		assert(categories.containsId(fromId)&&categories.containsId(toId));
		long id = relationships.getMaxId()+1;
		setRelationship(id, fromId, toId, type);
	}
	
	public long getRelationshipSize(){
		return relationships.getSize();
	}
	
	public Relationship[] getRelationshipArray(){
		ArrayList<Relationship> allContent = relationships.toArrayList();
		Relationship[] relArray = allContent.toArray(new Relationship[0]);
		return relArray;
	}
	
	public Relationship[] getAllRelationshipsFrom(long fromId){
		ArrayList<Relationship> allContent = fromIndex.getContent(fromId);
		Relationship[] relArray;
		if(allContent!=null){
			relArray = allContent.toArray(new Relationship[0]);
		}else {
			relArray = new Relationship[0];
		}
		return relArray;
	}
	
	public Relationship[] getAllRelationshipsTo(long toId){
		ArrayList<Relationship> allContent = toIndex.getContent(toId);
		Relationship[] relArray;
		if(allContent!=null){
			relArray = allContent.toArray(new Relationship[0]);
		}else {
			relArray = new Relationship[0];
		}
		
		return relArray;
	}
	
	public synchronized void deleteAllRelationshipsContaining(long cat){
		if(toIndex.getContent(cat)!=null){
			long[] toDelete = new long[toIndex.getContent(cat).size()];
			for(int i=0;i<toDelete.length;i++){
				toDelete[i]=toIndex.getContent(cat).get(i).getId();
			}
			toIndex.deleteNode(cat);
			for(int i=0;i<toDelete.length;i++){
				relationships.deleteNode(toDelete[i]);
			}
		}
		if(fromIndex.getContent(cat)!=null){
			long[] fromDelete = new long[fromIndex.getContent(cat).size()];
			for(int i=0;i<fromDelete.length;i++){
				fromDelete[i]=fromIndex.getContent(cat).get(i).getId();
			}		
			fromIndex.deleteNode(cat);
			for(int i=0;i<fromDelete.length;i++){
				relationships.deleteNode(fromDelete[i]);
			}
		}		
	}
	
	public synchronized void deleteAllCategories(){
		this.categories = new AVLTree<Category>();
		this.relationships = new AVLTree<Relationship>();
		this.fromIndex = new AVLTree<ArrayList<Relationship>>();
		this.toIndex = new AVLTree<ArrayList<Relationship>>();
	}
	
	public synchronized void deleteAllRelationships(){
		this.relationships = new AVLTree<Relationship>();
		this.fromIndex = new AVLTree<ArrayList<Relationship>>();
		this.toIndex = new AVLTree<ArrayList<Relationship>>();
	}
	
	public boolean containsCategory(long id){
		return categories.containsId(id);
	}
	
	public boolean containsRelationship(long id){
		return relationships.containsId(id);
	}
	
	public String getRelationshipHash(){
		String result="";
		byte[] relationshipHash = relationships.getContentHash();
		byte[] toHash = toIndex.getContentHash();
		byte[] fromHash = fromIndex.getContentHash();
		for(int i=0; i<relationshipHash.length;i++){
			byte contentHash;
			contentHash = (byte)(relationshipHash[i]+toHash[i]+fromHash[i]);
			result = result + Integer.toHexString(contentHash & 255);
		}
		
		return result;
	}
	
	public String getCategoryHash(){
		byte[] contentHash = categories.getContentHash();
		String result="";
		for(int i=0; i<contentHash.length;i++){
			result = result + Integer.toHexString(contentHash[i] & 255);
		}
		return result;
	}


}
