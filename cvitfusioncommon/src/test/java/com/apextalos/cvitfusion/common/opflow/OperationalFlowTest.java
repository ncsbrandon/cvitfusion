package com.apextalos.cvitfusion.common.opflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		
		Process n111 = new Process(111, true, 3, null, new Properties());
		Process n112 = new Process(112, true, 3, null, new Properties());
		Process n121 = new Process(121, true, 3, null, new Properties());
		Process n122 = new Process(122, true, 3, null, new Properties());
		Process n12 = new Process(12, true, 2, new ArrayList<>() {{add(n121); add(n122);}}, new Properties());
		Process n11 = new Process(11, true, 2, new ArrayList<>() {{add(n111); add(n112);}}, new Properties());
		Process n1 = new Process(1, true, 1, new ArrayList<>() {{add(n11); add(n12);}}, new Properties());
		
		of.getProcesses().add(n1);
		
		List<Parameter> parameters = new ArrayList<>();
		
		of.getTypes().add(new Type(1, 1, "Input", parameters, new ArrayList<>() {
			{
			add(Integer.valueOf(2));
			}
		}, true));
		of.getTypes().add(new Type(2, 1, "Logic", parameters, new ArrayList<>() {
			{
			add(Integer.valueOf(3));
			}
		}, false));
		of.getTypes().add(new Type(3, 1, "Output", parameters, null, false));
		
		of.getStyles().add(new Style(1, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, 20)));
		of.getStyles().add(new Style(2, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, 20)));
		of.getStyles().add(new Style(3, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, 20)));
		
		of.getTypeStyleMap().put(1, 1);
		of.getTypeStyleMap().put(2, 2);
		
		return of;
	}
	
	//@Test
	public void testToJSON() {
		ObjectMapper mapper = new ObjectMapper();

		OperationalFlow of1 = sample1();
		assertNotNull(of1);
		
		String json = "";
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(of1);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assertNotNull(json);
		logger.debug(json);

		OperationalFlow of2 = null;
		try {
			of2 = mapper.readValue(json, OperationalFlow.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assertNotNull(of2);
	}
	
	@Test
	public void processLookupRemoveTest() {
		OperationalFlow of1 = sample1();
		System.out.println("1------");
		dumpProcs(of1.getProcesses(), 0);
		
		Process n111 = of1.lookupProcess(111);
		of1.removeProcess(n111);
		System.out.println("2------");
		dumpProcs(of1.getProcesses(), 0);
		
		Process n11 = of1.lookupProcess(11);
		of1.removeProcess(n11);
		System.out.println("3------");
		dumpProcs(of1.getProcesses(), 0);
		
		Process n12 = of1.lookupProcess(12);
		of1.removeProcess(n12);
		System.out.println("4------");
		dumpProcs(of1.getProcesses(), 0);
		
		Process n1 = of1.lookupProcess(1);
		of1.removeProcess(n1);
		System.out.println("5------");
		dumpProcs(of1.getProcesses(), 0);
		
		assertEquals(0, of1.getProcesses().size());
	}
	
	private void dumpProcs(List<Process> procs, int indent) {
		procs.forEach(x -> dumpProc(x, indent));
	}
	
	private void dumpProc(Process proc, int indent) {
		for(int i=0; i<indent; i++)
			System.out.print("    ");
		
		System.out.println(String.format("ID %d: en[%b] children[%b] type[%d] ", proc.getProcessID(), proc.isEnabled(), proc.hasChildren(), proc.getTypeID()));
		
		List<Process> procs = proc.getChildren();
		if(procs != null)
			dumpProcs(proc.getChildren(), indent+1);
	}
}
