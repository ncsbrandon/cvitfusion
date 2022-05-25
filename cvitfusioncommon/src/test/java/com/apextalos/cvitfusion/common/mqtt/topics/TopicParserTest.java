package com.apextalos.cvitfusion.common.mqtt.topics;

import static org.junit.Assert.*;

import org.junit.Test;

public class TopicParserTest {

	@Test
	public void testMatch() {
		assertTrue(TopicParser.match("/apextalos/cvitfusion/status/#", "/apextalos/cvitfusion/status/87689768757685"));
		assertTrue(TopicParser.match("/apextalos/cvitfusion/status/87689768757685", "/apextalos/cvitfusion/status/#"));
		assertTrue(TopicParser.match("/apextalos/cvitfusion/status/#", "/apextalos/cvitfusion/status/#"));
		assertTrue(TopicParser.match("/apextalos/cvitfusion/status/", "/apextalos/cvitfusion/status/"));
		
		assertFalse(TopicParser.match("/apextalos/cvitfusion/status1", "/apextalos/cvitfusion/status2"));
	}

}
