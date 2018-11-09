package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Relationship {
	
	private long id;
	private Category from;
	private Category to;
	private RelationshipType type;
	
	public Relationship(long id, Category from, Category to, RelationshipType type) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Category getFrom() {
		return from;
	}
	public void setFrom(Category from) {
		this.from = from;
	}
	public Category getTo() {
		return to;
	}
	public void setTo(Category to) {
		this.to = to;
	}
	public RelationshipType getType() {
		return type;
	}
	public void setType(RelationshipType type) {
		this.type = type;
	}
	
	public String toString(){
		return ""+id+" "+from.toString()+" "+to.toString()+" "+type.toString();
	}
	
	

}
