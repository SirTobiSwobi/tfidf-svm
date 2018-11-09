package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCDocument {
	
	
	private long id;
	private String label;
	private String content;
	private String url;
	
	public TCDocument(){
		//Jackson deserialization
	}
	
	
	public TCDocument(long id, String label, String content, String url) {
		this.content = content;
		this.label = label;
		this.id = id;
		this.url = url;
	}
	
	@JsonProperty
	public long getId() {
		return id;
	}
	
	@JsonProperty
	public String getLabel() {
		return label;
	}
	
	@JsonProperty
	public String getContent() {
		return content;
	}

	@JsonProperty
	public String getUrl() {
		return url;
	}	

}
