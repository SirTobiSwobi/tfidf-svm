package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.AVLTree;
import org.junit.Test;

public class AVLTreeTest {

	@Test
	public void test() {
		
		AVLTree<String> tree = new AVLTree<String>();
		tree.setContent("44", 44);
		tree.setContent("17", 17);
		tree.setContent("88", 88);
		tree.setContent("32", 32);
		tree.setContent("65", 65);
		tree.setContent("97", 97);
		tree.setContent("28", 28);
		tree.setContent("54", 54);
		tree.setContent("82", 82);
		tree.setContent("29", 29);
		tree.setContent("76", 76);
		tree.setContent("80", 80);
		tree.setContent("78", 78);
		
		assertTrue("tree size is "+tree.getSize()+" it should be 13",tree.getSize()==13);
		assertTrue("tree balance is"+tree.getBalance(),true);
		testDelete(tree,97);
		assertTrue("tree size is "+tree.getSize()+" it should be 12",tree.getSize()==12);
		testDelete(tree,28);
		assertTrue("tree size is "+tree.getSize()+" it should be 11",tree.getSize()==11);
		testDelete(tree,32);
		assertTrue("tree size is "+tree.getSize()+" it should be 10",tree.getSize()==10);
		testDelete(tree,65);
		assertTrue("tree size is "+tree.getSize()+" it should be 9",tree.getSize()==9);
		testDelete(tree,44);
		assertTrue("tree size is "+tree.getSize()+" it should be 8",tree.getSize()==8);
		
		tree = new AVLTree<String>();
		tree.setContent("44", 44);
		tree.setContent("17", 17);
		tree.setContent("62", 62);
		tree.setContent("32", 32);
		tree.setContent("50", 50);
		tree.setContent("78", 78);
		tree.setContent("48", 48);
		tree.setContent("54", 54);
		tree.setContent("88", 88);
		
		
		tree = new AVLTree<String>();
		
		for(long i=0; i<1024; i++){
			tree.setContent(""+i, i);
		}
		assertTrue("tree height should be 10 but is "+tree.getHeight()+" root: "+tree.getRoot().getIndex(),tree.getHeight()==10);
		for(long i=255; i<1024; i++){
			tree.deleteNode(i);
		}
		assertTrue("tree height should be 9 but is "+tree.getHeight()+" root: "+tree.getRoot().getIndex(),tree.getHeight()==9);
		for(long i=255;i<2048; i++){
			tree.setContent(""+i, i);
		}
		assertTrue("tree height should be 11 but is "+tree.getHeight()+" root: "+tree.getRoot().getIndex(),tree.getHeight()==11);
		
		tree = new AVLTree<String>();
		tree.setContent("2", 2);
		tree.setContent("4",4);
		tree.setContent("1", 1);
		assertTrue("tree should not contain 3 but it does.", !tree.containsId(3));
		assertTrue("tree should contain 2 but it doesn't.", tree.containsId(2));
		assertTrue("tree should contain 4 but it doesn't.", tree.containsId(4));
		assertTrue("tree should contain 1 but it doesn't.", tree.containsId(1));
		assertTrue("tree should not contain 5 but it does.", !tree.containsId(5));
	}
	
	private void testDelete(AVLTree<String> tree, long id){
		assertTrue(id+" is still there",tree.getContent(id).equals(id+""));
		tree.deleteNode(id);
		assertTrue(id+" should be gone but is: "+tree.getContent(id)+")",tree.getContent(id)==null);
		
	}

}
