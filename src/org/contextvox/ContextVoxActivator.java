package org.contextvox;

import java.util.concurrent.LinkedBlockingQueue;

import org.contextvox.plugin.ContextVox;
import org.contextvox.plugin.VoxRequest;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ContextVoxActivator extends AbstractUIPlugin {

	private static final int QUEUE_SIZE = 100;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.contextvox"; //$NON-NLS-1$

	// The shared instance
	private static ContextVoxActivator plugin;
	private ContextVox instance;

	private final LinkedBlockingQueue<VoxRequest> queue;

	public ContextVoxActivator() {
		queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
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
}
