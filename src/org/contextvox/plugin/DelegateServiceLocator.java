package org.contextvox.plugin;

import org.contextvox.services.ServiceLocator;
import org.contextvox.services.ttsengine.TTSService;

class DelegateServiceLocator implements ServiceLocator {

	private final TTSService tts;

	public DelegateServiceLocator() {
		this.tts = new TTSService();
		this.tts.init();
	}

	@Override
	public TTSService getTTSService() {
		return this.tts;
	}

}
