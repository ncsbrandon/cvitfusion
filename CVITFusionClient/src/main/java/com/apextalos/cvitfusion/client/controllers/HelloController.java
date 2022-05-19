package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.client.app.Version;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.client.diagram.DiagramBuilder;
import com.apextalos.cvitfusion.client.models.DiagramNodeModel;
import com.apextalos.cvitfusion.client.models.HelloModel;
import com.apextalos.cvitfusion.client.models.KeyValuePairModel;
import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.ProcessLink;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class HelloController extends BaseController {

	private static final Logger logger = LogManager.getLogger(HelloController.class.getSimpleName());

	// Model
	private HelloModel model;

	public HelloModel getModel() {
		return model;
	}

	// View
	@FXML
	private Label welcomeText;
	@FXML
	private ListView<String> propertiesListView;
	@FXML
	private TextField welcomeTextField;
	@FXML
	private TableView<KeyValuePairModel> propertiesTable;
	@FXML
	private TableColumn<Object, Object> propertiesColumnKey;
	@FXML
	private TableColumn<Object, Object> propertiesColumnValue;
	@FXML
	private AnchorPane designPane;
	@FXML
	private ScrollPane designScroll;
	@FXML
	private Label versionInfo;
	@FXML
	private SplitPane sp1;
	@FXML
	private SplitPane sp11;
	@FXML
	private SplitPane sp112;
	@FXML
	private VBox vbox;
	@FXML
	private VBox vbox2;
	@FXML
	private TitledPane propertiesPanel;

	private DiagramBuilder db = new DiagramBuilder();
	private Node activeSelection = null;
	private OperationalFlow activeFlow = null;
	
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

		// link Controller to View - ensure only numeric input (integers) in text field
		welcomeTextField.setTextFormatter(new TextFormatter<>(change -> {
			if (change.getText().matches("\\d+") || change.getText().equals("")) {
				return change;
			}

			change.setText("");
			change.setRange(change.getRangeStart(), change.getRangeStart());
			return change;
		}));

		// bind heights to create fill
		propertiesTable.prefHeightProperty().bind(vbox2.heightProperty());
		designScroll.prefHeightProperty().bind(vbox.heightProperty());
		designPane.prefHeightProperty().bind(designScroll.heightProperty());

		// load some sample data
		sample1();
		
		// layout the design pane
		designPane.getChildren().addAll(db.layout(activeFlow, this));
	}

	@Override
	public void begin(ConfigFile cf) {
		super.begin(cf);
		
		// load the last view size
		sp1.setDividerPosition(0, cf.getDouble("sp1_divider_position", -1));
		sp11.setDividerPosition(0, cf.getDouble("sp11_divider_position", -1));
		sp112.setDividerPosition(0, cf.getDouble("sp112_divider_position", -1));
	}

	@Override
	public void end() {
		super.end();
		
		// save the last view size
		cf.setDouble("sp1_divider_position", sp1.getDividerPositions()[0]);
		cf.setDouble("sp11_divider_position", sp11.getDividerPositions()[0]);
		cf.setDouble("sp112_divider_position", sp112.getDividerPositions()[0]);
	}

	public void sample1() {
		activeFlow = new OperationalFlow(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());

		Process n111 = new Process(111, true, 3, null, "", 4317, new Properties());
		Process n112 = new Process(112, true, 6, null, "", 4317, new Properties());
		Process n121 = new Process(121, true, 8, null, "", 4317, new Properties());
		Process n122 = new Process(122, true, 3, null, "", 4317, new Properties());
		Process n12 = new Process(12, true, 2, new ArrayList<>() {
			{
				add(n121);
				add(n122);
			}
		}, "", 420, new Properties());
		Process n11 = new Process(11, true, 5, new ArrayList<>() {
			{
				add(n111);
				add(n112);
			}
		}, "", 420, new Properties());
		Process n1 = new Process(1, true, 1, new ArrayList<>() {
			{
				add(n11);
				add(n12);
			}
		}, "", 69, new Properties());
		activeFlow.getProcesses().add(n1);

		Process n211 = new Process(211, true, 6, null, "", 4317, new Properties());
		Process n21 = new Process(21, true, 7, new ArrayList<>() {
			{
				add(n211);
			}
		}, "", 420, new Properties());
		Process n2 = new Process(2, true, 4, new ArrayList<>() {
			{
				add(n21);
			}
		}, "", 69, new Properties());
		activeFlow.getProcesses().add(n2);

		// INPUTS----------------------
		activeFlow.getTypes().add(new Type(1, 1, "Lidar", new Properties(), null, new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}));
		activeFlow.getTypes().add(new Type(4, 1, "Camera", new Properties(), null, new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}));

		// LOGICS-------------------------
		activeFlow.getTypes().add(new Type(2, 1, "WWVD", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
			}
		}));
		activeFlow.getTypes().add(new Type(5, 1, "Curve Speed", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
			}
		}));
		activeFlow.getTypes().add(new Type(7, 1, "Queue Detection", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
			}
		}));

		// OUTPUTS -------------
		activeFlow.getTypes().add(new Type(3, 1, "Email", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null));
		activeFlow.getTypes().add(new Type(6, 1, "Flashing Beacon", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null));
		activeFlow.getTypes().add(new Type(8, 1, "Digital Output", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null));

		// https://coolors.co/233d4d-915e3d-fe7f2d-fda53a-fcca46-cfc664-a1c181-619b8a
		activeFlow.getStyles().add(new Style(1, 1, new Color(0x23, 0x3D, 0x4D, 1), new Color(255, 255, 255, 1))); // Charcoal
		activeFlow.getStyles().add(new Style(2, 1, new Color(0x91, 0x5E, 0x3D, 1), new Color(255, 255, 255, 1))); // Coyote Brown
		activeFlow.getStyles().add(new Style(3, 1, new Color(0xFE, 0x7F, 0x2D, 1), new Color(255, 255, 255, 1))); // Pumpkin
		activeFlow.getStyles().add(new Style(4, 1, new Color(0xFD, 0xA5, 0x3A, 1), new Color(0, 0, 0, 1))); // Yellow Orange
		activeFlow.getStyles().add(new Style(5, 1, new Color(0xFC, 0xCA, 0x46, 1), new Color(0, 0, 0, 1))); // Sunglow
		activeFlow.getStyles().add(new Style(6, 1, new Color(0xCF, 0xC6, 0x64, 1), new Color(0, 0, 0, 1))); // Straw
		activeFlow.getStyles().add(new Style(7, 1, new Color(0xA1, 0xC1, 0x81, 1), new Color(0, 0, 0, 1))); // Olivine
		activeFlow.getStyles().add(new Style(8, 1, new Color(0x61, 0x9B, 0x8A, 1), new Color(0, 0, 0, 1))); // Polished Pine

		activeFlow.getTypeStyle().put(1, 1);
		activeFlow.getTypeStyle().put(2, 2);
		activeFlow.getTypeStyle().put(3, 3);
		activeFlow.getTypeStyle().put(4, 4);
		activeFlow.getTypeStyle().put(5, 5);
		activeFlow.getTypeStyle().put(6, 6);
		activeFlow.getTypeStyle().put(7, 7);
		activeFlow.getTypeStyle().put(8, 8);
	}

	@FXML
	protected void onHelloButtonClick() {
		model.deposit(100);
		model.getListItems().add("deposit");
		model.getTableItems().clear();
		model.getTableItems().add(new KeyValuePairModel("last", "deposit"));
		model.getTableItems().add(new KeyValuePairModel("ts", DateTime.now().toString()));

		logger.debug("this is DEBUG");
		logger.error("this is ERROR");
	}

	@FXML
	protected void onMouseClicked(MouseEvent mouseEvent) {
		logger.debug("onMouseClicked " + mouseEvent.toString());
		mouseEvent.consume();
		onActionPerformed(null, EventType.DESELECTED);
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		if (et == EventType.SELECTED && o instanceof Line) {
			onActionPerformed(null, EventType.DESELECTED);
			onLineSelection((Line) o);
		} else if (et == EventType.SELECTED && o instanceof DiagramNodeControl) {
			onActionPerformed(null, EventType.DESELECTED);
			onProcessSelection((DiagramNodeControl) o);
		} else if (et == EventType.DESELECTED) {
			if (activeSelection != null && activeSelection instanceof Line) {
				onLineDeselection((Line) activeSelection);
			} else if (activeSelection != null && activeSelection instanceof DiagramNodeControl) {
				onProcessDeselection((DiagramNodeControl) activeSelection);
			}
		}
	}

	private void onLineSelection(Line line) {
		line.setEffect(new DropShadow());
		activeSelection = line;
		
		ProcessLink pc = (ProcessLink) line.getUserData();
		Process parentProcess = pc.getParentProcess();
		Type parentType = activeFlow.lookupType(parentProcess.getTypeID());
		Process childProcess = pc.getChildProcess();
		Type childType = activeFlow.lookupType(childProcess.getTypeID());
		
		model.getTableItems().clear();
		model.getTableItems().add(new KeyValuePairModel("From", String.format("%s %d", parentType.getName(), parentProcess.getProcessID())));
		model.getTableItems().add(new KeyValuePairModel("To", String.format("%s %d", childType.getName(), childProcess.getProcessID())));
	}
	
	private void onLineDeselection(Line line) {
		line.setEffect(null);
		activeSelection = null;
		model.getTableItems().clear();
	}

	private void onProcessSelection(DiagramNodeControl dnc) {
		dnc.getController().select(true);
		activeSelection = dnc;
		
		DiagramNodeController dncController = dnc.getController();
		DiagramNodeModel dncModel = dncController.getModel();
		
		Process process = activeFlow.lookupProcess(Integer.valueOf(dncModel.getIDProperty().get()));
		Type type = activeFlow.lookupType(process.getTypeID());
		
		model.getTableItems().clear();
		model.getTableItems().add(new KeyValuePairModel("Process", String.format("%s %d", type.getName(), process.getProcessID())));	
		model.getTableItems().add(new KeyValuePairModel("Version", String.valueOf(type.getVersion())));
		model.getTableItems().add(new KeyValuePairModel("Notes", process.getNotes()));
		model.getTableItems().add(new KeyValuePairModel("Enabled", String.valueOf(process.isEnabled())));
	}
	
	private void onProcessDeselection(DiagramNodeControl dnc) {
		dnc.getController().select(false);
		activeSelection = null;
		model.getTableItems().clear();
	}
}