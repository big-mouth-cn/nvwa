package org.bigmouth.nvwa.distributed.notify.zk;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.bigmouth.nvwa.distributed.notify.Notifier;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;

public class CuratorNotifier extends BaseLifeCycleSupport implements Notifier {

	public static final int MAX_RETRIES = 3;
	public static final int BASE_SLEEP_TIMEMS = 3000;

	private CuratorFramework zkClient;

	private final String connectString;
	private final int sessionTimeout;

	public CuratorFramework getCuratorClient() {
		return this.zkClient;
	}

	public CuratorNotifier(String connectString, int sessionTimeout) {
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
	}

	@Override
	public void notifyPathUpdate(String path, byte[] message) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		if (null == message)
			throw new NullPointerException("message");

		try {
			zkClient.setData().forPath(path, message);
		} catch (Exception e) {
			throw new RuntimeException("notifyPathUpdate:", e);
		}
	}

	@Override
	public void notifyPathAdded(String path, byte[] message) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");
		if (null == message)
			throw new NullPointerException("message");

		try {
		    Stat stat = zkClient.checkExists().forPath(path);
		    if (null != stat) {
		        zkClient.delete().forPath(path);
		    }
			zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, message);
		} catch (Exception e) {
			throw new RuntimeException("notifySubPathAdded:", e);
		}
	}

	@Override
	public void notifyPathRemoved(String path) {
		if (StringUtils.isBlank(path))
			throw new IllegalArgumentException("path is blank.");

		try {
			zkClient.delete().forPath(path);
		} catch (Exception e) {
			throw new RuntimeException("notifyPathRemoved:", e);
		}
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
			zkClient.close();
		}
	}
}
