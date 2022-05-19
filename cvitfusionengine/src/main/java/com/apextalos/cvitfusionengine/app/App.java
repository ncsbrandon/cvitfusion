package com.apextalos.cvitfusionengine.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        logger.info("app starting");

		ConfigFile cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);
    }
}
