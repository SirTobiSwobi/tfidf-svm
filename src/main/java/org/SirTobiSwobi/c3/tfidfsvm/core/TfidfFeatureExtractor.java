package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.Document;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

public class TfidfFeatureExtractor implements FeatureExtractor{
	private ReferenceHub refHub;
	private Model model;

	public TfidfFeatureExtractor(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
		this.model = refHub.getActiveModel();
	}

	public TfidfFeatureExtractor(ReferenceHub refHub, Model model) {
		super();
		this.refHub = refHub;
		this.model = model;
	}

	public double[] getVector(long docId) {
		Document doc = refHub.getDocumentManager().getByAddress(docId);
		String text = doc.getLabel()+" "+doc.getContent();
		return getVector(text);
	}

	public double[] getVector(String text) {
		int[] occurences = new int[model.getControlledVocabulary().length];
		double[] vector = new double[model.getControlledVocabulary().length];
		text = Utilities.sanitizeTextRemoveDigits(text);
		String[] words = text.split(" ");
		for(int i=0;i<words.length;i++){
			int termId=model.getTermId(words[i]);
			if(termId!=-1){
				occurences[termId]++;
			}
		}
		for(int i=0;i<vector.length;i++){
			vector[i]=(double)occurences[i]*Math.log(model.getTrainingSetSize()/((double)model.getControlledVocabulary()[i].getDocumentFrequency()+1.0)); 
		}
		return vector;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model=model;
	}
	
	
	
	
	
}
