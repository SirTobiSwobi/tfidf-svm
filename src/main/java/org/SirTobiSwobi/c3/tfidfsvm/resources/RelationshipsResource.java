package org.SirTobiSwobi.c3.tfidfsvm.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategories;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategory;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCHash;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCRelationship;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCRelationships;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.Relationship;
import org.SirTobiSwobi.c3.tfidfsvm.db.RelationshipType;
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;


@Path("/relationships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipsResource {
	private ReferenceHub refHub;
	
	public RelationshipsResource(ReferenceHub refHub) {
		this.refHub = refHub;
	}
	
	@GET
	public Response getRelationships(@QueryParam("hash") String hash){
		if(hash!=null&&hash.equals("1")){
			TCHash h = new TCHash("relationships",refHub.getCategoryManager().getRelationshipHash());
			return Response.ok(h).build();
		}else{
		
			TCRelationships output = buildRelationships(refHub.getCategoryManager().getRelationshipArray());
			return Response.ok(output).build();
		}	
	}
	
	@GET
	@Path("/from/{fromId}")
	@Produces(MediaType.APPLICATION_JSON)
	public TCRelationships getRelationshipsFrom(@PathParam("fromId") long fromId){
		Relationship[] relationships = refHub.getCategoryManager().getAllRelationshipsFrom(fromId);
		return buildRelationships(relationships);
	}
	
	@GET
	@Path("/to/{toId}")
	@Produces(MediaType.APPLICATION_JSON)
	public TCRelationships getRelationshipsTo(@PathParam("toId") long toId){
		Relationship[] relationships = refHub.getCategoryManager().getAllRelationshipsTo(toId);
		return buildRelationships(relationships);
	}
	
	@GET
	@Path("/ancestors/{ofId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAncestorsOf(@PathParam("ofId") long ofId){
		long[] ancestorIds = refHub.getTargetFunctionManager().findAllImplicitCatIds(ofId, SearchDirection.Ascending);
		
		TCCategory[] tcCategories = new TCCategory[ancestorIds.length];
		for(int i=0; i<ancestorIds.length; i++){
			Category category = refHub.getCategoryManager().getByAddress(ancestorIds[i]);
			if(category!=null){
				tcCategories[i] = new TCCategory(category.getId(),category.getLabel(),category.getDescription());
			}else{
				Response response = Response.status(404).build();
				return response;
			}
			
		}
		TCCategories returnValue;
		if(tcCategories.length>0){
			returnValue=new TCCategories(tcCategories);
		}else{
			returnValue = new TCCategories();
		}
		return Response.ok(returnValue).build();
	}
	
	
	private TCRelationships buildRelationships(Relationship[] relationships){
		TCRelationship[] TCrelationshipArray = new TCRelationship[relationships.length];
		for(int i=0; i<relationships.length;i++){
			Relationship rel = relationships[i];
			TCRelationship TCrel = new TCRelationship(rel.getId(), rel.getFrom().getId(), rel.getTo().getId(), rel.getType().toString());
			TCrelationshipArray[i]=TCrel;
		}
		TCRelationships TCrelationships;
		if(relationships.length>0){
			TCrelationships = new TCRelationships(TCrelationshipArray);
		}else{
			TCrelationships = new TCRelationships();
		}
		
		return TCrelationships;
		
	}
	
	@DELETE
	public Response deleteAllRelationships(){
		refHub.getCategoryManager().deleteAllRelationships();
		refHub.setNeedsRetraining(true);
		Response response = Response.ok().build();
		return response;
	}
	
	@POST
	public Response addRelationships(@NotNull @Valid TCRelationships relationships){
		
		if(relationships.getRelationships().length==0){
			Response response = Response.status(400).build();
			return response;
		}
		else if(relationships.getRelationships().length>0){
			for(int i=0; i<relationships.getRelationships().length; i++){
				TCRelationship rel=relationships.getRelationships()[i];
				RelationshipType type;
				if(rel.getType().equals("Sub")){
					type = RelationshipType.Sub;
				}else if(rel.getType().equals("Equality")){
					type = RelationshipType.Equality;
				}else{
					Response response = Response.status(400).build();
					return response;
				}
				if(rel.getId()>=0){
					refHub.getCategoryManager().setRelationship(rel.getId(), rel.getFromId(), rel.getToId(), type);
				}else{			
					refHub.getCategoryManager().addRelatonshipWithoutId(rel.getFromId(), rel.getToId(), type);
				}
			}
		}
		refHub.setNeedsRetraining(true);
		Response response = Response.ok().build();
		return response;
	}
	
}
