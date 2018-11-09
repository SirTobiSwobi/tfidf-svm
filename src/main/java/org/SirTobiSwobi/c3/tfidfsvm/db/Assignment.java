package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Assignment {
	protected long id;
	protected long documentId;
	protected long categoryId;
	public Assignment(long id, long documentId, long categoryId) {
		super();
		this.id = id;
		this.documentId = documentId;
		this.categoryId = categoryId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public String toString(){
		return ""+id+" "+documentId+" "+categoryId;
	}
}
