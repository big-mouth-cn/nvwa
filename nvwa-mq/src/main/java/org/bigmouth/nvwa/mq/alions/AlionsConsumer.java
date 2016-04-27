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

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.mq.ConsumeStatus;
import org.bigmouth.nvwa.mq.Consumer;
import org.bigmouth.nvwa.mq.Message;
import org.bigmouth.nvwa.mq.MessageListener;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * 基于阿里云ONS的消息实现<br>
 * 请参考：<a href="https://www.aliyun.com/product/ons">https://www.aliyun.com/product/ons</a>
 * @author Allen Hu - (big-mouth.cn) 
 * 2016-3-4
 */
public class AlionsConsumer extends BaseLifeCycleSupport implements Consumer {

    private final Properties properties = new Properties();
    private com.aliyun.openservices.ons.api.Consumer consumer;
    private final MessageListener messageListener;
    private String subExpression;
    
    public AlionsConsumer(String consumerId, String accessKey, String secretKey, 
            MessageListener messageListener) {
        super();
        Preconditions.checkNotNull(messageListener);
        properties.put(PropertyKeyConst.ConsumerId, consumerId);
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        consumer = ONSFactory.createConsumer(properties);
        if (StringUtils.isBlank(subExpression))
            this.subExpression = "*";
        this.messageListener = messageListener;
    }
    
    @Override
    protected void doInit() {
        if (null != consumer) {
            String topic = messageListener.getTopic();
            if (StringUtils.isBlank(topic))
                throw new RuntimeException("topic must has not blank!");
            consumer.subscribe(topic, subExpression, new com.aliyun.openservices.ons.api.MessageListener() {
                
                @Override
                public Action consume(com.aliyun.openservices.ons.api.Message message, ConsumeContext context) {
                    Message msg = new Message();
                    msg.setTopic(message.getTopic());
                    msg.setBody(message.getBody());
                    msg.setTags(message.getTag());
                    msg.setMsgId(message.getMsgID());
                    ConsumeStatus status = messageListener.consume(Lists.newArrayList(msg), context);
                    switch (status) {
                        case CONSUME_SUCCESS:
                            return Action.CommitMessage;
                        default:
                            return Action.ReconsumeLater;
                    }
                }
            });
            if (consumer.isClosed())
                consumer.start();
        }
    }

    @Override
    protected void doDestroy() {
        shutdown();
    }

    @Override
    public void suspend() {
        if (consumer.isStarted())
            consumer.shutdown();
    }

    @Override
    public void shutdown() {
        if (consumer.isStarted())
            consumer.shutdown();
    }

    public void setSubExpression(String subExpression) {
        this.subExpression = subExpression;
    }
}
