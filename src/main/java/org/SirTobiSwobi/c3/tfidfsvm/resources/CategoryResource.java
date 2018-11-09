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

import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategory;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

import com.codahale.metrics.annotation.Timed;

@Path("/categories/{cat}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
	private ReferenceHub refHub;

	public CategoryResource(ReferenceHub refHub) {
		this.refHub = refHub;
	}
	
	@GET
    @Timed
	public Response getCategory(@PathParam("cat") long cat){
		if(!refHub.getCategoryManager().containsCategory(cat)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		TCCategory output = new TCCategory(refHub.getCategoryManager().getByAddress(cat).getId(),
				refHub.getCategoryManager().getByAddress(cat).getLabel(),
				refHub.getCategoryManager().getByAddress(cat).getDescription());
		
		return Response.ok(output).build();
		
	}

	@PUT
	public Response setCategory(@PathParam("cat") long cat, @NotNull @Valid TCCategory category){
		if(category.getId()!=cat){
			Response response = Response.status(400).build();
			return response;
		}
		
		refHub.getCategoryManager().setCategory(new Category(category.getId(),category.getLabel(),category.getDescription()));
		refHub.setNeedsRetraining(true);
		Response response = Response.ok().build();
		return response;
	}
	
	@DELETE
	public Response deleteCategory(@PathParam("cat") long cat){
		if(!refHub.getCategoryManager().containsCategory(cat)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		refHub.getCategoryManager().deleteCategory(cat);
		refHub.setNeedsRetraining(true);
		Response response = Response.ok().build();
		return response;
	}
}
