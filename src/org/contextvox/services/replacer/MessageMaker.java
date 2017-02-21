package org.contextvox.services.replacer;

import java.util.EnumSet;
import java.util.Map;

import org.contextvox.services.replacer.dictionnaries.SentenceAlternative;
import org.contextvox.services.replacer.dictionnaries.SentenceType;

/**
 * Entry point to convert a message or a line of code in human-readable
 * language. It is also possible to choose a type of parameterized sentence and
 * fill its token places with information directly from the code.
 *
 * The configuration is an enum set containing which replacer have to be used.
 *
 * @author Simone Mignami
 */
public class MessageMaker {
	// TODO check for thread-safety and document it

	private static Replacer convert(final EnumSet<ReplacerOption> config) {
		final ReplacerBuilder builder = ReplacerBuilder.create();
		if (config.contains(ReplacerOption.SENTENCES))
			builder.sentences();
		if (config.contains(ReplacerOption.COMPLEX_SYMBOL))
			builder.complexSymbols();

		if (config.contains(ReplacerOption.SYMBOL_ALL))
			builder.allSymbols();
		else if (config.contains(ReplacerOption.SYMBOL_FAST))
			builder.fastSymbols();

		if (config.contains(ReplacerOption.CAMEL_CASE))
			builder.camelCaseProcessing(true);

		return builder.build();
	}

	/**
	 * Builds a parameterized sentence.
	 *
	 * @param type
	 *            the SentenceType used to determine which sentence use. The
	 *            <code>full</code> alternative is used.
	 * @param data
	 *            a map of SentenceToken and the relative information, to be
	 *            injected on the sentence
	 * @param config
	 *            the configuration
	 * @return the resulting string
	 */
	public static String makeSentence(final SentenceType type, final Map<SentenceToken, String> data,
			final EnumSet<ReplacerOption> config) {
		final RawMessage msg = new RawMessage(null);
		msg.setData(data);
		msg.setAlternative(SentenceAlternative.FULL);
		msg.setType(type);

		final Replacer replacer = convert(config);
		replacer.replace(msg);
		return msg.getText();
	}

	/**
	 * Like the other <code>makeSentence</code> method, but it is possible to
	 * specify the sentence alternative.
	 *
	 * @param type
	 *            sentence type
	 * @param data
	 *            mapped data
	 * @param alternative
	 *            the sentence alternative
	 * @param config
	 *            the configuration
	 * @return the resulting string
	 *
	 * @see #makeSentence(SentenceType, Map, Chainable)
	 */
	public static String makeSentence(final SentenceType type, final Map<SentenceToken, String> data,
			final SentenceAlternative alternative, final EnumSet<ReplacerOption> config) {
		if (data == null || alternative == null)
			throw new IllegalArgumentException("Argument null not allowed");

		// message data info
		final RawMessage msg = new RawMessage(null);
		msg.setType(type);
		msg.setAlternative(alternative);
		msg.setData(data);

		final Replacer replacer = convert(config);
		replacer.replace(msg);
		return msg.getText();
	}

	/**
	 * Replaces symbols starting from a line of code.
	 *
	 * @param str
	 *            the line of code
	 * @param config
	 *            configuration
	 * @return the resulting string
	 */
	public static String replaceSymbols(final String str, final EnumSet<ReplacerOption> config) {
		// TODO document how to set the config object
		final Replacer rep = convert(config);
		final RawMessage msg = new RawMessage(str);
		rep.replace(msg);
		return msg.getText();
	}

}
