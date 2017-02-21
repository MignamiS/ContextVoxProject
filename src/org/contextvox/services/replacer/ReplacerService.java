package org.contextvox.services.replacer;

import java.util.Map;

import org.contextvox.services.replacer.dictionnaries.SentenceAlternative;
import org.contextvox.services.replacer.dictionnaries.SentenceType;

/**
 * A service for translating from programming language into human readable text.
 * It uses the current ContextVox state to determine which translation is
 * better.
 *
 * @author Simone Mignami
 *
 */
public interface ReplacerService {

	/**
	 * Injects the given data on a parameterized sentence and replaces
	 * programming language symbols. it looks only for sentences with "full"
	 * alternative.
	 *
	 * @param type
	 *            the sentence type
	 * @param data
	 *            parameter data
	 * @return a string
	 */
	public String makeSentence(SentenceType type, Map<SentenceToken, String> data);

	/**
	 * Similar to the {@link #makeSentence(SentenceType, Map)}, but the client
	 * can choose the sentence alternative.
	 *
	 * @param type
	 * @param data
	 * @param alternative
	 * @return the string
	 */
	public String makeSentence(SentenceType type, Map<SentenceToken, String> data, SentenceAlternative alternative);

	/**
	 * Replaces symbols in a given string. No parameterized sentences.
	 *
	 * @param str
	 *            the string
	 * @return a human readable string
	 */
	public String replaceSymbols(String str);

}
