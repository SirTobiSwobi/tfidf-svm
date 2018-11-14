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
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategorization;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCCategorizations;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCDocument;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCProgress;
import org.SirTobiSwobi.c3.tfidfsvm.core.Classifier;
import org.SirTobiSwobi.c3.tfidfsvm.db.Categorization;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategorizationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

@Path("/categorizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategorizationsResource {
	private ReferenceHub refHub;
	private Client client;
	private Classifier classifier;

	public CategorizationsResource(ReferenceHub refHub, Client client, Classifier classifier) {
		super();
		this.refHub = refHub;
		this.client = client;
		this.classifier = classifier;
	}
	
	@GET
	public Response getCategorizations(){
		
		Categorization[] categorizations = refHub.getCategorizationManager().getCategorizationArray();
		TCCategorization[] TCcategorizations = new TCCategorization[categorizations.length];
		for(int i=0; i<categorizations.length; i++){
			TCcategorizations[i] = new TCCategorization(categorizations[i].getId(), categorizations[i].getDocumentId(), 
								categorizations[i].getCategoryId(), categorizations[i].getProbability());
		}
		TCCategorizations output = new TCCategorizations(TCcategorizations);
		
			
		return Response.ok(output).build();
			
	}
	
	@GET
	@Path("/documents/{docId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategorizationsForDocument(@PathParam("docId") long docId){
		Categorization[] categorizations = refHub.getCategorizationManager().getDocumentCategorizations(docId);
		TCCategorization[] TCcategorizations = new TCCategorization[categorizations.length];
		for(int i=0; i<categorizations.length; i++){
			TCcategorizations[i] = new TCCategorization(categorizations[i].getId(), categorizations[i].getDocumentId(), 
								categorizations[i].getCategoryId(), categorizations[i].getProbability());
		}
		TCCategorizations output = new TCCategorizations(TCcategorizations);
		
			
		return Response.ok(output).build();
	}
	
	@GET
	@Path("/categories/{catId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategorizationsForCategory(@PathParam("catId") long catId){
		Categorization[] categorizations = refHub.getCategorizationManager().getCategoryCategorizations(catId);
		TCCategorization[] TCcategorizations = new TCCategorization[categorizations.length];
		for(int i=0; i<categorizations.length; i++){
			TCcategorizations[i] = new TCCategorization(categorizations[i].getId(), categorizations[i].getDocumentId(), 
								categorizations[i].getCategoryId(), categorizations[i].getProbability());
		}
		TCCategorizations output = new TCCategorizations(TCcategorizations);
		
			
		return Response.ok(output).build();
	}
	
	@POST
	public Response performCategorizationOfNewDocument(@NotNull @Valid TCDocument doc){
		long docId;
		if(doc.getId()>=0){
			if(doc.getUrl().length()>0&&doc.getUrl().startsWith("http")){
				//downloading content from URL
				String content = client.target(doc.getUrl()).request().get(String.class);
				Document newDoc = new Document(doc.getId(),doc.getLabel(),content,doc.getUrl());
				refHub.getDocumentManager().setDocument(newDoc);
			}else{
				refHub.getDocumentManager().setDocument(new Document(doc.getId(),doc.getLabel(),doc.getContent()));
			}
			docId=doc.getId();
		}else{
			if(doc.getUrl().length()>0&&doc.getUrl().startsWith("http")){
				//downloading content from URL
				String content = client.target(doc.getUrl()).request().get(String.class);
				docId=refHub.getDocumentManager().addDocumentWithoutId(doc.getLabel(), content, doc.getUrl());
			}else{
				docId=refHub.getDocumentManager().addDocumentWithoutId(doc.getLabel(), doc.getContent());
			}
			
		}
		
		classifier.categorizeDocument(docId);
		TCProgress progress = new TCProgress(docId,"/categorizations/documents/"+docId,0.0);
		//URI location = UriBuilder.fromPath("/documents").build();
		Response response = Response.ok(progress).build();
		return response;
	}
	
	@POST
	@Path("/existing/{docId}")
	public Response performCategorizationOfExistingDocument(@PathParam("docId") long docId){
		if(!refHub.getDocumentManager().containsDocument(docId)){
			return Response.status(404).build();
		}
		classifier.categorizeDocument(docId);
		TCProgress progress = new TCProgress(docId,"/categorizations/documents/"+docId,0.0);
		//URI location = UriBuilder.fromPath("/documents").build();
		Response response = Response.ok(progress).build();
		return response;
	}
	
	@DELETE
	public Response deleteAllCategorizations(){
		refHub.setCategorizationManager(new CategorizationManager());
		refHub.getCategorizationManager().setRefHub(refHub);
		return Response.ok().build();
	}
	
}
