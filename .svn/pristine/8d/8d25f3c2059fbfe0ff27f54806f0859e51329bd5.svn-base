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
package org.bigmouth.nvwa.zookeeper.listener.children;

import org.bigmouth.nvwa.zookeeper.listener.Change;
import org.bigmouth.nvwa.zookeeper.listener.ChangeEventObject;
import org.bigmouth.nvwa.zookeeper.listener.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Allen Hu 
 * 2015-6-1
 */
public class ChildrenChangeListener implements ChangeListener<ChangeEventObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChildrenChangeListener.class);

    private final Change childrenChange;

    public ChildrenChangeListener(Change childrenChange) {
        if (null == childrenChange)
            throw new NullPointerException("childrenChange");
        this.childrenChange = childrenChange;
    }

    @Override
    public void onChanged(ChildrenMonitor monitor, ChangeEventObject event) {
        String path = event.getPath();
        byte[] data = event.getData();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("ZooKeeper: " + event.getEventType() + " Path " + path);
        }
        switch (event.getEventType()) {
            case CHILD_ADDED: {
                childrenChange.add(monitor, path, data);
                break;
            }
            case CHILD_UPDATED: {
                childrenChange.update(monitor, path, data);
                break;
            }
            case CHILD_REMOVED: {
                childrenChange.remove(monitor, path, data);
                break;
            }
        }
    }
}
