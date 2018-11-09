package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;

public class Classifier {
	private ReferenceHub refHub;

	public Classifier(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	public boolean categorizeDocument(long docId){
		//spawn classification thread;
		boolean categorizationPossible=false;
		if(refHub.getDocumentManager().containsDocument(docId)&&refHub.getCategoryManager().getSize()>0){
			categorizationPossible=true;
		}
		if(categorizationPossible){
			new CategorizationThread(refHub,docId).run();
		}
		return categorizationPossible;
	}
}
