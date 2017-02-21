package org.contextvox.services.replacer;

import java.util.ArrayList;
import java.util.List;

import org.contextvox.services.replacer.dictionnaries.MessageType;

/**
 * Configure a replacer. This class is not thread-safe and each reference should
 * be deleted after the replacer object has been built.
 *
 * @author Simone Mignami
 *
 */
class ReplacerBuilder {

	private List<SymbolReplacer> symbol;
	private TokenReplacer token;
	private ComplexReplacer complex;
	private boolean prettySpaces = true;
	private boolean camelCase = false;

	/**
	 * Creates a new builder
	 *
	 * @return a replacer builder
	 */
	public static ReplacerBuilder create() {
		return new ReplacerBuilder();
	}

	private ReplacerBuilder() {
	}

	/**
	 * Replaces all symbols without exception
	 *
	 * @return the builder
	 */
	public ReplacerBuilder allSymbols() {
		final List<SymbolReplacer> list = new ArrayList<>();
		list.add(new SymbolReplacer(MessageType.PARENTHESIS, false));
		list.add(new SymbolReplacer(MessageType.OPERATOR, false));
		list.add(new SymbolReplacer(MessageType.POINT, false));
		list.add(new SymbolReplacer(MessageType.DATATYPE, false));
		list.add(new SymbolReplacer(MessageType.KEYWORD, false));

		this.symbol = list;
		return this;
	}

	/**
	 * Builds the replacer. Remember to delete the reference of this builder
	 *
	 * @return the replacer
	 */
	public Replacer build() {
		final List<Replacer> list = new ArrayList<>();

		if (this.token != null)
			list.add(this.token);
		if (this.complex != null)
			list.add(this.complex);
		if (this.camelCase)
			list.add(new CamelCaseReplacer());
		if (this.symbol != null)
			list.addAll(this.symbol);
		if (this.prettySpaces)
			list.add(new SpaceCleanerReplacer());

		return new ReplacerContainer(list);
	}

	/**
	 * Processes complex symbols of Java language such as generics and array
	 * notation. By default is disabled.
	 *
	 * @return the builder
	 */
	public ReplacerBuilder complexSymbols() {
		this.complex = new ComplexReplacer();
		return this;
	}

	/**
	 * Processes the majority of symbols, except for some less important one
	 * such as semicolon or parenthesis.
	 *
	 * @return the builder
	 */
	public ReplacerBuilder fastSymbols() {
		final List<SymbolReplacer> list = new ArrayList<>();
		list.add(new SymbolReplacer(MessageType.OPERATOR, false));
		// filter parentheses
		final String[] exceptPar = { "{", "}", "[", "]" };
		list.add(new SymbolReplacer(MessageType.PARENTHESIS, true, exceptPar, false));
		// filter points
		final String[] exceptPoints = { ".", ",", ":" };
		list.add(new SymbolReplacer(MessageType.POINT, true, exceptPoints, false));
		list.add(new SymbolReplacer(MessageType.DATATYPE, false));
		list.add(new SymbolReplacer(MessageType.KEYWORD, false));

		this.symbol = list;
		return this;
	}

	/**
	 * Avoid space cleaning. This is enabled by default, but this feature can be
	 * turned off using this method
	 *
	 * @return the builder
	 */
	public ReplacerBuilder noPretty() {
		this.prettySpaces = false;
		return this;
	}

	/**
	 * Processes parameterized sentences
	 *
	 * @return the builder
	 */
	public ReplacerBuilder sentences() {
		this.token = new TokenReplacer();
		return this;
	}

	/**
	 * Enables the camel case processing, that helps less sophisticate TTS
	 * engines to better read Java style camel case. By default this option is
	 * disabled.
	 *
	 * @param camelCase
	 *            enable or not
	 * @return the builder
	 */
	public ReplacerBuilder camelCaseProcessing(final boolean camelCase) {
		this.camelCase = camelCase;
		return this;
	}
}
