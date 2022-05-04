package com.apextalos.cvitfusionclient.controllers;

import com.apextalos.cvitfusionclient.models.HelloModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HelloController {

    // Model
    HelloModel model;

    //View
    @FXML
    private Label welcomeText;
    @FXML
    private ListView propertiesListView;

    public void initialize() {
        //get model
        model = new HelloModel("Maxwell Planck", 6626, 1000d);

        // create bindings model to view
        welcomeText.textProperty().bind(model.accountBalanceProperty().asString());

        //link Controller to View - ensure only numeric input (integers) in text field
        /*
        amountTextField.setTextFormatter(new TextFormatter<>(change -> {
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
        */
    }
    @FXML
    protected void onHelloButtonClick() {
        model.deposit(100);
    }

    @FXML
    protected void onHelloMouseEntered() {
        model.withdraw(50);
    }
}