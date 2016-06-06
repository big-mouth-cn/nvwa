package org.bigmouth.nvwa.utils.jmx.mina;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.service.IoServiceStatistics;

public class DelegateIoServiceStatistics implements IoServiceStatisticsMBean {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private final IoServiceStatistics statistics;

	public DelegateIoServiceStatistics(IoServiceStatisticsGetter statisticsGetter) {
		if (null == statisticsGetter)
			throw new NullPointerException("statisticsGetter");
		IoServiceStatistics stats = statisticsGetter.getIoStatistics();
		if (null == stats)
			throw new NullPointerException("IoServiceStatistics");
		this.statistics = stats;
	}

	@Override
	public String getLastReadTime() {
		long t = statistics.getLastReadTime();
		if (0 >= t)
			return "";
		return DATE_FORMATTER.format(new Date(t));
	}

	@Override
	public String getLastWriteTime() {
		long t = statistics.getLastWriteTime();
		if (0 >= t)
			return "";
		return DATE_FORMATTER.format(new Date(t));
	}

	@Override
	public int getLargestManagedSessionCount() {
		return statistics.getLargestManagedSessionCount();
	}

//	@Override
//	public double getLargestReadBytesThroughput() {
//		return statistics.getLargestReadBytesThroughput();
//	}
//
//	@Override
//	public double getLargestReadMessagesThroughput() {
//		return statistics.getLargestReadMessagesThroughput();
//	}
//
//	@Override
//	public double getLargestWrittenBytesThroughput() {
//		return statistics.getLargestWrittenBytesThroughput();
//	}
//
//	@Override
//	public double getLargestWrittenMessagesThroughput() {
//		return statistics.getLargestWrittenMessagesThroughput();
//	}

	@Override
	public long getReadBytes() {
		return statistics.getReadBytes();
	}

//	@Override
//	public double getReadBytesThroughput() {
//		return statistics.getReadBytesThroughput();
//	}

	@Override
	public long getReadMessages() {
		return statistics.getReadMessages();
	}

//	@Override
//	public double getReadMessagesThroughput() {
//		return statistics.getReadMessagesThroughput();
//	}

	@Override
	public int getScheduledWriteBytes() {
		return statistics.getScheduledWriteBytes();
	}

	@Override
	public int getScheduledWriteMessages() {
		return statistics.getScheduledWriteMessages();
	}

//	@Override
//	public int getThroughputCalculationInterval() {
//		return statistics.getThroughputCalculationInterval();
//	}

	@Override
	public long getWrittenBytes() {
		return statistics.getWrittenBytes();
	}

//	@Override
//	public double getWrittenBytesThroughput() {
//		return statistics.getWrittenBytesThroughput();
//	}

	@Override
	public long getWrittenMessages() {
		return statistics.getWrittenMessages();
	}

//	@Override
//	public double getWrittenMessagesThroughput() {
//		return statistics.getWrittenMessagesThroughput();
//	}
//
//	@Override
//	public void setThroughputCalculationInterval(int throughputCalculationInterval) {
//		statistics.setThroughputCalculationInterval(throughputCalculationInterval);
//	}
}
