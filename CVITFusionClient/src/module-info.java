module CVITFusionClient {
	requires javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires org.joda.time;
	requires org.apache.logging.log4j;
	requires com.fasterxml.jackson.databind;
	requires transitive cvitfusioncommon;
	requires transitive javafx.base;

	exports com.apextalos.cvitfusion.client.app;

	opens com.apextalos.cvitfusion.client.app to javafx.graphics, javafx.fxml;

	exports com.apextalos.cvitfusion.client.controllers;

	opens com.apextalos.cvitfusion.client.controllers to javafx.graphics, javafx.fxml;

	exports com.apextalos.cvitfusion.client.models;

	opens com.apextalos.cvitfusion.client.models to javafx.graphics, javafx.fxml;

	exports com.apextalos.cvitfusion.client.controls;

	opens com.apextalos.cvitfusion.client.controls to javafx.graphics, javafx.fxml;
}
