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

import org.bigmouth.nvwa.zookeeper.annotation.PrimaryKey;
import org.bigmouth.nvwa.zookeeper.annotation.SynchronousSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SynchronousService {

    static final Logger logger = LoggerFactory.getLogger(SynchronousService.class);
    
    @SynchronousSupport(zkPath = "/nvwa-zookeeper/locks")
    public void execute(String name, @PrimaryKey String id) {
        logger.info("[{}] Hello " + name, id);
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
