package org.bigmouth.nvwa.cluster.node;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZooKeeperCuratorWrapper implements Wrapper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperCuratorWrapper.class);
    
    public static final int MAX_RETRIES = 3;
    public static final int BASE_SLEEP_TIMEMS = 3000;
    
    private CuratorFramework zkClient;
    
    private final ConcurrentMap<String, DataChangeListener> listeners = new ConcurrentHashMap<String, DataChangeListener>();
    
    private final String connectString;
    private final int sessionTimeout;

    public ZooKeeperCuratorWrapper(String connectString, int sessionTimeout) {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public boolean setData(String path, byte[] value) {
        if (StringUtils.isBlank(path))
            throw new IllegalArgumentException("path is blank");
        try {
            Stat stat = zkClient.checkExists().forPath(path);
            byte[] data = value;
            if (null == data || data.length == 0)
                data = new byte[0];
            if (null == stat) {
                zkClient.create().creatingParentsIfNeeded().forPath(path, data);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Created ZooKeeper path: {}", path);
                }
            }
            else {
                zkClient.setData().forPath(path, data);
            }
            return true;
        }
        catch (Exception e) {
            LOGGER.error("setData: ", e);
            return false;
        }
    }

    @Override
    public byte[] getData(String path) {
        return getData(path, new byte[0]);
    }

    @Override
    public byte[] getData(String path, byte[] defaultValue) {
        if (StringUtils.isBlank(path))
            throw new IllegalArgumentException("path is blank.");
        
        try {
            Stat stat = zkClient.checkExists().forPath(path);
            if (null == stat) {
                zkClient.create().creatingParentsIfNeeded().forPath(path, defaultValue);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Create zookeeper path: " + path);
                }
            }
            return zkClient.getData().forPath(path);
        }
        catch (Exception e) {
            LOGGER.error("getData:", e);
        }
        return null;
    }

    @Override
    public byte[] registerDataChangeListener(final String path, final DataChangeListener listener) {
        if (StringUtils.isBlank(path))
            throw new IllegalArgumentException("path is blank.");
        if (null == listener) 
            throw new NullPointerException("listener");
        
        Stat stat = null;
        try {
            stat = zkClient.checkExists().forPath(path);
        }
        catch (Exception e) {
            throw new RuntimeException("registerDataChangeListener:", e);
        }
        if (null == stat) 
            throw new NoSuchPathException("Path:" + path);
        
        if (!isRegister(path)) {
            try {
                cacheNode(path);
            }
            catch (Exception e) {
                LOGGER.error("cacheNode: ", e);
            }
        }
        listeners.put(path, listener);
        return getData(path);
    }
    
    private boolean isRegister(String path) {
        return listeners.containsKey(path);
    }
    
    private DataChangeListener getListener(String path) {
        if (StringUtils.isBlank(path))
            throw new IllegalArgumentException("path is blank.");
        return listeners.get(path);
    }
    
    private void cacheNode(final String path) throws Exception {
        final NodeCache cache = new NodeCache(zkClient, path);
        cache.getListenable().addListener(new NodeCacheListener() {
            
            @Override
            public void nodeChanged() throws Exception {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Path:{} data changed.", path);
                
                DataChangeListener listener = getListener(path);
                if (null != listener) {
                    byte[] content = cache.getCurrentData().getData();
                    listener.onDataChanged(path, content);
                }
            }
        });
        cache.start();
    }
    
    @Override
    public void init() {
        zkClient = CuratorFrameworkFactory.builder()
            .sessionTimeoutMs(sessionTimeout)
            .connectString(connectString)
            .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIMEMS, MAX_RETRIES))
            .build();
        zkClient.start();
    }

    @Override
    public void destroy() {
        if (null != zkClient)
            zkClient.close();
    }
    
}
