package org.contextvox.services.replacer.dictionnaries;

/**
 * Utility class that contains most used key to search messages using
 * {@link Messages#getSingleMessage(String)}.
 *
 */
public class MsgKey {

	// Error messages
	public static final String ERROR_NO_EDITOR = "noEditorSelected";
	public static final String ERROR_NO_STATEMENT = "noStatementSelected";
	public static final String ERROR_NO_NEXT_NODE = "noNextNodeFound";
	public static final String ERROR_LANDMARK_DIFFERENT_EDITOR = "lineInDifferentEditor";

	// warnings and info
	public static final String WARNING_BOTTOM_UP_SELECTION = "warningBottomUp";
	public static final String INFO_LANDMARK_RESET = "landmarksReset";

	// read mode selection
	public static final String INSPECTION_MODE = "inspectionMode";
	public static final String READ_MODE = "readMode";
	public static final String OVERVIEW_MODE = "overviewMode";
}
