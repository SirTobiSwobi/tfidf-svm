package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategoryManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.EvalCategory;
import org.SirTobiSwobi.c3.tfidfsvm.db.RelationshipType;
import org.junit.Test;

public class CategoryManagerTest {

	@Test
	public void test() {
		CategoryManager catMan = new CategoryManager();
		
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
		
		assertTrue("Diseases (0) has 2 subs ",catMan.getAllRelationshipsFrom(0).length==2);
		assertTrue("Melanoma (510) has 3 subs ",catMan.getAllRelationshipsFrom(510).length==3);
		assertTrue("Melanoma (510) has 1 from ",catMan.getAllRelationshipsTo(510).length==1);
		assertTrue("Melanoma (510) predecessor is Nevi and Melanomas (665)", catMan.getAllRelationshipsTo(510)[0].getFrom().getId()==665);
		long predecessorId = catMan.getAllRelationshipsTo(510)[0].getFrom().getId();
		assertTrue("Predecessor Id should be 665 ", predecessorId==665);
		assertTrue("Melanoma (510) predecessor Nevi and Melanomas has the right label", catMan.getByAddress(predecessorId).getLabel().equals("Nevi and Melanomas"));
		
		long deleteId = catMan.getAllRelationshipsTo(450)[0].getId();
		catMan.deleteRelationship(deleteId);
		assertTrue("There is no relationship between 465 and 450",catMan.getAllRelationshipsTo(450).length==0&&catMan.getAllRelationshipsFrom(465).length==0);
		assertTrue("The deleted Relationship is gone",catMan.getByAddress(deleteId)==null);
		
		catMan.deleteCategory(0);
		assertTrue("Relationship 1 and 8 shoudl be gone but are still there",catMan.getRelationshipByAddress(1)==null&&catMan.getRelationshipByAddress(8)==null);
		
		EvalCategory evalCat = new EvalCategory(1000, "Eval Cat Label", "Eval Cat description. (It's no evil cat ;-))");
		evalCat.setTP(5);
		catMan.setCategory(evalCat);
		EvalCategory newCat = (EvalCategory) catMan.getByAddress(1000);
		assertTrue("Category 1000 is an evalCat and has TP=5", newCat.getId()==1000&&newCat.getTP()==5);
		
		
	}

}
