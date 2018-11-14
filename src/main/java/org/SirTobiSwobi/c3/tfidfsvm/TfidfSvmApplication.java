package org.SirTobiSwobi.c3.tfidfsvm;

import javax.ws.rs.client.Client;

import org.SirTobiSwobi.c3.tfidfsvm.core.Classifier;
import org.SirTobiSwobi.c3.tfidfsvm.core.Trainer;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategorizationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategoryManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.DocumentManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.EvaluationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ModelManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.RelationshipType;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.SirTobiSwobi.c3.tfidfsvm.db.TargetFunctionManager;
import org.SirTobiSwobi.c3.tfidfsvm.health.ConfigHealthCheck;
import org.SirTobiSwobi.c3.tfidfsvm.resources.ActiveModelResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.AssignmentResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.CategoriesResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.CategorizationsResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.CategoryResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.ConfigurationResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.ConfigurationsResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.DocumentResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.DocumentsResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.EvaluationsResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.MetadataResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.ModelResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.ModelsResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.RelationshipResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.RelationshipsResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.RetrainingResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.TargetFunctionResource;
import org.SirTobiSwobi.c3.tfidfsvm.resources.TrainingSessionResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import libsvm.svm_parameter;

public class TfidfSvmApplication extends Application<TfidfSvmConfiguration> {

	public static void main(String[] args) throws Exception {
		new TfidfSvmApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "classifier-trainer";
	}
	
	@Override
	public void initialize(Bootstrap<TfidfSvmConfiguration> bootstrap){
	
		bootstrap.addBundle(new AssetsBundle("/assets/", "/html/", "index.html"));
	}

	@Override
	public void run(TfidfSvmConfiguration configuration, Environment environment){
		/*
		 * Initializing data structures
		 */
		
		DocumentManager docMan = new DocumentManager();
		CategoryManager catMan = new CategoryManager(); 
		TargetFunctionManager tfMan = new TargetFunctionManager();
		ConfigurationManager confMan = new ConfigurationManager();
		ModelManager modMan = new ModelManager();
		CategorizationManager cznMan = new CategorizationManager();
		EvaluationManager evalMan = new EvaluationManager();
		Model activeModel = null;
		ReferenceHub refHub = new ReferenceHub(catMan, docMan, tfMan, confMan, modMan, cznMan, evalMan, activeModel);
		tfMan.setRefHub(refHub);
		cznMan.setRefHub(refHub);
		evalMan.setRefHub(refHub);
		modMan.setRefHub(refHub);
		
		/*
		 * Initializing trainer and classifier (for also implementing the athlete API)
		 */
		
		Trainer trainer = new Trainer(refHub);
		trainer.setFeatureExtractionThreads(Integer.parseInt(configuration.getFeatureExtractionThreads()));
		Classifier classifier = new Classifier(refHub);
		
		/*
		 * Initializing HTTP client
		 */
		
		final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration()).build(getName());
		
		/*
		 * Initializing resources requiring data structures and clients. 
		 */
	
		final MetadataResource metadata = new MetadataResource(configuration);
		final DocumentsResource documents = new DocumentsResource(refHub,client);
		final DocumentResource document = new DocumentResource(refHub,client);
		final CategoriesResource categories = new CategoriesResource(refHub);
		final CategoryResource category = new CategoryResource(refHub);
		final RelationshipsResource relationships = new RelationshipsResource(refHub);
		final RelationshipResource relationship = new RelationshipResource(refHub);
		final ActiveModelResource activeModelResource = new ActiveModelResource(refHub, client);
		final CategorizationsResource categorizations = new CategorizationsResource(refHub, client, classifier);
		final RetrainingResource retraining = new RetrainingResource(refHub);
		
		if(configuration.getRunType().equals("trainer")){
			final TargetFunctionResource targetFunction = new TargetFunctionResource(refHub);
			final AssignmentResource assignment = new AssignmentResource(refHub);
			final ConfigurationsResource configurations = new ConfigurationsResource(refHub);
			final ConfigurationResource configurationR = new ConfigurationResource(refHub);
			final ModelsResource models = new ModelsResource(refHub, trainer);
			final ModelResource model = new ModelResource(refHub);
			final EvaluationsResource evaluations = new EvaluationsResource(refHub);
			final TrainingSessionResource trainingSession = new TrainingSessionResource(refHub);
			
			environment.jersey().register(targetFunction);
			environment.jersey().register(assignment);
			environment.jersey().register(configurations);
			environment.jersey().register(configurationR);
			environment.jersey().register(models);
			environment.jersey().register(model);
			environment.jersey().register(evaluations);
			environment.jersey().register(trainingSession);
		}
	
		
		
		/*
		 * Initializing health checks
		 */
		
		final ConfigHealthCheck configHealth = new ConfigHealthCheck(configuration);
		
		/*
		 * Registering everything 
		 */
		
		environment.healthChecks().register("config", configHealth);
		environment.jersey().register(metadata);
		environment.jersey().register(documents);
		environment.jersey().register(document);
		environment.jersey().register(categories);
		environment.jersey().register(category);
		environment.jersey().register(relationships);
		environment.jersey().register(relationship);
		environment.jersey().register(activeModelResource);
		environment.jersey().register(categorizations);
		environment.jersey().register(retraining);
		
		svm_parameter param = new svm_parameter();
		
		param.svm_type = svm_parameter.C_SVC;
		//param.kernel_type = svm_parameter.RBF;
		param.kernel_type = svm_parameter.LINEAR;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		
		Configuration cfgn = new Configuration(1,3, true, 0.5,SelectionPolicy.MicroaverageF1, 20, param);
		confMan.setConfiguration(cfgn);
		
		
		if(configuration.getDebugExamples().equals("true")){
			/*
			 * Generating example data for manual testing during development
			 */
			Category cat = new Category(0,"Diseases","");
			catMan.setCategory(cat);
			cat = new Category(4,"Neoplasms","");
			catMan.setCategory(cat);
			cat = new Category(557,"Neoplasms by Histologic Type","");
			catMan.setCategory(cat);
			cat = new Category(665,"Nevi and Melanomas","");
			catMan.setCategory(cat);
			cat = new Category(510,"Melanoma","");
			catMan.setCategory(cat);
			cat = new Category(385,"Hutchinson's Melanotic Freckle","");
			catMan.setCategory(cat);
			cat = new Category(515,"Melanoma, Amelanotic","");
			catMan.setCategory(cat);
			cat = new Category(525,"Melanoma, Experimental","");
			catMan.setCategory(cat);
			cat = new Category(2,"Virus Diseases","");
			catMan.setCategory(cat);
			cat = new Category(256,"DNA Virus Infections","");
			catMan.setCategory(cat);
			cat = new Category(466,"Herpesviridae Infections","");
			catMan.setCategory(cat);
			cat = new Category(382,"Herpes Simplex","");
			catMan.setCategory(cat);
			cat = new Category(465,"Keratitis, Herpetic","");
			catMan.setCategory(cat);
			cat = new Category(450,"Keratitis, Dendritic","");
			catMan.setCategory(cat);
			
			catMan.addRelatonshipWithoutId(0, 4, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(4, 557, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(557, 665, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(665, 510, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(510, 385, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(510, 515, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(510, 525, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(0, 2, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(2, 256, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(256, 466, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(466, 382, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(382, 465, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(465, 450, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(525, 256, RelationshipType.Equality);
			
			docMan.setDocument(new Document(0,"Neoplasms document label","first content"));
			docMan.setDocument(new Document(1,"Melanoma document label","second content"));
			docMan.setDocument(new Document(2,"Virus diseases document label","third content"));
			docMan.setDocument(new Document(3,"Keratitis, Dendritic document label","200th content"));
			docMan.setDocument(new Document(4,"525 document","Melanoma, Experimental and other stuff"));
			
			tfMan.setAssignment(0, 0, 4);
			tfMan.setAssignment(1, 1, 510);
			tfMan.setAssignment(2, 2, 2);
			tfMan.setAssignment(3, 3, 450);
			tfMan.setAssignment(4, 4, 525);
			
		
		}
	
		
		
		
		
		
	}

}
