package org.bigmouth.nvwa.utils.change;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractChangeMonitor<E extends ChangeEvent<S>, S> extends
		BaseLifeCycleSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChangeMonitor.class);
	private static final String DEFAULT_THREAD_NAME = "change-monitor-thread";
	private static final long DEFAULT_CHECK_PERIOD_MILLIS = 10000;

	private final String threadName;
	private final long checkPeriodMillis;
	private final Map<String, ChangeListener<S>> listeners = new ConcurrentHashMap<String, ChangeListener<S>>();

	private ScheduledExecutorService executorService;

	private S lastSnapshot = null;

	public AbstractChangeMonitor(Map<String, ChangeListener<S>> listeners) {
		this(DEFAULT_THREAD_NAME, DEFAULT_CHECK_PERIOD_MILLIS, listeners);
	}

	public AbstractChangeMonitor(String threadName, long checkPeriodMillis,
			Map<String, ChangeListener<S>> listeners) {
		if (StringUtils.isBlank(threadName))
			throw new IllegalArgumentException("threadName is blank.");
		if (0 >= checkPeriodMillis)
			throw new IllegalArgumentException("0 >= checkPeriodMillis");
		if (null == listeners)
			listeners = new HashMap<String, ChangeListener<S>>();
		this.threadName = threadName;
		this.checkPeriodMillis = checkPeriodMillis;
		this.listeners.putAll(listeners);
	}

	@Override
	protected void doInit() {
		executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}
		});
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("ChangeMonitor has been initialized.");
	}

	@Override
	protected void doDestroy() {
		executorService.shutdownNow();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("ChangeMonitor has been destroyed.");
	}

	public void registerListener(String name, ChangeListener<S> listener) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("name is blank.");
		if (null == listener)
			throw new NullPointerException("listener");
		this.listeners.put(name, listener);
	}

	public void unregisterListener(String name) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("name is blank.");
		this.listeners.remove(name);
	}

	/**
	 * return value must be not null.
	 * 
	 * @return
	 */
	protected abstract S createCurrentSnapshot();

	protected abstract E createChangeEvent(S oldSnapshot, S newSnapshot);

	private void checkChange() {
		S newSnapshot = createCurrentSnapshot();
		if (null == newSnapshot)
			throw new NullPointerException("newSnapshot is null.");
		if (!isModified(newSnapshot)) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("snapshot is not changed,ignore.");
			return;
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("snapshot is changed,new snapshot:" + newSnapshot);

		S oldSnapshot = updateLastSnapshot(newSnapshot);
		E event = createChangeEvent(oldSnapshot, newSnapshot);

		fireChangeEvent(event);
	}

	private S updateLastSnapshot(S newSnapshot) {
		S oldSnapshot = lastSnapshot;
		lastSnapshot = newSnapshot;
		return oldSnapshot;
	}

	private void fireChangeEvent(E event) {
		for (ChangeListener<S> cl : this.listeners.values()) {
			try {
				cl.onChanged(event);
			} catch (Exception e) {
				LOGGER.error("fireChangeEvent:", e);
			}
		}
	}

	private boolean isModified(Object newSnapshot) {
		if (null == lastSnapshot) {
			return true;
		} else {
			if (lastSnapshot == newSnapshot) {
				return false;
			}
			if (lastSnapshot.hashCode() != newSnapshot.hashCode()) {
				return true;
			} else {
				if (!lastSnapshot.equals(newSnapshot))
					return true;
			}
		}
		return false;
	}

	public void run() {
		if (!isInitialized())
			throw new IllegalStateException("ChangeMonitor has not been initialized.");

		executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					checkChange();
				} catch (RuntimeException e) {
					LOGGER.error("run:", e);
				}
			}
		}, 0, checkPeriodMillis, TimeUnit.MILLISECONDS);
	}
}
