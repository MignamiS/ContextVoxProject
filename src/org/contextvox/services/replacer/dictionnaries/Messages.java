package org.contextvox.services.replacer.dictionnaries;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * entry point to get the translations of messages and sentences. The class must
 * be initialized before with <code>load</code>, otherwise accessing data will
 * throw an exception.
 *
 * @author Simone Mignami
 */
public class Messages {

	/** Language loaded as default */
	public static final Language DEFAULT_LANGUAGE = Language.ENGLISH;
	private static final String EX_MSG_NOT_INIT = "The Messages class has not been correctly initialized";

	// current language
	private static volatile Language currentLang;

	private static final Logger logger = Logger.getLogger(Messages.class.getName());

	// whether the class has been initialized
	private static boolean initialized = false;
	private static MessagePack currentMessagePack = null;
	private static SentencePack currentSentencePack = null;

	public synchronized static Language getCurrentLanguage() {
		return currentLang;
	}

	/**
	 * Returns a list of translations related to the specified sentence type
	 * provided. This method returns only sentence alternative of type
	 * <code>full</code>.
	 *
	 * @param sentence
	 *            the sentence type to find the translations
	 * @return a list of translations of <code>full</code> alternative
	 * @throws IllegalStateException
	 *             when not initialized
	 */
	public synchronized static List<Translation> getSentence(final SentenceType sentence) {
		if (!initialized)
			throw new IllegalStateException(EX_MSG_NOT_INIT);
		return currentSentencePack.getTranslation(sentence, SentenceAlternative.FULL);
	}

	/**
	 * Return a list of translations related to the specified search key.
	 *
	 * @param sentence
	 *            the key to search
	 * @param alternative
	 *            the sentence alternative
	 * @return a list of translations
	 * @throws IllegalStateException
	 *             when not initialized
	 */
	public synchronized static List<Translation> getSentence(final SentenceType sentence,
			final SentenceAlternative alternative) {
		if (!initialized)
			throw new IllegalStateException(EX_MSG_NOT_INIT);
		return currentSentencePack.getTranslation(sentence, alternative);
	}

	/**
	 * Returns the corresponding message translation.
	 *
	 * @param key
	 *            the key to find
	 * @return translation, or throw NoSuchElementException
	 * @throws IllegalStateException
	 *             when not initialized
	 */
	public synchronized static String getSingleMessage(final String key) {
		if (!initialized)
			throw new IllegalStateException(EX_MSG_NOT_INIT);
		return currentMessagePack.getTranslation(key);
	}

	/**
	 * Returns all the translations of the current message pack.
	 *
	 * @return ordered list of translations
	 * @throws IllegalStateException
	 *             when not initialized
	 */
	public synchronized static List<Translation> getTranslations() {
		if (!initialized)
			throw new IllegalStateException(EX_MSG_NOT_INIT);
		return currentMessagePack.getGroupTranslation(null);
	}

	/**
	 * Returns a list of translations, grouped by type of message. The list is
	 * ordered following the ContextVox symbol specifications.
	 *
	 * @param type
	 *            the message type to group
	 * @return an ordered list of translations
	 * @throws IllegalStateException
	 *             when not initialized
	 */
	public static synchronized List<Translation> getTranslations(final MessageType type) {
		if (!initialized)
			throw new IllegalStateException(EX_MSG_NOT_INIT);
		return currentMessagePack.getGroupTranslation(type);
	}

	/**
	 * Switches the language of the message pack.
	 *
	 * @param lang
	 *            another language of which provided by ContextVox
	 * @return boolean indicating if the message pack has been changed. Note
	 *         that If the specified language is already the current, nothing
	 *         will happen and <code>false</code> is returned
	 */
	public static synchronized boolean load(final Messages.Language lang) {
		// check if the language is current
		if (currentLang == lang)
			return false;

		// MESSAGES
		final String msgPathString = String.format("/messages/%s.%s", lang.getIdentifier(),
				MessagePack.MESSAGES_EXTENSION);
		loadMessages(lang, msgPathString);

		// SENTENCES
		final String sntPath = String.format("/messages/%s_sentences.%s", lang.getIdentifier(),
				SentencePack.SENTENCE_FILE_EXTENSION);
		loadSentences(sntPath);
		initialized = true;
		return true;
	}

	/**
	 * Loads and initializes the entry point. This method is only for test and
	 * debug purpose.
	 *
	 * @param mesagePath
	 * @param sentencePath
	 */
	public static synchronized void load(final String mesagePath, final String sentencePath) {
		loadMessages(DEFAULT_LANGUAGE, mesagePath);
		loadSentences(sentencePath);
		initialized = true;
	}

	private static void loadMessages(final Messages.Language lang, final String msgPathString) {
		final InputStream messagesStream = Messages.class.getResourceAsStream(msgPathString);
		final MessagePack pack = MessagePack.build(lang, messagesStream);
		logger.info("Loaded message pack: " + pack.toString());
		Messages.currentMessagePack = pack;
		currentLang = lang;
	}

	private static void loadSentences(final String sntPath) {
		final InputStream sentencesStream = Messages.class.getResourceAsStream(sntPath);
		currentSentencePack = SentencePack.build(sentencesStream);
	}

	/**
	 * Delegates to {@link MessagePack#translationExists(String)}
	 *
	 * @throws IllegalStateException
	 *             when not initialized
	 */
	public static synchronized boolean translationExists(final String symbol) {
		if (!initialized)
			throw new IllegalStateException(EX_MSG_NOT_INIT);
		return currentMessagePack.translationExists(symbol);
	}

	/**
	 * Available languages
	 */
	public enum Language {
		ENGLISH {
			@Override
			public String getIdentifier() {
				return "en";
			}
		},
		ITALIAN {
			@Override
			public String getIdentifier() {
				return "it";
			}
		};

		public static Language convertValue(final String language) {
			for (final Language lang : Language.values()) {
				if (language.equals(lang.getIdentifier())) {
					return lang;
				}
			}
			return null;
		}

		/**
		 * Return the abbreviation for the given language (e.g English => "en")
		 *
		 * @return String abbreviation
		 */
		public abstract String getIdentifier();
	}
}
