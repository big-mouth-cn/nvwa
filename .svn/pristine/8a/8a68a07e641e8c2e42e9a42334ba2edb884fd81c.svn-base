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
package org.bigmouth.nvwa.zookeeper.concurrent;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.common.PathUtils;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;

/**
 * 基于普通互斥锁的方式实现同步
 * 
 * @author Allen Hu 2015-6-1
 */
public class MutexLockSynchronousWithoutResult extends ConcurrentSupport implements SynchronousWithoutResult {

    public MutexLockSynchronousWithoutResult(ZkClientHolder zkClientHolder, boolean splitIfNeeded,
            boolean deleteIfNeeded) {
        super(zkClientHolder, splitIfNeeded, deleteIfNeeded);
    }

    public MutexLockSynchronousWithoutResult(ZkClientHolder zkClientHolder) {
        super(zkClientHolder);
    }

    @Override
    public void execute(String path, SynchronousProcessorWithoutResult processor) {
        PathUtils.validatePath(path);
        InterProcessLock lock = new InterProcessMutex(zkClientHolder.get(), path);
        try {
            lock.acquire();
            if (null != processor)
                processor.process();
        }
        catch (CanCaughtException e) {
            if (null != processor)
                processor.exceptionCaught(e);
        }
        catch (CanNotCaughtException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SynchronousException(e);
        }
        finally {
            try {
                lock.release();
            }
            catch (Exception e) {
            }
            deleteIfNeeded(path);
        }
    }

    @Override
    public void execute(String zkPath, String primaryKey, SynchronousProcessorWithoutResult processor) {
        String path = getPath(zkPath, primaryKey);
        execute(path, processor);
    }
}
