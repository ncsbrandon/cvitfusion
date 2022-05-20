package com.apextalos.cvitfusionlicenseutil.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.license.Feature;
import com.apextalos.cvitfusion.common.license.FeatureManager;
import com.apextalos.cvitfusion.common.license.License;
import com.apextalos.cvitfusion.common.license.LicenseManager;

public class App 
{
	private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());

    public static void main( String[] args ) {
    	logger.info("----------------------------------");
		logger.info("app starting");
		logger.info("----------------------------------");
		
    	// get the license manager instance
    	LicenseManager lm = null;
    	try {
    		lm = LicenseManager.getInstance();
    		logger.info("license manager loaded");
    	} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
    		logger.error("Unable to instantiate the license manager");
    		return;
    	}
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	System.out.print("Enter License ID: ");
    	String licenseID;
		try {
			licenseID = br.readLine();
		} catch (IOException e1) {
			logger.error("Failure reading license ID");
    		return;
		}
    	
    	License license = null;
    	try {
			license = lm.loadLicense(licenseID);
		} catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			logger.error("failure loading license");
			return;
		}
    	
    	// for all features (new and old)
    	FeatureManager fm = FeatureManager.getInstance();
    	for(Feature feature : fm.getFeatures()) {
    		// print the description
    		System.out.print(feature.getDescription());
    		    		
    		// these are provided by the engine and cannot be changed
    		if(feature.getMode() == Feature.MODE_CLIENTGEN) {
    			System.out.print(" (readonly): ");
    			if(license.hasStringFeature(feature))
    				System.out.println(license.getStringFeature(feature));
    			else
    				System.out.println("--MISSING--");
    			continue;
    		}
    		
    		// prompt the user
    		if(feature.getType() == String.class) {
    			System.out.print(" (string): ");		
    		} else if(feature.getType() == Integer.class) {
    			System.out.print(" (number): ");		
    		} else if(feature.getType() == Boolean.class) {
    			System.out.print(" (y|n): ");	
    		} else {
    			logger.error("Unknown feature type " + feature.getType().toString());
    			continue;
    		}
    		
    		// user entry
    		String response;
    		try {
    			response = br.readLine();
    		} catch (IOException e1) {
    			logger.error("Failure reading license ID");
        		return;
    		}
    		
    		// add to the properties
    		if(feature.getType() == String.class) {
    			license.setStringFeature(feature, response);
    		} else if(feature.getType() == Integer.class) {
    			license.setIntFeature(feature, Integer.valueOf(response));
    		} else if(feature.getType() == Boolean.class) {
    			license.setBooleanFeature(feature, response.equalsIgnoreCase("y"));
    		} else {
    			logger.error("Unknown feature type " + feature.getType().toString());
    			continue;
    		}
    	}
    	
    	String licenseKey = "";
    	try {
			licenseKey = lm.generateLicenseKey(license);
		} catch (IllegalBlockSizeException | IOException e) {
			logger.error("License key generation failed: " + e.getMessage());
			return;
		}
    	
    	logger.info("Key: " + licenseKey);
    }
}
