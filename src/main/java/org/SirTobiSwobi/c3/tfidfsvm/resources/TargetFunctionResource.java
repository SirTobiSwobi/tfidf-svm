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

import org.SirTobiSwobi.c3.tfidfsvm.api.TCAssignment;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCHash;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCTargetFunction;
import org.SirTobiSwobi.c3.tfidfsvm.core.Utilities;
import org.SirTobiSwobi.c3.tfidfsvm.db.Assignment;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;
import org.SirTobiSwobi.c3.tfidfsvm.db.TargetFunctionManager;

@Path("/targetfunction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TargetFunctionResource {
	private ReferenceHub refHub;

	public TargetFunctionResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getTargetfunction(@QueryParam("hash") String hash, @QueryParam("manualOnly") String manualOnly){
		if(hash!=null&&hash.equals("1")){
			TCHash h = new TCHash("targetfunction",refHub.getTargetFunctionManager().getAssignmentHash());
			return Response.ok(h).build();
		}else{
			TCTargetFunction output = buildTargetFunction(refHub.getTargetFunctionManager().getAssignmentArray());
			if(!(manualOnly!=null&&manualOnly.equals("1"))){
				Document[] documents = refHub.getDocumentManager().getDocumentArray();
				if(documents==null){
					return Response.status(404).build();
				}else if(documents!=null&&refHub.getTargetFunctionManager().getSize()>0){
					Assignment[] assignments = null;
					for(int i=0; i<documents.length; i++){
						if(i==0){
							assignments = getAssignmentsForDoc(documents[i].getId());
						}else{
							assignments = Utilities.arrayUnionWithoutDuplicates(assignments, getAssignmentsForDoc(documents[i].getId()));
						}
					}
					output = buildTargetFunction(assignments);
				}
				
			}
			return Response.ok(output).build();
		}	
	}
	
	private Assignment[] getAssignmentsForDoc(long docId){
		Assignment[] assignments = refHub.getTargetFunctionManager().getDocumentAssignments(docId);
		long[] implicitCats=refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(docId);
		if(implicitCats!=null){
			for(int i=0;i<implicitCats.length;i++){
				assignments = addAssignmentIfCatNotIn(assignments, implicitCats[i], docId);
			}
		}
		return assignments;
	}
	
	@GET
	@Path("/documents/{docId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAssignmentsForDocument(@PathParam("docId") long docId, @QueryParam("manualOnly") String manualOnly){
		Assignment[] assignments;
		if(!(manualOnly!=null&&manualOnly.equals("1"))){
			assignments = getAssignmentsForDoc(docId);
		}else{
			assignments = refHub.getTargetFunctionManager().getDocumentAssignments(docId);
		}
		
		return Response.ok(buildTargetFunction(assignments)).build();
	}
	
	@GET
	@Path("/categories/{catId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAssignmentsForCategory(@PathParam("catId") long catId, @QueryParam("manualOnly") String manualOnly){
		Assignment[] assignments = refHub.getTargetFunctionManager().getCategoryAssignments(catId);
		if(!(manualOnly!=null&&manualOnly.equals("1"))){
			long[] implicitDocs=refHub.getTargetFunctionManager().getImplicitDocIdsForCategory(catId);
			for(int i=0; i<implicitDocs.length;i++){
				assignments = addAssignmentIfDocNotIn(assignments, catId, implicitDocs[i]);
			}
		}
		return Response.ok(buildTargetFunction(assignments)).build();
	}
	
	private Assignment[] addAssignmentIfCatNotIn(Assignment[] assignments, long catId, long docId){
		Assignment[] result = assignments.clone();
		boolean includesCategory = false;
		for(int i=0; i<assignments.length; i++){
			if(assignments[i].getCategoryId()==catId){
				includesCategory = true;
			}
		}
		if(!includesCategory){
			Assignment assignment = new Assignment(-1, docId, catId);
			result = new Assignment[assignments.length+1];
			for(int i=0; i<assignments.length; i++){
				result[i]=assignments[i];
			}
			result[assignments.length]=assignment;
		}
		return result;
	}
	
	private Assignment[] addAssignmentIfDocNotIn(Assignment[] assignments, long catId, long docId){
		Assignment[] result=null; 
		boolean includesCategory = false;
		if(assignments!=null){
			result = assignments.clone();
			for(int i=0; i<assignments.length; i++){
				if(assignments[i].getDocumentId()==docId){
					includesCategory = true;
				}
			}
			if(!includesCategory){
				Assignment assignment = new Assignment(-1, docId, catId);
				result = new Assignment[assignments.length+1];
				for(int i=0; i<assignments.length; i++){
					result[i]=assignments[i];
				}
				result[assignments.length]=assignment;
			}
		}else{
			result = new Assignment[1];
			result[0] = new Assignment(-1,docId,catId);
		}		
		return result;
	}
	
	private Assignment[] addAssignmentIfNotIn(Assignment[] assignments, long catId, long docId){
		Assignment[] result = assignments.clone();
		boolean includesCategory = false;
		for(int i=0; i<assignments.length; i++){
			if(assignments[i].getDocumentId()==docId&&assignments[i].getCategoryId()==catId){
				includesCategory = true;
			}
		}
		if(!includesCategory){
			Assignment assignment = new Assignment(-1, docId, catId);
			result = new Assignment[assignments.length+1];
			for(int i=0; i<assignments.length; i++){
				result[i]=assignments[i];
			}
			result[assignments.length]=assignment;
		}
		return result;
	}
	
	
	
	
	private TCTargetFunction buildTargetFunction(Assignment[] assignments){
		TCAssignment[] TCassignmentArray = new TCAssignment[assignments.length];
		for(int i=0; i<assignments.length;i++){
			Assignment ass = assignments[i];
			TCAssignment TCass = new TCAssignment(ass.getId(), ass.getDocumentId(), ass.getCategoryId());
			TCassignmentArray[i]=TCass;
		}
		TCTargetFunction TCtargetFunction;
		if(assignments.length>0){
			TCtargetFunction = new TCTargetFunction(TCassignmentArray);
		}else{
			TCtargetFunction = new TCTargetFunction();
		}
		
		return TCtargetFunction;
		
	}
	
	@DELETE
	public Response deleteAllAssignments(){
		refHub.setTargetFunctionManager(new TargetFunctionManager());
		refHub.getTargetFunctionManager().setRefHub(refHub);
		Response response = Response.ok().build();
		return response;
	}
	
	@POST
	public Response addTargetFunction(@NotNull @Valid TCTargetFunction targetFunction){
		
		if(targetFunction.getAssignments().length==0){
			Response response = Response.status(400).build();
			return response;
		}
		else if(targetFunction.getAssignments().length>0){
			for(int i=0; i<targetFunction.getAssignments().length; i++){
				TCAssignment ass=targetFunction.getAssignments()[i];
			
				if(refHub.getCategoryManager().containsCategory(ass.getCategoryId())&&refHub.getDocumentManager().containsDocument(ass.getDocumentId())){
					if(ass.getId()>=0){
						refHub.getTargetFunctionManager().setAssignment(ass.getId(), ass.getDocumentId(), ass.getCategoryId());
					}else{			
						refHub.getTargetFunctionManager().addAssignmentWithoutId(ass.getDocumentId(), ass.getCategoryId());
					}
				}
				
			}
		}
		
		Response response = Response.ok().build();
		return response;
	}
}
