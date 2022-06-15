package com.apextalos.cvitfusionlicenseutil.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.design.DesignManager;
import com.apextalos.cvitfusion.common.license.DesignTypeFeature;
import com.apextalos.cvitfusion.common.license.Feature;
import com.apextalos.cvitfusion.common.license.FeatureManager;
import com.apextalos.cvitfusion.common.license.License;
import com.apextalos.cvitfusion.common.license.LicenseManager;
import com.apextalos.cvitfusion.common.opflow.Type;

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
    	
    	// request the license ID
    	System.out.print("Enter License ID: ");
    	String licenseID;
		try {
			licenseID = br.readLine();
		} catch (IOException e1) {
			logger.error("Failure reading license ID");
    		return;
		}
    	
		// load into a license
    	License license = null;
    	try {
			license = lm.loadLicense(licenseID);
		} catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			logger.error("failure loading license");
			return;
		}
    	
    	// for all features (new and old)
    	FeatureManager fm = FeatureManager.getInstance();
    	
    	// the standard app features
    	List<Feature> features = fm.getFeatures();
    	
    	// plus "type" features
    	DesignManager dm = DesignManager.getInstance();
    	List<Type> types = dm.getTypes(null);
    	types.forEach(type -> features.add(new DesignTypeFeature(type)));
    	
    	for(Feature feature : features) {
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
    			System.out.print(" (y|N): ");	
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
    			int number = 0;
    			if(!response.isBlank())
    				number = Integer.valueOf(response);
    			license.setIntFeature(feature, number);
    		} else if(feature.getType() == Boolean.class) {
    			license.setBooleanFeature(feature, response.equalsIgnoreCase("y"));
    		} else {
    			logger.error("Unknown feature type %s", feature.getType().toString());
    		}
    	}
    	
    	// generate the key
    	String licenseKey = "";
    	try {
			licenseKey = lm.generateLicenseKey(license);
		} catch (IllegalBlockSizeException | IOException e) {
			logger.error("License key generation failed: " + e.getMessage());
			return;
		}
    	
    	// print to the user
    	logger.info(String.format("Generated Key: %s", licenseKey));
    }
}
