package org.SirTobiSwobi.c3.tfidfsvm.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.SirTobiSwobi.c3.tfidfsvm.TfidfSvmConfiguration;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCMetadata;

@Path("/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class MetadataResource {
	private TfidfSvmConfiguration configuration;
	
	public MetadataResource(){
		super();
	}
	
	public MetadataResource(TfidfSvmConfiguration configuration){
		this.configuration = configuration;
	}
	
	@GET
    @Timed
	public TCMetadata getMetadata(){
		TCMetadata metadata = new TCMetadata(
				configuration.getName(),
				configuration.getCalls(),
				configuration.getAlgorithm(),
				configuration.getPhases(),
				configuration.getAlgorithm(),
				configuration.getConfiguration(),
				configuration.getConfigOptions(),
				configuration.getArchetype(),
				configuration.getRunType(),
				configuration.getDebugExamples()
				);
		
		return metadata;
		
	}
	
}
