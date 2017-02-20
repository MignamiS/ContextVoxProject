package org.contextvox.utils;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Filter for avoid logging of vocal feedback.
 */
public class FeedbackFilter implements Filter {

	@Override
	public boolean isLoggable(final LogRecord record) {
		return record.getSourceClassName().startsWith("org.contextvox");
		// check log
		// final String methodName = record.getSourceMethodName();
		// if (methodName.equals("log")) {
		// // TODO check if DEBUG preference is set
		// return false;
		// }
		// return false;
	}

}
