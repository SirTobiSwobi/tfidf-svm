package org.SirTobiSwobi.c3.tfidfsvm.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfiguration;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCModel;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCProgress;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCVocabularyTripel;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmModel;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmNode;
import org.SirTobiSwobi.c3.tfidfsvm.core.LibSvmWrapper;
import org.SirTobiSwobi.c3.tfidfsvm.core.Utilities;

@Path("/models/{mod}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {
	private ReferenceHub refHub;
	
	public ModelResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}

	@GET
	public Response getModel(@PathParam("mod") long mod){
		if(!refHub.getModelManager().containsModel(mod)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Model model = refHub.getModelManager().getModelByAddress(mod);
		
		if(model.getProgress()<1.0){
			//TCProgress output = new TCProgress("/models/"+mod,model.getProgress());
			TCModel output = buildTCModel(model, refHub);	//uncomment for debugging
			return Response.ok(output).build();
		}else{
			TCModel output = buildTCModel(model, refHub);		
			return Response.ok(output).build();
		}
		
		
		
	}
	
	@DELETE
	public Response deleteModel(@PathParam("mod") long mod){
		if(!refHub.getModelManager().containsModel(mod)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		refHub.getModelManager().deleteModel(mod);
		refHub.getEvaluationManager().deleteTrainingSession(mod);
		Response response = Response.ok().build();
		return response;
	}
	
	public static TCModel buildTCModel(Model model, ReferenceHub refHub){
		if(model==null){
			return null;
		}
		
		Configuration conf = model.getConfiguration();
		String selectionPolicy="MicroaverageF1";
		if(conf.getSelectionPolicy()==SelectionPolicy.MacroaverageF1){
			selectionPolicy="MicroaverageF1";
		}else if(conf.getSelectionPolicy()==SelectionPolicy.MicroaveragePrecision){
			selectionPolicy="MicroaveragePrecision";
		}else if(conf.getSelectionPolicy()==SelectionPolicy.MicroaverageRecall){
			selectionPolicy="MicroaverageRecall";
		}else if(conf.getSelectionPolicy()==SelectionPolicy.MacroaverageF1){
			selectionPolicy="MacroaverageF1";
		}else if(conf.getSelectionPolicy()==SelectionPolicy.MacroaveragePrecision){
			selectionPolicy="MacroaveragePrecision";
		}else if(conf.getSelectionPolicy()==SelectionPolicy.MacroaverageRecall){
			selectionPolicy="MacroaverageRecall";
		}
		TCConfiguration configuration = new TCConfiguration(conf.getId(), conf.getFolds(), conf.isIncludeImplicits(), conf.getAssignmentThreshold(),
				selectionPolicy, conf.getTopTermsPerCat());
		
		TCVocabularyTripel[] controlledVocabulary = new TCVocabularyTripel[model.getControlledVocabulary().length];
		for(int i=0;i<controlledVocabulary.length;i++){
			controlledVocabulary[i]=new TCVocabularyTripel(model.getControlledVocabulary()[i].getId(),
															model.getControlledVocabulary()[i].getTerm(), 
															model.getControlledVocabulary()[i].getDocumentFrequency(),
															model.getControlledVocabulary()[i].getSumDimensionSquares());
		}
		org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmModel svmModel=null;
		if(model.getSvmModel()!=null){	
		TCSvmNode[][] SV=LibSvmWrapper.buildSupportVectors(model.getSvmModel().SV);
		svmModel = new TCSvmModel(LibSvmWrapper.svm_type_table[model.getSvmModel().param.svm_type],
							LibSvmWrapper.kernel_type_table[model.getSvmModel().param.kernel_type],
							model.getSvmModel().param.degree, 
							model.getSvmModel().param.gamma,
							model.getSvmModel().param.coef0,
							model.getSvmModel().nr_class,
							model.getSvmModel().l, 
							model.getSvmModel().rho,
							model.getSvmModel().label, 
							model.getSvmModel().probA, 
							model.getSvmModel().probB, 
							model.getSvmModel().nSV, 
							model.getSvmModel().sv_coef,
							SV
				);
		}
		TCModel output = new TCModel(model.getId(), model.getConfiguration().getId(), model.getProgress(), model.getTrainingLog(), configuration, controlledVocabulary, model.getTrainingSetSize(), svmModel);
		return output;
	}
	
}
