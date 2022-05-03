module com.apextalos.cvitfusionclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.apextalos.cvitfusionclient to javafx.fxml;
    exports com.apextalos.cvitfusionclient;
}