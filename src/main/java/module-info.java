module com.apextalos.cvitfusionclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.apextalos.cvitfusionclient to javafx.fxml;
    exports com.apextalos.cvitfusionclient;
    exports com.apextalos.cvitfusionclient.controllers;
    opens com.apextalos.cvitfusionclient.controllers to javafx.fxml;
    exports com.apextalos.cvitfusionclient.models;
    opens com.apextalos.cvitfusionclient.models to javafx.fxml;
}