package com.apextalos.cvitfusion.client.controllers;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable, ActionListener {

	public enum EventType {
		SELECTED, DESELECTED
	}

	protected ConfigFile cf = null;

	public void begin(ConfigFile cf) {
		this.cf = cf;
	}

	public void end() {
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

	public abstract void onActionPerformed(Object o, EventType et);
}
