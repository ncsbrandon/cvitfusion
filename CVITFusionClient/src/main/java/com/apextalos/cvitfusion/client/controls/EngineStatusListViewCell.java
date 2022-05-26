package com.apextalos.cvitfusion.client.controls;

import com.apextalos.cvitfusion.client.controllers.ResourceLoader;
import com.apextalos.cvitfusion.client.models.EngineStatusModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class EngineStatusListViewCell extends ListCell<EngineStatusModel> {

	//private static final Logger logger = LogManager.getLogger(EngineStatusListViewCell.class.getSimpleName());

	private FXMLLoader loader;
	private Image errorImage;
	//private Image runningImage;
	//private Image standbyImage;
	//private Image unknownImage;
	private ResourceLoader<Object> rl = new ResourceLoader<>();

	// View
	@FXML private VBox vboxParent;
	@FXML private Label idLabel;
	@FXML private ImageView modeImage;
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
			loader = rl.createLoader("engineStatusListViewCell.fxml", this);
			errorImage = rl.loadImage("cancel.png");
			//runningImage = rl.loadImage("accept.png");
			//standbyImage = rl.loadImage("block.png");
			//unknownImage = rl.loadImage("help.png");
		}	


		nameLabel.textProperty().bind(engineStatus.getLocationNameProperty());
		idLabel.textProperty().bind(engineStatus.getIdProperty());
		sinceLabel.textProperty().bind(engineStatus.getSinceLastUpdateProperty());

		//if (engineStatus.getMode().equals(EngineStatus.Mode.ERROR)) {
			modeImage.setImage(errorImage);
		//} else if (engineStatus.getMode().equals(EngineStatus.Mode.RUNNING)) {
		//	modeImage.setImage(runningImage);
		//} else if (engineStatus.getMode().equals(EngineStatus.Mode.STANDBY)) {
		//	modeImage.setImage(standbyImage);
		//} else {
			// UNKNOWN
		//	modeImage.setImage(unknownImage);
		//}
		
		modeImage.maxWidth(40);
		modeImage.maxHeight(40);			


		setText(null);
		setGraphic(vboxParent);
	}
}
