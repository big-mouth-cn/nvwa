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
package org.bigmouth.nvwa.mq;




/**
 * 消息生产者
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2016-1-8
 */
public interface Producer {

    String DEFAULT_PRODUCER_GROUP = "producer";
    
    /**
     * 发送消息
     * @param message 需要发送的消息
     * @return 如果发送失败，会直接抛异常
     */
    SendResult send(Message message) throws SendException;
}
