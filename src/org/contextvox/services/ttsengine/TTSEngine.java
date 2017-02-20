package org.contextvox.services.ttsengine;

import org.contextvox.plugin.MessageSource;

/**
 * Represent a single TTS processor.
 * 
 * @author Simone Mignami
 *
 */
public interface TTSEngine {

	public TTSEngine.Type getType();

	/**
	 * Reads the given string.
	 * 
	 * @param message
	 *            the message to be read
	 * @param source
	 *            the source of the message
	 */
	public void readString(String message, MessageSource source);

	/**
	 * Interrupts the current speech (if supported). The speech is cancelled.
	 */
	public void silence();

	/**
	 * Perform some shutdown task, optional
	 */
	public void shutdown();

	public enum Type {

		NONE("None"), VOX_BRIDGE("VoxBridge");

		private final String label;

		/**
		 * Get corresponding type from the given label
		 *
		 * @param label
		 * @return CoreType or <code>null</code> if noone corresponds
		 */
		public static Type getCoreTypeFromLabel(final String label) {
			final Type[] vals = Type.values();
			for (final Type item : vals) {
				if (item.getLabel().equalsIgnoreCase(label))
					return item;
			}
			return null;
		}

		private Type(final String label) {
			this.label = label;
		}

		/**
		 * Return the property label (value)
		 *
		 * @return value
		 */
		public String getLabel() {
			return label;
		}
	}

}
