module CVITFusionClient {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires org.joda.time;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.databind;
	requires transitive cvitfusioncommon;
	requires javafx.base;
    
	exports com.apextalos.cvitfusion.client.app;
    opens com.apextalos.cvitfusion.client.app to javafx.graphics, javafx.fxml;
    exports com.apextalos.cvitfusion.controllers;
    opens com.apextalos.cvitfusion.controllers to javafx.graphics, javafx.fxml;
    exports com.apextalos.cvitfusion.models;
    opens com.apextalos.cvitfusion.models to javafx.graphics, javafx.fxml;
    exports com.apextalos.cvitfusion.controls;
    opens com.apextalos.cvitfusion.controls  to javafx.graphics, javafx.fxml;
}
