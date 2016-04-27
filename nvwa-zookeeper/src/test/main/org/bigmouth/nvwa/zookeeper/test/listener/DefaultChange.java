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
package org.bigmouth.nvwa.zookeeper.test.listener;

import org.apache.zookeeper.CreateMode;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.bigmouth.nvwa.zookeeper.listener.Change;
import org.bigmouth.nvwa.zookeeper.listener.children.ChildrenMonitor;


/**
 * 
 * @author Allen Hu 
 * 2015-8-2
 */
public class DefaultChange implements Change {

    private ZkClientHolder zkClientHolder;
    
    @Override
    public void add(ChildrenMonitor monitor, String path, byte[] data) {
        
        try {
            zkClientHolder.get().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/observers/subject/USER-ORDER/P-");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Added " + path);
    }

    @Override
    public void update(ChildrenMonitor monitor, String path, byte[] data) {
        System.out.println("Updated " + path);
    }

    @Override
    public void remove(ChildrenMonitor monitor, String path, byte[] data) {
        System.out.println("Removed " + path);
    }

    public void setZkClientHolder(ZkClientHolder zkClientHolder) {
        this.zkClientHolder = zkClientHolder;
    }
}
