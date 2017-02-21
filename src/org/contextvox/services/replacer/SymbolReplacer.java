package org.contextvox.services.replacer;

import java.util.Arrays;

import org.contextvox.services.replacer.dictionnaries.MessageType;
import org.contextvox.services.replacer.dictionnaries.Messages;
import org.contextvox.services.replacer.dictionnaries.Translation;

/**
 * This component replaces symbols in a string with their corresponding literal
 * translation. For example the symbol <code>++</code>" is replaced by
 * <code>increment</code>.
 * <p>
 * Each instance will process a certain type of symbols (operators, parenthesis,
 * keywords, ...). It is possible to specify if the encountered symbols should
 * be removed or replaced with the literal translation. In some cases it is
 * possible to specify an array of exceptions which are skipped (the symbol
 * remains on the returned string), or just deleted.
 * </p>
 *
 */
class SymbolReplacer implements Replacer {

	/** Type of symbols to process */
	protected final MessageType type;

	/**
	 * If TRUE, the symbols found on the string will be removed and not
	 * replaced.
	 */
	protected final boolean remove;
	/** List of exceptions, the string will be skipped */
	protected final String[] exceptions;
	/** indicates if exceptions are skipped or not */
	protected final boolean skip;

	/**
	 * Builds a simple SymbolReplacer that replaces or removes symbols from the
	 * input string.
	 *
	 * @param type
	 *            the message type that is processed
	 * @param remove
	 *            if <code>true</code> the symbols found are simply removed,
	 *            otherwise they are replaced with the literal translation
	 */
	public SymbolReplacer(final MessageType type, final boolean remove) {
		this.type = type;
		this.remove = remove;
		// default
		this.exceptions = null;
		this.skip = true;
	}

	/**
	 * Builds an advanced SymbolReplacer that replaces symbols with their
	 * literal translation or removing them. The exceptions are skipped or just
	 * removed.
	 *
	 * @param type
	 *            the message type that is processed
	 * @param remove
	 *            if <code>true</code> the symbol found is removed from the
	 *            string, otherwise is replaced with its own literal translation
	 * @param exceptions
	 *            an array of strins containing the symbols that are considered
	 *            exceptional and are processed differently
	 * @param skip
	 *            if <code>true</code> indicates that the exceptional found
	 *            symbol should be skipped (and remains on the string),
	 *            otherwise will be removes
	 */
	public SymbolReplacer(final MessageType type, final boolean remove, final String[] exceptions, final boolean skip) {
		this.type = type;
		this.remove = remove;
		this.exceptions = exceptions;
		this.skip = skip;
	}

	// Add a whitespace before & after the string
	private CharSequence addBoundarySpace(final String str) {
		final String sp = " ";
		return new String(sp + str + sp);
	}

	public String[] getExceptions() {
		// defensive copy
		return exceptions.clone();
	}

	public boolean getRemove() {
		return remove;
	}

	public boolean getSkip() {
		return skip;
	}

	public MessageType getType() {
		return type;
	}

	// Process the string using this.remove parameter, with no exception
	private String processString(String result, final Translation t, final boolean remove) {
		if (!remove) {
			result = result.replace(t.getSymbol(), addBoundarySpace(t.getTranslation()));
		} else {
			result = result.replace(t.getSymbol(), " ");
		}
		return result;
	}

	private String processStringWithException(String result, final Translation t) {
		// check that the exception is the current symbol to process
		boolean exceptionMatching = false;
		for (final String except : this.exceptions) {
			if (t.getSymbol().equals(except)) {
				exceptionMatching = true;
				break;
			}
		}

		if (exceptionMatching) {
			// process string using the this.skip parameter
			if (this.skip == false)
				result = processString(result, t, !this.remove);
		} else {
			// process String normally
			result = processString(result, t, this.remove);
		}
		return result;
	}

	@Override
	public void replace(final RawMessage data) {
		String result = data.getText();

		// check if there are exception to process, or not
		final boolean exceptionPresence = (this.exceptions == null) ? false
				: (this.exceptions.length == 0) ? false : true;

		// iterate messages of the same type
		for (final Translation t : Messages.getTranslations(this.type)) {
			// Exceptions?
			if (!exceptionPresence) {
				// process the string without exception
				result = processString(result, t, this.remove);
			} else {
				result = processStringWithException(result, t);
			}

		}

		data.setText(result);
	}

	@Override
	public String toString() {
		final String r = (remove) ? "REMOVE" : "REPLACE";
		final String s = (skip) ? "SKIPPED" : (remove) ? "REPLACED" : "REMOVED";
		final String exc = Arrays.toString(exceptions);
		return new String("Filter " + r + " for " + type + " with exceptions " + exc + " " + s);
	}

}
