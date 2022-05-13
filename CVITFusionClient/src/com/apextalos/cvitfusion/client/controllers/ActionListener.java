package com.apextalos.cvitfusion.client.controllers;

import com.apextalos.cvitfusion.client.controllers.BaseController.EventType;

public interface ActionListener {
	public void onActionPerformed(Object o, EventType et);
}
