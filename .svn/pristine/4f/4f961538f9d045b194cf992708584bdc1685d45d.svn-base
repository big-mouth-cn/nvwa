/*
 * Copyright 2015 mopote.com
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.cache.xmc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.bigmouth.nvwa.cache.DataExtractor;
import org.bigmouth.nvwa.cache.KeepFetchService;
import org.bigmouth.nvwa.cache.KeepKeyGenerator;
import org.bigmouth.nvwa.cache.KeyKeeper;
import org.bigmouth.nvwa.cache.OriginalClientAware;
import org.bigmouth.nvwa.utils.Null;
import org.bigmouth.nvwa.utils.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
public class KeepXmcFetchService implements KeepFetchService, OriginalClientAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmcFetchService.class);
    private static final int DEFAULT_NULL_EXP = 12 * 60 * 60;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private MemcachedClient memcachedClient;
    private int nullObjectExpireTime = DEFAULT_NULL_EXP;
    private KeyKeeper keyKeeper;

    @Override
    public <T> T fetch(KeepKeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz) {
        return fetch(keyGenerator, dataExtractor, clazz, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fetch(KeepKeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz,
            int exp) {
        if (null == keyGenerator)
            throw new NullPointerException("keyGenerator");

        String key = keyGenerator.generateKey();
        int keyType = keyGenerator.getKeyType();
        Object fromCache = null;
        try {
            fromCache = memcachedClient.get(key);
        } catch (TimeoutException e) {
            // TODO Implements retry mechanism in the future,default
            // 1000ms.
            LOGGER.error("memcachedClient.get:", e);
            return (T) getFromStore(dataExtractor, key, keyType, exp);
        } catch (InterruptedException e) {
            LOGGER.error("memcachedClient.get:", e);
            return (T) getFromStore(dataExtractor, key, keyType, exp);
        } catch (MemcachedException e) {
            LOGGER.error("memcachedClient.get:", e);
            return (T) getFromStore(dataExtractor, key, keyType, exp);
        }

        if (null != fromCache) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("get data from memcached for key[" + key + "]");
            return (T) returnFromCache(fromCache);
        }

        if (null == dataExtractor) {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("get data from memcached is null,but dataExtractor also is null,is correct?");
            return (T) returnFromCache(fromCache);
        }

        return (T) getFromStore(dataExtractor, key, keyType, exp);
    }

    protected Object returnFromStore(Object fromStoreValue) {
        return fromStoreValue;
    }

    protected Object returnFromCache(Object fromCacheValue) {
        return fromCacheValue;
    }

    private Object getFromStore(DataExtractor dataExtractor, final String key, final int keyType, int exp) {
        Object fromStore = dataExtractor.extract();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get data from dataExtractor for key [" + key + "],!NOT! from memcached.");

        if (null != fromStore) {

            exp = adjustExpTimeIfNull(exp, fromStore);
            // set
            try {
                memcachedClient.set(key, exp, fromStore);
            } catch (TimeoutException e) {
                LOGGER.error("memcachedClient.set", e);
            } catch (InterruptedException e) {
                LOGGER.error("memcachedClient.set", e);
            } catch (MemcachedException e) {
                LOGGER.error("memcachedClient.set", e);
            }
            // keep
            pool.submit(new Runnable() {
                
                @Override
                public void run() {
                    if (null != keyKeeper) {
                        if (keyKeeper.count(key, keyType) <= 0) {
                            keyKeeper.keep(key, keyType);
                        }
                    }
                }
            });
        }
        return returnFromStore(fromStore);
    }

    private int adjustExpTimeIfNull(int exp, Object fromStore) {
        boolean isNull = false;
        if (fromStore instanceof Null) {
            isNull = true;
        } else if (fromStore instanceof Nullable) {
            if (((Nullable) fromStore).isNull()) {
                isNull = true;
            }
        }

        int ret = exp;
        if (isNull) {
            ret = (exp > nullObjectExpireTime) ? nullObjectExpireTime : (exp / 2);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Null object,fromStore object:?,expire time:?", fromStore,
                        nullObjectExpireTime);
        }
        return ret;
    }

    @Override
    public Object getOriginalClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(Object memcachedClient) {
        if (null == memcachedClient)
            throw new NullPointerException();
        if (!(memcachedClient instanceof MemcachedClient))
            throw new IllegalArgumentException(
                    "Illegal passed parameter,expect[MemcachedClient],but["
                            + memcachedClient.getClass() + "]");

        this.memcachedClient = (MemcachedClient) memcachedClient;
    }

    int getNullObjectExpireTime() {
        return nullObjectExpireTime;
    }

    void setNullObjectExpireTime(int nullObjectExpireTime) {
        if (nullObjectExpireTime < 0)
            throw new IllegalArgumentException("nullObjectExpireTime:" + nullObjectExpireTime);
        this.nullObjectExpireTime = nullObjectExpireTime;
    }

    public void setKeyKeeper(KeyKeeper keyKeeper) {
        this.keyKeeper = keyKeeper;
    }
}
