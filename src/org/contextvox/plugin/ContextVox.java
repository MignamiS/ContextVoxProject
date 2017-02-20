package org.contextvox.plugin;

import java.util.concurrent.LinkedBlockingQueue;

import org.contextvox.services.ServiceLocator;
import org.contextvox.services.ttsengine.TTSService;

/**
 * The ContextVox's main class. Is a mediator between many feature.
 * 
 * <p>
 * The program runs into a separate thread from the UI-thread because when
 * debugging using breakpoints will freeze the UI and crash NVDA. This is
 * (hopefully) a temporary solution. The instance of this class uses a blocking
 * queue to receive and process the requests from the handlers.
 * </p>
 * 
 * This class is thread-safe.
 * 
 * @author Simone Mignami
 *
 */
public class ContextVox implements Runnable, ServiceLocator {

	private final LinkedBlockingQueue<VoxRequest> queue;

	private final DelegateServiceLocator services;

	public ContextVox(final LinkedBlockingQueue<VoxRequest> queue) {
		this.queue = queue;
		this.services = new DelegateServiceLocator();
	}

	@Override
	public void run() {
		while (true) {
			VoxRequest request = null;
			try {
				request = queue.take();
			} catch (final InterruptedException e) {
				// TODO log error
				e.printStackTrace();
			}
			if (request instanceof BombRequest)
				break;

			// TODO implement request process
			getTTSService().readString("You pressed the menu item", MessageSource.GENERIC_READ);
		}

		// TODO implement main loop exit
	}

	@Override
	public TTSService getTTSService() {
		return this.services.getTTSService();
	}

	public void shutdown() {
		this.services.shutdownAll();
	}

}
