package org.contextvox.services.replacer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.contextvox.services.replacer.dictionnaries.Messages;
import org.contextvox.services.replacer.dictionnaries.SentenceAlternative;
import org.contextvox.services.replacer.dictionnaries.SentenceType;
import org.contextvox.services.replacer.dictionnaries.Translation;

/**
 * This component processes parameterized sentences and replaces tokens with
 * some provided information in a map. It is not necessary to pass a string to
 * the <code>process</code> or <code>replace</code> methods because the relative
 * sentence is pick up automatically from the MessagePack.
 * <p>
 * Note that this class is not thread-safe and each time a string is processed,
 *
 */
class TokenReplacer implements Replacer {

	// private final Map<SentenceToken, String> data;
	// private final SentenceType type;
	// private final SentenceAlternative alternative;

	@Override
	public void replace(final RawMessage data) {
		String sentence = null;
		final SentenceAlternative alternative = data.getAlternative();
		final SentenceType type = data.getType();
		final Map<SentenceToken, String> data2 = data.getData();
		// abort if data are missing
		if (alternative == null || type == null || data == null)
			return;

		sentence = elaborate(sentence, alternative, type, data2);
		if (sentence == null && alternative == SentenceAlternative.LIGHT) {
			// reduce sentence alternative level, trying to fit the tokens
			sentence = elaborate(sentence, SentenceAlternative.FULL, type, data2);
		}

		data.setText(sentence);
	}

	/*
	 * Elaborate the data with the given sentence alternative. Return null if
	 * there is not a sentence that fit the given data.
	 */
	private String elaborate(String sentence, final SentenceAlternative alternative, final SentenceType type,
			final Map<SentenceToken, String> data) {
		final List<Translation> sentences = Messages.getSentence(type, alternative);

		if (sentences == null)
			return null;
		// } else if (sentences.size() == 1) {
		// // replace every token, if found
		// // TODO apply choose also for a single sentence, cause if there is
		// // not match, another sentence alternative should be taken
		// sentence = sentences.get(0).getTranslation();
		// sentence = realReplace(data, sentence);
		sentence = choose(data.keySet(), sentences);

		if (sentence == null) {
			return null;
		} else {
			sentence = realReplace(data, sentence);
			return sentence;
		}
	}

	// choose between all sentences. If there is no matching, the nearest which
	// fills all matches will be returned.
	private static String choose(final Set<SentenceToken> tokens, final List<Translation> sentences) {
		// number of tokens for each sentence
		final int[] tokenNumbers = new int[sentences.size()];
		for (int i = 0; i < tokenNumbers.length; i++) {
			tokenNumbers[i] = tokenCount(sentences.get(i).getTranslation());
		}

		// choose now zero-token sentence
		if (tokens.size() == 0) {
			for (int i = 0; i < tokenNumbers.length; i++) {
				if (tokenNumbers[i] == 0)
					return sentences.get(i).getTranslation();
			}
		}

		// look for token fitness
		final boolean[] fitness = new boolean[tokenNumbers.length];
		for (int i = 0; i < fitness.length; i++) {
			int n = 0;
			for (final SentenceToken item : tokens) {
				if (sentences.get(i).getTranslation().contains(item.getToken()))
					n++;
			}
			if (n >= tokenNumbers[i])
				fitness[i] = true;
		}

		// abort if no candidate is found
		boolean foundCandidate = false;
		for (int i = 0; i < fitness.length; i++)
			foundCandidate = foundCandidate | fitness[i];
		if (!foundCandidate)
			return null;

		// choose best fit
		int max = 0, candidate = 0;
		for (int i = 0; i < fitness.length; i++) {
			if (fitness[i] && tokenNumbers[i] > max) {
				candidate = i;
				max = tokenNumbers[i];
			}
		}

		return sentences.get(candidate).getTranslation();
	}

	// count number of token in a sentence
	private static int tokenCount(final String sentence) {
		final SentenceToken[] tokens = SentenceToken.values();
		int n = 0;
		for (final SentenceToken item : tokens) {
			if (sentence.contains(item.getToken()))
				n++;
		}
		return n;
	}

	private static String realReplace(final Map<SentenceToken, String> data, String sentence) {
		final Set<SentenceToken> keys = data.keySet();
		for (final SentenceToken item : keys) {
			final String token = item.getToken();
			sentence = sentence.replace(token, data.get(item));
		}
		return sentence;
	}

}
