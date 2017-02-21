package org.contextvox.services.replacer;

/**
 * Token used for building parameterizable sentences
 */
public enum SentenceToken {
	// TODO need refactor

	BLANK {
		@Override
		public String getToken() {
			return "{$BLANK}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = {};
			return opt;
		}
	},
	ITERATOR {
		@Override
		public String getToken() {
			return "{$ITER}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "iterator", "i" };
			return opt;
		}
	},
	START {
		@Override
		public String getToken() {
			return "{$START}";
		}

		@Override
		public String[] getOptions() {
			return null;
		}
	},
	END {
		@Override
		public String getToken() {
			return "{$END}";
		}

		@Override
		public String[] getOptions() {
			return null;
		}

	},
	CONDITION {

		@Override
		public String getToken() {
			return "{$COND}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "condition", "c" };
			return opt;
		}
	},
	UPDATE {
		@Override
		public String getToken() {
			return "{$UPDATE}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "update", "u" };
			return opt;
		}
	},
	COLLECTION {
		@Override
		public String getToken() {
			return "{$COLLECTION}";
		}

		@Override
		public String[] getOptions() {
			return null;
		}
	},
	SELECTOR {
		@Override
		public String getToken() {
			return "{$SELECTOR}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "selector", "sel" };
			return opt;
		}
	},
	TYPE {
		@Override
		public String getToken() {
			return "{$TYPE}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "type", "t" };
			return opt;
		}
	},
	LIST {
		@Override
		public String getToken() {
			return "{$LIST}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "caselist", "cs", "list" };
			return opt;
		}
	},
	NAME {
		@Override
		public String getToken() {
			return "{$NAME}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "name", "n" };
			return opt;
		}
	},
	MODIFIERS {
		@Override
		public String getToken() {
			return "{$MOD}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "modifiers", "mod" };
			return opt;
		}
	},
	PARAMETERS {
		@Override
		public String getToken() {
			return "{$PARAM}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "param", "p" };
			return opt;
		}
	},
	RETURNTYPE {
		@Override
		public String getToken() {
			return "{$RETURN}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "return", "r" };
			return opt;
		}
	},
	EXTEND {
		@Override
		public String getToken() {
			return "{$EXTEND}";
		}

		@Override
		public String[] getOptions() {
			return null;
		}
	},
	IMPLEMENT {
		@Override
		public String getToken() {
			return "{$IMPLEMENT}";
		}

		@Override
		public String[] getOptions() {
			return null;
		}
	},
	EXCEPTION {
		@Override
		public String getToken() {
			return "{$EXCEPTION}";
		}

		@Override
		public String[] getOptions() {
			return null;
		}
	},
	NUMBER {
		@Override
		public String getToken() {
			return "{$NUMBER}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	STATUS {
		@Override
		public String getToken() {
			return "{$STATUS}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	STATEMENT {
		@Override
		public String getToken() {
			return "{$STATEMENT}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	ELSE_STATEMENT {
		@Override
		public String getToken() {
			return "{$ELSESTMT}";
		}

		@Override
		public String[] getOptions() {
			final String[] opt = { "else", "e" };
			return opt;
		}
	},
	FINALLY {
		@Override
		public String getToken() {
			return "{$FINALLY}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	METHOD {
		@Override
		public String getToken() {
			return "{$METHOD}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	CLASS {
		@Override
		public String getToken() {
			return "{$CLASS}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	PACKAGE {
		@Override
		public String getToken() {
			return "{$PACKAGE}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	OUTSIDE {
		@Override
		public String getToken() {
			return "{$OUTSIDE}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	LOCALIZATION {
		@Override
		public String getToken() {
			return "{$LOCALIZATION}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	LEFT_HAND {
		@Override
		public String getToken() {
			return "{$LEFTHAND}";
		}

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	/**
	 * Returns the plain token string, that is found on the ".messages" files.
	 *
	 * @return token string
	 */
	public abstract String getToken();

	/**
	 * Returns an array of possible option to match the ones get from the
	 * command line. Only the chars are returned, without the dash "-".
	 *
	 * @return an array of options
	 */
	public abstract String[] getOptions();

	/**
	 * Returns the corresponding SentenceToken from an option of the command
	 * line. Option must not contain the dash "-".
	 *
	 * @param option
	 *            the option from the command line
	 * @return the corresponding SentenceToken, or <code>null</code> if nothing
	 *         is found
	 */
	public static SentenceToken getTokenFromOption(final String option) {
		final SentenceToken[] vals = SentenceToken.values();
		for (final SentenceToken value : vals) {
			final String[] opts = value.getOptions();
			if (opts == null || opts.length == 0)
				continue;
			// look for given option
			for (final String opt : opts) {
				if (option.equalsIgnoreCase(opt))
					return value;
			}
		}

		return null;
	}
}
