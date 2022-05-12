package com.apextalos.cvitfusion.common.settings;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ConfigFileTest {

	@Test
	public void testLoad() {
		ConfigFile cf = new ConfigFile("");
		assertFalse(cf.load());
	}
}
