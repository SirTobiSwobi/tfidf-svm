package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.Model;
import org.SirTobiSwobi.c3.tfidfsvm.db.ModelManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.junit.Test;

public class ModelManagerTest {

	@Test
	public void test() {
		ConfigurationManager confMan =  new ConfigurationManager();
		Configuration cfgn = new Configuration(0,2, true, 0.5,SelectionPolicy.MicroaverageF1, 5);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(3,3, true, 0.6,SelectionPolicy.MacroaverageF1, 10);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(5,5, true, 0.4,SelectionPolicy.MicroaverageRecall, 15);
		confMan.setConfiguration(cfgn);
		
		Model model = new Model(0,confMan.getByAddress(0),null,0,null);
		ModelManager modMan = new ModelManager();
		
		ReferenceHub refHub = new ReferenceHub(null, null, null, confMan, modMan, null, null, null);
		modMan.setRefHub(refHub);
		
		modMan.setModel(model);
		modMan.addModelWithoutId(3);
		
		assertTrue("There are two models",modMan.getSize()==2);
		modMan.deleteModel(1);
		assertTrue("There is only one document",modMan.getSize()==1);
		assertTrue("First model has config-id 0",modMan.getModelByAddress(0).getConfigurationId()==0);
	}

}
