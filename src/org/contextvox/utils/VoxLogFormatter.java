package org.contextvox.utils;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.contextvox.services.ttsengine.VoxBridge;

public class VoxLogFormatter extends Formatter {

	private static final String IO_LEVEL_LABEL = "IO";

	@Override
	public String format(final LogRecord record) {
		final String levelName = levelname(record);
		final String className = record.getSourceClassName();
		final String methodName = record.getSourceMethodName();
		final String mess = record.getMessage();

		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(record.getMillis());
		final int h = calendar.get(Calendar.HOUR_OF_DAY);
		final int m = calendar.get(Calendar.MINUTE);
		final int s = calendar.get(Calendar.SECOND);

		return String.format("%s - %s.%s (%02d:%02d:%02d)%n%s%n", levelName, className, methodName, h, m, s, mess);
	}

	private String levelname(final LogRecord record) {
		final String levelName;
		final String source = record.getSourceClassName();
		if (source.equals(VoxBridge.class.getName()))
			levelName = IO_LEVEL_LABEL;
		else
			levelName = LogLevel.findMatcher(record.getLevel()).toString();
		return levelName;
	}

}
