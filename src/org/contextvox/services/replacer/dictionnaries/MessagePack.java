package org.contextvox.services.replacer.dictionnaries;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Contains a collection of translations. Each pack has only one language. A
 * message is different from a sentence: it is a simple translation in human
 * language of a symbol or a phrase (e.g. "editor opened").
 *
 * @see Translation
 * @see SentencePack
 * @author Simone Mignami
 */
class MessagePack {

	/** File extension for message storage */
	static final String MESSAGES_EXTENSION = "messages";

	/** The message pack language */
	private final Messages.Language language;

	/** List of translations separated in lists */
	private Map<MessageType, List<Translation>> partitionedLists;
	/** Entire list of translations */
	private final List<Translation> wholeList;

	/** Punctual translation, for better search performances */
	private final Map<String, Translation> mappedData;

	/**
	 * Factory method that loads the translation file and builds the
	 * corresponding Message Pack.
	 *
	 * @param language
	 *            the language of the message pack
	 * @param bundle
	 *            input stream of the translation file
	 * @return a built Message Pack
	 * @see {@link Messages.Language}
	 */
	public static MessagePack build(final Messages.Language lang, final InputStream bundle) {
		final List<Translation> data = loadFromFile(bundle);
		return new MessagePack(lang, data);

	}

	/**
	 * Returns a list of translations from the raw file input. The order of the
	 * list respect the order of the lines in input. All comments and blank
	 * lines are dropped. A commented line starts with a "#" symbol.
	 *
	 * @param rawData
	 *            a list of lines from a file
	 * @return list of translations
	 */
	private static List<Translation> convertRawData(final ArrayList<String> rawData) {
		// compose translations
		final ArrayList<Translation> data = rawData.stream().filter(line -> {
			// "#" is a comment and is skipped
			if (!line.startsWith("#")) {
				// split messages in symbol and translation
				final String[] tokens = line.split("\t");
				// a line must be made from 3 elements
				return tokens.length == 3;
			} else {
				return false;
			}
		}).map(item -> {
			// build Translation
			final String[] tokens = item.split("\t");
			final MessageType type = MessageType.getTypeFromSelector(tokens[1]);
			String key = tokens[0];
			// if a sentence, look for the alternative type
			if (type == MessageType.SENTENCE) {
				final SentenceAlternative alt = SentenceAlternative.convertFromSuffix(key);
				key = SentenceAlternative.trimPrefix(key);
				return new Translation(key, tokens[2], type, alt);
			} else {
				if (type == MessageType.WHITESPACE)
					key = specialSymbols(key);
				return new Translation(key, tokens[2], type);
			}
		}).collect(Collectors.toCollection(ArrayList::new));

		return data;

	}

	// convert letter key into real symbol
	private static String specialSymbols(final String key) {
		switch (key) {
		case "TAB":
			return "\t";
		case "SPC":
			return " ";
		case "EOL":
			return "\r\n";

		default:
			return key;
		}
	}

	private static List<Translation> loadFromFile(final InputStream bundle) {
		final Scanner scanner = new Scanner(bundle, "UTF-8");

		// load lines
		final ArrayList<String> temp = new ArrayList<>();
		while (scanner.hasNextLine())
			temp.add(scanner.nextLine());
		scanner.close();
		return convertRawData(temp);
	}

	private MessagePack(final Messages.Language language, final List<Translation> data) {
		// sort original list
		Collections.sort(data);
		this.wholeList = Collections.unmodifiableList(data);

		this.language = language;
		this.mappedData = new HashMap<>();
		preprocess(data);

		// TODO temp
		data.stream().filter(item -> {
			return item.getType() == MessageType.SENTENCE;
		}).collect(Collectors.toList());
	}

	/**
	 * Returns a list of translations related to the given message type. The
	 * list is ordered following the ContextVox' specifications.
	 *
	 * @param messageType
	 *            , the key to search messages, if <code>null</code> the entire
	 *            translation list is returned
	 * @return translations list
	 */
	public List<Translation> getGroupTranslation(final MessageType messageType) {
		if (messageType == null)
			return this.wholeList;
		else
			return partitionedLists.get(messageType);
	}

	public Messages.Language getLanguage() {
		return language;
	}

	/**
	 * Returns the translation of the given symbol.
	 *
	 * @param symbol
	 *            the key to search
	 * @return a string containing the translation of the symbol. If no
	 *         translation is found, the programmer is noticed with a
	 *         <code>NoSuchElementException</code
	 */
	public String getTranslation(final String symbol) {
		final Translation result = mappedData.get(symbol);
		if (result == null)
			throw new NoSuchElementException("no element found on message pack for " + symbol);
		else
			return result.getTranslation();
	}

	/** Preprocess some cached data */
	private void preprocess(final List<Translation> data) {
		// split lists
		partitionedLists = new HashMap<>();
		final MessageType[] types = MessageType.values();
		for (final MessageType type : types) {
			partitionedLists.put(type, new ArrayList<Translation>());
		}
		// fill lists
		this.wholeList.stream().forEach(item -> {
			final MessageType key = item.getType();
			partitionedLists.get(key).add(item);
			// fill also the mapped version
			mappedData.put(item.getSymbol(), item);
		});

		// make unmodifiable
		for (final MessageType type : types) {
			final List<Translation> list = Collections.unmodifiableList(partitionedLists.get(type));
			partitionedLists.put(type, list);
		}
	}

	/**
	 * Checks if the given symbol has a translation on the message pack.
	 *
	 * @param symbol
	 *            string
	 * @return <code>true</code> if the symbol has a translation
	 */
	public boolean translationExists(final String symbol) {
		final Translation translation = mappedData.get(symbol);
		return translation != null;
	}

	@Override
	public String toString() {
		return String.format("Message pack (%s): %d messages", language.toString(), wholeList.size());
	}
}
