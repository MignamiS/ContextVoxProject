package org.contextvox.services.replacer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Retreives camel case, to help less sophisticate TTS engines to better read
 * Java code style. Composed words such as <code>TextEditor</code> are separate
 * as <code>text editor</code>.
 *
 * @author Simone Mignami
 *
 */
class CamelCaseReplacer implements Replacer {

	private static final String CAMEL_CASE_REGEX = "([a-z]*([A-Z]\\w*)+)";

	@Override
	public void replace(final RawMessage data) {
		final StringBuilder sb = new StringBuilder(data.getText());
		final Pattern pattern = Pattern.compile(CAMEL_CASE_REGEX);
		final Matcher matcher = pattern.matcher(sb);

		while (matcher.find()) {
			// find next word with a camel case
			final String word = matcher.group(1);
			final int start = matcher.start();
			final int end = matcher.end();

			// separate composite sub-words
			final StringBuilder newSB = new StringBuilder();
			for (int i = 0; i < word.length(); i++) {
				if (Character.isUpperCase(word.charAt(i))) {
					// lowercase avoid matcher to match it again
					final char newChar = Character.toLowerCase(word.charAt(i));
					newSB.append(" " + newChar);
				} else
					newSB.append(word.charAt(i));
			}

			sb.replace(start, end, newSB.toString());
			matcher.reset();
		}

		data.setText(sb.toString());
	}

}
