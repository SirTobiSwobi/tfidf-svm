package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

import libsvm.svm_parameter;

public class ConfigurationManager {
	
	AVLTree<Configuration> configurations;

	public ConfigurationManager() {
		configurations = new AVLTree<Configuration>();
	}
	
	public long getSize(){
		return configurations.getSize();
	}
	
	public Configuration getByAddress(long address){	
		return configurations.getContent(address);
	}
	
	public void setConfiguration(Configuration configuration){
		configurations.setContent(configuration, configuration.getId());
	}
	
	/**
	 * This depends on the type of classifier trainer that is implemented. The parameter is not used in this API sample implementation. 
	 */
	public synchronized void addConfigurationWithoutId(int folds, boolean includeImplicits, double assignmentThreshold,
			SelectionPolicy selectionPolicy, int topTermsPerCat, svm_parameter svmParam){
		long id = configurations.getMaxId()+1;
		Configuration conf = new Configuration(id,folds, includeImplicits, assignmentThreshold, selectionPolicy, topTermsPerCat, svmParam);
		configurations.setContent(conf,id);
	}
	
	public synchronized void deleteConfiguration(long id){
		if(this.configurations.getSize()>0){ //Otherwise null pointer exceptions can occur if there are categories but no relationships.
			configurations.deleteNode(id);
		}				
	}
	
	public Configuration[] getConfigurationArray(){
		ArrayList<Configuration> allContent = configurations.toArrayList();
		Configuration[] confArray = allContent.toArray(new Configuration[0]);
		return confArray;
	}
	
	public boolean containsConfiguration(long id){
		return configurations.containsId(id);
	}
	
	public String getConfigurationHash(){
		byte[] contentHash = configurations.getContentHash();
		String result="";
		for(int i=0; i<contentHash.length;i++){
			result = result + Integer.toHexString(contentHash[i] & 255);
		}
		return result;
	}
	
	
}
