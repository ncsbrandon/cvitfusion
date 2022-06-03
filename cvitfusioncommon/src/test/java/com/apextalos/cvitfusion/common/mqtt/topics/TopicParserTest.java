package com.apextalos.cvitfusion.common.mqtt.topics;

import static org.junit.Assert.*;

import org.junit.Test;

public class TopicParserTest {

	@Test
	public void testMatch() {
		assertTrue(TopicParser.match("/topic/status/#", "/topic/status/87689768757685"));
		assertTrue(TopicParser.match("/topic/status/87689768757685", "/topic/status/#"));
		assertTrue(TopicParser.match("/topic/status/#", "/topic/status/#"));
		assertTrue(TopicParser.match("/topic/status/", "/topic/status/"));
		
		assertFalse(TopicParser.match("/topic/status1", "/topic/status2"));
	}

}
