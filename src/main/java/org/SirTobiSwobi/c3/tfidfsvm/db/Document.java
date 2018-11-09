package org.SirTobiSwobi.c3.tfidfsvm.db;

public class Document {
	
	private long id;
	private String label;
	private String content;
	private String URL;
	
	public Document(long id, String label, String content) {
		super();
		this.id = id;
		this.label = label;
		this.content = content;
		this.URL = "";
	}
	
	public Document(long id, String label, String content, String URL) {
		super();
		this.id = id;
		this.label = label;
		this.content = content;
		this.URL = URL;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	
	public String toString(){
		return ""+id+" "+label+" "+content+" "+URL;
	}
	
	

}
