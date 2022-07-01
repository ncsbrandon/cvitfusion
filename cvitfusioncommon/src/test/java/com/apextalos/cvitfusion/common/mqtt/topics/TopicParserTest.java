package com.apextalos.cvitfusion.common.mqtt.topics;

import static org.junit.Assert.*;

import org.junit.Test;

public class TopicParserTest {

	@Test
	public void testMatch() {
		// symmetry
		assertTrue(TopicParser.match("/testtopic/status/#", "/testtopic/status/87689768757685"));
		assertTrue(TopicParser.match("/testtopic/status/87689768757685", "/testtopic/status/#"));
		
		// with #
		assertTrue(TopicParser.match("/testtopic/status/#", "/testtopic/status/#"));
		
		// without #
		assertTrue(TopicParser.match("/testtopic/status/", "/testtopic/status/"));
		
		// don't match
		assertFalse(TopicParser.match("/testtopic/status1", "/testtopic/status2"));
	}

	@Test
	public void testEngineID() {
		String t1 = TopicBuilder.engineStatusAny();
		String t2 = TopicBuilder.engineStatus("UT_ENGINE");
		String t3 = TopicBuilder.requestProcessStatus("UT_ENGINE", 9999);
		
		assertEquals(0, "#".compareTo(TopicParser.getEngineID(t1)));
		assertEquals(0, "UT_ENGINE".compareTo(TopicParser.getEngineID(t2)));
		assertEquals(0, "UT_ENGINE".compareTo(TopicParser.getEngineID(t3)));
	}
	
	@Test
	public void testProcessID() {
		String t1 = TopicBuilder.engineStatusAny();
		String t3 = TopicBuilder.requestProcessStatus("UT_ENGINE", 9999);
		
		assertEquals(-1, TopicParser.getProcessID(t1));
		assertEquals(9999, TopicParser.getProcessID(t3));
	}
}
