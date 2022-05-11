module com.apextalos.cvitfusionclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.joda.time;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.databind;
    requires com.apextalos.cvitfusioncommon;
    requires javafx.graphics;

    exports com.apextalos.cvitfusionclient;
    opens com.apextalos.cvitfusionclient to javafx.fxml;
    exports com.apextalos.cvitfusionclient.controllers;
    opens com.apextalos.cvitfusionclient.controllers to javafx.fxml;
    exports com.apextalos.cvitfusionclient.models;
    opens com.apextalos.cvitfusionclient.models to javafx.fxml;
    exports com.apextalos.cvitfusionclient.controls;
    opens com.apextalos.cvitfusionclient.controls  to javafx.fxml;
}