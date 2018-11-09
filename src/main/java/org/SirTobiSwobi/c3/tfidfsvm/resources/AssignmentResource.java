package org.SirTobiSwobi.c3.tfidfsvm.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCAssignment;
import org.SirTobiSwobi.c3.tfidfsvm.db.Assignment;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

import com.codahale.metrics.annotation.Timed;

@Path("/targetfunction/{ass}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssignmentResource {
	private ReferenceHub refHub;

	public AssignmentResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
    @Timed
	public Response getAssignment(@PathParam("ass") long ass){
		if(!refHub.getTargetFunctionManager().containsAssignment(ass)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Assignment assignment = refHub.getTargetFunctionManager().getAssignmentByAddress(ass);
		
		TCAssignment output = new TCAssignment(assignment.getId(),assignment.getDocumentId(),assignment.getCategoryId());
		
		return Response.ok(output).build();
		
	}
	
	@PUT
	public Response setAssignment(@PathParam("ass") long ass, @NotNull @Valid TCAssignment assignment){
		if(assignment.getId()!=ass){
			Response response = Response.status(400).build();
			return response;
		}
		if(refHub.getCategoryManager().containsCategory(assignment.getCategoryId())&&refHub.getDocumentManager().containsDocument(assignment.getDocumentId())){
			if(ass!=-1){
				refHub.getTargetFunctionManager().setAssignment(assignment.getId(), assignment.getDocumentId(), assignment.getCategoryId());
			}else{
				refHub.getTargetFunctionManager().addAssignmentWithoutId(assignment.getDocumentId(), assignment.getCategoryId());
			}
		}
			
		Response response = Response.ok().build();
		return response;
	}
	
	@DELETE
	public Response deleteAssignment(@PathParam("ass") long ass){
		if(!refHub.getTargetFunctionManager().containsAssignment(ass)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		refHub.getTargetFunctionManager().deleteAssignment(ass);
		Response response = Response.ok().build();
		return response;
	}
}
