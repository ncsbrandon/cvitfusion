package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;

public class DiagramBuilderTest {

	public OperationalFlow sample1() {
		OperationalFlow of = new OperationalFlow(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());

		List<Parameter> parameters = new ArrayList<>();
		
		Process n111 = new Process(111, true, 1, null, "", new Properties());
		Process n112 = new Process(112, true, 1, null, "", new Properties());
		Process n121 = new Process(121, true, 1, null, "", new Properties());
		Process n122 = new Process(122, true, 1, null, "", new Properties());
		Process n12 = new Process(12, true, 1, new ArrayList<>() {
			{
				add(n121);
				add(n122);
			}
		}, "", new Properties());
		Process n11 = new Process(11, true, 1, new ArrayList<>() {
			{
				add(n111);
				add(n112);
			}
		}, "", new Properties());
		Process n1 = new Process(1, true, 1, new ArrayList<>() {
			{
				add(n11);
				add(n12);
			}
		}, "", new Properties());

		of.getProcesses().add(n1);

		of.getTypes().add(new Type(1, 1, "Input", parameters, null, true));
		of.getTypes().add(new Type(2, 1, "Logic", parameters, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
			}
		}, false));
		of.getTypes().add(new Type(3, 1, "Output", parameters, null, false));

		of.getStyles().add(new Style(1, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, .20)));
		of.getStyles().add(new Style(2, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, .20)));
		of.getStyles().add(new Style(3, 1, new Color(100, 100, 100, .20), new Color(100, 100, 100, .20)));

		of.getTypeStyleMap().put(1, 1);
		of.getTypeStyleMap().put(2, 2);

		return of;
	}

	@Test
	public void testGenerateNodes() {
		DiagramBuilder db = new DiagramBuilder();
		Assert.assertNotNull(db);
		
		// db.layout(sample1(), null);
	}

}
