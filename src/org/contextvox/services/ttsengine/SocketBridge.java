package org.contextvox.services.ttsengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible for managing low-level connection with the client. Note that only
 * a single client at the time is allowed.
 */
class SocketBridge {

	// TODO move into messages
	private static final String NO_CLIENT_CONNECTED = "No client connected";

	private static final Logger logger = Logger.getLogger(SocketBridge.class.getName());

	static {
		logger.setFilter(new FeedbackFilter());
	}

	// amount of seconds that the accepter-thread stays blocked on the accept()
	// method on server socket
	private static final int WAIT_TIMEOUT_SECS = 30;
	private Socket client = null;

	// TODO remove thread-safety volatile for now
	private volatile boolean running = false;
	private final ServerSocket server;
	private PrintWriter writer = null;

	private SocketBridge(final ServerSocket server) {
		this.server = server;
		this.running = true;
	}

	/**
	 * Return a new server running on the specified port.
	 *
	 * @param port
	 *            the specific server port
	 *
	 * @return SocketBridge or NULL if something goes wrong during the
	 *         connection setup, for example if the server port is already
	 *         bound.
	 */
	public static SocketBridge getNewServer(final int port) {
		logger.info("Setup socket bridge on port " + port);
		final String serverName = String.format("Server[%d]:", port);

		// building server
		ServerSocket server = null;
		try {
			server = new ServerSocket();
		} catch (final IOException e1) {
			logger.log(Level.SEVERE, serverName + "error when opening the socket", e1);
			return null;
		}

		// attempt to bind the address
		logger.fine("Try to bind address...");
		try {
			server.bind(new InetSocketAddress(port));
		} catch (final IOException e) {
			// the address is in use
			final String msg = String.format("%s the port %d is already in use", serverName, port);
			logger.warning(msg);
			// cleanup resources
			if (server != null && !server.isClosed())
				try {
					server.close();
				} catch (final IOException e1) {
					logger.log(Level.SEVERE, serverName + "Error when closing the broken server socket", e);
				}
			return null;
		}
		logger.info(String.format("%s listening on address 127.0.0.1:%d", serverName, port));

		// accept timeout
		logger.fine("Setting server socket timeout...");
		try {
			server.setSoTimeout(WAIT_TIMEOUT_SECS * 1000);
		} catch (final SocketException e) {
			logger.log(Level.SEVERE, serverName + "Error when setting the server accepter timeout", e);
		}

		final SocketBridge socketBridge = new SocketBridge(server);

		if (socketBridge != null)
			logger.fine("Initializing socket bridge...");
		socketBridge.init();

		return socketBridge;
	}

	/**
	 * Disconnect the client, if present.
	 */
	public synchronized void disconnect() {
		logger.info("Disconnecting socket bridge");
		this.running = false;
		try {
			if (client != null)
				client.close();
		} catch (final IOException e) {
			logger.log(Level.SEVERE, "Error when disconnecting the SocketBridge", e);
		}

		logger.info("VoxBridge disconnected");
	}

	@SuppressWarnings("unused")
	private synchronized Socket getClient() {
		return client;
	}

	// TODO remove it if not necessary
	public int getServerPort() {
		return this.server.getLocalPort();
	}

	private synchronized PrintWriter getWriter() {
		return writer;
	}

	/**
	 * Start server, wait for client.
	 */
	private void init() {
		// start thread that waits for clients
		final Thread daemon = new Thread(new Runnable() {

			@Override
			public void run() {
				logger.fine("Starting client-accepter daemon");
				while (running) {
					try {
						// wait for client
						final Socket newClient = server.accept();
						logger.info("New client connected");
						// add client and output reference
						setClient(newClient);
						setWriter(new PrintWriter(newClient.getOutputStream(), true));
						// send welcome message
						// writeMessage(Command.FREE,
						// Messages.getSingleMessage("welcomeMessage"));
						// FIXME re-activate
						writeMessage(ProtocolCommand.FREE, "Welcome to ContextVox");
					} catch (final java.io.InterruptedIOException ioe) {
						// catch timeout exception
						continue;
					} catch (final IOException e) {
						logger.log(Level.SEVERE, "Error accepting new client", e);
					}
				}

				// stop server
				try {
					if (!server.isClosed())
						server.close();
				} catch (final IOException e) {
					logger.log(Level.SEVERE, "Error closing server socket", e);
				}

				logger.info(String.format("Server stopped (%d)", getServerPort()));
			}
		});
		daemon.setName("Accepter");
		daemon.start();
	}

	/**
	 * Returns if the socket is in running mode.
	 * 
	 * @return true until disconnect method is called
	 */
	public boolean isRunning() {
		return this.running;
	}

	private synchronized void setClient(final Socket client) {
		if (this.client != null) {
			try {
				this.client.close();
			} catch (final IOException e) {
				logger.severe("Error when previous closing client connection");
			}
		}
		this.client = client;
	}

	private synchronized void setWriter(final PrintWriter writer) {
		this.writer = writer;
	}

	/**
	 * Write message through socket, client must specify the command for the
	 * screen reader and the relative message, but not the end-of-line (will be
	 * added automatically).
	 *
	 * @param command
	 * @param message
	 */
	public synchronized void writeMessage(final ProtocolCommand command, String message) {
		final PrintWriter wr = getWriter();
		logMessage(command, message, wr);

		// sending real message to the client
		if (wr != null) {
			message = message.trim();
			message = command.get(message);
			// wr.println(message);
			wr.print(message);
		}
	}

	private void logMessage(final ProtocolCommand command, final String message, final PrintWriter wr) {
		// log
		final StringBuilder sb = new StringBuilder();
		if (wr == null) {
			sb.append(String.format("[%s] ", NO_CLIENT_CONNECTED));

		}
		// TODO message source
		// if
		// (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.PROMPT_MESSAGESOURCE_KEY))
		// sb.append(String.format("[%s]", VoxBridge.getLastState()));

		// TODO choose beautify prompt or just the raw string
		// if
		// (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.BEAUTIFY_COMMAND_KEY))
		// sb.append(String.format("[%s]", command));
		// else
		// sb.append(command.getValue());
		// TODO re-activate
		// sb.append(Symbol.space);
		// sb.append(" ");
		sb.append(message);
		logger.fine(sb.toString());
	}

	/**
	 * Specify the message syntax for each command sent to the client.
	 */
	public enum ProtocolCommand {
		EXIT("e%s%n"), FREE("f%s%n"), READ("r%s%n");

		private final String template;

		private ProtocolCommand(final String template) {
			this.template = template;
		}

		/**
		 * Produces a protocol message.
		 * 
		 * @param message
		 *            the text to send, without end-of-line
		 * @return the message to be sent
		 */
		public String get(final String message) {
			return String.format(this.template, message);
		}
	}

}