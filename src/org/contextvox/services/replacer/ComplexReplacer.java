package org.contextvox.services.replacer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This component will replace some complex construct of the programming
 * language, such as array declaration, array access or parameterized types.
 */
class ComplexReplacer implements Replacer {

	@Override
	public void replace(final RawMessage data) {
		final StringBuilder sb = new StringBuilder(data.getText());
		searchGenerics(sb);
		searchArrayNotation(sb);

		data.setText(sb.toString());
	}

	private void searchArrayNotation(final StringBuilder sb) {
		// look for array definition statement
		final String rxDef = "(\\w+)\\[\\]";
		Pattern p = Pattern.compile(rxDef);
		Matcher m = p.matcher(sb);

		if (m.find()) {
			final String type = m.group(1);
			// TODO remove hardcoded version, use a sentence
			final int start = m.start();
			final int end = m.end();
			sb.replace(start, end, "array of " + type);
		}

		// look for array access statement
		final String rxAccess = "(\\w+)\\[(\\w+)\\]";
		p = Pattern.compile(rxAccess);
		m = p.matcher(sb);

		while (m.find()) {
			final String name = m.group(1);
			final String index = m.group(2);
			// TODO hardcoded version, use sentence
			final int start = m.start();
			final int end = m.end();
			sb.replace(start, end, String.format("array %s at index %s", name, index));
			m.reset();
		}
	}

	private void searchGenerics(final StringBuilder sb) {
		final String regex = "(\\w+)<([\\w|\\s|,]+|\\w+|\\w+,\\s*\\w+)>";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(sb);

		while (m.find()) {
			final String col = m.group(1);
			String type = m.group(2);
			type = type.replaceAll(",", " and ");
			final int start = m.start();
			final int end = m.end();
			// TODO remove hardcoded version, use a sentence
			sb.replace(start, end, col + " of " + type);
			m.reset();
		}

	}

}
