package org.bigmouth.nvwa.transport;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TargetSessionListener {
	static final Logger LOGGER = LoggerFactory.getLogger(TargetSessionListener.class);
	public static final TargetSessionListener DEFAULT = new TargetSessionListener() {

		@Override
		public void onSessionsChannged(Collection<IoSession> sessions) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("connected sessions changed,session detail:" + sessions);
		}
	};

	public void onSessionsChannged(Collection<IoSession> sessions);
}
