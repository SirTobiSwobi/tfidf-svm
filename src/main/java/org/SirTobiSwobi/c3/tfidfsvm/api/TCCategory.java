package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCCategory {
	
	private long id;
	private String label;
	private String description;
	
	public TCCategory(){
		//Jackson deserialization
	}
	
	public TCCategory(long id, String label, String description) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
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
	public String getDescription() {
		return description;
	}

}
