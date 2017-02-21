package org.contextvox;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.contextvox.plugin.ContextVox;
import org.contextvox.plugin.VoxRequest;
import org.contextvox.services.replacer.dictionnaries.Messages;
import org.contextvox.utils.FeedbackFilter;
import org.contextvox.utils.VoxLogFormatter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle This class is
 * thread-safe.
 */
public class ContextVoxActivator extends AbstractUIPlugin {

	private static final int QUEUE_SIZE = 100;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.contextvox"; //$NON-NLS-1$

	// The shared instance
	private static ContextVoxActivator plugin;
	private static ContextVox instance;

	private final LinkedBlockingQueue<VoxRequest> queue;

	public ContextVoxActivator() {
		queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
	}

	/**
	 * Returns the singleton of the ContextVox program.
	 * 
	 * @return the CV's instance
	 */
	public static ContextVox app() {
		return instance;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ContextVoxActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Processes normal request. This method is the only way to communicate with
	 * the thread that contains the CVox program.
	 * 
	 * @param request
	 *            the request generated by the user interaction
	 */
	public void processRequest(final VoxRequest request) {
		// TODO some check of fullness
		this.queue.offer(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		instance = new ContextVox(queue);
		plugin.getWorkbench().addWorkbenchListener(new WBListener());

		// logger
		// TODO preferences setup
		final Logger root = LogManager.getLogManager().getLogger("");
		root.setLevel(Level.FINE);
		final Handler handler = root.getHandlers()[0];
		handler.setLevel(Level.FINE);
		handler.setFormatter(new VoxLogFormatter());
		handler.setFilter(new FeedbackFilter());

		Messages.load(Messages.DEFAULT_LANGUAGE);

		new Thread(instance).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		instance = null;
		super.stop(context);
	}

	/**
	 * The activator class controls the plug-in life cycle
	 */
	class WBListener implements IWorkbenchListener {

		@Override
		public boolean preShutdown(final IWorkbench workbench, final boolean forced) {
			return true;
		}

		@Override
		public void postShutdown(final IWorkbench workbench) {
			instance.shutdown();
		}

	}
}
