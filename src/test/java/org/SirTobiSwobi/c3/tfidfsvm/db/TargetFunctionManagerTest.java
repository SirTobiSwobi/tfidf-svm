package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.core.Utilities;
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
import org.SirTobiSwobi.c3.tfidfsvm.db.SearchDirection;
import org.SirTobiSwobi.c3.tfidfsvm.db.TargetFunctionManager;
import org.junit.Test;

public class TargetFunctionManagerTest {

	@Test
	public void test() {
		DocumentManager docMan = new DocumentManager();
		CategoryManager catMan = new CategoryManager(); 
		TargetFunctionManager tfMan = new TargetFunctionManager();
		ConfigurationManager confMan = new ConfigurationManager();
		ModelManager modMan = new ModelManager();
		CategorizationManager cznMan = new CategorizationManager();
		EvaluationManager evalMan = new EvaluationManager();
		ReferenceHub refHub = new ReferenceHub(catMan, docMan, tfMan, confMan, modMan, cznMan,evalMan, null);
		tfMan.setRefHub(refHub);
		cznMan.setRefHub(refHub);
		evalMan.setRefHub(refHub);
		
		Category category = new Category(0,"Diseases","");
		catMan.setCategory(category);
		category = new Category(4,"Neoplasms","");
		catMan.setCategory(category);
		category = new Category(557,"Neoplasms by Histologic Type","");
		catMan.setCategory(category);
		category = new Category(665,"Nevi and Melanomas","");
		catMan.setCategory(category);
		category = new Category(510,"Melanoma","");
		catMan.setCategory(category);
		category = new Category(385,"Hutchinson's Melanotic Freckle","");
		catMan.setCategory(category);
		category = new Category(515,"Melanoma, Amelanotic","");
		catMan.setCategory(category);
		category = new Category(525,"Melanoma, Experimental","");
		catMan.setCategory(category);
		category = new Category(2,"Virus Diseases","");
		catMan.setCategory(category);
		category = new Category(256,"DNA Virus Infections","");
		catMan.setCategory(category);
		category = new Category(466,"Herpesviridae Infections","");
		catMan.setCategory(category);
		category = new Category(382,"Herpes Simplex","");
		catMan.setCategory(category);
		category = new Category(465,"Keratitis, Herpetic","");
		catMan.setCategory(category);
		category = new Category(450,"Keratitis, Dendritic","");
		catMan.setCategory(category);
		
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
		
		assertTrue("Document 0 is assigned to category 4",tfMan.getDocumentAssignments(0)[0].getCategoryId()==4);
		assertTrue("Category 2 contains document 2",tfMan.getCategoryAssignments(2)[0].getDocumentId()==2);
		assertTrue("Document 3 is assigned to category 450",tfMan.getDocumentAssignments(3)[0].getCategoryId()==450);
		String foundCatIdsString="";
		long[] foundCatIds = tfMan.findAllImplicitCatIds(525,SearchDirection.Ascending);
		for(int i=0;i<foundCatIds.length;i++){
			foundCatIdsString=foundCatIdsString+" "+foundCatIds[i];
		}
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 2));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 0));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 256));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 510));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 665));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 557));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 4));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 525));
		assertTrue("Found cat ids: "+foundCatIdsString,!Utilities.isIn(foundCatIds, 1));
		
		foundCatIds = tfMan.getImplicitCatIdsForDocument(4);
		foundCatIdsString="";
		for(int i=0;i<foundCatIds.length;i++){
			foundCatIdsString=foundCatIdsString+" "+foundCatIds[i];
		}
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 2));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 0));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 256));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 510));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 665));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 557));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 4));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 525));
		assertTrue("Found cat ids: "+foundCatIdsString,!Utilities.isIn(foundCatIds, 1));
		
		foundCatIds = tfMan.getImplicitCatIdsForDocument(3);
		foundCatIdsString="";
		for(int i=0;i<foundCatIds.length;i++){
			foundCatIdsString=foundCatIdsString+" "+foundCatIds[i];
		}
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 465));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 382));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 466));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 256));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 2));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 0));
		assertTrue("Found cat ids: "+foundCatIdsString,Utilities.isIn(foundCatIds, 450));
		assertTrue("Found cat ids: "+foundCatIdsString,!Utilities.isIn(foundCatIds, 510));
		
		long[] foundDocIds = tfMan.getImplicitDocIdsForCategory(0); //category 0 should implicitly contain all documents because it is a root
		String foundDocIdsString="";
		for(int i=0;i<foundDocIds.length;i++){
			foundDocIdsString=foundDocIdsString+" "+foundDocIds[i];
		}
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 0));
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 1));
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 2));
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 3));
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 4));
		
		foundDocIds = tfMan.getImplicitDocIdsForCategory(510); //category 510 should implicitly contain documents 1, 3 and 4
		foundDocIdsString="";
		for(int i=0;i<foundDocIds.length;i++){
			foundDocIdsString=foundDocIdsString+" "+foundDocIds[i];
		}
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 1));
		assertTrue("Found docs are: "+foundDocIdsString,!Utilities.isIn(foundDocIds, 2));
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 3));
		assertTrue("Found docs are: "+foundDocIdsString,Utilities.isIn(foundDocIds, 4));
	}

}
