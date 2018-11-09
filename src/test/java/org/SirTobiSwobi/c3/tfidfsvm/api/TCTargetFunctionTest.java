package org.SirTobiSwobi.c3.tfidfsvm.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCTargetFunction;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategorizationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategoryManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.DocumentManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.EvaluationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.ModelManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.RelationshipType;
import org.SirTobiSwobi.c3.tfidfsvm.db.TargetFunctionManager;
import org.SirTobiSwobi.c3.tfidfsvm.resources.TargetFunctionResource;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TCTargetFunctionTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
	
	@Test
	public void test() throws Exception{
		DocumentManager docMan = new DocumentManager();
		CategoryManager catMan = new CategoryManager(); 
		TargetFunctionManager tfMan = new TargetFunctionManager();
		ConfigurationManager confMan = new ConfigurationManager();
		ModelManager modMan = new ModelManager();
		CategorizationManager cznMan = new CategorizationManager();
		EvaluationManager evalMan = new EvaluationManager();
		ReferenceHub refHub = new ReferenceHub(catMan, docMan, tfMan, confMan, modMan, cznMan, evalMan, null);
		tfMan.setRefHub(refHub);
		cznMan.setRefHub(refHub);
		evalMan.setRefHub(refHub);
		
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
		

		final TargetFunctionResource targetFunction = new TargetFunctionResource(refHub);
		
		Response result = targetFunction.getTargetfunction("0", "0");
		TCTargetFunction generated = (TCTargetFunction) result.getEntity();
		
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCTargetFunction.json"), TCTargetFunction.class));
		assertTrue("Generated and expected JSON are not equal",MAPPER.writeValueAsString(generated).equals(expected));
	}

}
