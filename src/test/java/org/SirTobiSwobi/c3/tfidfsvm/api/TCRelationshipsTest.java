package org.SirTobiSwobi.c3.tfidfsvm.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCRelationship;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCRelationships;
import org.SirTobiSwobi.c3.tfidfsvm.db.Category;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategoryManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.RelationshipType;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TCRelationshipsTest {
	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		
		CategoryManager catMan =  new CategoryManager();
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
		
		TCRelationship[] manualArray = new TCRelationship[13];		
		catMan.addRelatonshipWithoutId(0, 4, RelationshipType.Sub);
		manualArray[0]=new TCRelationship(1, 0, 4, "Sub");
		catMan.addRelatonshipWithoutId(4, 557, RelationshipType.Sub);
		manualArray[1]=new TCRelationship(2, 4, 557, "Sub");
		catMan.addRelatonshipWithoutId(557, 665, RelationshipType.Sub);
		manualArray[2]=new TCRelationship(3, 557, 665, "Sub");
		catMan.addRelatonshipWithoutId(665, 510, RelationshipType.Sub);
		manualArray[3]=new TCRelationship(4, 665, 510, "Sub");
		catMan.addRelatonshipWithoutId(510, 385, RelationshipType.Sub);
		manualArray[4]=new TCRelationship(5, 510, 385, "Sub");
		catMan.addRelatonshipWithoutId(510, 515, RelationshipType.Sub);
		manualArray[5]=new TCRelationship(6, 510, 515, "Sub");
		catMan.addRelatonshipWithoutId(510, 525, RelationshipType.Sub);
		manualArray[6]=new TCRelationship(7, 510, 525, "Sub");
		catMan.addRelatonshipWithoutId(0, 2, RelationshipType.Sub);
		manualArray[7]=new TCRelationship(8, 0, 2, "Sub");
		catMan.addRelatonshipWithoutId(2, 256, RelationshipType.Sub);
		manualArray[8]=new TCRelationship(9, 2, 256, "Sub");
		catMan.addRelatonshipWithoutId(256, 466, RelationshipType.Sub);
		manualArray[9]=new TCRelationship(10, 256, 466, "Sub");
		catMan.addRelatonshipWithoutId(466, 382, RelationshipType.Sub);
		manualArray[10]=new TCRelationship(11, 466, 382, "Sub");
		catMan.addRelatonshipWithoutId(382, 465, RelationshipType.Sub);
		manualArray[11]=new TCRelationship(12, 382, 465, "Sub");
		catMan.addRelatonshipWithoutId(465, 450, RelationshipType.Sub);
		manualArray[12]=new TCRelationship(13, 465, 450, "Sub");
	
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCRelationships.json"), TCRelationships.class));
		TCRelationships TCrelationships = new TCRelationships(manualArray);
		
		assertThat(MAPPER.writeValueAsString(TCrelationships)).isEqualTo(expected);
		
	}

}
