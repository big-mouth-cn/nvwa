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
package org.bigmouth.nvwa.zookeeper.queue;


/**
 * 消息队列生产者
 * 
 * @author Allen Hu 
 * 2015-6-23
 */
public interface QueueProvider {

    /**
     * 生产一个消息
     * 
     * @param object
     */
    void put(final Object object);
    
    /**
     * 异步生产一个消息
     * 
     * @param object
     */
    void asynchPut(final Object object);
}
