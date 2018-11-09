package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Category {

	private long id;
	private String label;
	private String description;
	
	public Category(long id, String label, String description) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString(){
		return ""+id+" "+label+" "+description;
	}
}
