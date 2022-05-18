package com.apextalos.cvitfusion.common.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SimpleThread extends Thread {
	
	private static Logger logger = LogManager.getLogger(SimpleThread.class.getSimpleName());

	private boolean isStop = false;
	
	public synchronized void setStop() {
		logger.info("setting stop flag");
		isStop = true;
	}
	
	public synchronized boolean getStop() {
		return isStop;
	}
		
	public synchronized void setStopAndJoin(long millis) 
	{
		logger.info("setting stop flag and joining");
		isStop = true;
		
		try {
			join(millis);
		} catch (InterruptedException e) {
			logger.error("failed to join: " + e.getMessage());
		}
	}
		
	// this is initiated from another thread
	// by the start() method and shouldn't
	// be used directly
	@Override
	public void run() {
		isStop = false;
		super.run();
		running();
	}
	
	// periodically check getStop()
	// to see if the thread should exit
	protected abstract void running();
}