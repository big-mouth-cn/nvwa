package org.bigmouth.nvwa.transport.jmx;

public interface MinaIoStatisticsMBean {

	int getLargestManagedSessionCount();

	long getCumulativeManagedSessionCount();

	long getLastIoTime();

	long getLastReadTime();

	long getLastWriteTime();

	long getReadBytes();

	long getWrittenBytes();

	long getReadMessages();

	long getWrittenMessages();

	double getReadBytesThroughput();

	double getWrittenBytesThroughput();

	double getReadMessagesThroughput();

	double getWrittenMessagesThroughput();

	double getLargestReadBytesThroughput();

	double getLargestWrittenBytesThroughput();

	double getLargestReadMessagesThroughput();

	double getLargestWrittenMessagesThroughput();

	int getThroughputCalculationInterval();

	long getThroughputCalculationIntervalInMillis();

	int getScheduledWriteBytes();

	int getScheduledWriteMessages();

	void setThroughputCalculationInterval(int throughputCalculationInterval);
}
