/*
 * Copyright 2015 big-mouth.cn
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
package org.bigmouth.nvwa.zookeeper.concurrent.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.PathUtils;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.bigmouth.nvwa.zookeeper.annotation.PrimaryKey;
import org.bigmouth.nvwa.zookeeper.annotation.SynchronousSupport;
import org.bigmouth.nvwa.zookeeper.concurrent.ConcurrentSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import com.google.common.collect.Maps;


/**
 * 基于Spring AOP实现的Synchronous Advisor。
 * <pre>
 * 使用示例
 * 
 * -------------------
 * applicationContext.xml
 * -------------------
 * &lt;bean class="org.bigmouth.nvwa.zookeeper.concurrent.spring.SynchronousAdvisor" id="synchronousAdvisor"&gt;&lt;/bean&gt;
 * 
 * &lt;bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator"&gt;
 *  &lt;property name="beanNames"&gt;
 *      &lt;list&gt;
 *          &lt;value&gt;*Service&lt;/value&gt;
 *      &lt;/list&gt;
 *  &lt;/property&gt;
 *  &lt;property name="interceptorNames"&gt;
 *      &lt;list&gt;
 *          &lt;value&gt;synchronousAdvisor&lt;/value&gt;
 *      &lt;/list&gt;
 *  &lt;/property&gt;
 * &lt;/bean&gt;
 * 
 * -------------------
 * Java Code
 * -------------------
 * import org.bigmouth.nvwa.zookeeper.annotation.PrimaryKey;
 * import org.bigmouth.nvwa.zookeeper.annotation.SychronousSupport;
 * 
 * 
 * public class SynchronousService {
 * 
 *     ＠SynchronousSupport(zkPath = "/nvwa-zookeeper/locks")
 *     public void execute(String name, ＠PrimaryKey String id) {
 *         System.out.println("Hello " + name + ", Your ID is "+ id);
 *         try {
 *             Thread.sleep(5000);
 *         }
 *         catch (InterruptedException e) {
 *             e.printStackTrace();
 *         }
 *     }
 * }
 * </pre>
 * @see org.bigmouth.nvwa.zookeeper.annotation.SynchronousSupport
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-7-29
 */
public class SynchronousAdvisor extends ConcurrentSupport
     implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronousAdvisor.class);
    private final Map<String, InterProcessLock> LOCKS = Maps.newConcurrentMap();
    private SynchronousStatistics statistics;
    
    public SynchronousAdvisor(ZkClientHolder zkClientHolder, boolean splitIfNeeded, boolean deleteIfNeeded) {
        super(zkClientHolder, splitIfNeeded, deleteIfNeeded);
    }

    public SynchronousAdvisor(ZkClientHolder zkClientHolder) {
        super(zkClientHolder);
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception throwable) {  
        release(method, args);
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        acquire(method, args);
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        release(method, args);
    }

    private void acquire(Method method, Object[] args) {
        String path = getPath(method, args);
        if (StringUtils.isNotBlank(path)) {
            InterProcessLock lock = new InterProcessMutex(zkClientHolder.get(), path); 
            try {
                if (null != statistics)
                    statistics.increment(path);
                lock.acquire();
                LOCKS.put(path, lock);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Acquire lock '{}'!", path);
                    LOGGER.debug("Current locks of pool '{}'!", LOCKS);
                }
            }
            catch (Exception e) {
                LOGGER.error("Cannot acquire lock '{}'!", path);
            }
        }
    }
    
    private void release(Method method, Object[] args) {
        String path = getPath(method, args);
        if (StringUtils.isNotBlank(path)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("It's will release lock '{}' of pool {}!", path, LOCKS);
            }
            InterProcessLock lock = LOCKS.get(path);
            
            // 防止释放后未移除就被塞进新的锁，导致后面锁无法释放
            LOCKS.remove(path);
            try {
                if (null != lock)
                    lock.release();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Release lock '{}' of {}!", path, lock);
                }
            }
            catch (Exception e) {
                LOGGER.error("Lock '" + path + "' release failed!", e);
            }
            finally {
                deleteIfNeeded(path);
                if (null != statistics)
                    statistics.decrement(path);
            }
        }
    }
    
    private String getPath(Method method, Object[] args) {
        String path = null;
        if (method.isAnnotationPresent(SynchronousSupport.class)) {
            SynchronousSupport annotation = method.getAnnotation(SynchronousSupport.class);
            String zkPath = annotation.zkPath();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            String primaryKey = getPrimaryKey(args, parameterAnnotations);
            if (StringUtils.isNotBlank(primaryKey)) {
                path = getPath(zkPath, primaryKey);
                PathUtils.validatePath(path);
            }
        }
        return path;
    }
    
    private String getPrimaryKey(Object[] args, Annotation[][] parameterAnnotations) {
        String primary = null;
        if (ArrayUtils.isNotEmpty(parameterAnnotations)) {
            for (int i = 0, len = parameterAnnotations.length; i < len; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                if (ArrayUtils.isEmpty(annotations)) {
                    continue;
                }
                else {
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof PrimaryKey) {
                            if (args[i] instanceof String || 
                                    args[i] instanceof Integer ||
                                    args[i] instanceof Long) {
                                primary = String.valueOf(args[i]);
                                break;
                            }
                            else {
                                if (LOGGER.isWarnEnabled()) {
                                    LOGGER.warn("args[{}]'s java type must be java.lang.String|java.lang.Integer|java.lang.Long!", i);
                                }
                            }
                        }
                    }
                }
            }
        }
        return primary;
    }
    
    public void setStatistics(SynchronousStatistics statistics) {
        this.statistics = statistics;
    }
}
