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

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;

public class SynchronousStatistics {

    private Map<String, AtomicLong> wait = Maps.newConcurrentMap();

    public void increment(String path) {
        AtomicLong atomicLong = newIfNotExists(path);
        atomicLong.incrementAndGet();
    }

    public void decrement(String path) {
        AtomicLong atomicLong = newIfNotExists(path);
        atomicLong.decrementAndGet();
    }
    
    private AtomicLong newIfNotExists(String path) {
        AtomicLong atomicLong = wait.get(path);
        if (null == atomicLong) {
            wait.put(path, new AtomicLong(0));
        }
        return atomicLong;
    }

    public Map<String, AtomicLong> getWait() {
        return wait;
    }

    public void setWait(Map<String, AtomicLong> wait) {
        this.wait = wait;
    }
}
