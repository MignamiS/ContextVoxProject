package org.contextvox.utils;

import java.util.logging.Level;

/**
 * Defines the level of severity of a message in the Log system
 */
public enum LogLevel {
	/**
	 * An error occurred during an operation of ContextVox
	 */
	ERROR,
	/**
	 * The process can continue, but with care
	 */
	WARNING,
	/**
	 * Informative message
	 */
	INFO,
	/**
	 * Information for developer
	 */
	DEBUG;

	/**
	 * Given the standard Java logging level, returns the respective
	 * ContextVox's log level.
	 *
	 * @param level
	 * @return the corresponding ContextVox log level
	 */
	public static LogLevel findMatcher(final Level level) {
		if (level == Level.SEVERE)
			return ERROR;
		else if (level == Level.WARNING)
			return WARNING;
		else if (level == Level.INFO)
			return INFO;
		else if (level == Level.FINE)
			return DEBUG;
		else
			throw new IllegalArgumentException("No matching for " + level);
	}
}
