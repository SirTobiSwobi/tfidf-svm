package org.SirTobiSwobi.c3.tfidfsvm.core;

import org.SirTobiSwobi.c3.tfidfsvm.db.Model;

public interface FeatureExtractor {
	public double[] getVector(long docId);
	public double[] getVector(String text);
	public Model getModel();
	public void setModel(Model model);

}
