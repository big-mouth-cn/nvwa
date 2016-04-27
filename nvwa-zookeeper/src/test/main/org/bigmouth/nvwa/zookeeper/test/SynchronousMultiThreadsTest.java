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
package org.bigmouth.nvwa.zookeeper.test;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bigmouth.nvwa.spring.SpringContextHolder;
import org.bigmouth.nvwa.spring.boot.SpringBootstrap;
import org.bigmouth.nvwa.zookeeper.concurrent.SynchronousProcessorWithoutResult;
import org.bigmouth.nvwa.zookeeper.concurrent.SynchronousWithoutResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Allen Hu 
 * 2015-7-29
 */
public class SynchronousMultiThreadsTest {

    static final Logger logger = LoggerFactory.getLogger(SynchronousMultiThreadsTest.class);
    
    static {
        SpringBootstrap.bootUsingSpring(new String[] { "test/config/applicationContext.xml" }, new String[] {});
    }
    
    public static void main(String[] args) {
        final SynchronousService bean = SpringContextHolder.getBean("synchronousService1");
        final SynchronousWithoutResult mutexLockSynchronousWithoutResult = SpringContextHolder.getBean("mutexLockSynchronousWithoutResult");
        
        String[][] string = new String[][] {
                {"Allen", "01331"},
                {"Lulu", "01332"},
//                {"Small Allen", "01332"},
//                {"Kafka", "01333"},
//                {"ZooKeeper", "01334"},
//                {"MongoDB", "01335"},
//                {"Netty", "01336"},
//                {"Python", "01337"},
        };
        
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            for (String[] str1 : string) {
                final String name = str1[0];
                final String id = str1[1];
//                final String id = UUID.randomUUID().toString();
                pool.submit(new Runnable() {
                    
                    @Override
                    public void run() {
                        bean.execute(name, id);
                        logger.info("[{}] Executed!", id)
                        ;
                    }
                });
            }
        }
    }
}
