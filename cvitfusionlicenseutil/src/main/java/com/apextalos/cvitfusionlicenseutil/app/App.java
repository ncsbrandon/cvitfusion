package com.apextalos.cvitfusionlicenseutil.app;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.license.LicenseManager;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());

    public static void main( String[] args )
    {
    	// get the license manager instance
    	LicenseManager lm = null;
    	try {
    		lm = LicenseManager.getInstance();
    		logger.info("license manager loaded");
    	} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
    		logger.error("Unable to instantiate the license manager");
    		return;
    	}
    }
}
