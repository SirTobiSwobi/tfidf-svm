package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.TermDocMap;

public class WordCounter extends Thread {
	private ReferenceHub refHub;
	private long[] relevantIds;
	private TermDocMap termDocMap;
	private int wordCounterId;
	private Model model;
	private Trainer trainer;
	
	public WordCounter(ReferenceHub refHub, long[] relevantIds, TermDocMap termDocMap, int wordCounterId, long modelId, Trainer trainer) {
		super();
		this.refHub = refHub;
		this.relevantIds = relevantIds;
		this.termDocMap = termDocMap;
		this.wordCounterId = wordCounterId;
		this.model=refHub.getModelManager().getModelByAddress(modelId);
		this.trainer=trainer;
	}
	
	public void run(){
		for(int i=0; i<relevantIds.length;i++){
			long docId = relevantIds[i];
			Document doc = refHub.getDocumentManager().getByAddress(docId);
			String text = doc.getLabel()+" "+doc.getContent();
			text = Utilities.sanitizeTextRemoveDigits(text);
			String[] words = text.split(" ");
			//String log="WordCounter "+wordCounterId+"counted terms for doc: "+docId+": "+words.length+" words.<br>";
			for(int j=0; j<words.length;j++){
				termDocMap.addTermForDoc(words[j], docId);
			}
			//model.appendToTrainingLog(log);
			model.incrementCompletedSteps();
		}
		trainer.awaitFeatureExtraction();
	}

}
