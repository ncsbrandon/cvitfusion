package com.apextalos.cvitfusion.common.utils;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class DateTimeUtils {

	private DateTimeUtils() {
		// prevent instance
	}

	public static String timeSinceLastUpdate(DateTime from, DateTime to) {
		int sec = Seconds.secondsBetween(from, to).getSeconds();
		if (sec < 60)
			return String.format("%ds", sec);
		else if (sec < 3600)
			return String.format("%dm", sec / 60);
		else if (sec < 86400)
			return String.format("%dh", sec / 3600);
		else
			return ">1d";
	}
}
