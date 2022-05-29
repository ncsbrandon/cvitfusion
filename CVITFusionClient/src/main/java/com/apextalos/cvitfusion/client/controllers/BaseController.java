package com.apextalos.cvitfusion.client.controllers;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

public abstract class BaseController implements Initializable, ActionListener {

	public enum EventType {
		SELECTED, DESELECTED, ENABLED, DISABLED
	}

	protected ConfigFile cf = null;
	
	public void begin(ConfigFile cf, Stage stage) {
		this.cf = cf;
	}

	public void end(Stage stage) {
	}

	private final List<ActionListener> listeners = new ArrayList<>();

	public void addActionListener(ActionListener l) {
		listeners.add(l);
	}

	public void actionPerformed(Object o, EventType et) {
		for (ActionListener l : listeners) {
			l.onActionPerformed(o, et);
		}
	}
}
