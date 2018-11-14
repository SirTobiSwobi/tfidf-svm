package org.SirTobiSwobi.c3.tfidfsvm.resources;

import static io.dropwizard.testing.FixtureHelpers.fixture;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfiguration;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCModel;
import org.SirTobiSwobi.c3.tfidfsvm.core.LibSvmWrapper;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.SirTobiSwobi.c3.tfidfsvm.db.VocabularyTripel;
import org.SirTobiSwobi.c3.tfidfsvm.resources.ModelResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import libsvm.svm_model;
import libsvm.svm_parameter;

@Path("/model")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActiveModelResource {
	private ReferenceHub refHub;
	private Client client;

	public ActiveModelResource(ReferenceHub refHub, Client client) {
		super();
		this.refHub = refHub;
		this.client = client;
	}
	
	@GET
	public Response getModel(){
		Model model = refHub.getActiveModel();
		if(model==null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}	
		TCModel output = ModelResource.buildTCModel(model,refHub);	
		
		return Response.ok(output).build();
		
	}
	
	@POST
	public Response setActiveModel(TCModel model, @QueryParam("loadFrom") String source){
		if(source!=null&&source.length()>5&&source.startsWith("http")){
			String content = client.target(source).request().get(String.class);
			/*
			Response res = client.target(source).request("application/json").get();
			if(res.getStatusInfo() != Response.Status.OK){
				return Response.status(res.getStatus()).build();
			}else{
				ObjectMapper MAPPER = Jackson.newObjectMapper();
				String content = res.readEntity(String.class);
				try{
					TCModel retrievedModel = MAPPER.readValue(content, TCModel.class);
					Model activeModel = new Model(retrievedModel.getId(), retrievedModel.getConfigurationId(), retrievedModel.getTrainingLog());
					refHub.setActiveModel(activeModel);
				}catch(Exception e){
					return Response.status(404).build();
				}
			}
			*/		
			ObjectMapper MAPPER = Jackson.newObjectMapper();
			try{
				TCModel retrievedModel = MAPPER.readValue(content, TCModel.class);
				svm_model svmModel = LibSvmWrapper.buildSvmModel(retrievedModel.getSvmModel());
				svm_parameter param = LibSvmWrapper.buildSvmParameter(retrievedModel.getSvmModel().getParam());
				/*
				svm_model svmModel = new svm_model();
				svm_parameter param = new svm_parameter();
				param.svm_type = LibSvmWrapper.getIdForSvmType(retrievedModel.getSvmModel().getSvm_type());
				param.kernel_type = LibSvmWrapper.getIdForSvmType(retrievedModel.getSvmModel().getKernel_type());
				param.gamma = retrievedModel.getSvmModel().getGamma();
				param.coef0 = retrievedModel.getSvmModel().getCoef0();
				param.degree = retrievedModel.getSvmModel().getDegree();
				param.nu = retrievedModel.getSvmModel().get
				svmModel.param=param;
				svmModel.nr_class = retrievedModel.getSvmModel().getNr_class();
				svmModel.l = retrievedModel.getSvmModel().getTotal_sv();
				svmModel.rho = retrievedModel.getSvmModel().getRho();
				svmModel.label = retrievedModel.getSvmModel().getLabel();
				svmModel.probA = retrievedModel.getSvmModel().getProbA();
				svmModel.probB = retrievedModel.getSvmModel().getProbB();
				svmModel.nSV = retrievedModel.getSvmModel().getNr_sv();
				svmModel.sv_coef = retrievedModel.getSvmModel().getSv_coef();
				TCSvmNode[][] tcVectors = retrievedModel.getSvmModel().getSvm_node();
				svm_node[][] vectors = LibSvmWrapper.buildSupportVectors(tcVectors);
				svmModel.SV=vectors;
				*/
				TCConfiguration configuration = retrievedModel.getConfiguration();
				SelectionPolicy selectionPolicy = SelectionPolicy.MicroaverageF1;
				if(configuration.getSelectionPolicy().equals("MicroaverageF1")){
					selectionPolicy=SelectionPolicy.MicroaverageF1;
				}else if(configuration.getSelectionPolicy().equals("MicroaveragePrecision")){
					selectionPolicy=SelectionPolicy.MicroaveragePrecision;
				}else if(configuration.getSelectionPolicy().equals("MicroaverageRecall")){
					selectionPolicy=SelectionPolicy.MicroaverageRecall;
				}else if(configuration.getSelectionPolicy().equals("MacroaverageF1")){
					selectionPolicy=SelectionPolicy.MacroaverageF1;
				}else if(configuration.getSelectionPolicy().equals("MacroaveragePrecision")){
					selectionPolicy=SelectionPolicy.MacroaveragePrecision;
				}else if(configuration.getSelectionPolicy().equals("MacroaverageRecall")){
					selectionPolicy=SelectionPolicy.MacroaverageRecall;
				}
				Configuration conf = new Configuration(configuration.getId(), configuration.getFolds(), configuration.isIncludeImplicits(), configuration.getAssignmentThreshold(),
						selectionPolicy, configuration.getTopTermsPerCat(), param);
				Model activeModel = new Model(retrievedModel.getId(), conf, retrievedModel.getTrainingLog(),
											VocabularyTripel.buildControlledVocabulary(retrievedModel.getControlledVocabulary()), 
											retrievedModel.getTrainingSetSize(), svmModel);
				refHub.setActiveModel(activeModel);
			}catch(Exception e){
				System.out.println(e.getMessage());
				return Response.status(404).build();
			}	
			refHub.setNeedsRetraining(false);
			return Response.ok().build();
		}else if(model!=null){
			TCConfiguration configuration = model.getConfiguration();
			
			svm_model svmModel = LibSvmWrapper.buildSvmModel(model.getSvmModel());
			svm_parameter param = LibSvmWrapper.buildSvmParameter(model.getSvmModel().getParam());
			
			SelectionPolicy selectionPolicy = SelectionPolicy.MicroaverageF1;
			if(configuration.getSelectionPolicy().equals("MicroaverageF1")){
				selectionPolicy=SelectionPolicy.MicroaverageF1;
			}else if(configuration.getSelectionPolicy().equals("MicroaveragePrecision")){
				selectionPolicy=SelectionPolicy.MicroaveragePrecision;
			}else if(configuration.getSelectionPolicy().equals("MicroaverageRecall")){
				selectionPolicy=SelectionPolicy.MicroaverageRecall;
			}else if(configuration.getSelectionPolicy().equals("MacroaverageF1")){
				selectionPolicy=SelectionPolicy.MacroaverageF1;
			}else if(configuration.getSelectionPolicy().equals("MacroaveragePrecision")){
				selectionPolicy=SelectionPolicy.MacroaveragePrecision;
			}else if(configuration.getSelectionPolicy().equals("MacroaverageRecall")){
				selectionPolicy=SelectionPolicy.MacroaverageRecall;
			}
			Configuration conf = new Configuration(configuration.getId(), configuration.getFolds(), configuration.isIncludeImplicits(), configuration.getAssignmentThreshold(),
					selectionPolicy, configuration.getTopTermsPerCat(),param);
			Model activeModel = new Model(model.getId(), conf, model.getTrainingLog(), 
						VocabularyTripel.buildControlledVocabulary(model.getControlledVocabulary()), model.getTrainingSetSize(), svmModel);
			refHub.setActiveModel(activeModel);
			refHub.setNeedsRetraining(false);
			return Response.ok().build();
		}else{
			return Response.status(400).build();
		}
		
	
		
	}
	
	@PUT
	public Response updateActiveModel(TCModel model, @QueryParam("loadFrom") String source){
		return setActiveModel(model, source);
	}
}
