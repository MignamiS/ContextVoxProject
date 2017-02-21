package org.contextvox.services.replacer.dictionnaries;

/**
 * A complex sentence with a parameterized template.
 */
public enum SentenceType {

	// @formatter:off
	FOR("for"), FOREACH("forEach"), WHILE("while"), DO("do"), IF("if"), @Deprecated
	IFELSE("ifElse"), ELSE("else"), SWITCH("switch"), METHOD("method"), CLASS("class"), COMMENT("commentBlock"), TRY_CATCH("tryCatch"), CATCH("catch"), @Deprecated
	TRY_CATCH_FINALLY("tryCatchFinally"), PACKAGE_NOT_FOUND("packageNotFound"), CLASS_NOT_FOUND("classNotFound"), SYNCHRONIZE_BLOCK("synchronizeBlock"), CONSTRUCTOR("constructor"), EDITOR_FOCUSED("editorFocused"), INTERFACE("interface"), ENUM("enum"), JAVAPROJECT("javaProject"), FOLDER("folder"), PACKAGE("package"), FIELD("field"), SIMPLE_EXPRESSION("simpleExp"), FILE("file"), LOCALIZATION("localization"), SINGLE_INFORMATION("singleInfo"), SUMMARY("summary"), SELECTED("selected"), DELETED("deleted"), TEXT_SELECTED("textSelected"), TEXT_DESELECTED("textDeselected"), SELECT_LINE_STATUS("selectLineStatus");
	// @formatter:on

	private final String key;

	private SentenceType(final String key) {
		this.key = key;
	}

	/**
	 * Returns the key used for retreive dictionary entries.
	 *
	 * @return key
	 */
	public String getStringKey() {
		return key;
	}

	/**
	 * Given the string containing the key, returns the corresponding enum
	 * value. The string must not have extra spaces before or after the
	 * characters.
	 *
	 * @param key
	 *            the string with the key, that can be the key or just the enum
	 *            toString() value
	 * @return the sentence type enum value, or <code>null</code> if no one is
	 *         found
	 */
	public static SentenceType fetchFromKey(final String key) {
		for (final SentenceType type : values()) {
			if (key.equals(type.getStringKey()) || key.equals(type.toString()))
				return type;
		}
		return null;
	}
}