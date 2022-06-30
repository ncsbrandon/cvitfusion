package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

public abstract class Processor {

	private Properties properties;
	private ConfigFile cf;
	private List<Processor> children = new ArrayList<>();
	
	protected Processor(Properties properties, ConfigFile cf) {
		this.properties = properties;
		this.cf = cf;
	}
	
	public void addListener(Processor child) {
		children.add(child);
	}
	
	public abstract void start();
	public abstract void stop();
}
