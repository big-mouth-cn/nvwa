/*
 * 文件名称: BackupXMemcachedClient.java
 * 版权信息: Copyright 2013-2014 By Allen Hu. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2014-1-13
 * 修改内容: 
 */
package org.bigmouth.nvwa.cache.xmc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientStateListener;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.buffer.BufferAllocator;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.networking.Connector;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.Protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Allen.Hu / 2014-1-13
 */
public class XMemcachedClientProxy implements MemcachedClient {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(XMemcachedClientProxy.class);
    
    private final MemcachedClient masterMemcachedClient;
    
    private final MemcachedClient backupMemcachedClient;
    
    private boolean switchFlag = false;
    
    private ExecutorService executor = null;
    
    public XMemcachedClientProxy(MemcachedClient masterMemcachedClient, MemcachedClient backupMemcachedClient) {
        if (null == masterMemcachedClient)
            throw new NullPointerException("masterMemcachedClient");
        this.masterMemcachedClient = masterMemcachedClient;
        this.backupMemcachedClient = backupMemcachedClient;
    }
    
    public void init() {
        executor = Executors.newCachedThreadPool();
    }
    
    public void destroy() {
        executor.shutdownNow();
    }
    
    public void aysnSet(final String key, final int exp, final Object value) {
        if (isDisabled())
            return;
        
        if (null == executor)
            throw new NullPointerException("No call init!");
        
        executor.execute(new Runnable() {
            
            @Override
            public void run() {
                if (null != backupMemcachedClient && null != value) {
                    try {
                        backupMemcachedClient.set(key, exp, value);
                    }
                    catch (TimeoutException e) {
                        LOGGER.error("aysnSet:", e);
                    }
                    catch (InterruptedException e) {
                        LOGGER.error("aysnSet:", e);
                    }
                    catch (MemcachedException e) {
                        LOGGER.error("aysnSet:", e);
                    }
                }
            }
        });
    }

    private boolean isDisabled() {
        return !switchFlag;
    }

    @Override
    public void setMergeFactor(int mergeFactor) {
        masterMemcachedClient.setMergeFactor(mergeFactor);
    }

    @Override
    public long getConnectTimeout() {
        return masterMemcachedClient.getConnectTimeout();
    }

    @Override
    public void setConnectTimeout(long connectTimeout) {
        masterMemcachedClient.setConnectTimeout(connectTimeout);
    }

    @Override
    public Connector getConnector() {
        return masterMemcachedClient.getConnector();
    }

    @Override
    public void setOptimizeGet(boolean optimizeGet) {
        masterMemcachedClient.setOptimizeGet(optimizeGet);
    }

    @Override
    public void setOptimizeMergeBuffer(boolean optimizeMergeBuffer) {
        masterMemcachedClient.setOptimizeMergeBuffer(optimizeMergeBuffer);
    }

    @Override
    public boolean isShutdown() {
        return masterMemcachedClient.isShutdown();
    }

    @Override
    public void addServer(String server, int port) throws IOException {
        masterMemcachedClient.addServer(server, port);
    }

    @Override
    public void addServer(InetSocketAddress inetSocketAddress) throws IOException {
        masterMemcachedClient.addServer(inetSocketAddress);
    }

    @Override
    public void addServer(String hostList) throws IOException {
        masterMemcachedClient.addServer(hostList);
    }

    @Override
    public List<String> getServersDescription() {
        return masterMemcachedClient.getServersDescription();
    }

    @Override
    public void removeServer(String hostList) {
        masterMemcachedClient.removeServer(hostList);
    }

    @Override
    public void setBufferAllocator(BufferAllocator bufferAllocator) {
        masterMemcachedClient.setBufferAllocator(bufferAllocator);
    }

    @Override
    public <T> T get(String key, long timeout, Transcoder<T> transcoder) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.get(key, timeout, transcoder);
    }

    @Override
    public <T> T get(String key, long timeout) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.get(key, timeout);
    }

    @Override
    public <T> T get(String key, Transcoder<T> transcoder) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.get(key, transcoder);
    }

    @Override
    public <T> T get(String key) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.get(key);
    }

    @Override
    public <T> GetsResponse<T> gets(String key, long timeout, Transcoder<T> transcoder) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.gets(key, timeout, transcoder);
    }

    @Override
    public <T> GetsResponse<T> gets(String key) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.gets(key);
    }

    @Override
    public <T> GetsResponse<T> gets(String key, long timeout) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.gets(key, timeout);
    }

    @Override
    public <T> GetsResponse<T> gets(String key, Transcoder transcoder) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.gets(key, transcoder);
    }

    @Override
    public <T> Map<String, T> get(Collection<String> keyCollections, long timeout, Transcoder<T> transcoder)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.get(keyCollections, timeout, transcoder);
    }

    @Override
    public <T> Map<String, T> get(Collection<String> keyCollections, Transcoder<T> transcoder) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.get(keyCollections, transcoder);
    }

    @Override
    public <T> Map<String, T> get(Collection<String> keyCollections) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.get(keyCollections);
    }

    @Override
    public <T> Map<String, T> get(Collection<String> keyCollections, long timeout) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.get(keyCollections, timeout);
    }

    @Override
    public <T> Map<String, GetsResponse<T>> gets(Collection<String> keyCollections, long timeout,
            Transcoder<T> transcoder) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.gets(keyCollections, timeout, transcoder);
    }

    @Override
    public <T> Map<String, GetsResponse<T>> gets(Collection<String> keyCollections) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.gets(keyCollections);
    }

    @Override
    public <T> Map<String, GetsResponse<T>> gets(Collection<String> keyCollections, long timeout)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.gets(keyCollections, timeout);
    }

    @Override
    public <T> Map<String, GetsResponse<T>> gets(Collection<String> keyCollections, Transcoder<T> transcoder)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.gets(keyCollections, transcoder);
    }

    @Override
    public <T> boolean set(String key, int exp, T value, Transcoder<T> transcoder, long timeout)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.set(key, exp, value, transcoder, timeout);
    }

    @Override
    public boolean set(String key, int exp, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
        boolean flag = masterMemcachedClient.set(key, exp, value);
        aysnSet(key, exp, value);
        return flag;
    }

    @Override
    public boolean set(String key, int exp, Object value, long timeout) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.set(key, exp, value, timeout);
    }

    @Override
    public <T> boolean set(String key, int exp, T value, Transcoder<T> transcoder) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.set(key, exp, value, transcoder);
    }

    @Override
    public void setWithNoReply(String key, int exp, Object value) throws InterruptedException, MemcachedException {
        masterMemcachedClient.setWithNoReply(key, exp, value);
    }

    @Override
    public <T> void setWithNoReply(String key, int exp, T value, Transcoder<T> transcoder) throws InterruptedException,
            MemcachedException {
        masterMemcachedClient.setWithNoReply(key, exp, value, transcoder);
    }

    @Override
    public <T> boolean add(String key, int exp, T value, Transcoder<T> transcoder, long timeout)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.add(key, exp, value, transcoder, timeout);
    }

    @Override
    public boolean add(String key, int exp, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.add(key, exp, value);
    }

    @Override
    public boolean add(String key, int exp, Object value, long timeout) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.add(key, exp, value, timeout);
    }

    @Override
    public <T> boolean add(String key, int exp, T value, Transcoder<T> transcoder) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.add(key, exp, value, transcoder);
    }

    @Override
    public void addWithNoReply(String key, int exp, Object value) throws InterruptedException, MemcachedException {
        masterMemcachedClient.addWithNoReply(key, exp, value);
    }

    @Override
    public <T> void addWithNoReply(String key, int exp, T value, Transcoder<T> transcoder) throws InterruptedException,
            MemcachedException {
        masterMemcachedClient.addWithNoReply(key, exp, value, transcoder);
    }

    @Override
    public <T> boolean replace(String key, int exp, T value, Transcoder<T> transcoder, long timeout)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.replace(key, exp, value, transcoder, timeout);
    }

    @Override
    public boolean replace(String key, int exp, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.replace(key, exp, value);
    }

    @Override
    public boolean replace(String key, int exp, Object value, long timeout) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.replace(key, exp, value, timeout);
    }

    @Override
    public <T> boolean replace(String key, int exp, T value, Transcoder<T> transcoder) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.replace(key, exp, value, transcoder);
    }

    @Override
    public void replaceWithNoReply(String key, int exp, Object value) throws InterruptedException, MemcachedException {
        masterMemcachedClient.replaceWithNoReply(key, exp, value);
    }

    @Override
    public <T> void replaceWithNoReply(String key, int exp, T value, Transcoder<T> transcoder)
            throws InterruptedException, MemcachedException {
        masterMemcachedClient.replaceWithNoReply(key, exp, value, transcoder);
    }

    @Override
    public boolean append(String key, Object value) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.append(key, value);
    }

    @Override
    public boolean append(String key, Object value, long timeout) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.append(key, value, timeout);
    }

    @Override
    public void appendWithNoReply(String key, Object value) throws InterruptedException, MemcachedException {
        masterMemcachedClient.appendWithNoReply(key, value);
    }

    @Override
    public boolean prepend(String key, Object value) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.prepend(key, value);
    }

    @Override
    public boolean prepend(String key, Object value, long timeout) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.prepend(key, value, timeout);
    }

    @Override
    public void prependWithNoReply(String key, Object value) throws InterruptedException, MemcachedException {
        masterMemcachedClient.prependWithNoReply(key, value);
    }

    @Override
    public boolean cas(String key, int exp, Object value, long cas) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.cas(key, exp, value, cas);
    }

    @Override
    public <T> boolean cas(String key, int exp, T value, Transcoder<T> transcoder, long timeout, long cas)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, value, transcoder, timeout, cas);
    }

    @Override
    public boolean cas(String key, int exp, Object value, long timeout, long cas) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, value, timeout, cas);
    }

    @Override
    public <T> boolean cas(String key, int exp, T value, Transcoder<T> transcoder, long cas) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, value, transcoder, cas);
    }

    @Override
    public <T> boolean cas(String key, int exp, CASOperation<T> operation, Transcoder<T> transcoder)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, operation, transcoder);
    }

    @Override
    public <T> boolean cas(String key, int exp, GetsResponse<T> getsReponse, CASOperation<T> operation,
            Transcoder<T> transcoder) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, getsReponse, operation, transcoder);
    }

    @Override
    public <T> boolean cas(String key, int exp, GetsResponse<T> getsReponse, CASOperation<T> operation)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, getsReponse, operation);
    }

    @Override
    public <T> boolean cas(String key, GetsResponse<T> getsResponse, CASOperation<T> operation)
            throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, getsResponse, operation);
    }

    @Override
    public <T> boolean cas(String key, int exp, CASOperation<T> operation) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.cas(key, exp, operation);
    }

    @Override
    public <T> boolean cas(String key, CASOperation<T> operation) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.cas(key, operation);
    }

    @Override
    public <T> void casWithNoReply(String key, GetsResponse<T> getsResponse, CASOperation<T> operation)
            throws TimeoutException, InterruptedException, MemcachedException {
        masterMemcachedClient.casWithNoReply(key, getsResponse, operation);
    }

    @Override
    public <T> void casWithNoReply(String key, int exp, GetsResponse<T> getsReponse, CASOperation<T> operation)
            throws TimeoutException, InterruptedException, MemcachedException {
        masterMemcachedClient.casWithNoReply(key, exp, getsReponse, operation);
    }

    @Override
    public <T> void casWithNoReply(String key, int exp, CASOperation<T> operation) throws TimeoutException,
            InterruptedException, MemcachedException {
        masterMemcachedClient.casWithNoReply(key, exp, operation);
    }

    @Override
    public <T> void casWithNoReply(String key, CASOperation<T> operation) throws TimeoutException,
            InterruptedException, MemcachedException {
        masterMemcachedClient.casWithNoReply(key, operation);
    }

    @Override
    public boolean delete(String key, int time) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.delete(key, time);
    }

    @Override
    public Map<InetSocketAddress, String> getVersions() throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.getVersions();
    }

    @Override
    public long incr(String key, long delta) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.incr(key, delta);
    }

    @Override
    public long incr(String key, long delta, long initValue) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.incr(key, delta, initValue);
    }

    @Override
    public long incr(String key, long delta, long initValue, long timeout) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.incr(key, delta, initValue, timeout);
    }

    @Override
    public long decr(String key, long delta) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.decr(key, delta);
    }

    @Override
    public long decr(String key, long delta, long initValue) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.decr(key, delta, initValue);
    }

    @Override
    public long decr(String key, long delta, long initValue, long timeout) throws TimeoutException,
            InterruptedException, MemcachedException {
        return masterMemcachedClient.decr(key, delta, initValue, timeout);
    }

    @Override
    public void flushAll() throws TimeoutException, InterruptedException, MemcachedException {
        masterMemcachedClient.flushAll();
    }

    @Override
    public void flushAllWithNoReply() throws InterruptedException, MemcachedException {
        masterMemcachedClient.flushAllWithNoReply();
    }

    @Override
    public void flushAll(long timeout) throws TimeoutException, InterruptedException, MemcachedException {
        masterMemcachedClient.flushAll(timeout);
    }

    @Override
    public void flushAll(InetSocketAddress address) throws MemcachedException, InterruptedException, TimeoutException {
        masterMemcachedClient.flushAll(address);
    }

    @Override
    public void flushAllWithNoReply(InetSocketAddress address) throws MemcachedException, InterruptedException {
        masterMemcachedClient.flushAllWithNoReply(address);
    }

    @Override
    public void flushAll(InetSocketAddress address, long timeout) throws MemcachedException, InterruptedException,
            TimeoutException {
        masterMemcachedClient.flushAll(address, timeout);
    }

    @Override
    public void flushAll(String host) throws TimeoutException, InterruptedException, MemcachedException {
        masterMemcachedClient.flushAll(host);
    }

    @Override
    public Map<String, String> stats(InetSocketAddress address) throws MemcachedException, InterruptedException,
            TimeoutException {
        return masterMemcachedClient.stats(address);
    }

    @Override
    public Map<String, String> stats(InetSocketAddress address, long timeout) throws MemcachedException,
            InterruptedException, TimeoutException {
        return masterMemcachedClient.stats(address, timeout);
    }

    @Override
    public Map<InetSocketAddress, Map<String, String>> getStats(long timeout) throws MemcachedException,
            InterruptedException, TimeoutException {
        return masterMemcachedClient.getStats(timeout);
    }

    @Override
    public Map<InetSocketAddress, Map<String, String>> getStats() throws MemcachedException, InterruptedException,
            TimeoutException {
        return masterMemcachedClient.getStats();
    }

    @Override
    public Map<InetSocketAddress, Map<String, String>> getStatsByItem(String itemName) throws MemcachedException,
            InterruptedException, TimeoutException {
        return masterMemcachedClient.getStatsByItem(itemName);
    }

    @Override
    public void shutdown() throws IOException {
        masterMemcachedClient.shutdown();
    }

    @Override
    public boolean delete(String key) throws TimeoutException, InterruptedException, MemcachedException {
        return masterMemcachedClient.delete(key);
    }

    @Override
    public Transcoder getTranscoder() {
        return masterMemcachedClient.getTranscoder();
    }

    @Override
    public void setTranscoder(Transcoder transcoder) {
        masterMemcachedClient.setTranscoder(transcoder);
    }

    @Override
    public Map<InetSocketAddress, Map<String, String>> getStatsByItem(String itemName, long timeout)
            throws MemcachedException, InterruptedException, TimeoutException {
        return masterMemcachedClient.getStatsByItem(itemName, timeout);
    }

    @Override
    public long getOpTimeout() {
        return masterMemcachedClient.getOpTimeout();
    }

    @Override
    public void setOpTimeout(long opTimeout) {
        masterMemcachedClient.setOpTimeout(opTimeout);
    }

    @Override
    public Map<InetSocketAddress, String> getVersions(long timeout) throws TimeoutException, InterruptedException,
            MemcachedException {
        return masterMemcachedClient.getVersions(timeout);
    }

    @Override
    public Collection<InetSocketAddress> getAvaliableServers() {
        return masterMemcachedClient.getAvaliableServers();
    }

    @Override
    public void addServer(String server, int port, int weight) throws IOException {
        masterMemcachedClient.addServer(server, port, weight);
    }

    @Override
    public void addServer(InetSocketAddress inetSocketAddress, int weight) throws IOException {
        masterMemcachedClient.addServer(inetSocketAddress, weight);
    }

    @Override
    public void deleteWithNoReply(String key, int time) throws InterruptedException, MemcachedException {
        masterMemcachedClient.deleteWithNoReply(key, time);
    }

    @Override
    public void deleteWithNoReply(String key) throws InterruptedException, MemcachedException {
        masterMemcachedClient.deleteWithNoReply(key);
    }

    @Override
    public void incrWithNoReply(String key, long delta) throws InterruptedException, MemcachedException {
        masterMemcachedClient.incrWithNoReply(key, delta);
    }

    @Override
    public void decrWithNoReply(String key, long delta) throws InterruptedException, MemcachedException {
        masterMemcachedClient.decrWithNoReply(key, delta);
    }

    @Override
    public void setLoggingLevelVerbosity(InetSocketAddress address, int level) throws TimeoutException,
            InterruptedException, MemcachedException {
        masterMemcachedClient.setLoggingLevelVerbosity(address, level);
    }

    @Override
    public void setLoggingLevelVerbosityWithNoReply(InetSocketAddress address, int level) throws InterruptedException,
            MemcachedException {
        masterMemcachedClient.setLoggingLevelVerbosityWithNoReply(address, level);
    }

    @Override
    public void addStateListener(MemcachedClientStateListener listener) {
        masterMemcachedClient.addStateListener(listener);
    }

    @Override
    public void removeStateListener(MemcachedClientStateListener listener) {
        masterMemcachedClient.removeStateListener(listener);
    }

    @Override
    public Collection<MemcachedClientStateListener> getStateListeners() {
        return masterMemcachedClient.getStateListeners();
    }

    @Override
    public void flushAllWithNoReply(int exptime) throws InterruptedException, MemcachedException {
        masterMemcachedClient.flushAllWithNoReply(exptime);
    }

    @Override
    public void flushAll(int exptime, long timeout) throws TimeoutException, InterruptedException, MemcachedException {
        masterMemcachedClient.flushAll(exptime, timeout);
    }

    @Override
    public void flushAllWithNoReply(InetSocketAddress address, int exptime) throws MemcachedException,
            InterruptedException {
        masterMemcachedClient.flushAllWithNoReply(address, exptime);
    }

    @Override
    public void flushAll(InetSocketAddress address, long timeout, int exptime) throws MemcachedException,
            InterruptedException, TimeoutException {
        masterMemcachedClient.flushAll(address, timeout, exptime);
    }

    @Override
    public void setHealSessionInterval(long healConnectionInterval) {
        masterMemcachedClient.setHealSessionInterval(healConnectionInterval);
    }

    @Override
    public long getHealSessionInterval() {
        return masterMemcachedClient.getHealSessionInterval();
    }

    @Override
    public Protocol getProtocol() {
        return masterMemcachedClient.getProtocol();
    }

    @Override
    public void setPrimitiveAsString(boolean primitiveAsString) {
        masterMemcachedClient.setPrimitiveAsString(primitiveAsString);
    }

    @Override
    public void setConnectionPoolSize(int poolSize) {
        masterMemcachedClient.setConnectionPoolSize(poolSize);
    }

    @Override
    public void setEnableHeartBeat(boolean enableHeartBeat) {
        masterMemcachedClient.setEnableHeartBeat(enableHeartBeat);
    }

    @Override
    public void setSanitizeKeys(boolean sanitizeKey) {
        masterMemcachedClient.setSanitizeKeys(sanitizeKey);
    }

    @Override
    public boolean isSanitizeKeys() {
        return masterMemcachedClient.isSanitizeKeys();
    }

    @Override
    public Counter getCounter(String key) {
        return masterMemcachedClient.getCounter(key);
    }

    @Override
    public Counter getCounter(String key, long initialValue) {
        return masterMemcachedClient.getCounter(key, initialValue);
    }

    @Override
    public KeyIterator getKeyIterator(InetSocketAddress address) throws MemcachedException, InterruptedException,
            TimeoutException {
        return masterMemcachedClient.getKeyIterator(address);
    }

    @Override
    public void setAuthInfoMap(Map<InetSocketAddress, AuthInfo> map) {
        masterMemcachedClient.setAuthInfoMap(map);
    }

    @Override
    public Map<InetSocketAddress, AuthInfo> getAuthInfoMap() {
        return masterMemcachedClient.getAuthInfoMap();
    }

    @Override
    public String getName() {
        return masterMemcachedClient.getName();
    }

    @Override
    public void setName(String name) {
        masterMemcachedClient.setName(name);
    }

    public void setSwitchFlag(boolean switchFlag) {
        this.switchFlag = switchFlag;
    }

}
