package org.bigmouth.nvwa.distributed.monitor.zk;

import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.bigmouth.nvwa.distributed.monitor.ChildrenChangeEvent;
import org.bigmouth.nvwa.distributed.monitor.ChildrenChangeListener;
import org.bigmouth.nvwa.distributed.monitor.ChildrenMonitor;
import org.bigmouth.nvwa.distributed.monitor.ChildrenChangeEvent.EventType;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CuratorChildrenMonitor extends BaseLifeCycleSupport implements ChildrenMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorChildrenMonitor.class);
    public static final int MAX_RETRIES = 3;
    public static final int BASE_SLEEP_TIMEMS = 3000;

    private CuratorFramework zkClient;
    private PathChildrenCache pathChildrenCache;

    private final String path;
    private final String connectString;
    private final int sessionTimeout;
    private ChildrenChangeListener defaultListener;

    public CuratorFramework getCuratorClient() {
        return this.zkClient;
    }

    public CuratorChildrenMonitor(String path, String connectString, int sessionTimeout) {
        this.path = path;
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public void addListener(final ChildrenChangeListener listener) {
        if (null != listener) {
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    String path = event.getData().getPath();
                    byte[] data = event.getData().getData();
                    
                    EventType eventType = null;
                    Type type = event.getType();
                    if (type == Type.CHILD_ADDED)
                        eventType = EventType.CHILD_ADDED;
                    else if (type == Type.CHILD_UPDATED)
                        eventType = EventType.CHILD_UPDATED;
                    else if (type == Type.CHILD_REMOVED)
                        eventType = EventType.CHILD_REMOVED;
                    else
                        return;
                    listener.onChanged(ChildrenChangeEvent.of(path, eventType, data));
                }
            });
        }
    }

    private void start() {
        pathChildrenCache = new PathChildrenCache(zkClient, path, true);
        if (null != defaultListener)
            addListener(defaultListener);
        try {
            pathChildrenCache.start();
        }
        catch (Exception e) {
            throw new RuntimeException("Children Listener start failured!");
        }
    }

    @Override
    protected void doInit() {
        zkClient = CuratorFrameworkFactory.builder().sessionTimeoutMs(sessionTimeout).connectString(connectString)
                .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIMEMS, MAX_RETRIES)).build();
        zkClient.start();
        start();
    }

    @Override
    protected void doDestroy() {
        if (null != pathChildrenCache) {
            try {
                pathChildrenCache.close();
            }
            catch (IOException e) {
                LOGGER.error("destroy:", e);
            }
        }
        if (null != zkClient)
            zkClient.close();
    }

    public void setDefaultListener(ChildrenChangeListener defaultListener) {
        this.defaultListener = defaultListener;
    }
}
