package org.contextvox.services.replacer.dictionnaries;

/**
 * Alternatives of a sentence, with more or less information token depending on
 * the context.
 */
public enum SentenceAlternative {
	// TODO remove this enum when finished migrate JSON
	/**
	 * Sentence with many informations.
	 */
	FULL {
		@Override
		public String getPrefix() {
			return "F";
		}
	},
	/**
	 * Light version of a sentence, with less informations.
	 */
	LIGHT {
		@Override
		public String getPrefix() {
			return "L";
		}
	},
	/**
	 * Sentence alternative with partial informations, usually used to punctual
	 * queries.
	 */
	PARTIAL {
		@Override
		public String getPrefix() {
			return "P";
		}
	};

	/**
	 * Returns the corresponding literal prefix as string.
	 *
	 * @return prefix the letter that follows the sentence type
	 */
	public abstract String getPrefix();

	/**
	 * Parses the string and return the corresponding enum type.
	 *
	 * @param string
	 *            the source to parse
	 * @return the corresponding sentence alternative
	 */
	public static SentenceAlternative convertFromSuffix(final String string) {
		// get last letter
		final String prefix = "" + string.charAt(string.length() - 1);
		final SentenceAlternative[] alt = SentenceAlternative.values();
		for (final SentenceAlternative item : alt) {
			if (item.getPrefix().equals(prefix))
				return item;
		}

		throw new IllegalArgumentException("No prefix found for " + string);
	}

	/**
	 * Returns a trimmed version of the string, without the suffix.
	 *
	 * @param source
	 *            the string to be parsed
	 * @return a version of the string without the suffix at the end. Notice
	 *         that this method removes the final part of the string, without
	 *         checking if a suffix is really present
	 */
	public static String trimPrefix(final String source) {
		return source.substring(0, source.length() - 1);
	}
}
