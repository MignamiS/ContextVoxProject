package org.contextvox.services.replacer.dictionnaries;

/**
 * Type of translation, such as sentences, messages, an operator, paranthesis,
 * etc.
 */
public enum MessageType {

	/**
	 * Message that can be parameterized with tokens.
	 */
	SENTENCE("se"),
	/**
	 * Code operators like ++, != or ?
	 */
	OPERATOR("op"), PARENTHESIS("pa"), POINT("po"),
	/**
	 * Specific code keyword (with a translable sense, e.g. class, interface or
	 * extends).
	 */
	KEYWORD("ke"),
	/**
	 * Difference between data types (e.g. double and Double).
	 */
	DATATYPE("ty"),
	/**
	 * Characters such as spaces, tabs and EOL.
	 */
	WHITESPACE("sp"),
	/**
	 * Simple sentence not parameterizable.
	 */
	MESSAGE("me");

	private final String value;

	private MessageType(final String value) {
		this.value = value;
	}

	/**
	 * Return the corresponding string used to determine the type of a message.
	 *
	 * @return String selector
	 */
	public String getTypeSelector() {
		return this.value;
	}

	/**
	 * Convert a string into a MessageType.
	 *
	 * @param selector
	 * @return MessageType
	 */
	public static MessageType getTypeFromSelector(final String selector) {
		final MessageType[] val = MessageType.values();
		for (final MessageType item : val) {
			if (selector.equalsIgnoreCase(item.getTypeSelector()))
				return item;
		}
		// the selector is undefined
		throw new IllegalArgumentException("Selector " + selector + " is undefined");
	}
}