package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.Categorization;
import org.SirTobiSwobi.c3.tfidfsvm.db.CategorizationManager;
import org.junit.Test;

public class CategorizationManagerTest {

	@Test
	public void test() {
		
		CategorizationManager cznMan = new CategorizationManager();
		
		
		Categorization czn = new Categorization(0,0,0,.8,"");
		cznMan.setCategorization(czn);
		czn = new Categorization(1,3,2,.2,"");
		cznMan.setCategorization(czn);
		Categorization result = cznMan.getCategorizationByAddress(0);
		assertTrue("Categorization(0) has docId=0 ("+result.getDocumentId()+")and catId=0 ("+result.getCategoryId()+") and probability=0.8("+
				result.getProbability()+")",result.getDocumentId()==0&&result.getCategoryId()==0&&result.getProbability()==0.8);
		result = cznMan.getCategoryCategorizations(0)[0];
		assertTrue("Category Categorization(0) has docId=0 ("+result.getDocumentId()+")and catId=0 ("+result.getCategoryId()+") and probability=0.8("+
				result.getProbability()+")",result.getDocumentId()==0&&result.getCategoryId()==0&&result.getProbability()==0.8);
		result = cznMan.getDocumentCategorizations(0)[0];
		assertTrue("Document Categorization(0) has docId=0 ("+result.getDocumentId()+")and catId=0 ("+result.getCategoryId()+") and probability=0.8("+
				result.getProbability()+")",result.getDocumentId()==0&&result.getCategoryId()==0&&result.getProbability()==0.8);
		result = cznMan.getCategorizationByAddress(1);
		assertTrue("Document Categorization(1) has docId=3 ("+result.getDocumentId()+")and catId=2 ("+result.getCategoryId()+") and probability=0.2("+
				result.getProbability()+")",result.getDocumentId()==3&&result.getCategoryId()==2&&result.getProbability()==0.2);
		result = cznMan.getCategoryCategorizations(2)[0];
		assertTrue("Document Categorization(2) has docId=3 ("+result.getDocumentId()+")and catId=2 ("+result.getCategoryId()+") and probability=0.2("+
				result.getProbability()+")",result.getDocumentId()==3&&result.getCategoryId()==2&&result.getProbability()==0.2);
		cznMan.deleteCategorization(1);
		assertTrue("CategorizationManager doesn't contain categorization 1",!cznMan.containsCategorization(1));
		
	}

}
