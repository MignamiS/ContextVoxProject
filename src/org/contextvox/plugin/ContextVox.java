package org.contextvox.plugin;

import java.util.concurrent.LinkedBlockingQueue;

import org.contextvox.services.ServiceHandler;

// TODO jd
public class ContextVox implements Runnable, ServiceHandler {

	private final LinkedBlockingQueue<VoxRequest> queue;

	public ContextVox(final LinkedBlockingQueue<VoxRequest> queue) {
		this.queue = queue;
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
		}

		// TODO implement main loop exit
	}

}
