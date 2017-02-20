package org.contextvox.services;

/**
 * A service represent a module that provides a certain feature. A service
 * should be independent from the entire application.
 * 
 * @author Simone Mignami
 *
 */
public interface Service {

	/**
	 * Initialization of the service.
	 */
	public void init();

	/**
	 * Clean-up operations.
	 */
	public void shutdown();
}
