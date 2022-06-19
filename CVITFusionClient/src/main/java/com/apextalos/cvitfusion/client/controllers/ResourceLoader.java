package com.apextalos.cvitfusion.client.controllers;

import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class ResourceLoader<T> {

	private static final Logger logger = LogManager.getLogger(ResourceLoader.class.getSimpleName());

	public static final String IMAGE_SAVE = "save.png";
	public static final String IMAGE_MISSLE = "missile.png";
	public static final String IMAGE_REFRESH = "refresh.png";
	public static final String IMAGE_ACCEPT = "accept.png";
	
	private T res = null;
	
	public T getResource() {
		return res;
	}
	
	public FXMLLoader createLoader(String name, Object controller) {
		FXMLLoader loader = null;

		try {
			// try loading as the jar
			InputStream in = getClass().getResourceAsStream("/" + name);
			logger.info(String.format("getResourceAsStream is null: %b", in == null));
			if (in != null) {
				loader = new FXMLLoader();
				if(controller != null)
					loader.setController(controller);
				res = loader.load(in);
			} else {
				// try loading as the debugger
				URL url = getClass().getResource("../../../../../" + name);
				logger.info(String.format("getResource is null: %b", url == null));
				if (url != null) {
					loader = new FXMLLoader(url);
					if(controller != null)
						loader.setController(controller);
					res = loader.load();
				} else {
					logger.error("Unable to find the fxml " + name);
				}
			}
		} catch (Exception e) {
			logger.error("Failure loading fxml: " + e.getMessage());
		}

		return loader;
	}
	
	public Image loadImageByFilename(String name) {
		Image image = null;
		
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
		
		return image;
	}
}
