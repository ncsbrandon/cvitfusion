package com.apextalos.cvitfusionclient.controllers;

import com.apextalos.cvitfusionclient.app.Version;
import com.apextalos.cvitfusionclient.controls.DiagramNodeControl;
import com.apextalos.cvitfusionclient.models.HelloModel;
import com.apextalos.cvitfusionclient.models.KeyValuePairModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HelloController.class.getSimpleName());

    // Model
    private HelloModel model;
    public HelloModel getModel() {
        return model;
    }

    // View
    @FXML private Label welcomeText;
    @FXML private ListView propertiesListView;
    @FXML private TextField welcomeTextField;
    @FXML private TableView propertiesTable;
    @FXML private TableColumn propertiesColumnKey;
    @FXML private TableColumn propertiesColumnValue;
    @FXML private AnchorPane designPane;
    @FXML private Label versionInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create model
        model = new HelloModel(1000d);

        // create bindings
        welcomeText.textProperty().bind(model.getAccountBalanceProperty().asString());
        propertiesListView.setItems(model.getListItems());
        propertiesColumnKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        propertiesColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        propertiesTable.setItems(model.getTableItems());

        versionInfo.setText(String.format("%s.%s", Version.getInstance().getVersion(), Version.getInstance().getBuild()));

        //link Controller to View - ensure only numeric input (integers) in text field
        welcomeTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+") || change.getText().equals("")) {
                return change;
            }

            change.setText("");
            change.setRange(change.getRangeStart(), change.getRangeStart());
            return change;
        }));
    }

    @FXML
    protected void onHelloButtonClick() {
        model.deposit(100);
        model.getListItems().add("deposit");
        model.getTableItems().clear();
        model.getTableItems().add(new KeyValuePairModel("last", "deposit"));
        model.getTableItems().add(new KeyValuePairModel("ts", DateTime.now().toString()));

        DiagramNodeControl r = new DiagramNodeControl();
        r.setLayoutX(20);
        r.setLayoutY(20);
        designPane.getChildren().add(r);

        r.getController().getModel().setNodeName("Node 1");

        logger.debug("this is DEBUG");
        logger.error("this is ERROR");

        //Main m = new Main();

        /*
        r.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            Rectangle r2 = new Rectangle();
            r2.setX(100);
            r2.setY(100);
            r2.setWidth(50);
            r2.setHeight(50);
            designPane.getChildren().add(r2);
        });
        */
    }

    @FXML
    protected void onHelloMouseEntered() {
        model.withdraw(50);
        model.getListItems().add("withdraw");
        model.getTableItems().clear();
        model.getTableItems().add(new KeyValuePairModel("last", "withdraw"));
        model.getTableItems().add(new KeyValuePairModel("ts", DateTime.now().toString()));
    }
}