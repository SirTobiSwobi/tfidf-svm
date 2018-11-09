package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

import org.SirTobiSwobi.c3.tfidfsvm.db.Document;

public class DocumentManager {
	
	private AVLTree<Document> documents;
	
	public DocumentManager(){
		documents = new AVLTree<Document>();
	}
	
	public long getSize(){
		return documents.getSize();
	}
	
	public Document getByAddress(long address){
		
		return documents.getContent(address);
	}
	
	public void setDocument(Document document){
		documents.setContent(document, document.getId());
	}

	public synchronized long addDocumentWithoutId(String label, String content){
		long id = documents.getMaxId()+1;
		Document doc = new Document(id,label,content);
		documents.setContent(doc,id);
		return id;
	}
	
	public synchronized long addDocumentWithoutId(String label, String content, String url){
		long id = documents.getMaxId()+1;
		Document doc = new Document(id,label,content,url);
		documents.setContent(doc,id);
		return id;
	}
	
	public void deleteDocument(long id){
		documents.deleteNode(id);
	}
	
	public Document[] getDocumentArray(){
		ArrayList<Document> allContent = documents.toArrayList();
		Document[] docArray = allContent.toArray(new Document[0]);
		return docArray;
	}
	
	public boolean containsDocument(long id){
		return documents.containsId(id);
	}

	public String getDocumentHash(){
		byte[] contentHash = documents.getContentHash();
		String result="";
		for(int i=0; i<contentHash.length;i++){
			result = result + Integer.toHexString(contentHash[i] & 255);
		}
		return result;
	}
}
