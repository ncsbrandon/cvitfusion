package com.apextalos.cvitfusion.common.opflow;

public class ProcessComm {

	private Process parentProcess;
	private Process childProcess;
	
	public Process getParentProcess() {
		return parentProcess;
	}

	public void setParentProcess(Process parentProcess) {
		this.parentProcess = parentProcess;
	}

	public Process getChildProcess() {
		return childProcess;
	}

	public void setChildProcess(Process childProcess) {
		this.childProcess = childProcess;
	}

	public ProcessComm(Process parentProcess, Process childProcess) {
		this.parentProcess = parentProcess;
		this.childProcess = childProcess;
	}
}
