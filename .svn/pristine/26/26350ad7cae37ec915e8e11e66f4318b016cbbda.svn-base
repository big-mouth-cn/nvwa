package org.bigmouth.nvwa.transport;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.integration.jmx.IoServiceMBean;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.bigmouth.nvwa.jmx.MBeanExportSupport;
import org.bigmouth.nvwa.transport.jmx.MinaIoStatistics;
import org.bigmouth.nvwa.transport.jmx.MinaIoStatisticsMBean;
import org.bigmouth.nvwa.transport.jmx.MinaStatisticsUpdater;
import org.bigmouth.nvwa.utils.Closure;
import org.bigmouth.nvwa.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MinaReceiver implements Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinaReceiver.class);

    private static final int MAX_RETRY = 20;
    private static final long RETRY_TIMEOUT = 30 * 1000; // 30s
    private static final AttributeKey REQUEST_RECVD = new AttributeKey(MinaReceiver.class,
            "request.received.transport.flag");
    private static final String DEFAULT_ACCEPT_IP = "0.0.0.0";
    private static final TcpConfig DEFAULT_TCP_CONFIG = new TcpConfig();

    private final Object lock = new Object();
    private final Map<String, IoFilter> filters = Maps.newLinkedHashMap();
    private final Set<InetSocketAddress> boundAddresses = Sets.newHashSet();
    private final TcpConfig tcpConfig;

    private String receiverMBeanName = null;
    private String receiverIoStatsMBeanName = null;
    private MinaStatisticsUpdater statisticsUpdater = null;
    private MBeanExportSupport mbeanLifeCycleSupport = null;

    private SocketAcceptor acceptor = null;
    private boolean multiTransaction = true;
    private int idleTime = 0; // in seconds, 5 minutes
    private Closure reactor;

    private volatile boolean initialized = false;

    public MinaReceiver(Map<String, IoFilter> filters, int port) {
        this(DEFAULT_TCP_CONFIG, filters, Sets.newHashSet(new InetSocketAddress(DEFAULT_ACCEPT_IP,
                port)));
    }

    public MinaReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters, int port) {
        this(tcpConfig, filters, Sets.newHashSet(new InetSocketAddress(DEFAULT_ACCEPT_IP, port)));
    }

    public MinaReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters, String ip, int port) {
        this(tcpConfig, filters, Sets.newHashSet(new InetSocketAddress(ip, port)));
    }

    public MinaReceiver(TcpConfig tcpConfig, List<Pair<String, IoFilter>> filters, String ip,
            int port) {
        this(tcpConfig, getFilters(filters), ip, port);
    }

    private static Map<String, IoFilter> getFilters(List<Pair<String, IoFilter>> filters) {
        Map<String, IoFilter> ioFilters = Maps.newLinkedHashMap();
        for (Pair<String, IoFilter> p : filters) {
            ioFilters.put(p.getFirst(), p.getSecond());
        }
        return ioFilters;
    }

    public MinaReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters,
            InetSocketAddress boundAddress) {
        this(tcpConfig, filters, Sets.newHashSet(boundAddress));
    }

    public MinaReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters,
            Set<InetSocketAddress> boundAddresses) {
        if (null != filters)
            this.filters.putAll(filters);
        if (null == boundAddresses)
            throw new NullPointerException("boundAddresses");
        final Set<InetSocketAddress> copyBoundAddresses = Sets.newHashSet();
        for (InetSocketAddress addr : boundAddresses) {
            if (null == addr)
                continue;
            copyBoundAddresses.add(addr);
        }
        if (null == copyBoundAddresses || 0 == copyBoundAddresses.size())
            throw new IllegalArgumentException("boundAddresses is blank.");

        if (null == tcpConfig)
            this.tcpConfig = DEFAULT_TCP_CONFIG;
        else
            this.tcpConfig = tcpConfig;

        this.boundAddresses.addAll(copyBoundAddresses);
    }

    private final class InnerHandler extends IoHandlerAdapter {

        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            if (!(cause instanceof IOException)) {
                LOGGER.error("exceptionCaught: ", cause);
            }
            session.close(true);
        }

        public void messageReceived(IoSession session, Object message) throws Exception {
            onMessageReceived(session, message);
            fireReactor(message);
        }

        public void sessionClosed(IoSession session) throws Exception {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sessionClosed: [" + session.getId() + "]");
            }

            onSessionClosed(session);
        }

        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("sessionIdle: " + status + ", for " + idleTime
                        + "seconds, close session[" + session.getId() + "]");
            }
            session.close(true);
        }

        public void sessionOpened(IoSession session) throws Exception {
            // in 5 minutes
            if (0 != idleTime) {
                session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
            }
        }
    }

    protected void onMessageReceived(IoSession session, Object message) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("access acceptor received message:" + message);

        if (!multiTransaction) {
            Integer count = (Integer) session.getAttribute(REQUEST_RECVD);
            if (null != count) {
                count = count.intValue() + 1;
                session.setAttribute(REQUEST_RECVD, count);
                LOGGER.error("session [" + session + "] have multi-request[" + count
                        + "], just ignore.");
                return;
            }

            session.setAttribute(REQUEST_RECVD, new Integer(1));
        }
    }

    protected Closure getReactor() {
        return this.reactor;
    }

    protected void onSessionClosed(IoSession session) throws Exception {
    }

    private void fireReactor(Object message) {
        if (null == getReactor()) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("reactor is null,ignore.");
            return;
        }

        getReactor().execute(message);
    }

    public void init() {
        synchronized (lock) {
            int retryCount = 0;
            do {
                try {
                    acceptor = new NioSocketAcceptor();

                    for (Entry<String, IoFilter> e : this.filters.entrySet()) {
                        acceptor.getFilterChain().addLast(e.getKey(), e.getValue());
                    }

                    acceptor.getSessionConfig().setReuseAddress(tcpConfig.isReuseAddress());
                    acceptor.getSessionConfig().setReceiveBufferSize(
                            tcpConfig.getReceiveBufferSize());
                    acceptor.getSessionConfig().setSendBufferSize(tcpConfig.getSendBufferSize());
                    acceptor.getSessionConfig().setTcpNoDelay(tcpConfig.isTcpNoDelay());
                    acceptor.getSessionConfig().setSoLinger(tcpConfig.getSoLinger());
                    acceptor.setBacklog(tcpConfig.getBacklog());

                    acceptor.setHandler(new InnerHandler());
                    acceptor.bind(boundAddresses);
                    initialized = true;

                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Receiver started,listening on " + boundAddresses + ".");
                } catch (BindException e) {
                    LOGGER.warn("start failed : " + e + ", and retry...");
                    acceptor = null;
                    retryCount++;
                    if (retryCount >= MAX_RETRY) {
                        throw new RuntimeException("MinaReceiver.init:", e);
                    }
                    try {
                        Thread.sleep(RETRY_TIMEOUT);
                    } catch (InterruptedException e1) {
                    }
                } catch (IOException e) {
                    // TODO:
                    throw new RuntimeException("MinaReceiver.init:", e);
                } catch (RuntimeException e) {
                    throw e;
                }
            } while (null == acceptor);

            registerMBean();
        }
    }

    private void registerMBean() {
        if (null == mbeanLifeCycleSupport) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("mbeanLifeCycleSupport is null,ignore.");
            return;
        }

        if (null != receiverMBeanName) {
            IoServiceMBean acceptorMBean = new IoServiceMBean(acceptor);
            try {
                ObjectName acceptorName = new ObjectName(this.receiverMBeanName);
                mbeanLifeCycleSupport.register(acceptorMBean, acceptorName);
            } catch (Exception e) {
                LOGGER.error("Unable MinaReceiver jmx register:", e);
            }
        }

        if (null != receiverIoStatsMBeanName) {
            IoServiceStatistics ioStats = acceptor.getStatistics();
            if (null != statisticsUpdater) {
                statisticsUpdater.register(boundAddresses.toArray(new InetSocketAddress[0])[0],
                        ioStats);
            }
            MinaIoStatisticsMBean statsMBean = new MinaIoStatistics(ioStats);

            try {
                ObjectName statsName = new ObjectName(this.receiverIoStatsMBeanName);
                mbeanLifeCycleSupport.register(statsMBean, statsName);
            } catch (Exception e) {
                LOGGER.error("Unable MinaReceiver jmx register:", e);
            }
        }
    }

    private void unregisterMBean() {
        if (null == mbeanLifeCycleSupport) {
            return;
        }

        if (null != receiverMBeanName) {
            try {
                ObjectName acceptorName = new ObjectName(this.receiverMBeanName);
                mbeanLifeCycleSupport.unregister(acceptorName);
            } catch (Exception e) {
                LOGGER.error("Unable MinaReceiver jmx unregister:", e);
            }
        }

        if (null != receiverIoStatsMBeanName) {
            if (null != statisticsUpdater) {
                statisticsUpdater.unregister(boundAddresses.toArray(new InetSocketAddress[0])[0]);
            }
            try {
                ObjectName statsName = new ObjectName(this.receiverIoStatsMBeanName);
                mbeanLifeCycleSupport.unregister(statsName);
            } catch (Exception e) {
                LOGGER.error("Unable MinaReceiver jmx", e);
            }
        }
    }

    public void destroy() {
        synchronized (lock) {
            if (null != acceptor) {

                unregisterMBean();

                acceptor.dispose();
                acceptor = null;

                reactor = null;
                initialized = false;
            }
        }
    }

    private boolean isInitialized() {
        return initialized;
    }

    public IoServiceStatistics getStatistics() {
        if (!isInitialized())
            throw new IllegalStateException(
                    "Can not get statistics,MinaReceiver has not been initialized.");
        return acceptor.getStatistics();
    }

    public void setMultiTransaction(boolean multiTransaction) {
        if (isInitialized())
            throw new IllegalStateException(
                    "Can not modify multiTransaction,MinaReceiver has been initialized.");
        this.multiTransaction = multiTransaction;
    }

    public void setIdleTime(int idleTime) {
        if (isInitialized())
            throw new IllegalStateException("Can not modify idleTime,MinaReceiver has initialized.");
        this.idleTime = idleTime;
    }

    public void setReactor(Closure closure) {
        if (null == closure)
            throw new NullPointerException("reactor");
        this.reactor = closure;
    }

    public void setReceiverMBeanName(String receiverMBeanName) {
        if (StringUtils.isBlank(receiverMBeanName))
            throw new IllegalArgumentException("receiverMBeanName is blank.");
        this.receiverMBeanName = receiverMBeanName;
    }

    public void setReceiverIoStatsMBeanName(String receiverIoStatsMBeanName) {
        if (StringUtils.isBlank(receiverIoStatsMBeanName))
            throw new IllegalArgumentException("receiverIoStatsMBeanName is blank.");
        this.receiverIoStatsMBeanName = receiverIoStatsMBeanName;
    }

    public void setStatisticsUpdater(MinaStatisticsUpdater statisticsUpdater) {
        this.statisticsUpdater = statisticsUpdater;
    }

    public void setMbeanLifeCycleSupport(MBeanExportSupport mbeanLifeCycleSupport) {
        if (null == mbeanLifeCycleSupport)
            throw new NullPointerException("mbeanLifeCycleSupport");
        this.mbeanLifeCycleSupport = mbeanLifeCycleSupport;
    }
}
