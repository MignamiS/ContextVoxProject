package org.contextvox.services.ttsengine;

import org.contextvox.plugin.MessageSource;
import org.contextvox.services.Service;

/**
 * A module that converts text into vocal representation (Text-to-Speech). This
 * class is not thread-safe.
 * 
 * @author Simone Mignami
 *
 */
public class TTSService implements Service {

	private TTSEngine engine;

	@Override
	public void init() {
		// TODO persist default
		engine = VoxBridge.build(VoxBridge.PRIMARY_SERVER_PORT);
	}

	/**
	 * Checks whether the service is active and can produce a result.
	 * 
	 * @return whether the service is active
	 */
	public boolean isActive() {
		return engine != null;
	}

	/**
	 * Reads the given string.
	 * 
	 * @param message
	 *            the text to be read
	 * @param source
	 *            the source of the message. In some TTS engines produces
	 *            different effects
	 */
	public void readString(final String message, final MessageSource source) {
		if (isActive())
			engine.readString(message, source);
	}

	@Override
	public void shutdown() {
		engine = null;
	}

	/**
	 * Stop speech.
	 */
	public void silence() {
		if (isActive())
			engine.silence();
	}

}
