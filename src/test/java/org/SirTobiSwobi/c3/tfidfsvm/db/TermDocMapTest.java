package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.TermDocMap;
import org.junit.Test;

public class TermDocMapTest {

	@Test
	public void test() {
		TermDocMap tdm = new TermDocMap();
		tdm.addTermForDoc("first", 0);
		tdm.addTermForDoc("second",0);
		tdm.addTermForDoc("first", 1);
		tdm.computeDocumentFrequency();
		assertTrue("First term should be first",tdm.getTerms().get(0).equals("first"));
		assertTrue("Second term should be second", tdm.getTerms().get(1).equals("second"));
		assertTrue("There should be two terms",tdm.getTerms().size()==2);
		assertTrue("Document Frequency of first should be 2",tdm.getDocumentFrequency().get(0)==2);
		assertTrue("Document Frequency of second should be 1",tdm.getDocumentFrequency().get(1)==1);
		tdm.addTermForDoc("first", 0);
		tdm.computeDocumentFrequency();
		assertTrue("Document Frequency of first should still be 2",tdm.getDocumentFrequency().get(0)==2);
		long firstIndex=tdm.getTerms().indexOf("first");
		assertTrue("Index of first is 0",firstIndex==0);
		assertTrue("The term first should occur two times on document 0",tdm.getWordOccurences().getContent(0).getContent(firstIndex)==2);
		assertTrue("Index of second = 1",tdm.getTerms().indexOf("second")==1);
		assertTrue("Document 1 should not include the term second",!tdm.getWordOccurences().getContent(1).containsId(tdm.getTerms().indexOf("second")));
		tdm.computeTfidf(null);
		assertTrue("Document 0, term 0 should have tfidf 0",tdm.getTfidf().getContent(0).getContent(0)==0.0);
		assertTrue("Document 0, term 1 should have tfidf of log(2)",tdm.getTfidf().getContent(0).getContent(1)==Math.log(2));
		tdm.addTermForDoc("first", 3);
		tdm.computeDocumentFrequency();
		assertTrue("Document Frequency of first should still be 3",tdm.getDocumentFrequency().get(0)==3);
		assertTrue("Sum Dimension Square of 1 should be log(2)^2",tdm.getSumDimensionSquares().getContent(1)==Math.log(2)*Math.log(2));
		
		tdm = new TermDocMap();
		String text = "the text of the zero index document";
		String[] words = text.split(" ");
		for(int i=0; i<words.length;i++){
			tdm.addTermForDoc(words[i],0);
		}
		text= "the quick brown fox jumps over the lazy rabbit";
		words = text.split(" ");
		for(int i=0; i<words.length;i++){
			tdm.addTermForDoc(words[i],1);
		}
		text = "coming up with arbitrary text is annoying";
		words = text.split(" ");
		for(int i=0; i<words.length;i++){
			tdm.addTermForDoc(words[i],2);
		}
		tdm.computeDocumentFrequency();
		tdm.computeTfidf(null);
		/*
		fail("\n term 0: "+tdm.getTerms().get(0)+" docFreq: "+tdm.getDocumentFrequency().get(0)+" tfidf(d0) "+tdm.getTfidf().getContent(0).getContent(0)+
				" tfidf(d1) "+tdm.getTfidf().getContent(1).getContent(0)+" sumSquares: "+tdm.getSumDimensionSquares().getContent(0));*/
		
	}

}
