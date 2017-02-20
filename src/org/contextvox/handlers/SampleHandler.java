package org.contextvox.handlers;

import org.contextvox.ContextVoxActivator;
import org.contextvox.plugin.VoxRequest;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final VoxRequest r = new VoxRequest();
		ContextVoxActivator.getDefault().processRequest(r);

		// final IWorkbenchWindow window =
		// HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// MessageDialog.openInformation(window.getShell(), "ContextVox",
		// "Hello, Eclipse world");
		return null;
	}
}
