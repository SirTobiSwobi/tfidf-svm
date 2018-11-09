package org.SirTobiSwobi.c3.tfidfsvm.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategories;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategory;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCHash;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriesResource {
	

	ReferenceHub refHub;

	public CategoriesResource(ReferenceHub refHub) {
		this.refHub=refHub;
	}
	
	@POST
	public Response addCategories(@NotNull @Valid TCCategories categories){
		if(categories.getCategories().length==0){
			Response response = Response.status(400).build();
			return response;
		}
		else if(categories.getCategories().length>0){
			for(int i=0; i<categories.getCategories().length; i++){
				TCCategory cat=categories.getCategories()[i];
				if(cat.getId()>=0){
					refHub.getCategoryManager().setCategory(new Category(cat.getId(),cat.getLabel(),cat.getDescription()));
				}else{			
					refHub.getCategoryManager().addCategoryWithoutId(cat.getLabel(), cat.getDescription());
				}
			}
		}
		refHub.setNeedsRetraining(true);
		Response response = Response.ok().build();
		return response;
	}

	
	@GET
	public Response getCategories(@QueryParam("hash") String hash){
		if(hash!=null&&hash.equals("1")){
			TCHash h = new TCHash("categories",refHub.getCategoryManager().getCategoryHash());
			return Response.ok(h).build();
		}else{
			Category[] categories = refHub.getCategoryManager().getCategoryArray();
			TCCategory[] TCcategoryArray = new TCCategory[categories.length];
			for(int i=0; i<categories.length;i++){
				Category cat = categories[i];
				TCCategory TCcat = new TCCategory(cat.getId(),cat.getLabel(),cat.getDescription());
				TCcategoryArray[i]=TCcat;
			}
			TCCategories TCcategories;
			if(categories.length>0){
				TCcategories = new TCCategories(TCcategoryArray);
			}else{
				TCcategories = new TCCategories();
			}
			
			return Response.ok(TCcategories).build();
		}
	}
	
	@DELETE
	public Response deleteAllCategories(){
		refHub.getCategoryManager().deleteAllCategories();
		refHub.setNeedsRetraining(true);
		Response response = Response.ok().build();
		return response;
	}

}
