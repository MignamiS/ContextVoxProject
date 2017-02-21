package org.contextvox.services.replacer;

interface Replacer {

	/**
	 * Given a set of text data, will process it.
	 *
	 * @param data
	 */
	public void replace(RawMessage data);

}