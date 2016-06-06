package org.bigmouth.nvwa.transport;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.filterchain.IoFilter;
import org.bigmouth.nvwa.transport.jmx.MinaStatisticsUpdater;
import org.bigmouth.nvwa.utils.Closure;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class DefaultSenderFactory implements SenderFactory {

    private static final int DEFAULT_MAX_SESSION_COUNT = 3;
    private static final long DEFAULT_RECONNECT_TIMEOUT = 10 * 1000;

    private final int maxSessionCount;
    private final long reconnectTimeout;
    private final Map<String, IoFilter> filters = Maps.newHashMap();
    private final TcpConfig tcpConfig;

    /*--------MBean name---------*/
    private volatile String senderMBeanName;
    private volatile String senderIoStatsMBeanName;
    private volatile String senderQueueMBeanName;

    private volatile MinaStatisticsUpdater statisticsUpdater;
    private volatile Sender sendFailMessageCollector;
    private volatile Closure callback;

    public DefaultSenderFactory(TcpConfig tcpConfig, Map<String, IoFilter> filters) {
        this(tcpConfig, filters, DEFAULT_MAX_SESSION_COUNT, DEFAULT_RECONNECT_TIMEOUT);
    }

    public DefaultSenderFactory(TcpConfig tcpConfig, Map<String, IoFilter> filters,
            int maxSessionCount, long reconnectTimeout) {
        super();
        this.maxSessionCount = maxSessionCount;
        this.reconnectTimeout = reconnectTimeout;
        this.filters.putAll(filters);
        this.tcpConfig = tcpConfig;
    }

    @Override
    public Sender create(InetSocketAddress targetAddress) {
        Preconditions.checkNotNull(targetAddress, "targetAddress");

        MinaSender ret = new MinaSender(tcpConfig, filters, targetAddress,
                new DefaultTargetSessionLocatorBuilder(maxSessionCount, reconnectTimeout), null);

        String mbeanFlag = "_" + targetAddress.getAddress().getHostAddress() + "_"
                + targetAddress.getPort();
        if (StringUtils.isNotBlank(senderMBeanName)) {
            ret.setSenderMBeanName(senderMBeanName + mbeanFlag);
        }
        if (StringUtils.isNotBlank(senderIoStatsMBeanName)) {
            ret.setSenderIoStatsMBeanName(senderIoStatsMBeanName + mbeanFlag);
        }
        if (StringUtils.isNotBlank(senderQueueMBeanName)) {
            ret.setSenderQueueMBeanName(senderQueueMBeanName + mbeanFlag);
        }

        if (null != statisticsUpdater) {
            ret.setStatisticsUpdater(statisticsUpdater);
        }
        if (null != sendFailMessageCollector) {
            ret.setSendFailMessageCollector(sendFailMessageCollector);
        }

        if (null != callback) {
            ret.setCallback(callback);
        }

        return ret;
    }

    public void setSenderMBeanName(String senderMBeanName) {
        this.senderMBeanName = senderMBeanName;
    }

    public void setSenderIoStatsMBeanName(String senderIoStatsMBeanName) {
        this.senderIoStatsMBeanName = senderIoStatsMBeanName;
    }

    public void setSenderQueueMBeanName(String senderQueueMBeanName) {
        this.senderQueueMBeanName = senderQueueMBeanName;
    }

    public void setStatisticsUpdater(MinaStatisticsUpdater statisticsUpdater) {
        this.statisticsUpdater = statisticsUpdater;
    }

    public void setSendFailMessageCollector(Sender sendFailMessageCollector) {
        this.sendFailMessageCollector = sendFailMessageCollector;
    }

    public void setCallback(Closure callback) {
        this.callback = callback;
    }
}
