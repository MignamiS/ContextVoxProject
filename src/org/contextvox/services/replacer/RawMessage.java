package org.contextvox.services.replacer;

import java.util.Map;

import org.contextvox.services.replacer.dictionnaries.SentenceAlternative;
import org.contextvox.services.replacer.dictionnaries.SentenceType;

/**
 * Textual data package. This class is not thread-safe.
 *
 * @author Simone Mignami
 *
 */
class RawMessage {

	private String text;
	private Map<SentenceToken, String> data;
	private SentenceAlternative alternative;
	private SentenceType type;

	public RawMessage(final String text) {
		this.text = text;
	}

	public RawMessage() {
	}

	public SentenceAlternative getAlternative() {
		return alternative;
	}

	public Map<SentenceToken, String> getData() {
		return data;
	}

	public String getText() {
		return text;
	}

	public SentenceType getType() {
		return type;
	}

	public void setAlternative(final SentenceAlternative alternative) {
		this.alternative = alternative;
	}

	public void setData(final Map<SentenceToken, String> data) {
		this.data = data;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setType(final SentenceType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RawMessage [text=" + text + ", data=" + data + "]";
	}

}
