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

import org.bigmouth.nvwa.spring.SpringContextHolder;
import org.bigmouth.nvwa.spring.boot.SpringBootstrap;

import java.util.concurrent.CountDownLatch;


/**
 * 
 * @author Allen Hu 
 * 2015-7-29
 */
public class SynchronousTest {

    static {
        SpringBootstrap.bootUsingSpring(new String[] { "test/config/applicationContext.xml" }, new String[] {});
    }

    static class MyThread implements Runnable {
        final CountDownLatch latch;
        final String id;
        final SynchronousService service;

        public MyThread(CountDownLatch latch, String id, SynchronousService service) {
            this.latch = latch;
            this.id = id;
            this.service = service;
        }

        @Override
        public void run() {
            try {
                service.execute("Allen", id);
            } finally {
                latch.countDown();
            }
        }
    }
    
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);

        SynchronousService service = (SynchronousService) SpringContextHolder.getBean("synchronousService1");
        Thread t1 = new Thread(new MyThread(latch, "1000", service));
        t1.setName("t1");
        t1.start();
        Thread t2 = new Thread(new MyThread(latch, "1000", service));
        t2.setName("t2");
        t2.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finished!");
    }
}
