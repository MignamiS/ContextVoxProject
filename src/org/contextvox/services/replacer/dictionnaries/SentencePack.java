package org.contextvox.services.replacer.dictionnaries;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 * Contains a collection of translations of parametherizable sentences. A P.S is
 * a template that contains tokens that can be filled with on-the-fly
 * information, such as variable name, conditions or numerical values.
 *
 * @see MessagePack
 * @author Simone Mignami
 *
 */
class SentencePack {

	private static final String JSON_KEY_SNT_TYPE = "type";

	static final String SENTENCE_FILE_EXTENSION = "json";

	private final Map<SentenceType, List<Translation>> sentences;

	/**
	 * Factory method that builds a Sentence pack from the stream connected to
	 * the respective file.
	 *
	 * @param stream
	 * @return a sentence pack
	 */
	public static SentencePack build(final InputStream stream) {
		final List<Translation> data = readJSONData(stream);
		return new SentencePack(data);
	}

	// builds the configuration map for "beautify" the JSON output
	private static Map<String, Boolean> buildConfig(final String... options) {
		final Map<String, Boolean> config = new HashMap<String, Boolean>();

		if (options != null) {
			for (final String option : options) {
				config.put(option, true);
			}
		}

		return config;
	}

	static List<Translation> readJSONData(final InputStream stream) {
		final List<Translation> data = new ArrayList<>();
		final JsonReader reader = Json.createReader(stream);
		// external container (array)
		final JsonArray container = reader.readArray();
		for (int i = 0; i < container.size(); i++) {
			final JsonObject obj = container.getJsonObject(i);
			final String type = obj.getString(JSON_KEY_SNT_TYPE);
			// tryies all alternatives to see if there is at least one
			for (final SentenceAlternative alternative : SentenceAlternative.values()) {
				final JsonArray alternatives = obj.getJsonArray(alternative.toString());
				if (alternatives != null) {
					// iterates all translation of same alternative
					for (int j = 0; j < alternatives.size(); j++) {
						final String translation = alternatives.getString(j);
						final Translation t = new Translation(type, translation, null, alternative);
						data.add(t);
					}
				}
			}
		}
		reader.close();

		return data;
	}

	private SentencePack(final List<Translation> data) {
		final Map<SentenceType, List<Translation>> mapped = convertDataListToMap(data);
		this.sentences = mapped;
	}

	private JsonStructure buildObject() {
		// external container, can contain some global properties
		final JsonArrayBuilder container = Json.createArrayBuilder();
		// An array of custom object: each object is a sentence type
		final List<SentenceType> keys = sort(sentences.keySet());
		for (final SentenceType type : keys) {
			final JsonObjectBuilder aType = Json.createObjectBuilder();
			aType.add(JSON_KEY_SNT_TYPE, type.toString());

			// for each alternative type, an array of translations
			for (final SentenceAlternative alt : SentenceAlternative.values()) {
				final JsonArrayBuilder alts = Json.createArrayBuilder();
				int n = 0;
				for (final Translation translation : sentences.get(type)) {
					if (translation.getAlternative() == alt) {
						alts.add(translation.getTranslation());
						n++;
					}
				}
				// put only alternatives with translation
				if (n > 0)
					aType.add(alt.toString(), alts);
			}

			container.add(aType);
		}

		return container.build();
	}

	private Map<SentenceType, List<Translation>> convertDataListToMap(final List<Translation> data) {
		final Map<SentenceType, List<Translation>> mapped = new HashMap<>();
		for (final Translation translation : data) {
			// get enum value
			final SentenceType sentenceType = SentenceType.fetchFromKey(translation.getSymbol());
			if (sentenceType == null)
				continue;
			if (!mapped.containsKey(sentenceType))
				// initialize empty list
				mapped.put(sentenceType, new ArrayList<>());
			mapped.get(sentenceType).add(translation);
		}
		return mapped;
	}

	/**
	 * Returns a list of translations of the given sentence type and
	 * alternative.
	 *
	 * @param type
	 *            the sentence type (symbol)
	 * @param alternative
	 * @return list of translations. If there is not an alternative, an empty
	 *         list is returnd
	 *
	 * @throws NoSuchElementException
	 *             if the symbol is not on the list
	 */
	public List<Translation> getTranslation(final SentenceType type, final SentenceAlternative alternative) {
		final SentenceAlternative alt = (alternative == null) ? SentenceAlternative.FULL : alternative;
		final List<Translation> list = this.sentences.get(type);
		if (list == null || list.isEmpty())
			throw new NoSuchElementException("no element found on message pack for " + type);

		// filter alternative and produce a list
		return list.stream().filter(t -> {
			return t.getAlternative() == alt;
		}).collect(Collectors.toList());
	}

	private String jsonFormat(final JsonStructure json, final String... options) {
		final StringWriter stringWriter = new StringWriter();
		final Map<String, Boolean> config = buildConfig(options);
		final JsonWriterFactory writerFactory = Json.createWriterFactory(config);
		final JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

		jsonWriter.write(json);
		jsonWriter.close();

		return stringWriter.toString();
	}

	void save(final OutputStream output) {
		final JsonStructure obj = buildObject();
		final String str = jsonFormat(obj, JsonGenerator.PRETTY_PRINTING);
		// TODO implement
		// write string on output
		System.out.println(str);
	}

	public int size() {
		return this.sentences.size();
	}

	private List<SentenceType> sort(final Set<SentenceType> keySet) {
		final List<SentenceType> list = new ArrayList<>(keySet);
		Collections.sort(list, (p1, p2) -> p1.toString().compareTo(p2.toString()));
		return list;
	}
}
