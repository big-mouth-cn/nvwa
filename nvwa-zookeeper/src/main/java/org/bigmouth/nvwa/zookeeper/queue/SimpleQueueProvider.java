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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.GsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 简单的消息队列生产者
 * 
 * @author Allen Hu 
 * 2015-6-18
 */
public class SimpleQueueProvider extends BaseLifeCycleSupport implements QueueProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQueueProvider.class);
    private final SimpleQueue simpleQueue;
    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public SimpleQueueProvider(SimpleQueue simpleQueue) {
        this.simpleQueue = simpleQueue;
    }
    
    @Override
    protected void doInit() {
    }

    @Override
    protected void doDestroy() {
        pool.shutdown();
    }

    @Override
    public void asynchPut(final Object object) {
        pool.submit(new Runnable() {
            
            @Override
            public void run() {
                put(object);
            }
        });
    }

    public void put(final Object object) {
        byte[] data = GsonHelper.convert(object);
        if (null == data) {
            return;
        }
        try {
            while (!simpleQueue.offer(data)) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Unable to add new element to the queue!");
                }
                simpleQueue.poll();
            }
        }
        catch (Exception e) {
            LOGGER.error("put: ", e);
        }
    }
}
