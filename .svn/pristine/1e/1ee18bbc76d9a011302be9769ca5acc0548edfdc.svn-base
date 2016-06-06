package org.bigmouth.nvwa.session.xmc;

import org.bigmouth.nvwa.session.DefaultSession;
import org.bigmouth.nvwa.session.SessionHolder;

public class SimpleXmcSession extends DefaultSession {

	private static final long serialVersionUID = -5440640898228565040L;

    public SimpleXmcSession(String id, SessionHolder sessionHolder) {
		super(id, sessionHolder);
	}

	@Override
	public void setAttribute(String key, Object value) {
		super.setAttribute(key, value);
		storeSession();
	}

	@Override
	public void update(long time) {
		super.update(time);
		storeSession();
	}

	private void storeSession() {
		getHolder().put(this);
	}
}
