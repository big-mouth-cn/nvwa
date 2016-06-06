/*
 * Copyright 2016 mopote.com
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
package org.bigmouth.nvwa.transport;

import java.lang.management.ManagementFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.util.NamePreservingRunnable;
import org.bigmouth.nvwa.transport.jmx.DelegatedSenderMBean;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.CyclicCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


/**
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
public class DelegatedSender extends BaseLifeCycleSupport implements Sender {

    private static final Logger LOG = LoggerFactory.getLogger(DelegatedSender.class);
    private static final String DEFAULT_THREAD_NAME = "Delegated-Sender";
    private static final CyclicCounter processorId = new CyclicCounter();

    private final String threadName;
    private volatile Sender nextSender;

    private String senderMBeanName = null;

    private int processorCount = 1;
    private final CyclicCounter idx = new CyclicCounter();
    private int pendingMessageCount = 5000;

    private ExecutorService sendExecutor;
    private BlockingQueue<Object> sendQueue;

    public DelegatedSender() {
        this(DEFAULT_THREAD_NAME);
    }

    public DelegatedSender(String threadName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(threadName), "threadName is blank.");
        this.threadName = threadName;
    }

    public void setNextSender(Sender nextSender) {
        this.nextSender = nextSender;
    }

    @Override
    protected void doInit() {
        Preconditions.checkNotNull(this.nextSender, "nextSender");

        sendExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName + "-" + idx.get());
            }
        });

        sendQueue = new LinkedBlockingQueue<Object>(pendingMessageCount);
        startupProcessor();

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        if (null != senderMBeanName) {
            DelegatedSenderMBean acceptorMBean = new DelegatedSenderMBean(this);
            try {
                ObjectName acceptorName = new ObjectName(this.senderMBeanName);
                mBeanServer.registerMBean(acceptorMBean, acceptorName);
            } catch (MalformedObjectNameException e) {
                LOG.error("Unable DelegatedSender jmx register:", e);
            } catch (NullPointerException e) {
                LOG.error("Unable DelegatedSender jmx register:", e);
            } catch (InstanceAlreadyExistsException e) {
                LOG.error("Unable DelegatedSender jmx register:", e);
            } catch (MBeanRegistrationException e) {
                LOG.error("Unable DelegatedSender jmx register:", e);
            } catch (NotCompliantMBeanException e) {
                LOG.error("Unable MinaSender jmx register:", e);
            }
        }
    }

    @Override
    protected void doDestroy() {
        sendExecutor.shutdownNow();
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        if (null != senderMBeanName) {

            try {
                ObjectName acceptorName = new ObjectName(this.senderMBeanName);
                mBeanServer.unregisterMBean(acceptorName);
            } catch (MalformedObjectNameException e) {
                LOG.error("Unable DelegatedSender jmx unregister:", e);
            } catch (NullPointerException e) {
                LOG.error("Unable DelegatedSender jmx unregister:", e);
            } catch (MBeanRegistrationException e) {
                LOG.error("Unable DelegatedSender jmx unregister:", e);
            } catch (InstanceNotFoundException e) {
                LOG.error("Unable DelegatedSender jmx unregister:", e);
            }
        }
    }

    @Override
    public void send(Object message) {
        enqueueMessage(message);
    }

    public long getPendingSendMessages() {
        if (!isInitialized())
            throw new IllegalStateException(
                    "Can not getPendingSendMessages,DelegatedSender has not been initialized.");
        return sendQueue.size();
    }

    public int getProcessors() {
        return processorCount;
    }

    public void setProcessors(int processorCount) {
        this.processorCount = processorCount;
    }

    private void enqueueMessage(Object bean) {
        if (null == bean) {
            if (LOG.isDebugEnabled())
                LOG.debug("message is null,just ignore.");
            return;
        }

        while (!sendQueue.offer(bean)) {
            if (LOG.isInfoEnabled()) {
                LOG.info("enqueueMessage: offer message to queue failed, try remove early cached message.");
            }
            sendQueue.poll();
        }
    }

    private void startupProcessor() {

        // start the processor if it is not already started.
        for (int i = 0; i < processorCount; i++) {
            Processor processor = new Processor();
            sendExecutor.execute(new NamePreservingRunnable(processor, "Target-Sender-thread-"
                    + processorId.get()));
        }
    }

    private final class Processor implements Runnable {

        @Override
        public void run() {
            for (;;) {
                try {
                    Object message = sendQueue.take();
                    doSend(message);
                } catch (Exception e) {
                    LOG.error("processor:", e);
                    continue;
                }
            }
        }
    }

    private void doSend(Object message) {
        this.nextSender.send(message);
    }

    @Override
    public SenderStatus getStatus() {
        return SenderStatus.AVAILABLE;
    }

    public void setSenderMBeanName(String senderMBeanName) {
        this.senderMBeanName = senderMBeanName;
    }
}
