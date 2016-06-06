package org.bigmouth.nvwa.dpl.service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceStatistics {

	private final AbstractService service;

	private final AtomicLong largestTaskConsumeTime = new AtomicLong(0);
	private final AtomicLong lastTaskConsumeTime = new AtomicLong(0);
	private final AtomicLong successTasks = new AtomicLong(0);
	private final AtomicLong failTasks = new AtomicLong(0);

	public ServiceStatistics(AbstractService service) {
		super();
		if (null == service)
			throw new NullPointerException("service");
		this.service = service;
	}

	private ThreadPoolExecutor getThreadPool() {
		return (ThreadPoolExecutor) service.getExecutor();
	}

	/* ThreadPoolExecutor statistics */
	public int getMaxThreads() {
		return getThreadPool().getMaximumPoolSize();
	}

	public int getCoreThreads() {
		return getThreadPool().getCorePoolSize();
	}

	public int getCurrentThreads() {
		return getThreadPool().getPoolSize();
	}

	public int getLargestThreads() {
		return getThreadPool().getLargestPoolSize();
	}

	public int getPendingTasks() {
		return getThreadPool().getQueue().size();
	}

	/* Transaction task statistics */
	public long getAvgTaskConsumeInMillis() {
		return -1L;
	}

	public long getLargestTaskConsumeInMillis() {
		return largestTaskConsumeTime.longValue();
	}

	public long getLastTaskConsumeInMillis() {
		return lastTaskConsumeTime.longValue();
	}

	public long getSuccessTasks() {
		return successTasks.longValue();
	}

	public long getFailTasks() {
		return failTasks.longValue();
	}

	/* Transaction task throughput */
	// TODO:
	public double getSuccessTasksThroughput() {
		return 0.0D;
	}

	// TODO:
	public double getLargestSuccessTasksThroughput() {
		return 0.0D;
	}

	// TODO:
	public double getFailTasksThroughput() {
		return 0.0D;
	}

	// TODO:
	public double getLargestFailTasksThroughput() {
		return 0.0D;
	}

	// TODO:
	public void updateThroughput(long currentTime) {
		// Do nothing
	}

	public void increaseSuccessTask(long consumingTime) {
		lastTaskConsumeTime.set(consumingTime);
		successTasks.incrementAndGet();

		processLargestTaskConsumeTime(consumingTime);
	}

	public void increaseFailTask(long consumingTime) {
		lastTaskConsumeTime.set(consumingTime);
		failTasks.incrementAndGet();

		processLargestTaskConsumeTime(consumingTime);
	}

	private void processLargestTaskConsumeTime(long consumingTime) {
		int retry = 0;
		boolean done = false;
		do {
			long expect = this.largestTaskConsumeTime.longValue();
			if (consumingTime > expect) {
				done = this.largestTaskConsumeTime.compareAndSet(expect, consumingTime);
			}
			retry++;
		} while ((!done) && (retry < 3));
	}
}
