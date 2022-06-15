package com.apextalos.cvitfusion.common.license;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

public class LicenseTest {

private final Logger logger = LogManager.getLogger(LicenseTest.class.getSimpleName());
	
	@BeforeClass
	public static void init() throws Exception {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);
	}
	
	@Test
	public void test() throws Exception {
		LicenseManager licenseManager = LicenseManager.getInstance();
		
		// interface methods
		licenseManager.enumerateInterfaces();
		String ifname = licenseManager.getFirstInterface();
		logger.info("Interface: " + ifname);
		
		// create an ID
		String licenseID = licenseManager.generateLicenseID(ifname, "unittest");
		logger.info("License ID: " + licenseID);
		
		// turn it into an empty license
		License license1 = licenseManager.loadLicense(licenseID);
		for(Entry<Object, Object> prop : license1.getProperties().entrySet()) {
			logger.debug(prop.getKey() + ": " + prop.getValue());
		}
		
		// verify the license
		assertTrue(licenseManager.verifyAddress(ifname, license1));
		
		// add some features to the license
		license1.setBooleanFeature(FeatureManager.FEATURE_ORG, true);
		license1.setIntFeature(FeatureManager.FEATURE_SALESORDER, 10);
		
		// generate a license key with features
		String licenseKey = licenseManager.generateLicenseKey(license1);
		assertTrue(licenseKey.length() > 0);
		
		License license2 = licenseManager.loadLicense(licenseKey);
		
		// verify the license and features
		assertTrue(licenseManager.verifyAddress(ifname, license2));
		assertTrue(license2.getBooleanFeature(FeatureManager.FEATURE_ORG));
		assertEquals(10, license2.getIntFeature(FeatureManager.FEATURE_SALESORDER));
		
		//logger.info("Expires: " + license.getExpirationDateAsString());
		//logger.info("In days: " + license.getDaysTillExpire());
	}
}
