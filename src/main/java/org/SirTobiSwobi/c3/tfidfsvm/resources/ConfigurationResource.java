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

import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfiguration;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCSvmParameter;
import org.SirTobiSwobi.c3.tfidfsvm.core.LibSvmWrapper;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;

import com.codahale.metrics.annotation.Timed;

import libsvm.svm_parameter;

@Path("/configurations/{conf}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
	private ReferenceHub refHub;

	public ConfigurationResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
    @Timed
	public Response getConfiguration(@PathParam("conf") long conf){
		if(!refHub.getConfigurationManager().containsConfiguration(conf)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Configuration configuration = refHub.getConfigurationManager().getByAddress(conf);	
		String selectionPolicy="MicroaverageF1";
		if(configuration.getSelectionPolicy()==SelectionPolicy.MacroaverageF1){
			selectionPolicy="MicroaverageF1";
		}else if(configuration.getSelectionPolicy()==SelectionPolicy.MicroaveragePrecision){
			selectionPolicy="MicroaveragePrecision";
		}else if(configuration.getSelectionPolicy()==SelectionPolicy.MicroaverageRecall){
			selectionPolicy="MicroaverageRecall";
		}else if(configuration.getSelectionPolicy()==SelectionPolicy.MacroaverageF1){
			selectionPolicy="MacroaverageF1";
		}else if(configuration.getSelectionPolicy()==SelectionPolicy.MacroaveragePrecision){
			selectionPolicy="MacroaveragePrecision";
		}else if(configuration.getSelectionPolicy()==SelectionPolicy.MacroaverageRecall){
			selectionPolicy="MacroaverageRecall";
		}
		TCSvmParameter svmParam = LibSvmWrapper.buildTcSvmParameter(configuration.getSvmParameter());
		TCConfiguration output = new TCConfiguration(
						configuration.getId(),
						configuration.getFolds(),
						configuration.isIncludeImplicits(), 
						configuration.getAssignmentThreshold(),
						selectionPolicy,
						configuration.getTopTermsPerCat(), 
						svmParam);	
		return Response.ok(output).build();
		
	}
	
	@PUT
	public Response setConfiguration(@PathParam("conf") long conf, @NotNull @Valid TCConfiguration configuration){
		if(configuration.getId()!=conf){
			Response response = Response.status(400).build();
			return response;
		}
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
		
		svm_parameter svmParam = new svm_parameter();
		svmParam.C = configuration.getSvmParameter().getC();
		svmParam.coef0 = configuration.getSvmParameter().getCoef0();
		svmParam.degree = configuration.getSvmParameter().getDegree();
		svmParam.gamma = configuration.getSvmParameter().getGamma();
		svmParam.kernel_type = LibSvmWrapper.getIdForKernelType(configuration.getSvmParameter().getKernel_type());
		svmParam.nu = configuration.getSvmParameter().getNu();
		svmParam.p = configuration.getSvmParameter().getP();
		svmParam.probability = configuration.getSvmParameter().getProbability_estimates();
		svmParam.shrinking = configuration.getSvmParameter().getShrinking();
		svmParam.svm_type = LibSvmWrapper.getIdForSvmType(configuration.getSvmParameter().getSvm_type());
		
		
		refHub.getConfigurationManager().setConfiguration(
				new Configuration(configuration.getId(),
								configuration.getFolds(),
								configuration.isIncludeImplicits(),
								configuration.getAssignmentThreshold(),
								selectionPolicy,
								configuration.getTopTermsPerCat(),
								svmParam));
		Response response = Response.ok().build();
		return response;
	}
	
	@DELETE
	public Response deleteConfiguration(@PathParam("conf") long conf){
		if(!refHub.getConfigurationManager().containsConfiguration(conf)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		refHub.getConfigurationManager().deleteConfiguration(conf);
		Response response = Response.ok().build();
		return response;
	}

}
