package org.SirTobiSwobi.c3.tfidfsvm.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCRetraining;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

@Path("/retraining")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RetrainingResource {
	private ReferenceHub refHub;

	public RetrainingResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getRelationships(@QueryParam("hash") String hash){
		
		TCRetraining output = new TCRetraining(refHub.needsRetraining());
		return Response.ok(output).build();
		
	}
	
}
