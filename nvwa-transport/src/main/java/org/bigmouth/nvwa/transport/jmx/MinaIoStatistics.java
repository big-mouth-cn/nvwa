package org.bigmouth.nvwa.transport.jmx;

import org.apache.mina.core.service.IoServiceStatistics;

public final class MinaIoStatistics implements MinaIoStatisticsMBean {

	private final IoServiceStatistics statistics;

	public MinaIoStatistics(IoServiceStatistics statistics) {
		if (null == statistics)
			throw new NullPointerException("statistics");
		this.statistics = statistics;
	}

	@Override
	public int getLargestManagedSessionCount() {
		return statistics.getLargestManagedSessionCount();
	}

	@Override
	public long getCumulativeManagedSessionCount() {
		return statistics.getCumulativeManagedSessionCount();
	}

	@Override
	public long getLastIoTime() {
		return statistics.getLastIoTime();
	}

	@Override
	public long getLastReadTime() {
		return statistics.getLastReadTime();
	}

	@Override
	public long getLastWriteTime() {
		return statistics.getLastWriteTime();
	}

	@Override
	public long getReadBytes() {
		return statistics.getReadBytes();
	}

	@Override
	public long getWrittenBytes() {
		return statistics.getWrittenBytes();
	}

	@Override
	public long getReadMessages() {
		return statistics.getReadMessages();
	}

	@Override
	public long getWrittenMessages() {
		return statistics.getWrittenMessages();
	}

	@Override
	public double getReadBytesThroughput() {
		return statistics.getReadBytesThroughput();
	}

	@Override
	public double getWrittenBytesThroughput() {
		return statistics.getWrittenBytesThroughput();
	}

	@Override
	public double getReadMessagesThroughput() {
		return statistics.getReadMessagesThroughput();
	}

	@Override
	public double getWrittenMessagesThroughput() {
		return statistics.getWrittenMessagesThroughput();
	}

	@Override
	public double getLargestReadBytesThroughput() {
		return statistics.getLargestReadBytesThroughput();
	}

	@Override
	public double getLargestWrittenBytesThroughput() {
		return statistics.getLargestWrittenBytesThroughput();
	}

	@Override
	public double getLargestReadMessagesThroughput() {
		return statistics.getLargestReadMessagesThroughput();
	}

	@Override
	public double getLargestWrittenMessagesThroughput() {
		return statistics.getLargestWrittenMessagesThroughput();
	}

	@Override
	public int getThroughputCalculationInterval() {
		return statistics.getThroughputCalculationInterval();
	}

	@Override
	public long getThroughputCalculationIntervalInMillis() {
		return statistics.getThroughputCalculationIntervalInMillis();
	}

	@Override
	public int getScheduledWriteBytes() {
		return statistics.getScheduledWriteBytes();
	}

	@Override
	public int getScheduledWriteMessages() {
		return statistics.getScheduledWriteMessages();
	}

	@Override
	public void setThroughputCalculationInterval(int throughputCalculationInterval) {
		statistics.setThroughputCalculationInterval(throughputCalculationInterval);
	}
}
