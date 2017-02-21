package org.contextvox.services.replacer;

/**
 * Message maker configuration.
 *
 * @author Simone Mignami
 *
 * @see MessageMaker
 *
 */
public enum ReplacerOption {
	/**
	 * All symbols are replaced.
	 */
	SYMBOL_ALL,
	/**
	 * Fast read, removes semicolons and other less useful symbols.
	 */
	SYMBOL_FAST,
	/**
	 * Indicates to use parameterized sentences. Using a
	 * <code>makeSentence</code> method might be omitted.
	 */
	SENTENCES,
	/**
	 * Replaces language complex symbols such as array notation.
	 */
	COMPLEX_SYMBOL,
	/**
	 * Enables the camel case processing, that helps less sophisticate TTS
	 * engines to better read Java style camel case. By default this option is
	 * disabled.
	 */
	CAMEL_CASE;
}