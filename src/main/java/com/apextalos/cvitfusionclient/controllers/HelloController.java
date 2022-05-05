package com.apextalos.cvitfusionclient.controllers;

import com.apextalos.cvitfusionclient.models.HelloModel;
import com.apextalos.cvitfusionclient.models.KeyValuePair;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.joda.time.DateTime;

public class HelloController {

    // Model
    HelloModel model;

    //View
    @FXML private Label welcomeText;
    @FXML private ListView propertiesListView;
    @FXML private TextField welcomeTextField;
    @FXML private TableView propertiesTable;
    @FXML private TableColumn propertiesColumnKey;
    @FXML private TableColumn propertiesColumnValue;

    public void initialize() {
        //get model
        model = new HelloModel("Maxwell Planck", 6626, 1000d);

        // create bindings model to view
        welcomeText.textProperty().bind(model.accountBalanceProperty().asString());
        propertiesListView.setItems(model.getListItems());

        propertiesColumnKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        propertiesColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        propertiesTable.setItems(model.getTableItems());

        //link Controller to View - ensure only numeric input (integers) in text field
        welcomeTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+") || change.getText().equals("")) {
                return change;
            } else {
                change.setText("");
                change.setRange(
                        change.getRangeStart(),
                        change.getRangeStart()
                );
                return change;
            }
        }));
    }

    @FXML
    protected void onHelloButtonClick() {
        model.deposit(100);
        model.getListItems().add("deposit");
        model.getTableItems().clear();
        model.getTableItems().add(new KeyValuePair("last", "deposit"));
        model.getTableItems().add(new KeyValuePair("ts", DateTime.now().toString()));
    }

    @FXML
    protected void onHelloMouseEntered() {
        model.withdraw(50);
        model.getListItems().add("withdraw");
        model.getTableItems().clear();
        model.getTableItems().add(new KeyValuePair("last", "deposit"));
        model.getTableItems().add(new KeyValuePair("ts", DateTime.now().toString()));
    }
}