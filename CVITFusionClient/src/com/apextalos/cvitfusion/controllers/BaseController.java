package com.apextalos.cvitfusion.controllers;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable {

	 protected ConfigFile cf = null;
	    
	 public void begin(ConfigFile cf) {
		 this.cf = cf;
	 }
	 
	 public void end() {
	 }
}
