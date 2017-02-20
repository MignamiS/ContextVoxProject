package org.contextvox.services.ttsengine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.contextvox.plugin.MessageSource;
import org.contextvox.services.ttsengine.SocketBridge.ProtocolCommand;

/**
 * The VoxBridge uses a socket communication to transmit a string to an external
 * program, that produces the audio feedback.
 * 
 * <p>
 * This engine uses a protocol to specify in which way the message has to be
 * read
 * </p>
 */
public class VoxBridge implements TTSEngine {

	/** First server port */
	public static final int PRIMARY_SERVER_PORT = 23418;
	/** Alternative server port, if the principal is in use */
	public static final int SECONDARY_SERVER_PORT = 23421;

	// last sent command, for performance purpose
	private static ProtocolCommand lastCommand;

	// last message source that sent a message (used for log purpose)
	private static MessageSource lastState;

	private static final Logger logger = Logger.getLogger(VoxBridge.class.getName());
	// milliseconds of buffer storage
	// private static final int THRESHOLD = 2;

	/** Defines how to read each message */
	private final Map<MessageSource, ReadCommand> selector;
	private SocketBridge server;

	VoxBridge(final SocketBridge server) {
		this.server = server;
		// init message source selector map
		this.selector = new HashMap<>();
		populateSelector();
	}

	/**
	 * Factory method, builds a new core.
	 *
	 * @param port
	 *            the specific server port
	 *
	 * @return the core or <code>null</code> if something goes wrong, for
	 *         example when the port is already use
	 */
	public static TTSEngine build(final int port) {
		logger.info("VoxBridge setup procedure started");
		// init server
		final SocketBridge bridge = SocketBridge.getNewServer(port);

		if (bridge != null) {
			// server has been instantiated correctly
			logger.info("VoxBridge setup successful");
			return new VoxBridge(bridge);
		} else {
			// TODO instead of returning null, throw a custom exception.
			// something goes wrong during the server setup
			return null;
		}
	}

	public static MessageSource getLastState() {
		return lastState;
	}

	/**
	 * Returns the current server port.
	 *
	 * @return the server port or <code>-1</code> if there is no address bound
	 *         or server in use.
	 */
	// TODO remove if not necessary
	public int getServerPort() {
		if (server != null)
			return server.getServerPort();
		else
			return -1;
	}

	@Override
	public TTSEngine.Type getType() {
		return TTSEngine.Type.VOX_BRIDGE;
	}

	private void populateSelector() {
		// some standard read command
		final ReadCommand onlyFree = new ReadCommand() {
			@Override
			public void read(final String message) {
				// VoxBridge bridge = null;
				// if (CoreProxy.getCurrentMediator() instanceof VoxBridge)
				// bridge = (VoxBridge) CoreProxy.getCurrentMediator();
				// else
				// return;

				if (lastCommand != ProtocolCommand.FREE) {
					server.writeMessage(ProtocolCommand.FREE, message);
					lastCommand = ProtocolCommand.FREE;
				} else {
					logger.fine("(Free)" + message);
				}
			}
		};

		final ReadCommand selectHighOrLowLevel = new ReadCommand() {
			@Override
			public void read(final String message) {
				// VoxBridge bridge = null;
				// if (CoreProxy.getCurrentMediator() instanceof VoxBridge)
				// bridge = (VoxBridge) CoreProxy.getCurrentMediator();
				// else
				// return;

				// TODO re-activate
				// if (ContextVoxProgram.isHighLevelReading()) {
				// // read a high level read
				server.writeMessage(ProtocolCommand.READ, message);
				lastCommand = ProtocolCommand.READ;
				// } else {
				// read freely the GUI
				if (lastCommand != ProtocolCommand.FREE) {
					server.writeMessage(ProtocolCommand.FREE, message);
					lastCommand = ProtocolCommand.FREE;
				} else {
					logger.fine("(Free) " + message);
				}
			}
		};

		final ReadCommand onlyRead = new ReadCommand() {
			@Override
			public void read(final String message) {
				// VoxBridge bridge = null;
				// if (CoreProxy.getCurrentMediator() instanceof VoxBridge)
				// bridge = (VoxBridge) CoreProxy.getCurrentMediator();
				// else
				// return;

				lastCommand = ProtocolCommand.READ;
				server.writeMessage(ProtocolCommand.READ, message);
			}
		};

		// populates
		selector.put(MessageSource.GENERIC_READ, onlyRead);
		selector.put(MessageSource.GENERIC_FREE, onlyFree);
		// TODO re-activate
		// selector.put(MessageSource.MENU, onlyRead);
		// selector.put(MessageSource.JAVACHANGE_EVENT, onlyRead);
		// selector.put(MessageSource.VIEW, onlyRead);

		// mapping editor
		// selector.put(MessageSource.EDITOR, onlyRead);
		// selector.put(MessageSource.EDITOR_TYPING, onlyRead);
		// selector.put(MessageSource.EDITOR_READ_LINE, onlyRead);
		// selector.put(MessageSource.EDITOR_READ_WORD, onlyRead);
		// selector.put(MessageSource.EDITOR_READ_CHAR, onlyRead);
		// selector.put(MessageSource.EDITOR_SELECTION, onlyRead);

		// mapping active shell
		// selector.put(MessageSource.TEXT, onlyFree);
		// selector.put(MessageSource.DIALOG, onlyFree);
		// selector.put(MessageSource.TREE, onlyFree);
		// selector.put(MessageSource.TREE_ITEM, selectHighOrLowLevel);
		// selector.put(MessageSource.LINK, onlyFree);
		// selector.put(MessageSource.TABLE, onlyFree);
		// selector.put(MessageSource.TABLE_ITEM, onlyFree);
		// selector.put(MessageSource.WINDOW, onlyFree);

		// mapping views
		// selector.put(MessageSource.CODEINSPECTOR, onlyRead);
		// selector.put(MessageSource.COMMANDLINE, onlyRead);
		// selector.put(MessageSource.CONSOLE, onlyFree);
		// selector.put(MessageSource.OUTLINE, onlyRead);
		// selector.put(MessageSource.PACKAGE_EXPLORER, onlyRead);
		// selector.put(MessageSource.JUNIT, onlyFree);
		// selector.put(MessageSource.PROBLEMS_VIEW, onlyFree);
	}

	@Override
	public void readString(final String message, final MessageSource source) {
		// TODO implement thrashold using a job of Eclipse, that sends the
		// message after delay
		// concatenate other request
		// String message = data.getMessage();
		// MessageSource lastSource = data.getSource();
		// boolean isBomb = data.isBomb();
		// // wait thrashold
		// Thread.sleep(THRESHOLD);
		// do {
		// data = queue.poll();
		// if (data != null) {
		// logger.fine("+ " + data.toString() + " (concatenated");
		// message += data.getMessage();
		// lastSource = data.getSource();
		// // flag must stay true when is true
		// isBomb = (isBomb) ? true : data.isBomb();
		// }
		// } while (data != null);
		// readString(message, lastSource);

		// // send EXIT command to client
		// server.writeMessage(Command.EXIT,
		// Messages.getSingleMessage("disconnectingServer"));
		//
		// // terminate
		// realShutdown();

		// look into selector if the message-source is mapped
		if (selector.containsKey(source)) {
			// cleanup and read
			lastState = source;
			selector.get(source).read(message);
		} else {
			logger.severe("Selector map has no operation for " + source);
		}
	}

	@Override
	public void shutdown() {
		server.disconnect();
		server = null;
	}

	@Override
	public void silence() {
		// TODO better?
		readString(" ", MessageSource.GENERIC_FREE);
	}

	/**
	 * Read strategy.
	 */
	private interface ReadCommand {
		public void read(String message);
	}

}
