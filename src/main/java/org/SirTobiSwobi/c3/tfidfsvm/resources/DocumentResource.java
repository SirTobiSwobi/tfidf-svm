package org.SirTobiSwobi.c3.tfidfsvm.resources;

import com.codahale.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCDocument;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

@Path("/documents/{doc}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {
	

	private ReferenceHub refHub;
	private Client client;
	
	public DocumentResource(ReferenceHub refHub, Client client){
		this.refHub=refHub;
		this.client=client;
	}
	
	@GET
    @Timed
	public Response getDocument(@PathParam("doc") long doc){
		if(!refHub.getDocumentManager().containsDocument(doc)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		TCDocument output = new TCDocument(refHub.getDocumentManager().getByAddress(doc).getId(),
				refHub.getDocumentManager().getByAddress(doc).getLabel(),
				refHub.getDocumentManager().getByAddress(doc).getContent(),
				refHub.getDocumentManager().getByAddress(doc).getURL());
		return Response.ok(output).build();
		
	}
	
	@PUT
	public Response setDocument(@PathParam("doc") long doc, @NotNull @Valid TCDocument document){
		if(document.getId()!=doc){
			Response response = Response.status(400).build();
			return response;
		}
		if(document.getUrl().length()>0&&document.getUrl().startsWith("http")){
			//downloading content from URL
			String content = client.target(document.getUrl()).request().get(String.class);
			Document newDoc = new Document(document.getId(),document.getLabel(),content,document.getUrl());
			refHub.getDocumentManager().setDocument(newDoc);
		}else{
			refHub.getDocumentManager().setDocument(new Document(document.getId(),document.getLabel(),document.getContent()));
		}
		Response response = Response.ok().build();
		return response;
	}
	
	@DELETE
	public Response deleteDocument(@PathParam("doc") long doc){
		refHub.getDocumentManager().deleteDocument(doc);
		Response response = Response.ok().build();
		return response;
	}
	

}
