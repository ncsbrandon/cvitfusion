package com.apextalos.cvitfusion.common.mqtt.topics;

import static org.junit.Assert.*;

import org.junit.Test;

public class TopicParserTest {

	@Test
	public void testMatch() {
		assertTrue(TopicParser.match("/testtopic/status/#", "/testtopic/status/87689768757685"));
		assertTrue(TopicParser.match("/testtopic/status/87689768757685", "/testtopic/status/#"));
		assertTrue(TopicParser.match("/testtopic/status/#", "/testtopic/status/#"));
		assertTrue(TopicParser.match("/testtopic/status/", "/testtopic/status/"));
		
		assertFalse(TopicParser.match("/testtopic/status1", "/testtopic/status2"));
	}

}
