package org.SirTobiSwobi.c3.tfidfsvm.db;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.tfidfsvm.db.Configuration;
import org.SirTobiSwobi.c3.tfidfsvm.db.ConfigurationManager;
import org.SirTobiSwobi.c3.tfidfsvm.db.SelectionPolicy;
import org.junit.Test;

public class ConfigurationManagerTest {

	@Test
	public void test() {
		ConfigurationManager confMan =  new ConfigurationManager();
		Configuration cfgn = new Configuration(0,2, true, 0.5,SelectionPolicy.MicroaverageF1, 5);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(3,3, true, 0.6,SelectionPolicy.MacroaverageF1, 10);
		confMan.setConfiguration(cfgn);
		cfgn = new Configuration(5,5, true, 0.4,SelectionPolicy.MicroaverageRecall, 15);
		confMan.setConfiguration(cfgn);
		
		assertTrue("There are 3 configs",confMan.getSize()==3);
		confMan.deleteConfiguration(1);
		assertTrue("There are only two documents",confMan.getSize()==2);
		assertTrue("First config has id 0",confMan.getByAddress(0).getId()==0);
	}

}
