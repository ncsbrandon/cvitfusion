package com.apextalos.cvitfusioncommon;

import static org.junit.Assert.*;

import org.junit.Test;

import com.apextalos.cvitfusioncommon.settings.ConfigFile;

public class ConfigFileTest {

	@Test
	public void testLoad() {
		ConfigFile cf = new ConfigFile("");
		assertFalse(cf.load());
	}
}
