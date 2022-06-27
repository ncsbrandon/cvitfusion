package com.apextalos.cvitfusion.client.controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class ResourceLoader {

	private static final Logger logger = LogManager.getLogger(ResourceLoader.class.getSimpleName());

	public static final String IMAGE_SAVE = "save.png";
	public static final String IMAGE_MISSLE = "missile.png";
	public static final String IMAGE_REFRESH = "refresh.png";
	public static final String IMAGE_ACCEPT = "accept.png";
	
	private static final Map<String, Image> nameImageMapCache = new HashMap<>();
	private static final Map<String, FXMLResource> nameLoaderMapCache = new HashMap<>();
	
	public FXMLResource createLoader(String name, Object controller) {
		return createLoader(name, controller, false);
	}
	
	public FXMLResource createLoader(String name, Object controller, boolean allowCache) {
		// first check the cache
		if(allowCache && nameLoaderMapCache.containsKey(name)) {
			return nameLoaderMapCache.get(name);
		}
				
		FXMLLoader loader = null;
		Node resource = null;

		try {
			// try loading as the jar
			InputStream in = getClass().getResourceAsStream("/" + name);
			logger.info(String.format("getResourceAsStream is null: %b", in == null));
			if (in != null) {
				loader = new FXMLLoader();
				if(controller != null)
					loader.setController(controller);
				resource = loader.load(in);
			} else {
				// try loading as the debugger
				URL url = getClass().getResource("../../../../../" + name);
				logger.info(String.format("getResource is null: %b", url == null));
				if (url != null) {
					loader = new FXMLLoader(url);
					if(controller != null)
						loader.setController(controller);
					resource = loader.load();
				} else {
					logger.error("Unable to find the fxml " + name);
				}
			}
		} catch (Exception e) {
			logger.error("Failure loading fxml ["+name+"]: " + e.getMessage());
		}

		FXMLResource fr = new FXMLResource(loader, resource);
		if(allowCache)
			nameLoaderMapCache.put(name, fr);
		return fr;
	}
	
	public Image loadImageByFilename(String name) {
		// first check the cache
		if(nameImageMapCache.containsKey(name))
			return nameImageMapCache.get(name);
		
		Image image = null;
		
		try {
			// try loading as the jar
			InputStream in = getClass().getResourceAsStream("/" + name);
			logger.info(String.format("getResourceAsStream is null: %b", in==null));
			if(in != null) {
				image = new Image(in);
			} else {	
				// try loading as the debugger
				URL url = getClass().getResource("../../../../../" + name);
				logger.info(String.format("getResource is null: %b", url==null));
				if(url != null) {
					image = new Image(url.toExternalForm());
				} else {
					logger.error("unable to find the image " + name);
				}
			}
		} catch (Exception e) {
			logger.error("Failure loading fxml ["+name+"]: " + e.getMessage());
		}
		
		nameImageMapCache.put(name, image);
		return image;
	}
}
