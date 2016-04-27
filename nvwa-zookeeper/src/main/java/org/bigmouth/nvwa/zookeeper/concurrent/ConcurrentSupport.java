/*
 * Copyright 2016 big-mouth.cn
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
package org.bigmouth.nvwa.zookeeper.concurrent;

import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.bigmouth.nvwa.zookeeper.utils.ZkPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConcurrentSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentSupport.class);
    protected final ZkClientHolder zkClientHolder;
    /** 任务节点是否需要进行分组 */
    private boolean splitIfNeeded = false;
    /** 释放锁之后是否需要删除Lock节点 */
    private boolean deleteIfNeeded = true;
    
    public ConcurrentSupport(ZkClientHolder zkClientHolder) {
        this.zkClientHolder = zkClientHolder;
    }

    public ConcurrentSupport(ZkClientHolder zkClientHolder, boolean splitIfNeeded, boolean deleteIfNeeded) {
        super();
        this.zkClientHolder = zkClientHolder;
        this.splitIfNeeded = splitIfNeeded;
        this.deleteIfNeeded = deleteIfNeeded;
    }

    protected void deleteIfNeeded(String path) {
        if (isDeleteIfNeeded()) {
            try {
                zkClientHolder.get().delete().guaranteed().inBackground().forPath(path);
            }
            catch (KeeperException.NotEmptyException e) {
                // ignore
            }
            catch (KeeperException.NoNodeException e) {
                // ignore - already deleted (possibly expired session, etc.)
            }
            catch (Exception e) {
                LOGGER.error("", e);
            }
        }
    }
    
    protected String getPath(String zkPath, String primaryKey) {
        String child = splitIfNeeded ? ZkPathUtils.group(primaryKey) : primaryKey;
        return ZKPaths.makePath(zkPath, child);
    }
    
    public boolean isSplitIfNeeded() {
        return splitIfNeeded;
    }
    
    public void setSplitIfNeeded(boolean splitIfNeeded) {
        this.splitIfNeeded = splitIfNeeded;
    }
    
    public boolean isDeleteIfNeeded() {
        return deleteIfNeeded;
    }
    
    public void setDeleteIfNeeded(boolean deleteIfNeeded) {
        this.deleteIfNeeded = deleteIfNeeded;
    }
}
