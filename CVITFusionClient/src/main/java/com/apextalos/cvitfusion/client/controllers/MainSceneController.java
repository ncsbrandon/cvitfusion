package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ArrayList;
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
import com.apextalos.cvitfusion.client.models.MainSceneModel;
import com.apextalos.cvitfusion.client.models.ParameterModel;
import com.apextalos.cvitfusion.client.mqtt.ClientConfigMqttTransceiver;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigResponseGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigResultGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusGuiListener;
import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent.Change;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionListener;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Parameter.Form;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainSceneController extends BaseController implements EngineStatusGuiListener, EngineConfigResponseGuiListener, EngineConfigResultGuiListener {

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
    @FXML private Button designButtonSave;
    @FXML private VBox parameterEditVBox;
    @FXML private VBox propertiesVbox;
    @FXML private TableView<ParameterModel> parametersTable;
    @FXML private TableColumn<Object, Object> parametersColumnKey;
    @FXML private TableColumn<Object, Object> parametersColumnValue;
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
	private ResourceLoader resourceLoader = new ResourceLoader();
	
	//*********************
	// SCENE EVENTS
	//*********************
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// create model
		model = new MainSceneModel(1000d);

		// node status view
		statusListView.setItems(model.getStatusListItems());
		
		// properties view
		parametersColumnKey.setCellValueFactory(new PropertyValueFactory<>("key"));
		parametersColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
		parametersTable.setRowFactory(new Callback<TableView<ParameterModel>, TableRow<ParameterModel>>() {
			@Override
			public TableRow<ParameterModel> call(TableView<ParameterModel> param) {
				return new TableRow<ParameterModel>() {
					@Override
					protected void updateItem(ParameterModel item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null && item.isChanged()) {
							setStyle("-fx-font-weight: bold");
						} else {
							setStyle("");
						}
					}
				};
			}
		});
		parametersTable.setItems(model.getParameterListItems());
		parametersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParameterModel>() {
			@Override
			public void changed(ObservableValue<? extends ParameterModel> observable, ParameterModel oldValue, ParameterModel newValue) {
				onParameterSelected(newValue);				
			}
		});

		// version view
		versionInfo.setText(String.format("%s.%s", Version.getInstance().getVersion(), Version.getInstance().getBuild()));

		// engine status view
		engineStatusListView.setCellFactory(new Callback<ListView<EngineStatusModel>, ListCell<EngineStatusModel>>() {
		    @Override
		    public ListCell<EngineStatusModel> call(ListView<EngineStatusModel> engineStatusListView) {
		        return new EngineStatusListViewCell();
		    }
		});
		engineStatusListView.setItems(engineStatusModelList);
		engineStatusListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EngineStatusModel>() {
			@Override
			public void changed(ObservableValue<? extends EngineStatusModel> observable, EngineStatusModel oldValue, EngineStatusModel newValue) {
				onEngineStatusSelected(newValue);
			}
		});
		engineStatusListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue)
					fillStatusList(engineStatusListView.getSelectionModel().getSelectedItem());
			}
		});
		
		// bind heights to create window fill
		engineStatusListView.prefHeightProperty().bind(enginesVbox.heightProperty());
		parametersTable.prefHeightProperty().bind(propertiesVbox.heightProperty());
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
		
		noDesignSelection();
		noParameters();
		clearDesignPane();
		
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
	@FXML protected void onActionDisconnectMenu(ActionEvent event) {
		// go back to connections
		Stage stage = (Stage)topBorderPane.getScene().getWindow();
		SceneManager.getInstance(cf).showConnections(stage);
	}
	
	@FXML protected void onActionCloseMenu(ActionEvent event) {
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
		esm.getImageProperty().set(resourceLoader.loadImageByFilename(ResourceLoader.IMAGE_REFRESH));
		// make it spin
		esm.setBusy(true);
		// create a subscription, publish a request, and callback on the response
		ccmt.requestConfig(esm.getIdProperty().getValue(), this);
		// clear the selections
		noDesignSelection();
		noParameters();
		clearDesignPane();
		fillStatusList(esm);
	}
	
	
	//*********************
	// DESIGN PANE EVENTS
	//*********************
	@FXML protected void onDesignPaneMouseClicked(MouseEvent mouseEvent) {
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
		
		model.getParameterListItems().clear();
		model.getParameterListItems().add(new ParameterModel("From", String.format("%s %d", parentType.getName(), parentProcess.getProcessID())));
		model.getParameterListItems().add(new ParameterModel("To", String.format("%s %d", childType.getName(), childProcess.getProcessID())));
		
		model.getStatusListItems().clear();
	}
	
	private void onLineDeselection(Line line) {
		line.setEffect(null);
		noDesignSelection();
	}
	
	private void onProcessSelection(DiagramNodeControl dnc) {
		dnc.getController().select(true);
		designSelection = dnc;
		
		DiagramNodeController dncController = dnc.getController();
		DiagramNodeModel dncModel = dncController.getModel();
		
		Process process = activeDesign.lookupProcess(Integer.valueOf(dncModel.getIDProperty().get()));
		Type type = activeDesign.lookupType(process.getTypeID());
		
		fillParameterTable(process, type);
		fillStatusList(process, type);
		
		// add output button
		designButtonAddOutput.setVisible(type.hasSupportedOutputs());
		designButtonAddOutput.getItems().clear();
		if(type.hasSupportedOutputs()) {
			for(Integer id : type.getSupportedOutputs()) {
				Type outputType = activeDesign.lookupType(id);
				if(outputType == null)
					continue; // probably it wasn't licensed
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
	
	private void fillParameterTable(Process process, Type type) {
		// read-only
		model.getParameterListItems().clear();
		model.getParameterListItems().add(new ParameterModel("Process", String.format("%s %d", type.getName(), process.getProcessID())));	
		model.getParameterListItems().add(new ParameterModel("Version", String.valueOf(type.getVersion())));
		if(process.hasChildren())
			model.getParameterListItems().add(new ParameterModel("# Children", String.format("%s", process.getChildren().size())));
		else
			model.getParameterListItems().add(new ParameterModel("Children", "none"));
		model.getParameterListItems().add(new ParameterModel("Enabled", String.valueOf(process.isEnabled())));
		
		// changeable parameters
		for(Parameter parameter : type.getParameters()) {
			model.getParameterListItems().add(new ParameterModel(
					parameter,
					type,
					process,
					process.getPropertyValue(parameter.getParameterID())
					));
		}
	}
	
	private void fillStatusList(Process process, Type type) {
		model.getStatusListItems().clear();
		model.getStatusListItems().add(process.toString());
		model.getStatusListItems().add(type.toString());
	}
	
	private void fillStatusList(EngineStatusModel esm) {
		model.getStatusListItems().clear();
		model.getStatusListItems().add(esm.toString());
	}
	
	private void onProcessDeselection(DiagramNodeControl dnc) {
		dnc.getController().select(false);
		noDesignSelection();
	}
	
	private void noDesignSelection() {
		designSelection = null;
		model.getParameterListItems().clear();
		model.getStatusListItems().clear();
		
		designButtonAddOutput.setVisible(false);
		designButtonDisable.setVisible(false);
		designButtonRemove.setVisible(false);
	}
	
	@FXML private void onDesignButtonDisable(ActionEvent event) {
		logger.info("onDesignButtonDisable");

		Process process = (Process) designSelection.getUserData();

		// toggle the enabled status
		process.setEnabled(!process.isEnabled());
		
		// update the design
		fillDesignPane();
		
		// update the disable/enable button
		designButtonDisable.setText(process.isEnabled() ? "Disable" : "Enable");
		
		// automatically re-select it
		DiagramNodeControl dnc = findDiagramNodeControl(process);
		if(dnc != null) {
			onActionPerformed(null, EventType.DESELECTED);
			onProcessSelection(dnc);
		}
		
		// we have changes to save
		designButtonSave.setDisable(false);
	}
	
	@FXML private void onDesignButtonRemove(ActionEvent event) {
		logger.info("onDesignButtonRemove");

		Process process = (Process) designSelection.getUserData();

		// remove the process
		activeDesign.removeProcess(process);

		// remove selection
		onActionPerformed(null, EventType.DESELECTED);

		// update the design
		fillDesignPane();
		
		// we have changes to save
		designButtonSave.setDisable(false);
	}
	
	@FXML private void onDesignButtonAddOutput(ActionEvent event) {
		logger.info("onDesignButtonAddOutput " + ((Type) ((MenuItem) event.getSource()).getUserData()).getName());

		Process parentProcess = (Process) designSelection.getUserData();
		Type childType = (Type) ((MenuItem) event.getSource()).getUserData();
		Process childProcess = new Process(parentProcess.nextChildID(), true, childType.getTypeID(), null, "", new Properties());

		// add it to the children
		if (parentProcess.getChildren() == null)
			parentProcess.setChildren(new ArrayList<>(List.of(childProcess)));
		else
			parentProcess.getChildren().add(childProcess);
		
		// update the design
		fillDesignPane();
		
		// automatically select it
		DiagramNodeControl dnc = findDiagramNodeControl(childProcess);
		if(dnc != null) {
			onActionPerformed(null, EventType.DESELECTED);
			onProcessSelection(dnc);
		}
		
		// we have changes to save
		designButtonSave.setDisable(false);
	}
	
	@FXML private void onDesignButtonSave(ActionEvent event) {
		EngineStatusModel esm = engineStatusListView.getSelectionModel().getSelectedItem();
		if(esm == null)
			return;
		
		// set it to the refresh arrow
		esm.getImageProperty().set(resourceLoader.loadImageByFilename(ResourceLoader.IMAGE_SAVE));
		// make it spin
		esm.setBusy(true);
		// create a subscription, publish a request, and callback on the response
		ccmt.saveConfig(esm.getIdProperty().getValue(), activeDesign, this);
	}
	
	@FXML private void onDesignButtonCreateInput(ActionEvent event) {
		logger.info("onDesignButtonCreateInput " + ((Type) ((MenuItem) event.getSource()).getUserData()).getName());

		Type type = (Type) ((MenuItem) event.getSource()).getUserData();
		Process process = new Process(activeDesign.getProcesses().size() + 1, true, type.getTypeID(), null, "", null);

		// add it to the top level
		activeDesign.getProcesses().add(process);
		
		// update the design
		fillDesignPane();
		
		// automatically select it
		DiagramNodeControl dnc = findDiagramNodeControl(process);
		if(dnc != null) {
			onActionPerformed(null, EventType.DESELECTED);
			onProcessSelection(dnc);
		}
		
		// we have changes to save
		designButtonSave.setDisable(false);
	}
	
	private void fillDesignPane() {
		// clear the pane
		ObservableList<Node> children = designAnchor.getChildren();
		if (children != null && !children.isEmpty())
			designAnchor.getChildren().clear();

		// layout the design and add it to the pane
		designAnchor.getChildren().addAll(db.layout(activeDesign, this));

		// clear the create input button
		designButtonCreateInput.getItems().clear();
		designButtonCreateInput.setDisable(false);
		
		// create input button
		List<Type> topLevelTypes = activeDesign.getTopLevelTypes();
		if (topLevelTypes != null && !topLevelTypes.isEmpty()) {
			for (Type topLevelType : topLevelTypes) {
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
		if (children != null && !children.isEmpty())
			designAnchor.getChildren().clear();

		// clear the create input button
		designButtonCreateInput.setDisable(true);
		designButtonCreateInput.getItems().clear();
		
		designButtonSave.setDisable(true);
	}
	
	private DiagramNodeControl findDiagramNodeControl(Process process) {
		// this is a reverse lookup from the Node to the control representing it
		ObservableList<Node> children = designAnchor.getChildren();
		if (children == null || children.isEmpty())
			return null;
		
		for(Node n : children) {
			if(n.getUserData() == process)
				return (DiagramNodeControl) n;
		}
			
		return null;
	}
	
	
	// *********************
	// PARAMETER EVENTS
	// *********************
	private void onParameterSelected(ParameterModel newValue) {
		parameterEditVBox.getChildren().clear();
		
		// selection removed or read-only
		if(newValue == null || newValue.getParameter() == null)
			return;
		
		if(newValue.getParameter().getForm() == Form.STRINGLIST) {
			FXMLResource fr = resourceLoader.createLoader("parameterChoice.fxml", null);
			ParameterChoiceController parameterChoiceController = fr.getLoader().getController();
			Pane parameterChoicePane = (Pane) fr.getResource();
			parameterChoiceController.setParameterModel(newValue);
			parameterEditVBox.getChildren().add(parameterChoicePane);
			
			parameterChoiceController.addActionListener(new ActionListener() {
				@Override
				public void onActionPerformed(Object o, EventType et) {
					parametersTable.refresh();
					designButtonSave.setDisable(false);
					fillDesignPane();
				}
			});
		} else if(newValue.getParameter().getForm() == Form.BOOLEAN) {
			FXMLResource fr = resourceLoader.createLoader("parameterBoolean.fxml", null);
			ParameterBooleanController parameterBooleanController = fr.getLoader().getController();
			Pane parameterBooleanPane = (Pane) fr.getResource();
			parameterBooleanController.setParameterModel(newValue);
			parameterEditVBox.getChildren().add(parameterBooleanPane);
			
			parameterBooleanController.addActionListener(new ActionListener() {
				@Override
				public void onActionPerformed(Object o, EventType et) {
					parametersTable.refresh();
					designButtonSave.setDisable(false);
					fillDesignPane();
				}
			});
		} else if(newValue.getParameter().getForm() == Form.STRING) {
			FXMLResource fr = resourceLoader.createLoader("parameterString.fxml", null);
			ParameterStringController parameterStringController = fr.getLoader().getController();
			Pane parameterStringPane = (Pane) fr.getResource();
			parameterStringController.setParameterModel(newValue);
			parameterEditVBox.getChildren().add(parameterStringPane);
			
			parameterStringController.addActionListener(new ActionListener() {
				@Override
				public void onActionPerformed(Object o, EventType et) {
					parametersTable.refresh();
					designButtonSave.setDisable(false);
					fillDesignPane();
				}
			});
			
		} else if(newValue.getParameter().getForm() == Form.INTEGER) {
			FXMLResource fr = resourceLoader.createLoader("parameterInteger.fxml", null);
			ParameterIntegerController parameterIntegerController = fr.getLoader().getController();
			Pane parameterIntegerPane = (Pane) fr.getResource();
			parameterIntegerController.setParameterModel(newValue);
			parameterEditVBox.getChildren().add(parameterIntegerPane);
			
			parameterIntegerController.addActionListener(new ActionListener() {
				@Override
				public void onActionPerformed(Object o, EventType et) {
					parametersTable.refresh();
					designButtonSave.setDisable(false);
					fillDesignPane();
				}
			});
		} else if(newValue.getParameter().getForm() == Form.DECIMAL) {
			FXMLResource fr = resourceLoader.createLoader("parameterDecimal.fxml", null);
			ParameterDecimalController parameterDecimalController = fr.getLoader().getController();
			Pane parameterDecimalPane = (Pane) fr.getResource();
			parameterDecimalController.setParameterModel(newValue);
			parameterEditVBox.getChildren().add(parameterDecimalPane);
			
			parameterDecimalController.addActionListener(new ActionListener() {
				@Override
				public void onActionPerformed(Object o, EventType et) {
					parametersTable.refresh();
					designButtonSave.setDisable(false);
					fillDesignPane();
				}
			});
		} else if(newValue.getParameter().getForm() == Form.EMAIL) {
			FXMLResource fr = resourceLoader.createLoader("parameterEmail.fxml", null);
			ParameterEmailController parameterEmailController = fr.getLoader().getController();
			Pane parameterEmailPane = (Pane) fr.getResource();
			parameterEmailController.setParameterModel(newValue);
			parameterEditVBox.getChildren().add(parameterEmailPane);
			
			parameterEmailController.addActionListener(new ActionListener() {
				@Override
				public void onActionPerformed(Object o, EventType et) {
					parametersTable.refresh();
					designButtonSave.setDisable(false);
					fillDesignPane();
				}
			});
		}
	}
	
	private void noParameters() {
		parameterEditVBox.getChildren().clear();
	}
	
	
	// *********************
	// MQTT EVENTS
	// *********************
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
		 * model.deposit(100); model.getListItems().add("deposit");
		 * model.getTableItems().clear(); model.getTableItems().add(new
		 * KeyValuePairModel("last", "deposit")); model.getTableItems().add(new
		 * KeyValuePairModel("ts", DateTime.now().toString()));
		 */

		if (e.getChange() == Change.CONNECTING) {
			clearDesignPane();
			mqttStatusLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
			mqttStatusLabel.setText(e.getMessage());
		} else if (e.getChange() == Change.CONNECTSUCCESS) {
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
		model.getStatusListItems().add(es.toString());

		// 1 - update an existing status
		EngineStatusModel esm = null;
		for (EngineStatusModel e : engineStatusModelList) {
			if (0 == e.getIdProperty().getValue().compareToIgnoreCase(es.getId())) {
				e.update(es);
				esm = e;
			}
		}
		// OR 2 - add a new status
		if (esm == null) {
			esm = new EngineStatusModel(es);
			engineStatusModelList.add(esm);
		}

		// ok status
		esm.getImageProperty().set(resourceLoader.loadImageByFilename(ResourceLoader.IMAGE_ACCEPT));
		esm.setBusy(false);
	}
	
	@Override
	public void onEngineConfigRequest(String engineID, String topic, String payload, OperationalFlow engineConfig) {
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onEngineConfigRequest(engineID, topic, payload, engineConfig);
				}
			});
			return;
		}

		// we are done with this subscription
		ccmt.requestConfigComplete(topic);

		// remove the spinner
		for (EngineStatusModel esm : engineStatusListView.getItems()) {
			if (0 == esm.getIdProperty().get().compareToIgnoreCase(engineID)) {
				// stop spinning
				esm.setBusy(false);
				// ok status
				esm.getImageProperty().set(resourceLoader.loadImageByFilename(ResourceLoader.IMAGE_ACCEPT));
				esm.setBusy(false);
			}
		}

		// show context
		activeDesign = engineConfig;
		fillDesignPane();
		
	}

	@Override
	public void onEngineConfigSave(String engineID, String topic, String payload, boolean result) {
		// if this event is coming from another thread (MQTT)
		// run it later on the GUI thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					onEngineConfigSave(engineID, topic, payload, result);
				}
			});
			return;
		}

		// we are done with this subscription
		ccmt.saveConfigComplete(topic);
		
		// remove the spinner
		for (EngineStatusModel esm : engineStatusListView.getItems()) {
			if (0 == esm.getIdProperty().get().compareToIgnoreCase(engineID)) {
				// stop spinning
				esm.setBusy(false);
				// ok status
				esm.getImageProperty().set(resourceLoader.loadImageByFilename(ResourceLoader.IMAGE_ACCEPT));
				esm.setBusy(false);
			}
		}

		// clear changes from the design
		activeDesign.clearChanges();
		fillDesignPane();
		
		parametersTable.refresh();
		
		// no changes to save
		designButtonSave.setDisable(true);
	}
}