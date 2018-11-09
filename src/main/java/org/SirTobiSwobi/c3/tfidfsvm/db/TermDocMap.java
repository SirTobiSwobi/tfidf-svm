package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;


public class TermDocMap {
	private ArrayList<String> terms;
	private ArrayList<Integer> documentFrequency;
	private AVLTree<AVLTree<Integer>> wordOccurences; //outer AVLTree with documents as index, inner AVLTree with terms as indices
	private AVLTree<AVLTree<Double>> tfidf;
	private ArrayList<Long> docIds;
	
	public TermDocMap(){
		terms = new ArrayList<String>();
		documentFrequency = new ArrayList<Integer>();
		wordOccurences = new AVLTree<AVLTree<Integer>>();
		docIds = new ArrayList<Long>();
	}
	
	public synchronized void addTermForDoc(String term, long docId){
		long termId;
		if(!docIds.contains(docId)){
			docIds.add(docId);
		}
		if(!terms.contains(term)){	//term is new
			terms.add(term);
			termId=terms.indexOf(term);
			//documentFrequency.add(1); //indices between terms and docFreq are equal. It occurs in at least one document.
			if(!wordOccurences.containsId(docId)){ //term is new and document is new
				AVLTree<Integer> occurence = new AVLTree<Integer>();
				occurence.setContent(1, termId);
				wordOccurences.setContent(occurence, docId);
			}else{	//term is new but document already exists
				wordOccurences.getContent(docId).setContent(1, termId);
			}
		}else{	//term is not new
			termId=terms.indexOf(term);
			if(!wordOccurences.containsId(docId)){ //term is not new and document is new: Must increase overall docFreq
				AVLTree<Integer> occurence = new AVLTree<Integer>();
				occurence.setContent(1, termId);
				wordOccurences.setContent(occurence, docId);
				//documentFrequency.set((int)termId, documentFrequency.get((int)termId)+1);
			}else{ //term is not new and the document already exists
				if(!wordOccurences.getContent(docId).containsId(termId)){//term is not new, the document already exists and the term does not exist within the document
					wordOccurences.getContent(docId).setContent(1, termId);
				}else{ //term is not new, the document already exists, the term already exists within the document.
					wordOccurences.getContent(docId).setContent(wordOccurences.getContent(docId).getContent(termId)+1, termId);
				}
			}
		}
	}
	
	public synchronized void computeDocumentFrequency(){
		documentFrequency = new ArrayList<Integer>();
		for(int i=0; i<terms.size(); i++){
			for(int j=0; j<docIds.size(); j++){
				long docId=docIds.get(j);
				if(wordOccurences.getContent(docId).containsId(i)){
					if(documentFrequency.size()<=i){
						documentFrequency.add(1);
					}else{
						documentFrequency.set(i, documentFrequency.get(i)+1);
					}
				}
			}
		}
	}
	
	public synchronized void computeTfidf(Model model){
		tfidf = new AVLTree<AVLTree<Double>>();
		for(int i=0; i<docIds.size(); i++){
			long docId = docIds.get(i);
			ArrayList<Long> termIds = wordOccurences.getContent(docId).getUsedIds();
			AVLTree<Double> tfidfVals = new AVLTree<Double>();
			for(int j=0; j<termIds.size(); j++){
				long termId = termIds.get(j);
				double tfidfVal=(double)wordOccurences.getContent(docId).getContent(termId)*Math.log((double)docIds.size()/(double)documentFrequency.get((int)termId));
				tfidfVals.setContent(tfidfVal, termId);
			}
			tfidf.setContent(tfidfVals, docId);
			if(model!=null){
				model.appendToTrainingLog("Computed TFIDF for document "+docId);
				model.incrementCompletedSteps();
			}	
		}
	}

	public ArrayList<String> getTerms() {
		return terms;
	}

	public ArrayList<Integer> getDocumentFrequency() {
		return documentFrequency;
	}

	public AVLTree<AVLTree<Integer>> getWordOccurences() {
		return wordOccurences;
	}

	public AVLTree<AVLTree<Double>> getTfidf() {
		return tfidf;
	}

	public ArrayList<Long> getDocIds() {
		return docIds;
	}	
	
	

}
