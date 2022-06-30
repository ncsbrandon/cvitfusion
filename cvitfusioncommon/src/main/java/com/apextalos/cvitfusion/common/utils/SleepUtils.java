package com.apextalos.cvitfusion.common.utils;

import org.apache.logging.log4j.*;

public class SleepUtils {

	private static Logger logger = LogManager.getLogger(SleepUtils.class.getSimpleName());

	private SleepUtils() {
		// prevent instances
	}

	public static boolean safeSleep(long length) {
		try {
			Thread.sleep(length);
		} catch (InterruptedException e) {
			// if name isn't available, use ID
			String strThreadName = Thread.currentThread().getName();
			if (strThreadName == null || strThreadName.length() == 0)
				strThreadName = String.format("No name (ID=%d)", Thread.currentThread().getId());

			logger.info("Thread [{}] was interrupted", strThreadName);
			return false;
		}

		return true;
	}
}
