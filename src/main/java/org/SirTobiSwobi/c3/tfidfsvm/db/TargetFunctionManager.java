package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

import org.SirTobiSwobi.c3.tfidfsvm.core.Utilities;

public class TargetFunctionManager {
	protected AVLTree<Assignment> assignments;
	protected AVLTree<ArrayList<Assignment>> documentIndex;
	protected AVLTree<ArrayList<Assignment>> categoryIndex;
	protected ReferenceHub refHub;
	
	public TargetFunctionManager() {
		super();
		this.refHub = null;
		this.assignments=new AVLTree<Assignment>();
		this.documentIndex=new AVLTree<ArrayList<Assignment>>();
		this.categoryIndex=new AVLTree<ArrayList<Assignment>>();
	}
	
	
	
	public ReferenceHub getRefHub() {
		return refHub;
	}

	public void setRefHub(ReferenceHub refHub) {
		this.refHub = refHub;
	}

	public long getSize(){
		return assignments.getSize();
	}
	
	public Assignment getAssignmentByAddress(long address){	
		return assignments.getContent(address);
	}
	
	public synchronized void setAssignment(long id, long documentId, long categoryId){
		assert(refHub.getCategoryManager().containsCategory(categoryId)&&refHub.getDocumentManager().containsDocument(documentId));
		Assignment assignment = new Assignment(id, documentId, categoryId);
		assignments.setContent(assignment, id);
		if(documentIndex.getSize()==0){
			ArrayList<Assignment> newList = new ArrayList<Assignment>();
			newList.add(assignment);
			documentIndex.setContent(newList, documentId);
		}else if(documentIndex.containsId(documentId)){
			documentIndex.getContent(documentId).add(assignment);
		}else{
			ArrayList<Assignment> newList = new ArrayList<Assignment>();
			newList.add(assignment);
			documentIndex.setContent(newList, documentId);
		}
		if(categoryIndex.getSize()==0){
			ArrayList<Assignment> newList = new ArrayList<Assignment>();
			newList.add(assignment);
			categoryIndex.setContent(newList, categoryId);
		}else if(categoryIndex.containsId(categoryId)){
			categoryIndex.getContent(categoryId).add(assignment);
		}else{
			ArrayList<Assignment> newList = new ArrayList<Assignment>();
			newList.add(assignment);
			categoryIndex.setContent(newList, categoryId);
		}
	}
	
	public synchronized void addAssignmentWithoutId(long documentId, long categoryId){
		assert(refHub.getCategoryManager().containsCategory(categoryId)&&refHub.getDocumentManager().containsDocument(documentId));
		long id = assignments.getMaxId()+1;
		setAssignment(id, documentId, categoryId);
	}
	
	public synchronized void deleteAssignment(long id){
		Assignment assignment = assignments.getContent(id);
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
	
	public Assignment[] getAssignmentArray(){
		ArrayList<Assignment> allContent = assignments.toArrayList();
		Assignment[] relArray = allContent.toArray(new Assignment[0]);
		return relArray;
	}
	
	public Assignment[] getDocumentAssignments(long docId){
		ArrayList<Assignment> allContent = documentIndex.getContent(docId);
		Assignment[] relArray;
		if(allContent!=null){
			relArray = allContent.toArray(new Assignment[0]);
		}else {
			relArray = new Assignment[0];
		}
		return relArray;
	}
	
	public Assignment[] getCategoryAssignments(long catId){
		ArrayList<Assignment> allContent = categoryIndex.getContent(catId);
		Assignment[] relArray;
		if(allContent!=null){
			relArray = allContent.toArray(new Assignment[0]);
		}else {
			relArray = new Assignment[0];
		}
		
		return relArray;
	}
	
	public synchronized void deleteAllAssignmentsContaining(long ass){
		if(documentIndex.getContent(ass)!=null){
			long[] toDelete = new long[documentIndex.getContent(ass).size()];
			for(int i=0;i<toDelete.length;i++){
				toDelete[i]=documentIndex.getContent(ass).get(i).getId();
			}
			documentIndex.deleteNode(ass);
			for(int i=0;i<toDelete.length;i++){
				assignments.deleteNode(toDelete[i]);
			}
		}
		if(categoryIndex.getContent(ass)!=null){
			long[] fromDelete = new long[categoryIndex.getContent(ass).size()];
			for(int i=0;i<fromDelete.length;i++){
				fromDelete[i]=categoryIndex.getContent(ass).get(i).getId();
			}		
			categoryIndex.deleteNode(ass);
			for(int i=0;i<fromDelete.length;i++){
				assignments.deleteNode(fromDelete[i]);
			}
		}		
	}
	
	public boolean containsAssignment(long id){
		return assignments.containsId(id);
	}
	
	public String getAssignmentHash(){
		String result="";
		byte[] assignmentHash = assignments.getContentHash();
		byte[] categoryHash = categoryIndex.getContentHash();
		byte[] documentHash = documentIndex.getContentHash();
		for(int i=0; i<assignmentHash.length;i++){
			byte contentHash;
			contentHash = (byte)(assignmentHash[i]+categoryHash[i]+documentHash[i]);
			result = result + Integer.toHexString(contentHash & 255);
		}
		
		return result;
	}
	
	public synchronized long[] getImplicitCatIdsForDocument(long docId){
		Assignment[] explicitAssignments = getDocumentAssignments(docId);
		long[] explicitCatIds=null;
		for(int i=0; i<explicitAssignments.length;i++){
			long catId = explicitAssignments[i].getCategoryId();
			explicitCatIds=Utilities.increaseAndAddValueIfNotIn(catId, explicitCatIds);
		}
		
		long[] allCatIds=null;
		if(explicitCatIds!=null){
			allCatIds = explicitCatIds.clone();
			for(int i=0; i<explicitCatIds.length;i++){
				allCatIds = Utilities.arrayUnionWithoutDuplicates(allCatIds, findAllImplicitCatIds(explicitCatIds[i],SearchDirection.Ascending));
			}
		}
			
		return allCatIds;
	}
	
	public long[] findAllImplicitCatIds(long catId, SearchDirection direction){
		Relationship[] relationships = refHub.getCategoryManager().getRelationshipArray();
		ArrayList<long[]> edges = new ArrayList<long[]>();
		for(int i=0; i<relationships.length; i++){
			if(relationships[i].getType().equals(RelationshipType.Sub)){
				long[] edge = new long[2];
				if(direction.equals(SearchDirection.Ascending)){
					edge[0] = relationships[i].getTo().getId();
					edge[1] = relationships[i].getFrom().getId();
				}else if(direction.equals(SearchDirection.Descending)){
					edge[0] = relationships[i].getFrom().getId();
					edge[1] = relationships[i].getTo().getId();
				}
				edges.add(edge);
			}else if(relationships[i].getType().equals(RelationshipType.Equality)){
				long[] edge = new long[2];
				edge[0] = relationships[i].getTo().getId();
				edge[1] = relationships[i].getFrom().getId();
				edges.add(edge);
				edge[0] = relationships[i].getFrom().getId();
				edge[1] = relationships[i].getTo().getId();
				edges.add(edge);
			}
		}
		long[][] edgeArray = new long[edges.size()][2];
		for(int i=0; i<edges.size();i++){
			edgeArray[i][0]=edges.get(i)[0];
			edgeArray[i][1]=edges.get(i)[1];
		}
		return Utilities.BFSreachable(edgeArray, catId);
	}
	
	public synchronized long[] getImplicitDocIdsForCategory(long catId){
		//find all descendant categories. Add their assignments to the result set. 
		long[] allCatIds = findAllImplicitCatIds(catId, SearchDirection.Descending);
		long[] allDocIds = null;
		Assignment[] explicitAssignments = this.getCategoryAssignments(catId);
		for(int i=0; i<explicitAssignments.length; i++){
			allDocIds = Utilities.increaseAndAddValueIfNotIn(explicitAssignments[i].getDocumentId(), allDocIds);
		}
		for(int i=0; i<allCatIds.length; i++){
			Assignment[] assignments = this.getCategoryAssignments(allCatIds[i]);
			long[] docIds = null;
			for(int j=0; j<assignments.length;j++){
				docIds = Utilities.increaseAndAddValueIfNotIn(assignments[j].getDocumentId(), docIds);
			}
			if(allDocIds==null&&docIds!=null){
				allDocIds=docIds;
			}else if(allDocIds!=null&&docIds!=null){
				allDocIds = Utilities.arrayUnionWithoutDuplicates(allDocIds, docIds);
			}
		}
		return allDocIds;
		
	}
	
	
	
	
}
