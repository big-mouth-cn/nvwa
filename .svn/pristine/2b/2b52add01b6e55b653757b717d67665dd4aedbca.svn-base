package org.bigmouth.nvwa.transport;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetSessionHolder {
	private static final Logger LOGGER = LoggerFactory.getLogger(TargetSessionHolder.class);
	private static final TargetSessionListener DEFAULT_SESSION_LISTENER = TargetSessionListener.DEFAULT;

	private final IoConnector ioConnector;
	private final InetSocketAddress endPointAddress;
	private final TargetSessionListener changeListener;

	private volatile int maxSessionCount;
	private volatile long reconnectTimeout;

	private final ScheduledExecutorService exec;
	private final Collection<IoSession> sessionStore = new LinkedList<IoSession>();
	private ConnectFuture connectFuture = null;

	public TargetSessionHolder(IoConnector ioConnector, InetSocketAddress endPointAddress,
			int maxSessionCount, long reconnectTimeout, TargetSessionListener changeListener,
			final String threadName) {
		if (null == ioConnector)
			throw new NullPointerException("ioConnector");
		if (null == ioConnector.getHandler())
			throw new IllegalStateException("proxy ioConnector' handler is not set.");
		if (null == endPointAddress)
			throw new NullPointerException("endPointAddress");
		if (maxSessionCount < 1)
			throw new IllegalArgumentException("maxSessionCount:" + maxSessionCount);
		if (reconnectTimeout < 1)
			throw new IllegalArgumentException("reconnectTimeout:" + reconnectTimeout);
		if (null == changeListener)
			changeListener = DEFAULT_SESSION_LISTENER;
		this.ioConnector = ioConnector;
		this.endPointAddress = endPointAddress;
		this.maxSessionCount = maxSessionCount;
		this.reconnectTimeout = reconnectTimeout;
		this.changeListener = changeListener;
		if (StringUtils.isBlank(threadName)) {
			exec = Executors.newSingleThreadScheduledExecutor();
		} else {
			exec = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, threadName);
				}
			});
		}
	}

	public void start() {
		this.ioConnector.getFilterChain().addLast("controller", new IoFilterAdapter() {
			public void sessionClosed(NextFilter nextFilter, final IoSession session)
					throws Exception {
				super.sessionClosed(nextFilter, session);
				exec.submit(new Runnable() {

					public void run() {
						onSessionClosed(session);
					}
				});
			}
		});

		exec.scheduleAtFixedRate(new Runnable() {

			public void run() {
				try {
					doConnect();
				} catch (Exception e) {
					LOGGER.error("doConnect:", e);
				}
			}

		}, 0, reconnectTimeout, TimeUnit.MILLISECONDS);
	}

	private void onSessionClosed(IoSession session) {
		if (sessionStore.remove(session)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("onSessionClosed: remove " + session.getId() + " ok.");
			}
		} else {
			LOGGER.error("onSessionClosed: remove " + session.getId() + " !FAILED!.");
		}

		fireSessionChanged();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("onSessionClosed: " + session.getId() + " closed.");
		}
	}

	private void doConnect() {
		if (sessionStore.size() >= maxSessionCount) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("doConnect: sessionStore.size() >= maxSession.");
			return;
		}
		if (null != connectFuture && !connectFuture.isDone()) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("doConnect: connect already doing, ignore this action.");
			}
			return;
		}

		if (null != endPointAddress) {
			LOGGER.info("start connect " + endPointAddress.getAddress().getHostAddress() + ":"
					+ endPointAddress.getPort());
			connectFuture = ioConnector.connect(endPointAddress);
		} else {
			LOGGER.info("failed to using address provider get address");
			return;
		}

		connectFuture.addListener(new IoFutureListener<ConnectFuture>() {

			public void operationComplete(final ConnectFuture connectFuture) {
				exec.submit(new Runnable() {

					public void run() {
						onConnectComplete(connectFuture);
					}
				});
			}
		});
	}

	private void onConnectComplete(ConnectFuture future) {
		if (future.isConnected()) {
			IoSession session = future.getSession();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("onConnectComplete: session [" + session + "] connected.");
			}
			sessionStore.add(session);
			fireSessionChanged();
		} else {
			// not connected
			LOGGER.error("onConnectComplete: connect failed.");
		}
	}

	private void fireSessionChanged() {
		if (null != changeListener) {
			changeListener.onSessionsChannged(sessionStore);
		}
	}

	public void destory() {
		exec.shutdownNow();
		sessionStore.clear();
	}

	public int getMaxSessionCount() {
		return maxSessionCount;
	}

	public void setMaxSessionCount(int maxSessionCount) {
		this.maxSessionCount = maxSessionCount;
	}

	public long getReconnectTimeout() {
		return reconnectTimeout;
	}

	public void setReconnectTimeout(long reconnectTimeout) {
		this.reconnectTimeout = reconnectTimeout;
	}
}
