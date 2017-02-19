package org.contextvox.services;

/**
 * Manages services and provides them to clients. If not specified, the service
 * reference is always valid and can be stored in a variable; that means that
 * there is no need to call the service handler each time a service is require
 * by same client. The service acquisition is thread-safe, but each service has
 * its own policy about thread-safety.
 *
 * @author Simone Mignami
 *
 */
public interface ServiceHandler {

	// TODO implement
	// public ReplacerService getReplacerService();
	// public TextualContextService getTextualContext();
	// public TTSService getTTSService();

}
