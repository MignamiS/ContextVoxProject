package org.contextvox.services.replacer;

import java.util.List;

/**
 * Contains other replacers and call them.
 *
 * @author Simone Mignami
 *
 */
class ReplacerContainer implements Replacer {

	private final List<Replacer> replacers;

	public ReplacerContainer(final List<Replacer> replacers) {
		this.replacers = replacers;
	}

	@Override
	public void replace(final RawMessage data) {
		for (final Replacer replacer : replacers)
			replacer.replace(data);
	}

}
