package org.bigmouth.nvwa.distributed.monitor.zk;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.bigmouth.nvwa.distributed.monitor.ChangeMonitor;
import org.bigmouth.nvwa.distributed.monitor.DataChangeEvent;
import org.bigmouth.nvwa.distributed.monitor.DataChangeListener;
import org.bigmouth.nvwa.distributed.monitor.SubPathChangeEvent;
import org.bigmouth.nvwa.distributed.monitor.SubPathChangeListener;
import org.bigmouth.nvwa.distributed.monitor.SubPathChangeEvent.SubEventType;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

public class CuratorChangeMonitor extends BaseLifeCycleSupport implements ChangeMonitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuratorChangeMonitor.class);

	public static final int MAX_RETRIES = 3;
	public static final int BASE_SLEEP_TIMEMS = 3000;

	private CuratorFramework zkClient;

	private final String connectString;
	private final int sessionTimeout;

	private final ConcurrentLinkedQueue<Closeable> caches = Queues.newConcurrentLinkedQueue();

	public CuratorChangeMonitor(String connectString, int sessionTimeout) {
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
	}

	public CuratorFramework getCuratorClient() {
		return this.zkClient;
	}

	@Override
	public byte[] getDataOf(String path) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");

		try {
			return zkClient.getData().forPath(path);
		} catch (Exception e) {
			throw new RuntimeException("getDataOf:", e);
		}
	}

	@Override
	public void addListener(final String path, final DataChangeListener listener) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		if (null == listener)
			throw new NullPointerException("listener");

		NodeCache cache = new NodeCache(zkClient, path);
		cache.getListenable().addListener(new NodeCacheListener() {

			@Override
			public void nodeChanged() throws Exception {
				try {
					listener.onChanged(DataChangeEvent.of(path, zkClient.getData().forPath(path)));
				} catch (Exception e) {
					LOGGER.error("listener.onChanged:", e);
				}
			}
		});
		try {
			cache.start();
		} catch (Exception e) {
			throw new RuntimeException("addListener:", e);
		}
		caches.add(cache);
	}

	@Override
	public List<Pair<String, byte[]>> getSubPathsOf(String path) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		try {
			List<Pair<String, byte[]>> ret = Lists.newArrayList();
			List<String> subPaths = zkClient.getChildren().forPath(path);
			for (String sp : subPaths) {
				byte[] spContent = zkClient.getData().forPath(
						new StringBuilder(32).append(path).append("/").append(sp).toString());
				ret.add(Pair.of(sp, spContent));
			}
			return ret;
		} catch (Exception e) {
			throw new RuntimeException("getSubPathsOf:", e);
		}
	}

	@Override
	public void addListener(final String path, final SubPathChangeListener listener) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		if (null == listener)
			throw new NullPointerException("listener");

		PathChildrenCache cache = new PathChildrenCache(zkClient, path, true);
		cache.getListenable().addListener(new PathChildrenCacheListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
					throws Exception {
				SubEventType et = null;
				Type type = event.getType();
				if (Type.CHILD_ADDED == type) {
					et = SubEventType.CHILD_ADDED;
				} else if (Type.CHILD_REMOVED == type) {
					et = SubEventType.CHILD_REMOVED;
				} else if (Type.CHILD_UPDATED == type) {
					et = SubEventType.CHILD_UPDATED;
				} else {
					return;
				}

				String totlePath = event.getData().getPath();
				String[] factors = totlePath.split("/");
				String subPath = factors[factors.length - 1];

				SubPathChangeEvent tEvent = SubPathChangeEvent.of(path, et,
						Lists.newArrayList(Pair.of(subPath, event.getData().getData())));

				listener.onChanged(tEvent);
			}
		});
		try {
			cache.start();
		} catch (Exception e) {
			throw new RuntimeException("addListener:", e);
		}

		caches.add(cache);
	}

	@Override
	protected void doInit() {
		zkClient = CuratorFrameworkFactory.builder().sessionTimeoutMs(sessionTimeout)
				.connectString(connectString)
				.retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIMEMS, MAX_RETRIES)).build();
		zkClient.start();
	}

	@Override
	protected void doDestroy() {
		if (null != zkClient) {
			for (Closeable c : caches) {
				try {
					c.close();
				} catch (IOException e) {
					LOGGER.error("doDestroy:", e);
				}
			}
			caches.clear();
			zkClient.close();
		}
	}
}
