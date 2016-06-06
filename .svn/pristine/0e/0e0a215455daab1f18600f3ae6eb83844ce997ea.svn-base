package org.bigmouth.nvwa.transport;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DefaultTargetSessionLocator implements TargetSessionLocator,
		TargetSessionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTargetSessionLocator.class);

	private AtomicInteger sessionIdx = new AtomicInteger(0);
	private AtomicReference<IoSession[]> targetIoSessions = new AtomicReference<IoSession[]>();
	private TargetSessionHolder targetSessionHolder;

	@Override
	public IoSession lookup() {
		IoSession[] sessions = targetIoSessions.get();

		while (sessions == null || 0 == sessions.length) {
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("lookupSendSession:", e);
				}
			}
			sessions = targetIoSessions.get();
		}

		// decide which idx for session
		int idx = sessionIdx.getAndIncrement();
		if (idx >= sessions.length) {
			idx = 0;
			sessionIdx.set(idx);
		}

		final IoSession session = sessions[idx];
		return session;
	}

	@Override
	public void onSessionsChannged(Collection<IoSession> sessions) {
		targetIoSessions.set(sessions.toArray(new IoSession[0]));
	}

	public void setSessionHolder(TargetSessionHolder proxySessionHolder) {
		if (null == proxySessionHolder)
			throw new NullPointerException("proxySessionHolder");
		this.targetSessionHolder = proxySessionHolder;
	}

	@Override
	public void destroy() {
		targetSessionHolder.destory();
		targetSessionHolder = null;
		targetIoSessions.set(null);
	}
	
	@Override
    public int getSessions() {
        return targetIoSessions.get().length;
    }
}
