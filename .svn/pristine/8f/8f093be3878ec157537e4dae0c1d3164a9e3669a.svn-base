package org.bigmouth.nvwa.transport;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.integration.jmx.IoServiceMBean;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.mina.util.NamePreservingRunnable;
import org.bigmouth.nvwa.transport.jmx.MinaIoStatistics;
import org.bigmouth.nvwa.transport.jmx.MinaIoStatisticsMBean;
import org.bigmouth.nvwa.transport.jmx.MinaSenderQueueMBean;
import org.bigmouth.nvwa.transport.jmx.MinaStatisticsUpdater;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.Closure;
import org.bigmouth.nvwa.utils.CyclicCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class MinaSender extends BaseLifeCycleSupport implements CallbackSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinaSender.class);

    private static final int DEFAULT_CACHED_MESSAGE_COUNT = 1000;
    private static final TcpConfig DEFAULT_TCP_CONFIG = new TcpConfig();
    private static final long STATUS_THRESHOLD_BUSY = 5000;

    private int cachedMessageCount = DEFAULT_CACHED_MESSAGE_COUNT;

    private final TargetSessionLocatorBuilder targetSessionLocatorBuilder;
    private final InetSocketAddress targetAddress;
    private final Map<String, IoFilter> filters = Maps.newHashMap();
    private final TcpConfig tcpConfig;

    private volatile TargetSessionLocator targetSessionLocator;
    private NioSocketConnector connector = null;
    private BlockingQueue<Object> sendQueue;

    private String senderMBeanName = null;
    private String senderQueueMBeanName = null;
    private String senderIoStatsMBeanName = null;

    private MinaStatisticsUpdater statisticsUpdater = null;

    private Sender sendFailMessageCollector;

    private static final CyclicCounter processorId = new CyclicCounter();
    private final ExecutorService sendExecutor;
    private final boolean createdExecutor;

    private volatile Closure callback;

    private int processorCount = 1;

    public MinaSender(Map<String, IoFilter> filters, InetSocketAddress targetAddress,
            TargetSessionLocatorBuilder targetSessionLocatorBuilder) {
        this(DEFAULT_TCP_CONFIG, filters, targetAddress, targetSessionLocatorBuilder, null);
    }

    public MinaSender(TcpConfig tcpConfig, Map<String, IoFilter> filters, int port,
            TargetSessionLocatorBuilder targetSessionLocatorBuilder) {
        this(tcpConfig, filters, new InetSocketAddress(port), targetSessionLocatorBuilder, null);
    }

    public MinaSender(TcpConfig tcpConfig, Map<String, IoFilter> filters, String ip, int port,
            TargetSessionLocatorBuilder targetSessionLocatorBuilder) {
        this(tcpConfig, filters, new InetSocketAddress(ip, port), targetSessionLocatorBuilder, null);
    }

    public MinaSender(TcpConfig tcpConfig, Map<String, IoFilter> filters,
            InetSocketAddress targetAddress, TargetSessionLocatorBuilder targetSessionLocatorBuilder) {
        this(tcpConfig, filters, targetAddress, targetSessionLocatorBuilder, null);
    }

    public MinaSender(TcpConfig tcpConfig, Map<String, IoFilter> filters,
            InetSocketAddress targetAddress,
            TargetSessionLocatorBuilder targetSessionLocatorBuilder, ExecutorService sendExecutor) {
        if (null == targetSessionLocatorBuilder)
            throw new NullPointerException("targetSessionLocatorBuilder");
        if (null == targetAddress)
            throw new NullPointerException("targetAddress");
        if (null == tcpConfig)
            throw new NullPointerException("tcpConfig");
        this.targetSessionLocatorBuilder = targetSessionLocatorBuilder;
        this.targetAddress = targetAddress;
        if (null != filters)
            this.filters.putAll(filters);
        this.tcpConfig = tcpConfig;
        if (sendExecutor == null) {
            this.sendExecutor = Executors.newCachedThreadPool();
            createdExecutor = true;
        } else {
            this.sendExecutor = sendExecutor;
            createdExecutor = false;
        }

        // threadName = "Target-Sender" + '-' + processorId.incrementAndGet();
    }

    public long getPendingSendMessages() {
        if (!isInitialized())
            throw new IllegalStateException(
                    "Can not getPendingSendMessages,MinaSender has not been initialized.");
        return sendQueue.size();
    }

    @Override
    public void send(Object input) {
        if (!isInitialized())
            throw new IllegalStateException(
                    "Can not send any message,MinaSender has not been initialized.");
        enqueueMessage(input);
    }

    private void enqueueMessage(Object bean) {
        if (null == bean) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("message is null,just ignore.");
            return;
        }

        while (!sendQueue.offer(bean)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("addSendRequest: offer message enqueue failed, try remove early cached message.");
            }
            sendQueue.poll();
        }
    }

    private void registerMBean() {

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        if (null != senderMBeanName) {
            IoServiceMBean acceptorMBean = new IoServiceMBean(connector);
            try {
                ObjectName acceptorName = new ObjectName(this.senderMBeanName);
                mBeanServer.registerMBean(acceptorMBean, acceptorName);
            } catch (MalformedObjectNameException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (NullPointerException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (InstanceAlreadyExistsException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (MBeanRegistrationException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (NotCompliantMBeanException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            }
        }

        if (null != senderIoStatsMBeanName) {
            IoServiceStatistics ioStats = connector.getStatistics();
            if (null != statisticsUpdater) {
                statisticsUpdater.register(targetAddress, ioStats);
            }
            MinaIoStatisticsMBean statsMBean = new MinaIoStatistics(ioStats);
            try {
                ObjectName statsName = new ObjectName(this.senderIoStatsMBeanName);
                mBeanServer.registerMBean(statsMBean, statsName);
            } catch (MalformedObjectNameException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (NullPointerException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (InstanceAlreadyExistsException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (MBeanRegistrationException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (NotCompliantMBeanException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            }
        }

        if (null != senderQueueMBeanName) {
            MinaSenderQueueMBean queueMBean = new MinaSenderQueueMBean(this);
            try {
                ObjectName queueName = new ObjectName(this.senderQueueMBeanName);
                mBeanServer.registerMBean(queueMBean, queueName);
            } catch (MalformedObjectNameException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (NullPointerException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (InstanceAlreadyExistsException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (MBeanRegistrationException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            } catch (NotCompliantMBeanException e) {
                LOGGER.error("Unable MinaSender jmx register:", e);
            }
        }
    }

    private void unregisterMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        if (null != senderMBeanName) {

            try {
                ObjectName acceptorName = new ObjectName(this.senderMBeanName);
                mBeanServer.unregisterMBean(acceptorName);
            } catch (MalformedObjectNameException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (NullPointerException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (MBeanRegistrationException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (InstanceNotFoundException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            }
        }

        if (null != senderIoStatsMBeanName) {
            if (null != statisticsUpdater) {
                statisticsUpdater.unregister(targetAddress);
            }
            try {
                ObjectName statsName = new ObjectName(this.senderIoStatsMBeanName);
                mBeanServer.unregisterMBean(statsName);
            } catch (MalformedObjectNameException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (NullPointerException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (MBeanRegistrationException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (InstanceNotFoundException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            }
        }

        if (null != senderQueueMBeanName) {
            try {
                ObjectName queueName = new ObjectName(this.senderQueueMBeanName);
                mBeanServer.unregisterMBean(queueName);
            } catch (MalformedObjectNameException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (NullPointerException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (MBeanRegistrationException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            } catch (InstanceNotFoundException e) {
                LOGGER.error("Unable MinaSender jmx unregister:", e);
            }
        }
    }

    // private boolean isInitialized() {
    // return initialized;
    // }

    @Override
    protected void doInit() {
        sendQueue = new LinkedBlockingQueue<Object>(cachedMessageCount);
        connector = new NioSocketConnector();

        for (Entry<String, IoFilter> e : this.filters.entrySet()) {
            connector.getFilterChain().addLast(e.getKey(), e.getValue());
        }

        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 0);
        connector.setHandler(new InnerIoHandler());

        // network config
        connector.getSessionConfig().setReuseAddress(tcpConfig.isReuseAddress());
        connector.getSessionConfig().setReceiveBufferSize(tcpConfig.getReceiveBufferSize());
        connector.getSessionConfig().setSendBufferSize(tcpConfig.getSendBufferSize());
        connector.getSessionConfig().setTcpNoDelay(tcpConfig.isTcpNoDelay());
        connector.getSessionConfig().setSoLinger(tcpConfig.getSoLinger());

        // build proxySessionLocator
        targetSessionLocator = targetSessionLocatorBuilder.build(connector, targetAddress,
                "Target-Sender-Holder-thread");

        // startup processor
        startupProcessor();

        // register MBean
        registerMBean();
    }

    @Override
    protected void doDestroy() {
        unregisterMBean();

        if (createdExecutor) {
            this.sendExecutor.shutdownNow();
        }
        this.connector.dispose();
        this.sendQueue.clear();
        this.targetSessionLocator.destroy();
        this.targetSessionLocator = null;
    }

    private void doSend(final Object message) {
        final IoSession session = lookupSendSession();

        if (null == session) {
            if (null != sendFailMessageCollector) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("can not found any availble session,send message to sendFailMessageCollector.");
                sendFailMessageCollector.send(message);
            } else {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("can not found any availble session,and no sendFailMessageCollector,just ignore message.");
            }
            return;
        }

        WriteFuture future = session.write(message);
        future.addListener(new IoFutureListener<WriteFuture>() {

            public void operationComplete(WriteFuture future) {
                onWriteRequestComplete(session, message, future);
            }
        });
    }

    private void onWriteRequestComplete(IoSession session, Object message, WriteFuture future) {
        if (!future.isWritten()) {
            if (null != sendFailMessageCollector) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("send request failed,send message to sendFailMessageCollector,fail reason:"
                            + (null == future.getException() ? "" : future.getException()));
                sendFailMessageCollector.send(message);
            } else {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("send request failed,and no sendFailMessageCollector,just ignore message,"
                            + (null == future.getException() ? "" : future.getException()));
            }
        }
    }

    private IoSession lookupSendSession() {
        return targetSessionLocator.lookup();
    }

    private void startupProcessor() {

        // start the processor if it is not already started.
        for (int i = 0; i < processorCount; i++) {
            Processor processor = new Processor();
            sendExecutor.execute(new NamePreservingRunnable(processor, "Target-Sender-thread-"
                    + processorId.get()));
        }
    }

    public InetSocketAddress getTargetAddress() {
        if (!isInitialized())
            throw new IllegalStateException(
                    "Can not getTargetAddress,MinaSender has not been initialized.");
        return targetAddress;
    }

    private final class InnerIoHandler extends IoHandlerAdapter {
        public void messageReceived(IoSession session, Object message) throws Exception {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("received message:" + message);

            if (null == callback) {
                LOGGER.warn("callback is null,ignore.");
                return;
            }
            callback.execute(message);
        }

        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            if (!(cause instanceof IOException)) {
                LOGGER.error("exceptionCaught: ", cause);
            }
            // long connection,do not closed when occur any exception.
            // session.close(true);
        }

        public void sessionClosed(IoSession session) throws Exception {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sessionClosed: [" + session.getId() + "]");
            }
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
                    LOGGER.error("processor:", e);
                    continue;
                }
            }
        }
    }

    @Override
    public void setCallback(Closure closure) {
        if (null == closure)
            throw new NullPointerException("callback");
        this.callback = closure;
    }

    public void setSenderMBeanName(String senderMBeanName) {
        if (StringUtils.isBlank(senderMBeanName))
            throw new IllegalArgumentException("senderMBeanName");
        this.senderMBeanName = senderMBeanName;
    }

    public void setSenderIoStatsMBeanName(String senderIoStatsMBeanName) {
        if (StringUtils.isBlank(senderIoStatsMBeanName))
            throw new IllegalArgumentException("senderIoStatsMBeanName is blank.");
        this.senderIoStatsMBeanName = senderIoStatsMBeanName;
    }

    public void setSenderQueueMBeanName(String senderQueueMBeanName) {
        if (StringUtils.isBlank(senderQueueMBeanName))
            throw new IllegalArgumentException("senderQueueMBeanName is blank.");
        this.senderQueueMBeanName = senderQueueMBeanName;
    }

    public void setStatisticsUpdater(MinaStatisticsUpdater statisticsUpdater) {
        this.statisticsUpdater = statisticsUpdater;
    }

    public int getProcessorCount() {
        return processorCount;
    }

    public void setProcessorCount(int processorCount) {
        this.processorCount = processorCount;
    }

    public void setSendFailMessageCollector(Sender sendFailMessageCollector) {
        this.sendFailMessageCollector = sendFailMessageCollector;
    }

    @Override
    public SenderStatus getStatus() {
        return 0 == this.targetSessionLocator.getSessions() ? SenderStatus.UNAVAILABLE
                : (STATUS_THRESHOLD_BUSY < this.getPendingSendMessages() ? SenderStatus.BUSY
                        : SenderStatus.AVAILABLE);
    }
}
