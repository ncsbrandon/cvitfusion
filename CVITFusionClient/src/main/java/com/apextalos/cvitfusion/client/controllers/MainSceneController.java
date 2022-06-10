package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
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
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusGuiListener;
import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent.Change;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionListener;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.ProcessLink;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainSceneController extends BaseController implements EngineStatusGuiListener, EngineConfigGuiListener {

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
    @FXML private Label messageStats;
    
    // sub-models
	private ObservableList<EngineStatusModel> engineStatusModelList = FXCollections.observableArrayList();
	private DiagramBuilder db = new DiagramBuilder();
	private Node designSelection = null;
	private OperationalFlow activeDesign = null;
	
	// services
	private ClientConfigMqttTransceiver ccmt;
	private Timer guiUpdateTimer;
	private ResourceLoader<Image> imageLoader = new ResourceLoader<>();
	
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
				onUpdateTimer();
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
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onUpdateTimer();
				}
			});
			return;
		}
		
		// update the engine status "since last update"  with it's "last update" time
		for(EngineStatusModel esm : engineStatusModelList) {
			esm.getSinceLastUpdateProperty().set(DateTimeUtils.timeSinceLastUpdate(esm.getLastUpdate(), DateTime.now()));
			if(esm.getBusy())
				esm.getSpinProperty().set(esm.getSpinProperty().get() + 15);
		}
		
		updateMessageStats();
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
	// STATUS BAR EVENTS
	//*********************
	protected void updateMessageStats() {
		messageStats.setText(String.format("Sub[%d:%d] Pub[%d:%d]",
				ccmt.getActiveSubscriptions().size(),
				ccmt.getReceivedCount(),
				ccmt.getActivePublications().size(),
				ccmt.getSentCount()
				));
	}
	
	
	//*********************
	// ENGINE EVENTS
	//*********************
	protected void onEngineStatusSelected(EngineStatusModel esm) {
		// set it to the refresh arrow
		esm.getImageProperty().set(imageLoader.loadImage("refresh.png"));
		// make it spin
		esm.setBusy(true);
		// create a subscription, pubish a request, and callback on the repsonse
		ccmt.requestConfig(esm.getIdProperty().getValue(), this);
		// clear the selections
		clearDesignPane();
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
		
		// add output button
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
		
		// disable/enable button
		designButtonDisable.setVisible(true);
		designButtonDisable.setText(process.isEnabled() ? "Disable" : "Enable");
		
		// remove button
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
    private void onDesignButtonDisable(ActionEvent event) {
    	logger.info("onDesignButtonDisable");
    	
    	Process process = (Process) designSelection.getUserData();
    	
    	process.setEnabled(!process.isEnabled());
    	fillDesignPane();
    }

    @FXML
    private void onDesignButtonRemove(ActionEvent event) {
    	logger.info("onDesignButtonRemove");
    	
    	Process process = (Process) designSelection.getUserData();
    	
    	activeDesign.removeProcess(process);
    	fillDesignPane();
    }
    
    @FXML
    private void onDesignButtonAddOutput(ActionEvent event) {
    	logger.info("onDesignButtonAddOutput " + ((Type)((MenuItem)event.getSource()).getUserData()).getName());
    	
    	Process parentProcess = (Process) designSelection.getUserData();	    	
    	Type childType = (Type) ((MenuItem)event.getSource()).getUserData();
    	Process childProcess = new Process(parentProcess.nextChildID(), true, childType.getTypeID(), null, "", 0, new Properties());
    			
    	parentProcess.getChildren().add(childProcess);
    	fillDesignPane();
    }
    
    @FXML
    private void onDesignButtonCreateInput(ActionEvent event) {
    	logger.info("onDesignButtonCreateInput " + ((Type)((MenuItem)event.getSource()).getUserData()).getName());
    	
    	Type type = (Type) ((MenuItem)event.getSource()).getUserData();
    	Process process = new Process(activeDesign.getProcesses().size() + 1, true, type.getTypeID(), null, "", 0, null);
    	
    	activeDesign.getProcesses().add(process);
    	fillDesignPane();
    }
	
	private void fillDesignPane() {
		// clear the pane
		ObservableList<Node> children = designAnchor.getChildren();
        if(children != null && !children.isEmpty())
        	designAnchor.getChildren().clear();
        
		// layout the design and add it to the pane
		designAnchor.getChildren().addAll(db.layout(activeDesign, this));
		
		// clear the create input button
		designButtonCreateInput.getItems().clear();
		
		// create input button
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
		// not active design
		activeDesign = null;
		
		// clear the pane
		ObservableList<Node> children = designAnchor.getChildren();
        if(children != null && !children.isEmpty())
        	designAnchor.getChildren().clear();
        
        // clear the create input button
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
			mqttStatusLabel.setTextFill(javafx.scene.paint.Color.BLACK);
			mqttStatusLabel.setText(e.getMessage());
		} else if (e.getChange() == Change.CONNECTFAILURE || e.getChange() == Change.DISCONNECT) {	
			clearDesignPane();
			mqttStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
			mqttStatusLabel.setText(e.getMessage());
		}
	}
		
	@Override
	public void onEngineStatus(String topic, String payload, EngineStatus es) {
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onEngineStatus(topic, payload, es);
				}
			});
			return;
		}
						
		// print in the status
		model.getListItems().add(es.toString());
				
		// 1 - update an existing status
		EngineStatusModel esm = null;
		for(EngineStatusModel e : engineStatusModelList) {
			if(0 == e.getIdProperty().getValue().compareToIgnoreCase(es.getId())) {
				e.update(es);
				esm = e;
			}
		}		
		// OR 2 - add a new status
		if(esm == null) {
			esm = new EngineStatusModel(es);
			engineStatusModelList.add(esm);
		}
		
		// ok status
		esm.getImageProperty().set(imageLoader.loadImage("accept.png"));
	}
	
	@Override
	public void onEngineConfig(String engineID, String topic, String payload, OperationalFlow engineConfig) {
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onEngineConfig(engineID, topic, payload, engineConfig);
				}
			});
			return;
		}
				
		// we are done with this subscription
		ccmt.requestConfigComplete(topic);
		
		// remove the spinner
		for(EngineStatusModel esm : engineStatusListView.getItems()) {
			if(0 == esm.getIdProperty().get().compareToIgnoreCase(engineID)) {
				// stop spinning
				esm.setBusy(false);
				// ok status
				esm.getImageProperty().set(imageLoader.loadImage("accept.png"));
			}
		}
		
		// show context
		activeDesign = engineConfig;
		fillDesignPane();
	}
}