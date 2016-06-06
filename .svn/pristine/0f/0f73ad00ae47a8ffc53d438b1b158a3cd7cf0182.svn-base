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

import org.apache.commons.lang.ArrayUtils;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的消息队列消费者看守者
 * 
 * @author Allen Hu 2015-6-5
 */
public class SimpleQueueConsumerKeeper extends BaseLifeCycleSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQueueConsumerKeeper.class);
    private final SimpleQueue simpleQueue;

    private final ExecutorService pool = Executors.newSingleThreadExecutor();
    private SimpleQueueConsumer simpleQueueConsumer;

    public SimpleQueueConsumerKeeper(SimpleQueue simpleQueue) {
        super();
        this.simpleQueue = simpleQueue;
    }

    @Override
    protected void doInit() {
        pool.submit(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] take = simpleQueue.take();
                        if (ArrayUtils.isNotEmpty(take)) {
                            if (null != simpleQueueConsumer) {
                                simpleQueueConsumer.consumeMessage(take);
                            }
                        }
                    }
                    catch (Exception e) {
                        LOGGER.error("take:", e);
                    }
                }
            }
        });
    }

    @Override
    protected void doDestroy() {
        pool.shutdownNow();
    }

    public void setSimpleQueueConsumer(SimpleQueueConsumer simpleQueueConsumer) {
        this.simpleQueueConsumer = simpleQueueConsumer;
    }
}
