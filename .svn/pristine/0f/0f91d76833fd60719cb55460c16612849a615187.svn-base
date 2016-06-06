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


/**
 * <p>允许捕获的异常</p>
 * 使用场景：一般在同一个事务中不会造成影响的可抛出这个异常。
 * <pre>
 * 
synchronousWithoutResult.execute("/zk/synchronous", new SynchronousProcessorWithoutResult() {

    public void process() throws Exception {
        // 插入一条数据到数据库
        if (dao.insert(1) <= 0) {
            throw new CanNotCaughtException();
        }
        if (true) {
            // throw new CanNotCaughtException(); // 不会捕获这个异常
            throw new CanCaughtException(); // 会捕获这个异常，并执行 exceptionCaught 方法
        }
    }

    public void exceptionCaught(Throwable throwable) {
        LOGGER.error("exceptionCaught:", throwable);
    }
});
 * </pre>
 * @author Allen Hu 
 * 2015-6-3
 */
public class CanCaughtException extends Exception {

    private static final long serialVersionUID = 576893290198245819L;

    /**
     * 
     */
    public CanCaughtException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public CanCaughtException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public CanCaughtException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CanCaughtException(Throwable cause) {
        super(cause);
    }
}
