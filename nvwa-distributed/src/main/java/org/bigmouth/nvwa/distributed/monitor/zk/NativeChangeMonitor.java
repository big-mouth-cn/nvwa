package org.bigmouth.nvwa.distributed.monitor.zk;

import java.io.IOException;
import java.util.List;
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
import org.bigmouth.nvwa.distributed.monitor.ChangeMonitor;
import org.bigmouth.nvwa.distributed.monitor.DataChangeEvent;
import org.bigmouth.nvwa.distributed.monitor.DataChangeListener;
import org.bigmouth.nvwa.distributed.monitor.NoSuchPathException;
import org.bigmouth.nvwa.distributed.monitor.SubPathChangeListener;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class NativeChangeMonitor extends BaseLifeCycleSupport implements ChangeMonitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(NativeChangeMonitor.class);

	private final String connectString;
	private final int sessionTimeout;

	private ZooKeeper zk;

	private final ConcurrentMap<String, DataChangeListener> dataChangeListeners = new ConcurrentHashMap<String, DataChangeListener>();

	public NativeChangeMonitor(String connectString, int sessionTimeout) {
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
	}

	public ZooKeeper getNativeClient() {
		if (!isInitialized())
			throw new IllegalStateException("NativeChangeMonitor has not been initialized yet.");
		return zk;
	}

	@Override
	public byte[] getDataOf(String path) {
		if (!isInitialized())
			throw new IllegalStateException("NativeChangeMonitor has not been initialized yet.");
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		try {
			Stat stat = zk.exists(path, true);
			if (null == stat) {
				throw new NoSuchPathException("path:" + path);
			}
			return zk.getData(path, true, stat);
		} catch (KeeperException e) {
			throw new RuntimeException("getDataOf:", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("getDataOf:", e);
		}
	}

	@Override
	public List<Pair<String, byte[]>> getSubPathsOf(String path) {
		if (!isInitialized())
			throw new IllegalStateException("NativeChangeMonitor has not been initialized yet.");
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		try {
			Stat stat = zk.exists(path, true);
			if (null == stat) {
				throw new NoSuchPathException("path:" + path);
			}

			List<Pair<String, byte[]>> ret = Lists.newArrayList();
			List<String> paths = zk.getChildren(path, false, stat);
			for (String p : paths) {
				byte[] subContent = this.getDataOf(p);
				ret.add(Pair.of(p, subContent));
			}

			return ret;
		} catch (KeeperException e) {
			throw new RuntimeException("getSubPathsOf:", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("getSubPathsOf:", e);
		}
	}

	private DataChangeListener getDataChangeListener(String path) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		return dataChangeListeners.get(path);
	}

	@Override
	public void addListener(String path, DataChangeListener listener) {
		if (!isInitialized())
			throw new IllegalStateException("NativeChangeMonitor has not been initialized yet.");
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		if (null == listener)
			throw new NullPointerException("listener");

		Stat stat = null;
		try {
			stat = zk.exists(path, true);
		} catch (KeeperException e) {
			throw new RuntimeException("addListener:", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("addListener:", e);
		}
		if (null == stat)
			throw new NoSuchPathException("Path:" + path);

		this.dataChangeListeners.put(path, listener);
	}

	@Override
	public void addListener(String path, SubPathChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doInit() {
		try {
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
								DataChangeListener dcl = getDataChangeListener(path);
								if (null == dcl) {
									return;
								}

								dcl.onChanged(DataChangeEvent.of(path, content));
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
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("NativeChangeMonitor has been initialized.");
		} catch (IOException e) {
			LOGGER.error("doInit:", e);
		}
	}

	@Override
	protected void doDestroy() {
		try {
			zk.close();
		} catch (InterruptedException e) {
			throw new RuntimeException("doDestroy:", e);
		}
		this.dataChangeListeners.clear();
	}
}
