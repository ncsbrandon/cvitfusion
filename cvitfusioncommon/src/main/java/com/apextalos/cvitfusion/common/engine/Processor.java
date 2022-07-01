package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

public abstract class Processor {

	private Process process;
	private ConfigFile cf;
	private List<Processor> children = new ArrayList<>();
	
	protected Processor(Process process, ConfigFile cf) {
		this.process = process;
		this.cf = cf;
	}
	
	public void addListener(Processor child) {
		children.add(child);
	}
	
	public void start() {
		if(process.isEnabled())
			enabled();
	}
	
	public abstract void enabled();
	public abstract void stop();
}
