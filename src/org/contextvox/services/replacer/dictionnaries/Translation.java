package org.contextvox.services.replacer.dictionnaries;

import java.util.Objects;

/**
 * This class contains an association between a symbol and his translation in
 * "human" language (e.g "+" -> "plus"). The instances of this class are
 * immutable.
 */
public final class Translation implements Comparable<Translation> {

	private final String symbol;
	private final String translation;
	private final MessageType type;
	private final SentenceAlternative alternative;

	/**
	 * Constructor
	 *
	 * @param symbol
	 *            the translation symbol
	 * @param translation
	 *            the symbol literal translation
	 * @param type
	 *            message type
	 */
	public Translation(final String symbol, final String translation, final MessageType type) {
		this.symbol = symbol;
		this.translation = translation;
		this.type = type;
		if (type == MessageType.SENTENCE)
			this.alternative = SentenceAlternative.FULL;
		else
			this.alternative = null;
	}

	/**
	 * Constructor
	 *
	 * @param symbol
	 *            the symbol of the translation
	 * @param translation
	 *            the literal translation of the symbol
	 * @param type
	 *            the message type
	 * @param alternative
	 *            the sentence alternative
	 */
	public Translation(final String symbol, final String translation, final MessageType type,
			final SentenceAlternative alternative) {
		this.symbol = symbol;
		this.translation = translation;
		this.type = type;
		this.alternative = alternative;
	}

	/**
	 * Order translations with following rules.
	 * <ol>
	 * <li>Group by message type</li>
	 * <li>Symbol length ascending</li>
	 * </ol>
	 *
	 * @param o
	 *            other Translation to compare
	 * @return int 0 if equals, -1 if other is smaller and 1 if bigger
	 */
	@Override
	public int compareTo(final Translation o) {
		if (this.type == o.type) {
			return o.symbol.length() - this.symbol.length();
		} else {
			return this.type.compareTo(o.type);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Translation))
			return false;

		final Translation other = (Translation) obj;
		return Objects.equals(this.symbol, other.symbol) && Objects.equals(this.translation, other.translation)
				&& Objects.equals(this.alternative, other.alternative) && this.type == other.type;

	}

	/**
	 * @return the Sentence alternative or <code>null</code> if the translation
	 *         is not a sentence
	 */
	public SentenceAlternative getAlternative() {
		return alternative;
	}

	/**
	 * @return the symbol (e.g "+=")
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the corresponding translation of the symbol (e.g "plus equal")
	 */
	public String getTranslation() {
		return translation;
	}

	/**
	 * @return the type of the message, e.g a parenthesis
	 */
	public MessageType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		// return (new String(this.symbol + this.translation +
		// this.type)).hashCode();
		int result = 37 * Objects.hashCode(symbol);
		result = 37 * result + Objects.hashCode(translation);
		result = 37 * result + Objects.hashCode(alternative);
		result = 37 * result + Objects.hashCode(type);

		return result;
	}

	@Override
	public String toString() {
		return String.format("Translation (%s) \"%s\": %s", this.type, this.symbol, this.translation);
	}
}
