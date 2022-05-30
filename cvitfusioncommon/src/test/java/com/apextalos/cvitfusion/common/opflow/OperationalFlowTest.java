package com.apextalos.cvitfusion.common.opflow;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

public class OperationalFlowTest {

	private final Logger logger = LogManager.getLogger(OperationalFlowTest.class.getSimpleName());
	
	@BeforeClass
	public static void init() throws Exception {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);
	}
	
	public OperationalFlow sample1() {
		OperationalFlow of = new OperationalFlow(
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new HashMap<>());
		
		Process n111 = new Process(111, true, 1, null, "", 4317, new Properties());
		Process n112 = new Process(112, true, 1, null, "", 4317, new Properties());
		Process n121 = new Process(121, true, 1, null, "", 4317, new Properties());
		Process n122 = new Process(122, true, 1, null, "", 4317, new Properties());
		Process n12 = new Process(12, true, 1, new ArrayList<>() {{add(n121); add(n122);}}, "", 420, new Properties());
		Process n11 = new Process(11, true, 1, new ArrayList<>() {{add(n111); add(n112);}}, "", 420, new Properties());
		Process n1 = new Process(1, true, 1, new ArrayList<>() {{add(n11); add(n12);}}, "", 69,  new Properties());
		
		of.getProcesses().add(n1);
		
		of.getTypes().add(new Type(1, 1, "Input", new Properties(), null, new ArrayList<>() {
			{
			add(Integer.valueOf(2));
			}
		}, true));
		of.getTypes().add(new Type(2, 1, "Logic", new Properties(), new ArrayList<>() {
			{
			add(Integer.valueOf(1));
			}
		}, new ArrayList<>() {
			{
			add(Integer.valueOf(3));
			}
		}, false));
		of.getTypes().add(new Type(3, 1, "Output", new Properties(), new ArrayList<>() {
			{
			add(Integer.valueOf(2));
			}
		}, null, false));
		
		of.getStyles().add(new Style(1, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, 20)));
		of.getStyles().add(new Style(2, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, 20)));
		of.getStyles().add(new Style(3, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, 20)));
		
		of.getTypeStyle().put(1, 1);
		of.getTypeStyle().put(2, 2);
		
		return of;
	}
	
	@Test
	public void testToJSON() {
		OperationalFlow of = sample1();
		assertNotNull(of);
		logger.debug(OperationalFlow.toJSON(of));
	}
}
