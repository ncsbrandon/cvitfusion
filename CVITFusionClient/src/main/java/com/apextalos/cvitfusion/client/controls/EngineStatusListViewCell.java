package com.apextalos.cvitfusion.client.controls;

import com.apextalos.cvitfusion.client.controllers.FXMLResource;
import com.apextalos.cvitfusion.client.controllers.ResourceLoader;
import com.apextalos.cvitfusion.client.models.EngineStatusModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class EngineStatusListViewCell extends ListCell<EngineStatusModel> {

	private ResourceLoader resourceLoader = new ResourceLoader();
	private FXMLLoader loader = null;
	
	// View
	@FXML private VBox vboxParent;
	@FXML private Label idLabel;
	@FXML private ImageView statusImage;
	@FXML private Label nameLabel;
	@FXML private Label sinceLabel;

	@Override
	protected void updateItem(EngineStatusModel engineStatus, boolean empty) {
		super.updateItem(engineStatus, empty);

		if (empty || engineStatus == null) {
			setText(null);
			setGraphic(null);
			return;
		}
			
		if (loader == null) {
			FXMLResource fr = resourceLoader.createLoader("engineStatusListViewCell.fxml", this);
			loader = fr.getLoader();
		}	


		nameLabel.textProperty().bind(engineStatus.getLocationNameProperty());
		idLabel.textProperty().bind(engineStatus.getIdProperty());
		sinceLabel.textProperty().bind(engineStatus.getSinceLastUpdateProperty());
		statusImage.imageProperty().bind(engineStatus.getImageProperty());
		statusImage.rotateProperty().bind(engineStatus.getSpinProperty());

		setText(null);
		setGraphic(vboxParent);
	}
}
