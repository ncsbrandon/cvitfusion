package com.apextalos.cvitfusion.common.opflow;

import static org.junit.Assert.*;

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
	
	@Test
	public void test() {
		OperationalFlow of = new OperationalFlow(
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new HashMap<>());
		
		Node n3 = new Node(3, "email",  true, 1, null, "", 4317, new Properties());
		Node n2 = new Node(2, "queue",  true, 1, new ArrayList<>() {{add(n3);}}, "", 420, new Properties());
		Node n1 = new Node(1, "camera", true, 1, new ArrayList<>() {{add(n2);}}, "", 69,  new Properties());
		
		of.getNodes().add(n1);
		
		of.getTypes().add(new Type(1, 1, "Input", new Properties(), null, new ArrayList<>() {
			{
			add(Integer.valueOf(2));
			}
		}));
		of.getTypes().add(new Type(2, 1, "Logic", new Properties(), new ArrayList<>() {
			{
			add(Integer.valueOf(1));
			}
		}, new ArrayList<>() {
			{
			add(Integer.valueOf(3));
			}
		}));
		of.getTypes().add(new Type(3, 1, "Output", new Properties(), new ArrayList<>() {
			{
			add(Integer.valueOf(2));
			}
		}, null));
		
		of.getStyles().add(new Style(1, 1, new Color(100, 100, 100, 20), new Color(100, 100, 100, 20)));
		of.getStyles().add(new Style(2, 1, new Color(100, 100, 100, 20), new Color(100, 100, 100, 20)));
		of.getStyles().add(new Style(3, 1, new Color(100, 100, 100, 20), new Color(100, 100, 100, 20)));
		
		of.getTypeStyle().put(1, 1);
		of.getTypeStyle().put(2, 2);
		
		logger.debug(of.toJSON());
	}

}
