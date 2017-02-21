package org.contextvox.services.replacer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This component makes the string more readable and removes duplicated spaces,
 * tabulations and useless carriage return.
 */
class SpaceCleanerReplacer implements Replacer {

	@Override
	public void replace(final RawMessage data) {
		final StringBuilder sb = new StringBuilder(data.getText());
		final Pattern p = Pattern.compile("[\\s]{2,}");
		final Matcher m = p.matcher(sb);

		while (m.find()) {
			final int start = m.start();
			final int end = m.end();
			sb.replace(start, end, " ");
			m.reset();
		}

		data.setText(sb.toString());
	}

}
