package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.client.app.ConfigItems;
import com.apextalos.cvitfusion.client.app.Version;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.client.controls.EngineStatusListViewCell;
import com.apextalos.cvitfusion.client.diagram.DiagramBuilder;
import com.apextalos.cvitfusion.client.models.DiagramNodeModel;
import com.apextalos.cvitfusion.client.models.EngineStatusModel;
import com.apextalos.cvitfusion.client.models.KeyValuePairModel;
import com.apextalos.cvitfusion.client.models.MainSceneModel;
import com.apextalos.cvitfusion.client.mqtt.ClientConfigMqttTransceiver;
import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent.Change;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionListener;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionListener;
import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.ProcessLink;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.utils.DateTimeUtils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainSceneController extends BaseController implements SubscriptionListener {

	private static final Logger logger = LogManager.getLogger(MainSceneController.class.getSimpleName());

	// Model
	private MainSceneModel model;

	public MainSceneModel getModel() {
		return model;
	}

	// View
	@FXML private BorderPane topBorderPane;
	@FXML private SplitPane sp1;
    @FXML private SplitPane sp11;
    @FXML private SplitPane sp112;
    @FXML private VBox enginesVbox;
    @FXML private ListView<EngineStatusModel> engineStatusListView;
    @FXML private VBox designVbox;
    @FXML private AnchorPane designAnchor;
    @FXML private ScrollPane designScroll;
    @FXML private MenuButton designButtonAddOutput;
    @FXML private MenuButton designButtonCreateInput;
    @FXML private Button designButtonDisable;
    @FXML private Button designButtonRemove;
    @FXML private VBox propertiesVbox;
    @FXML private TableView<KeyValuePairModel> propertiesTable;
    @FXML private TableColumn<Object, Object> propertiesColumnKey;
    @FXML private TableColumn<Object, Object> propertiesColumnValue;
    @FXML private ListView<String> statusListView;
    @FXML private Label mqttStatusLabel;
    @FXML private Label versionInfo;
    
    // sub-models
	private ObservableList<EngineStatusModel> engineStatusModelList = FXCollections.observableArrayList();
	private DiagramBuilder db = new DiagramBuilder();
	private Node designSelection = null;
	private OperationalFlow activeDesign = null;
	
	// services
	private ClientConfigMqttTransceiver ccmt;
	private Timer guiUpdateTimer;
	
	//*********************
	// SCENE EVENTS
	//*********************
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// create model
		model = new MainSceneModel(1000d);

		// status view
		statusListView.setItems(model.getListItems());
		
		// properties view
		propertiesColumnKey.setCellValueFactory(new PropertyValueFactory<>("key"));
		propertiesColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
		propertiesTable.setItems(model.getTableItems());

		// version view
		versionInfo.setText(String.format("%s.%s", Version.getInstance().getVersion(), Version.getInstance().getBuild()));

		// engine status view
		engineStatusListView.setItems(engineStatusModelList);
		engineStatusListView.setCellFactory(new Callback<ListView<EngineStatusModel>, ListCell<EngineStatusModel>>() {
		    @Override
		    public ListCell<EngineStatusModel> call(ListView<EngineStatusModel> engineStatusListView) {
		        return new EngineStatusListViewCell();
		    }
		});
		engineStatusListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EngineStatusModel>() {
			@Override
			public void changed(ObservableValue<? extends EngineStatusModel> observable, EngineStatusModel oldValue, EngineStatusModel newValue) {
				onEngineStatusSelected(newValue);
			}
		});
		
		/*
		// link Controller to View - ensure only numeric input (integers) in text field
		welcomeTextField.setTextFormatter(new TextFormatter<>(change -> {
			if (change.getText().matches("\\d+") || change.getText().equals("")) {
				return change;
			}

			change.setText("");
			change.setRange(change.getRangeStart(), change.getRangeStart());
			return change;
		}));
		*/

		// bind heights to create window fill
		engineStatusListView.prefHeightProperty().bind(enginesVbox.heightProperty());
		propertiesTable.prefHeightProperty().bind(propertiesVbox.heightProperty());
		designScroll.prefHeightProperty().bind(designVbox.heightProperty());
		designAnchor.prefHeightProperty().bind(designScroll.heightProperty());
		
		guiUpdateTimer = new Timer();
		guiUpdateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!Platform.isFxApplicationThread()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							onUpdateTimer();
						}
					});
				}
			}
		}, 100, 100);
	}
	

	@Override
	public void begin(ConfigFile cf, Stage stage) {
		super.begin(cf, stage);
		
		// stage size
		if(cf.hasKey(ConfigItems.MAIN_WIDTH_CONFIG))
			stage.setWidth(cf.getDouble(ConfigItems.MAIN_WIDTH_CONFIG, -1));
		if(cf.hasKey(ConfigItems.MAIN_HEIGHT_CONFIG))
			stage.setHeight(cf.getDouble(ConfigItems.MAIN_HEIGHT_CONFIG, -1));
		
		// stage position
		stage.setX(cf.getDouble(ConfigItems.MAIN_POSITION_X_CONFIG, ConfigItems.MAIN_POSITION_X_DEFAULT));
		stage.setY(cf.getDouble(ConfigItems.MAIN_POSITION_Y_CONFIG, ConfigItems.MAIN_POSITION_Y_DEFAULT));
		
		// stage other
		stage.setMaximized(cf.getBoolean(ConfigItems.MAIN_MAXIMIZED_CONFIG, ConfigItems.MAIN_MAXIMIZED_DEFAULT));
		
		// divider positions
		if(cf.hasKey(ConfigItems.MAIN_SP1_DIV_POS_CONFIG))
			sp1.setDividerPosition(0, cf.getDouble(ConfigItems.MAIN_SP1_DIV_POS_CONFIG, -1));
		if(cf.hasKey(ConfigItems.MAIN_SP11_DIV_POS_CONFIG))
			sp11.setDividerPosition(0, cf.getDouble(ConfigItems.MAIN_SP11_DIV_POS_CONFIG, -1));
		if(cf.hasKey(ConfigItems.MAIN_SP112_DIV_POS_CONFIG))
			sp112.setDividerPosition(0, cf.getDouble(ConfigItems.MAIN_SP112_DIV_POS_CONFIG, -1));
		
		onNoSelection();
		
		// start MQTT
		ccmt = new ClientConfigMqttTransceiver(cf);
		ccmt.addConnectionListener(new ConnectionListener() {	
			@Override
			public void connectionChange(ConnectionEvent e) {
				onConnectionChanged(e);
			}
		});
		ccmt.start(this);	
	}
		
	@Override
	public void end(Stage stage) {
		super.end(stage);
		
		if(stage == null)
			return;
		
		// stage position
		cf.setDouble(ConfigItems.MAIN_POSITION_X_CONFIG, stage.getX());
		cf.setDouble(ConfigItems.MAIN_POSITION_Y_CONFIG, stage.getY());
		
		// stage size
		cf.setDouble(ConfigItems.MAIN_WIDTH_CONFIG, stage.getWidth());
		cf.setDouble(ConfigItems.MAIN_HEIGHT_CONFIG, stage.getHeight());
		
		// stage other
		cf.setBoolean(ConfigItems.MAIN_MAXIMIZED_CONFIG, stage.isMaximized());
		
		// divider positions
		cf.setDouble(ConfigItems.MAIN_SP1_DIV_POS_CONFIG, sp1.getDividerPositions()[0]);
		cf.setDouble(ConfigItems.MAIN_SP11_DIV_POS_CONFIG, sp11.getDividerPositions()[0]);
		cf.setDouble(ConfigItems.MAIN_SP112_DIV_POS_CONFIG, sp112.getDividerPositions()[0]);
		
		// shutdown mqtt
		ccmt.stop();
		guiUpdateTimer.cancel();
	}
	
	
	protected void onUpdateTimer() {
		// update the engine status "last update"
		for(EngineStatusModel esm : engineStatusModelList) {
			esm.getSinceLastUpdateProperty().set(DateTimeUtils.timeSinceLastUpdate(esm.getLastUpdate(), DateTime.now()));
		}
	}
	
	
	
	//*********************
	// MENU EVENTS
	//*********************
	@FXML
	protected void onActionDisconnectMenu(ActionEvent event) {
		// go back to connections
		Stage stage = (Stage)topBorderPane.getScene().getWindow();
		SceneManager.getInstance(cf).showConnections(stage);
	}
	
	@FXML
	protected void onActionCloseMenu(ActionEvent event) {
		Stage stage = (Stage)topBorderPane.getScene().getWindow();
		SceneManager.getInstance(cf).close(stage);
	}
	

	
	//*********************
	// ENGINE EVENTS
	//*********************
	protected void onEngineStatusSelected(EngineStatusModel newValue) {
		//TBD
	}
	
	
	//*********************
	// DESIGN PANE EVENTS
	//*********************
	@FXML
	protected void onDesignPaneMouseClicked(MouseEvent mouseEvent) {
		logger.debug("onDesignPaneMouseClicked");
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
			if (designSelection instanceof Line) {
				onLineDeselection((Line) designSelection);
			} else if (designSelection instanceof DiagramNodeControl) {
				onProcessDeselection((DiagramNodeControl) designSelection);
			}
		}
	}
	
	private void onLineSelection(Line line) {
		line.setEffect(new DropShadow());
		designSelection = line;
		
		ProcessLink pc = (ProcessLink) line.getUserData();
		Process parentProcess = pc.getParentProcess();
		Type parentType = activeDesign.lookupType(parentProcess.getTypeID());
		Process childProcess = pc.getChildProcess();
		Type childType = activeDesign.lookupType(childProcess.getTypeID());
		
		model.getTableItems().clear();
		model.getTableItems().add(new KeyValuePairModel("From", String.format("%s %d", parentType.getName(), parentProcess.getProcessID())));
		model.getTableItems().add(new KeyValuePairModel("To", String.format("%s %d", childType.getName(), childProcess.getProcessID())));
	}
	
	private void onLineDeselection(Line line) {
		line.setEffect(null);
		onNoSelection();
	}

	private void onProcessSelection(DiagramNodeControl dnc) {
		dnc.getController().select(true);
		designSelection = dnc;
		
		DiagramNodeController dncController = dnc.getController();
		DiagramNodeModel dncModel = dncController.getModel();
		
		Process process = activeDesign.lookupProcess(Integer.valueOf(dncModel.getIDProperty().get()));
		Type type = activeDesign.lookupType(process.getTypeID());
		
		model.getTableItems().clear();
		model.getTableItems().add(new KeyValuePairModel("Process", String.format("%s %d", type.getName(), process.getProcessID())));	
		model.getTableItems().add(new KeyValuePairModel("Version", String.valueOf(type.getVersion())));
		model.getTableItems().add(new KeyValuePairModel("Notes", process.getNotes()));
		model.getTableItems().add(new KeyValuePairModel("Enabled", String.valueOf(process.isEnabled())));
		
		designButtonAddOutput.setVisible(type.hasSupportedOutputs());
		designButtonAddOutput.getItems().clear();
		if(type.hasSupportedOutputs()) {
			for(Integer id : type.getSupportedOutputs()) {
				Type outputType = activeDesign.lookupType(id);
				MenuItem mi = new MenuItem(outputType.getName());
				mi.setUserData(outputType);
				mi.setOnAction(e -> onDesignButtonAddOutput(e));
				designButtonAddOutput.getItems().add(mi);
			}
		}
		designButtonDisable.setVisible(true);
		designButtonDisable.setText(process.isEnabled() ? "Disable" : "Enable");
		designButtonRemove.setVisible(true);
	}
	
	private void onProcessDeselection(DiagramNodeControl dnc) {
		dnc.getController().select(false);
		onNoSelection();
	}
	
	private void onNoSelection() {
		designSelection = null;
		model.getTableItems().clear();
		designButtonAddOutput.setVisible(false);
		designButtonDisable.setVisible(false);
		designButtonRemove.setVisible(false);
	}

    @FXML
    void onDesignButtonDisable(ActionEvent event) {
    	logger.info("onDesignButtonDisable");
    }

    @FXML
    void onDesignButtonRemove(ActionEvent event) {
    	logger.info("onDesignButtonRemove");
    }
    
    @FXML
    void onDesignButtonAddOutput(ActionEvent event) {
    	logger.info("onDesignButtonAddOutput " + ((Type)((MenuItem)event.getSource()).getUserData()).getName());
    }
    
    @FXML
    void onDesignButtonCreateInput(ActionEvent event) {
    	logger.info("onDesignButtonCreateInput " + ((Type)((MenuItem)event.getSource()).getUserData()).getName());
    }
	
	private void fillDesignPane() {
		// layout the design and add it to the pane
		designAnchor.getChildren().addAll(db.layout(activeDesign, this));
		
		designButtonCreateInput.getItems().clear();
		List<Type> topLevelTypes = activeDesign.getTopLevelTypes();
		if(topLevelTypes != null && !topLevelTypes.isEmpty()) {
			for(Type topLevelType : topLevelTypes) {
				MenuItem mi = new MenuItem(topLevelType.getName());
				mi.setUserData(topLevelType);
				mi.setOnAction(e -> onDesignButtonCreateInput(e));
				designButtonCreateInput.getItems().add(mi);
			}
		}
	}
	
	private void clearDesignPane() {
		activeDesign = null;
		
		ObservableList<Node> children = designAnchor.getChildren();
        if(children != null && !children.isEmpty())
        	designAnchor.getChildren().clear();
        
        designButtonCreateInput.getItems().clear();
	}
	
	
	
	
	//*********************
	// MQTT EVENTS
	//*********************
	private void onConnectionChanged(ConnectionEvent e) {	
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onConnectionChanged(e);
				}
			});
			return;
		}
		
		/*
		model.deposit(100);
		model.getListItems().add("deposit");
		model.getTableItems().clear();
		model.getTableItems().add(new KeyValuePairModel("last", "deposit"));
		model.getTableItems().add(new KeyValuePairModel("ts", DateTime.now().toString()));
		*/
		
		if(e.getChange() == Change.CONNECTING) {
			clearDesignPane();
			mqttStatusLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
			mqttStatusLabel.setText(e.getMessage());
		} else if(e.getChange() == Change.CONNECTSUCCESS) {
			sample1();
			fillDesignPane();
			mqttStatusLabel.setTextFill(javafx.scene.paint.Color.BLACK);
			mqttStatusLabel.setText(e.getMessage());
		} else if (e.getChange() == Change.CONNECTFAILURE || e.getChange() == Change.DISCONNECT) {	
			clearDesignPane();
			mqttStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
			mqttStatusLabel.setText(e.getMessage());
		}
	}
	
	@Override
	public void onSubscriptionArrived(SubscriptionEvent subscriptionEvent) {
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onSubscriptionArrived(subscriptionEvent);
				}
			});
			return;
		}

		// print in the status
		model.getListItems().add(subscriptionEvent.getObj().toString());
		
		if(0 == subscriptionEvent.getObjType().compareToIgnoreCase(EngineStatus.class.getSimpleName()))
			onEngineStatus(subscriptionEvent);
		
	}
	
	private void onEngineStatus(SubscriptionEvent subscriptionEvent) {
		EngineStatus es = (EngineStatus) subscriptionEvent.getObj();
		
		// update an existing status
		for(EngineStatusModel esm : engineStatusModelList) {
			if(0 == esm.getIdProperty().getValue().compareToIgnoreCase(es.getId())) {
				esm.update(es);
				return;
			}
		}
		
		// add a new status
		engineStatusModelList.add(new EngineStatusModel(es));
	}
		
	
	
	
	
	
	
	
	
	public void sample1() {
		activeDesign = new OperationalFlow(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());

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
		activeDesign.getProcesses().add(n1);

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
		activeDesign.getProcesses().add(n2);

		// INPUTS----------------------
		activeDesign.getTypes().add(new Type(1, 1, "Lidar", new Properties(), null, new ArrayList<>() {
			{
				add(Integer.valueOf(2));
				add(Integer.valueOf(5));
				add(Integer.valueOf(7));
			}
		}, true));
		activeDesign.getTypes().add(new Type(4, 1, "Camera", new Properties(), null, new ArrayList<>() {
			{
				add(Integer.valueOf(2));
				add(Integer.valueOf(5));
				add(Integer.valueOf(7));
			}
		}, true));

		// LOGICS-------------------------
		activeDesign.getTypes().add(new Type(2, 1, "WWVD", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
				add(Integer.valueOf(4));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
				add(Integer.valueOf(6));
				add(Integer.valueOf(8));
			}
		}, false));
		activeDesign.getTypes().add(new Type(5, 1, "Curve Speed", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
				add(Integer.valueOf(4));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
				add(Integer.valueOf(6));
				add(Integer.valueOf(8));
			}
		}, false));
		activeDesign.getTypes().add(new Type(7, 1, "Queue Detection", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
				add(Integer.valueOf(4));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
				add(Integer.valueOf(6));
				add(Integer.valueOf(8));
			}
		}, false));

		// OUTPUTS -------------
		activeDesign.getTypes().add(new Type(3, 1, "Email", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null, false));
		activeDesign.getTypes().add(new Type(6, 1, "Flashing Beacon", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null, false));
		activeDesign.getTypes().add(new Type(8, 1, "Digital Output", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null, false));

		// https://coolors.co/233d4d-915e3d-fe7f2d-fda53a-fcca46-cfc664-a1c181-619b8a
		activeDesign.getStyles().add(new Style(1, 1, new Color(0x23, 0x3D, 0x4D, 1), new Color(255, 255, 255, 1))); // Charcoal
		activeDesign.getStyles().add(new Style(2, 1, new Color(0x91, 0x5E, 0x3D, 1), new Color(255, 255, 255, 1))); // Coyote Brown
		activeDesign.getStyles().add(new Style(3, 1, new Color(0xFE, 0x7F, 0x2D, 1), new Color(255, 255, 255, 1))); // Pumpkin
		activeDesign.getStyles().add(new Style(4, 1, new Color(0xFD, 0xA5, 0x3A, 1), new Color(0, 0, 0, 1))); // Yellow Orange
		activeDesign.getStyles().add(new Style(5, 1, new Color(0xFC, 0xCA, 0x46, 1), new Color(0, 0, 0, 1))); // Sunglow
		activeDesign.getStyles().add(new Style(6, 1, new Color(0xCF, 0xC6, 0x64, 1), new Color(0, 0, 0, 1))); // Straw
		activeDesign.getStyles().add(new Style(7, 1, new Color(0xA1, 0xC1, 0x81, 1), new Color(0, 0, 0, 1))); // Olivine
		activeDesign.getStyles().add(new Style(8, 1, new Color(0x61, 0x9B, 0x8A, 1), new Color(0, 0, 0, 1))); // Polished Pine

		activeDesign.getTypeStyle().put(1, 1);
		activeDesign.getTypeStyle().put(2, 2);
		activeDesign.getTypeStyle().put(3, 3);
		activeDesign.getTypeStyle().put(4, 4);
		activeDesign.getTypeStyle().put(5, 5);
		activeDesign.getTypeStyle().put(6, 6);
		activeDesign.getTypeStyle().put(7, 7);
		activeDesign.getTypeStyle().put(8, 8);
	}
}