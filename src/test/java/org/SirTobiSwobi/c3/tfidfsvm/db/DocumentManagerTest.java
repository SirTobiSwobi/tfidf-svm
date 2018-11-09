package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.DocumentManager;
import org.junit.Test;

public class DocumentManagerTest {

	@Test
	public void test() {
		DocumentManager manager = new DocumentManager();
		manager.setDocument(new Document(0,"first label","first content"));
		manager.setDocument(new Document(1,"second label","second content"));
		manager.setDocument(new Document(2,"third label","third content"));
		long autoId=manager.addDocumentWithoutId("fourth label", "fourth content");
		manager.setDocument(new Document(200,"200th label","200th content"));
		manager.setDocument(new Document(199,"199th label","199th content"));
		manager.setDocument(new Document(198,"198th label","198th content"));
		long autoId2=manager.addDocumentWithoutId("201st label", "201st content");
		
		assertTrue("Document amount should be 3",manager.getSize()==8);
		assertTrue("Document(2) label should be third label",manager.getByAddress(2).getLabel().equals("third label"));
		assertTrue("Document(3) label should be fourth label",manager.getByAddress(3).getLabel().equals("fourth label"));
		assertTrue("Document(200) label should be 200th label",manager.getByAddress(200).getLabel().equals("200th label"));
		assertTrue("Document(201) content should be 200th content",manager.getByAddress(201).getContent().equals("201st content"));

	}

}
