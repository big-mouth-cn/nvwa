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
package org.bigmouth.nvwa.zookeeper.concurrent;


/**
 * 有返回值的同步任务处理，支持分布式。
 * <p>
 * 如果想希望通过Spring-AOP来实现，那么请参考{@linkplain org.bigmouth.nvwa.zookeeper.concurrent.spring.SynchronousAdvisor SychronousAdvisor}实现。
 * </p>
 * 
 * @author Allen Hu 
 * 2015-4-17
 * @see org.bigmouth.nvwa.zookeeper.concurrent.SynchronousWithoutResult
 */
public interface Synchronous {

    /**
     * 同步执行，根据<code>path</code>标识来区分同步工作。不同的<code>path</code>将不会同步进行。
     * 
     * @param <T> 处理结果类型
     * @param path 任务节点
     * <pre>
     * e.g. "/project/synchronous/0000001"
     * </pre>
     * @param processor 业务处理器
     * @return 处理结果
     * @throws org.bigmouth.nvwa.zookeeper.concurrent.CanNotCaughtException
     * @throws org.bigmouth.nvwa.zookeeper.concurrent.SynchronousException
     */
    @Deprecated
    <T> T execute(String path, SynchronousProcessor<T> processor);
    
    /**
     * 同步执行，根据<code>primaryKey</code>标识来区分同步工作。不同的<code>primaryKey</code>将不会同步进行。
     * 
     * @param <T> 处理结果类型
     * @param zkPath 任务根节点
     * <pre>
     * e.g. "/project/synchronous"
     * </pre>
     * @param primaryKey 任务编号
     * <pre>
     * e.g. "0000001"
     * </pre>
     * @param processor 业务处理器
     * @return 处理结果
     * @throws org.bigmouth.nvwa.zookeeper.concurrent.CanNotCaughtException
     * @throws org.bigmouth.nvwa.zookeeper.concurrent.SynchronousException
     */
    <T> T execute(String zkPath, String primaryKey, SynchronousProcessor<T> processor);
}
