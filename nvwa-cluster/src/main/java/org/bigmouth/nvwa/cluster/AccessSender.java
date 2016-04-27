package org.bigmouth.nvwa.cluster;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.filterchain.IoFilter;
import org.bigmouth.nvwa.transport.CallbackSender;
import org.bigmouth.nvwa.transport.MinaSender;
import org.bigmouth.nvwa.transport.Sender;
import org.bigmouth.nvwa.transport.SenderStatus;
import org.bigmouth.nvwa.transport.TargetSessionLocatorBuilder;
import org.bigmouth.nvwa.transport.TcpConfig;
import org.bigmouth.nvwa.transport.jmx.MinaStatisticsUpdater;
import org.bigmouth.nvwa.utils.Closure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AccessSender implements Sender {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccessSender.class);

	private final TcpConfig tcpConfig;
	private final Map<String, IoFilter> filters;
	private final TargetSessionLocatorBuilder targetSessionLocatorBuilder;
	private final Closure callback;
	private final ServiceLogicNodeHolder serviceLogicNodeHolder;

	private volatile ScheduledExecutorService executor;
	private volatile Sender sender;

	// mbean support
	private String senderMBeanName = null;
	private String senderQueueMBeanName = null;
	private String senderIoStatsMBeanName = null;
	private MinaStatisticsUpdater statisticsUpdater = null;

	public AccessSender(TcpConfig tcpConfig, Map<String, IoFilter> filters,
			TargetSessionLocatorBuilder targetSessionLocatorBuilder, Closure callback,
			ServiceLogicNodeHolder serviceLogicNodeHolder) {
		super();
		this.tcpConfig = tcpConfig;
		this.filters = filters;
		this.targetSessionLocatorBuilder = targetSessionLocatorBuilder;
		this.callback = callback;
		this.serviceLogicNodeHolder = serviceLogicNodeHolder;
	}

	@Override
	public void send(Object message) {
		while (null == sender) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				LOGGER.error("send:", e);
			}
		}
		sender.send(message);
	}

	@Override
	public void init() {
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (null != sender)
					return;
				InetSocketAddress serviceLogicNode = serviceLogicNodeHolder.getServiceLogicNode();
				if (null == serviceLogicNode) {
					return;
				}

				sender = new MinaSender(tcpConfig, filters, serviceLogicNode,
						targetSessionLocatorBuilder);
				((CallbackSender) sender).setCallback(callback);

				// mbean support
				MinaSender minaSender = (MinaSender) sender;
				minaSender.setSenderIoStatsMBeanName(senderIoStatsMBeanName);
				minaSender.setSenderMBeanName(senderMBeanName);
				minaSender.setSenderQueueMBeanName(senderQueueMBeanName);
				minaSender.setStatisticsUpdater(statisticsUpdater);

				sender.init();
			}

		}, 1, 1, TimeUnit.SECONDS);
	}

	@Override
	public void destroy() {
		if (null != sender) {
			sender.destroy();
		}
		if (null != executor) {
			executor.shutdownNow();
		}
	}

	@Override
    public SenderStatus getStatus() {
        return SenderStatus.AVAILABLE;
    }

    public void setSenderMBeanName(String senderMBeanName) {
		this.senderMBeanName = senderMBeanName;
	}

	public void setSenderQueueMBeanName(String senderQueueMBeanName) {
		this.senderQueueMBeanName = senderQueueMBeanName;
	}

	public void setSenderIoStatsMBeanName(String senderIoStatsMBeanName) {
		this.senderIoStatsMBeanName = senderIoStatsMBeanName;
	}

	public void setStatisticsUpdater(MinaStatisticsUpdater statisticsUpdater) {
		this.statisticsUpdater = statisticsUpdater;
	}
}
