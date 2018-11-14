package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.junit.Test;

import libsvm.svm_parameter;

public class ConfigurationManagerTest {

	@Test
	public void test() {
		
		svm_parameter param = new svm_parameter();
		
		param.svm_type = svm_parameter.C_SVC;
		//param.kernel_type = svm_parameter.RBF;
		param.kernel_type = svm_parameter.LINEAR;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		ConfigurationManager confMan =  new ConfigurationManager();
		Configuration cfgn = new Configuration(0,2, true, 0.5,SelectionPolicy.MicroaverageF1, 5, param);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(3,3, true, 0.6,SelectionPolicy.MacroaverageF1, 10, param);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(5,5, true, 0.4,SelectionPolicy.MicroaverageRecall, 15, param);
		confMan.setConfiguration(cfgn);
		
		assertTrue("There are 3 configs",confMan.getSize()==3);
		confMan.deleteConfiguration(1);
		assertTrue("There are only two documents",confMan.getSize()==2);
		assertTrue("First config has id 0",confMan.getByAddress(0).getId()==0);
	}

}
