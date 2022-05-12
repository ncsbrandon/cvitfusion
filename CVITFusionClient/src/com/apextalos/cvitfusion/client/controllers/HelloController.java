package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.client.app.Version;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.client.models.HelloModel;
import com.apextalos.cvitfusion.client.models.KeyValuePairModel;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class HelloController extends BaseController {

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
    @FXML private SplitPane sp1;
    @FXML private SplitPane sp11;
    @FXML private SplitPane sp112;
    @FXML private VBox vbox;
    @FXML private VBox vbox2;
    @FXML private TitledPane propertiesPanel;
    
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
        
        propertiesTable.prefHeightProperty().bind(vbox2.heightProperty());
        designPane.prefHeightProperty().bind(vbox.heightProperty());
    }
    
    @Override
	public void begin(ConfigFile cf) {
		super.begin(cf);
		sp1.setDividerPosition(0, cf.getDouble("sp1_divider_position", -1));
		sp11.setDividerPosition(0, cf.getDouble("sp11_divider_position", -1));
		sp112.setDividerPosition(0, cf.getDouble("sp112_divider_position", -1));
		model.getListItems().add(String.format("begin [%s]",  cf.getString("some_setting", "default_value")));
	}
    
    @Override
    public void end() {
		super.end();
		cf.setDouble("sp1_divider_position", sp1.getDividerPositions()[0]);
		cf.setDouble("sp11_divider_position", sp11.getDividerPositions()[0]);
		cf.setDouble("sp112_divider_position", sp112.getDividerPositions()[0]);
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
        r.getController().getModel().setName("Node 1");
        r.getController().begin(cf);

        logger.debug("this is DEBUG");
        logger.error("this is ERROR");
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