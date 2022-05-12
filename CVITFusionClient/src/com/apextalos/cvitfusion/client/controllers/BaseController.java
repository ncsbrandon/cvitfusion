package com.apextalos.cvitfusion.client.controllers;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable {

	public enum EventType {
		SELECTED
	}
	
	protected ConfigFile cf = null;

	public void begin(ConfigFile cf) {
		this.cf = cf;
	}

	public void end() {
	}

	private final List<BaseController> listeners = new ArrayList<BaseController>();

	public void addActionListener(BaseController l) {
		listeners.add(l);
	}

	public void actionPerformed(Object o, EventType et) {
		for (BaseController l : listeners) {
			l.onActionPerformed(o, et);
		}
	}

	public abstract void onActionPerformed(Object o, EventType et);
}
