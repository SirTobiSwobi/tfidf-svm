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

import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfiguration;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCConfigurations;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCHash;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;

@Path("/configurations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigurationsResource {
	private ReferenceHub refHub;

	public ConfigurationsResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getConfigurations(@QueryParam("hash") String hash){
		if(hash!=null&&hash.equals("1")){
			TCHash h = new TCHash("configurations",refHub.getConfigurationManager().getConfigurationHash());
			return Response.ok(h).build();
		}else{
			Configuration[] confArray=refHub.getConfigurationManager().getConfigurationArray();
			TCConfiguration[] outputArray = new TCConfiguration[confArray.length];
			for(int i=0; i<confArray.length;i++){
				String selectionPolicy="MicroaverageF1";
				if(confArray[i].getSelectionPolicy()==SelectionPolicy.MacroaverageF1){
					selectionPolicy="MicroaverageF1";
				}else if(confArray[i].getSelectionPolicy()==SelectionPolicy.MicroaveragePrecision){
					selectionPolicy="MicroaveragePrecision";
				}else if(confArray[i].getSelectionPolicy()==SelectionPolicy.MicroaverageRecall){
					selectionPolicy="MicroaverageRecall";
				}else if(confArray[i].getSelectionPolicy()==SelectionPolicy.MacroaverageF1){
					selectionPolicy="MacroaverageF1";
				}else if(confArray[i].getSelectionPolicy()==SelectionPolicy.MacroaveragePrecision){
					selectionPolicy="MacroaveragePrecision";
				}else if(confArray[i].getSelectionPolicy()==SelectionPolicy.MacroaverageRecall){
					selectionPolicy="MacroaverageRecall";
				}
				
				outputArray[i]=new TCConfiguration(confArray[i].getId(),
						confArray[i].getFolds(),
						confArray[i].isIncludeImplicits(), 
						confArray[i].getAssignmentThreshold(),
						selectionPolicy,
						confArray[i].getTopTermsPerCat()
						);
			}
			TCConfigurations output = new TCConfigurations(outputArray);
			return Response.ok(output).build();
		}	
	}
	
	@POST
	public Response addConfiguration(@NotNull @Valid TCConfigurations configurations){
		
		if(configurations.getConfigurations().length==0){
			Response response = Response.status(400).build();
			return response;
		}
		else if(configurations.getConfigurations().length>0){
			for(int i=0; i<configurations.getConfigurations().length; i++){
				TCConfiguration conf=configurations.getConfigurations()[i];
				SelectionPolicy selectionPolicy = SelectionPolicy.MicroaverageF1;
				if(conf.getSelectionPolicy().equals("MicroaverageF1")){
					selectionPolicy=SelectionPolicy.MicroaverageF1;
				}else if(conf.getSelectionPolicy().equals("MicroaveragePrecision")){
					selectionPolicy=SelectionPolicy.MicroaveragePrecision;
				}else if(conf.getSelectionPolicy().equals("MicroaverageRecall")){
					selectionPolicy=SelectionPolicy.MicroaverageRecall;
				}else if(conf.getSelectionPolicy().equals("MacroaverageF1")){
					selectionPolicy=SelectionPolicy.MacroaverageF1;
				}else if(conf.getSelectionPolicy().equals("MacroaveragePrecision")){
					selectionPolicy=SelectionPolicy.MacroaveragePrecision;
				}else if(conf.getSelectionPolicy().equals("MacroaverageRecall")){
					selectionPolicy=SelectionPolicy.MacroaverageRecall;
				}
				if(conf.getId()>=0){
					
					Configuration config = new Configuration(conf.getId(), conf.getFolds(), conf.isIncludeImplicits(), conf.getAssignmentThreshold(),selectionPolicy, conf.getTopTermsPerCat());
					refHub.getConfigurationManager().setConfiguration(config);
				}else{			
					refHub.getConfigurationManager().addConfigurationWithoutId(conf.getFolds(), conf.isIncludeImplicits(), conf.getAssignmentThreshold(), selectionPolicy, conf.getTopTermsPerCat());
				}
			}
		}
		
		Response response = Response.status(201).build();
		return response;
	}
	
	@DELETE
	public Response deleteAllConfigurations(){
		refHub.setConfigurationManager(new ConfigurationManager());
		Response response = Response.ok().build();
		return response;
	}
}
