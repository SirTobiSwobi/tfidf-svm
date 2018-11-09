package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCCategories {
	
	private TCCategory[] categories;
	
	public TCCategories(){
		//Jackson deserialization
	}

	public TCCategories(TCCategory[] categories) {
		super();
		this.categories = categories;
	}

	@JsonProperty
	public TCCategory[] getCategories() {
		return categories;
	}
	
	

}
