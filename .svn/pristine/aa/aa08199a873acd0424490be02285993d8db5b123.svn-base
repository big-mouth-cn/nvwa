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
package org.bigmouth.nvwa.zookeeper.observers;

import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.JsonHelper;
import org.bigmouth.nvwa.utils.StringHelper;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Allen Hu 
 * 2015-8-2
 */
public abstract class AbstractObserver<T> extends BaseLifeCycleSupport implements Observer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractObserver.class);
    
    protected final ZkClientHolder zkClientHolder;
    protected final String subjectType;
    protected final String name;

    protected SubjectConfig config = SubjectConfig.DEFAULT;
    protected PathChildrenCache pathChildrenCache;
    
    public AbstractObserver(ZkClientHolder zkClientHolder, String subjectType, String name) {
        this.zkClientHolder = zkClientHolder;
        this.subjectType = subjectType;
        this.name = name;
    }

    @Override
    protected void doInit() {
        pathChildrenCache = new PathChildrenCache(zkClientHolder.get(), config.getSubjectPath(subjectType), true);
        pathChildrenCache.getListenable().addListener(new ObserverPathChildrenCacheListener());
        try {
            pathChildrenCache.start();
        }
        catch (Exception e) {
            throw new RuntimeException("Children Listener start failured!");
        }
    }

    @Override
    protected void doDestroy() {
        try {
            pathChildrenCache.close();
        }
        catch (IOException e) {
        }
    }
    
    class ObserverPathChildrenCacheListener implements PathChildrenCacheListener {

        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            String path = event.getData().getPath();
            byte[] data = event.getData().getData();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Execute listener of '{}'", path);
            }
            if (null != data) {
                String json = StringHelper.convert(data);
                T message = JsonHelper.convert(json, getClassType());
                Type type = event.getType();
                
                createObserver(path);
                
                if (type == Type.CHILD_ADDED) {
                    add(message);
                }
                else if (type == Type.CHILD_UPDATED) {
                    update(message);
                }
                else if (type == Type.CHILD_REMOVED) {
                    remove(message);
                }
            }
        }
        
        // Create observer node.
        private void createObserver(String path) throws Exception {
            String observer = ZKPaths.makePath(path, getName());
            if (zkClientHolder.get().checkExists().forPath(observer) != null)
                zkClientHolder.get().create().creatingParentsIfNeeded().forPath(observer);
        }
    }
    
    public String getName() {
        return name;
    }

    public void setConfig(SubjectConfig config) {
        this.config = config;
    }
}
