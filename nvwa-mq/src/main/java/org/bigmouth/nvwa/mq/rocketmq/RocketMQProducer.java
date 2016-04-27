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
package org.bigmouth.nvwa.mq.rocketmq;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.mq.Message;
import org.bigmouth.nvwa.mq.Producer;
import org.bigmouth.nvwa.mq.SendException;
import org.bigmouth.nvwa.mq.SendResult;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendStatus;


public class RocketMQProducer extends BaseLifeCycleSupport implements Producer {
    
    private final DefaultMQProducer producer;
    private final String nameSrvAddr;

    private String producerGroup;

    public RocketMQProducer(String nameSrvAddr) {
        if (StringUtils.isBlank(producerGroup))
            this.producerGroup = Producer.DEFAULT_PRODUCER_GROUP;
        this.producer = new DefaultMQProducer(producerGroup);
        this.nameSrvAddr = nameSrvAddr;
        this.producer.setNamesrvAddr(nameSrvAddr);
    }

    @Override
    public SendResult send(Message message) throws SendException {
        try {
            com.alibaba.rocketmq.common.message.Message msg = new com.alibaba.rocketmq.common.message.Message();
            msg.setTopic(message.getTopic());
            msg.setBody(message.getBody());
            msg.setTags(message.getTags());
            int level = message.getDelayTimeLevel();
            if (level > 0)
                msg.setDelayTimeLevel(level);
            com.alibaba.rocketmq.client.producer.SendResult sendResult = producer.send(msg);
            if (null == sendResult)
                throw new SendException();
            if (sendResult.getSendStatus() != SendStatus.SEND_OK)
                throw new SendException(String.format("Send failed! %s", sendResult.getSendStatus()));
            
            return new SendResult(sendResult.getMsgId());
        }
        catch (Exception e) {
            throw new SendException(e);
        }
    }

    @Override
    protected void doInit() {
        try {
            producer.start();
        }
        catch (MQClientException e) {
            throw new RuntimeException("doInit:", e);
        }
    }

    @Override
    protected void doDestroy() {
        producer.shutdown();
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }
}
