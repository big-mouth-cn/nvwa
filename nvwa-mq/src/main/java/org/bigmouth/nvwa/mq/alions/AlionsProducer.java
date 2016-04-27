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
package org.bigmouth.nvwa.mq.alions;

import java.util.Properties;

import org.bigmouth.nvwa.mq.DelayTimeLevel;
import org.bigmouth.nvwa.mq.Message;
import org.bigmouth.nvwa.mq.Producer;
import org.bigmouth.nvwa.mq.SendException;
import org.bigmouth.nvwa.mq.SendResult;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;


/**
 * 基于阿里云ONS的消息实现<br>
 * 请参考：<a href="https://www.aliyun.com/product/ons">https://www.aliyun.com/product/ons</a>
 * @author Allen Hu - (big-mouth.cn) 
 * 2016-3-4
 */
public class AlionsProducer extends BaseLifeCycleSupport implements Producer {

    private final Properties properties = new Properties();
    private com.aliyun.openservices.ons.api.Producer producer;
    
    public AlionsProducer(String producerId, String accessKey, String secretKey) {
        super();
        properties.put(PropertyKeyConst.ProducerId, producerId);
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        producer = ONSFactory.createProducer(properties);
    }

    @Override
    public SendResult send(Message message) {
        com.aliyun.openservices.ons.api.Message msg = new com.aliyun.openservices.ons.api.Message();
        msg.setTopic(message.getTopic());
        msg.setBody(message.getBody());
        msg.setTag(message.getTags());
        int level = message.getDelayTimeLevel();
        if (level > 0)
            msg.setStartDeliverTime(DelayTimeLevel.ofSystemTimeInMillis(level));
        com.aliyun.openservices.ons.api.SendResult sendResult = producer.send(msg);
        if (null == sendResult) 
            throw new SendException();
        return new SendResult(sendResult.getMessageId());
    }

    @Override
    protected void doInit() {
        if (null != producer && producer.isClosed())
            producer.start();
    }

    @Override
    protected void doDestroy() {
        if (null != producer && producer.isStarted()) {
            producer.shutdown();
        }
    }
}
