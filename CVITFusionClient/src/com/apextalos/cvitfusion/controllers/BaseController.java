package com.apextalos.cvitfusion.controllers;

import com.apextalos.cvitfusioncommon.settings.ConfigFile;

import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable {

	 private ConfigFile cf;
	    
	 public void begin(ConfigFile cf) {
		 this.cf = cf;
	 }
}
