package org.SirTobiSwobi.c3.tfidfsvm.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCTargetFunction {
	private TCAssignment[] assignments;
	public TCTargetFunction(){
		//JAckson deserialization
	}
	public TCTargetFunction(TCAssignment[] assignments) {
		super();
		this.assignments = assignments;
	}
	@JsonProperty
	public TCAssignment[] getAssignments() {
		return assignments;
	}
	
}
