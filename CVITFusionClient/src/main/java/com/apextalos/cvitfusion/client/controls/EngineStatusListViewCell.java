package com.apextalos.cvitfusion.client.controls;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class EngineStatusListViewCell extends ListCell<EngineStatus> {

	private static final Logger logger = LogManager.getLogger(EngineStatusListViewCell.class.getSimpleName());

	private FXMLLoader loader;

	// View
	@FXML private VBox vboxParent;
	@FXML private Label idLabel;
	@FXML private ImageView modeImage;
	@FXML private Label nameLabel;
	@FXML private Label sinceLabel;

	@Override
	protected void updateItem(EngineStatus engineStatus, boolean empty) {
		super.updateItem(engineStatus, empty);

		if (empty || engineStatus == null) {
			setText(null);
			setGraphic(null);
			return;
		}

		if (loader == null) {
			String name = "engineStatusListViewCell.fxml";

			InputStream in = getClass().getResourceAsStream("/" + name);
			logger.info(String.format("getResourceAsStream is null: %b", in == null));
			if (in != null) {
				loader = new FXMLLoader();
			} else {
				// try loading as the debugger
				URL url = getClass().getResource("../../../../../" + name);
				logger.info(String.format("getResource is null: %b", url == null));
				if (url != null) {
					loader = new FXMLLoader(url);
				} else {
					logger.error("unable to find the fxml " + name);
				}
			}

			loader.setController(this);

			try {
				loader.load();
			} catch (IOException e) {
				logger.error("unable to load the fxml " + name);
			}

		}

		nameLabel.setText(engineStatus.getLocationName());
		idLabel.setText(engineStatus.getId());

		if (engineStatus.getMode().equals(EngineStatus.Mode.ERROR)) {
			modeImage.setImage(loadIcon("cancel.png"));
		} else if (engineStatus.getMode().equals(EngineStatus.Mode.RUNNING)) {
			modeImage.setImage(loadIcon("accept.png"));
		} else if (engineStatus.getMode().equals(EngineStatus.Mode.STANDBY)) {
			modeImage.setImage(loadIcon("block.png"));
		} else {
			// UNKNOWN
			modeImage.setImage(loadIcon("help.png"));
		}

		setText(null);
		setGraphic(vboxParent);
	}
	
	private Image loadIcon(String name) {
		Image icon = null;
		
		// try loading as the jar
		InputStream in = getClass().getResourceAsStream("/" + name);
		logger.info(String.format("getResourceAsStream is null: %b", in==null));
		if(in != null) {
			icon = new Image(in);
		} else {	
			// try loading as the debugger
			URL url = getClass().getResource("../../../../../" + name);
			logger.info(String.format("getResource is null: %b", url==null));
			if(url != null) {
				icon = new Image(url.toExternalForm());
			} else {
				logger.error("unable to load the icon fxml " + name);
			}
		}
		
		return icon;
	}
}
