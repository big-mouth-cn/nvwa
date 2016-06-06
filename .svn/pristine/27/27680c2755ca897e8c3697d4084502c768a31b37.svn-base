package org.bigmouth.nvwa.cluster.node;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZooKeeperWrapper implements Wrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperWrapper.class);

	private final String connectString;
	private final int sessionTimeout;

	private ZooKeeper zk;

	private final ConcurrentMap<String, DataChangeListener> listeners = new ConcurrentHashMap<String, DataChangeListener>();

	private final Object lock = new Object();
	private volatile boolean initialized = false;

	public ZooKeeperWrapper(String connectString, int sessionTimeout) {
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
	}

	public ZooKeeper getZooKeeperClient() {
		if (!initialized)
			throw new IllegalStateException("ZooKeeperWrapper has not been initialized yet.");
		return zk;
	}

	public byte[] registerDataChangeListener(String path, DataChangeListener listener) {
		if (!initialized)
			throw new IllegalStateException("ZooKeeperWrapper has not been initialized yet.");
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		if (null == listener)
			throw new NullPointerException("listener");

		Stat stat = null;
		try {
			stat = zk.exists(path, true);
		} catch (KeeperException e) {
			throw new RuntimeException("registerDataChangeListener:", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("registerDataChangeListener:", e);
		}
		if (null == stat)
			throw new NoSuchPathException("Path:" + path);

		listeners.put(path, listener);

		try {
			return zk.getData(path, true, stat);
		} catch (KeeperException e) {
			throw new RuntimeException("registerDataChangeListener:", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("registerDataChangeListener:", e);
		}
	}

	private DataChangeListener getListener(String path) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		return listeners.get(path);
	}

	public byte[] getData(String path) {
		if (!initialized)
			throw new IllegalStateException("ZooKeeperWrapper has not been initialized yet.");
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		try {
			Stat stat = zk.exists(path, true);
			if (null == stat) {
				throw new NoSuchPathException("path:" + path);
			}
			return zk.getData(path, true, stat);
		} catch (KeeperException e) {
			throw new RuntimeException("getData:", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("getData:", e);
		}
	}

	@Override
    public byte[] getData(String path, byte[] defaultValue) {
	    throw new RuntimeException("!Not! supported method!");
    }

    public void init() throws IOException {
		synchronized (lock) {
			if (initialized)
				return;
			zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

				public void process(WatchedEvent event) {

					KeeperState keeperState = event.getState();
					EventType eventType = event.getType();
					String path = event.getPath();

					if (KeeperState.SyncConnected == keeperState) {

						if (EventType.NodeDataChanged == eventType) {
							if (LOGGER.isDebugEnabled())
								LOGGER.debug("Path:{} data changed.", path);

							try {
								Stat stat = zk.exists(path, true);
								if (null == stat) {
									if (LOGGER.isDebugEnabled())
										LOGGER.debug("Path:{} is not exists.", path);
									return;
								}
								byte[] content = zk.getData(path, true, stat);
								DataChangeListener dcl = getListener(path);
								if (null == dcl) {
									return;
								}

								dcl.onDataChanged(path, content);
							} catch (KeeperException e) {
								LOGGER.error("process:", e);
							} catch (InterruptedException e) {
								LOGGER.error("process:", e);
							}
						}
					} else if (KeeperState.Disconnected == keeperState) {
						LOGGER.error("ZooKeeper server is disconnected.");
					} else if (KeeperState.AuthFailed == keeperState) {
						LOGGER.error("ZooKeeper auth failed.");
					} else if (KeeperState.Expired == keeperState) {
						LOGGER.error("ZooKeeper session expired.");
					}
				}
			});
			initialized = true;
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("ZooKeeperWrapper has been initialized.");
		}
	}

	public void destroy() {
		synchronized (lock) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				throw new RuntimeException("stop:", e);
			}
			this.listeners.clear();
			initialized = false;
		}
	}
}
