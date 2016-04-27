package org.bigmouth.nvwa.access.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class AccessStatistics {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccessStatistics.class);

	private final AtomicLong successTasks = new AtomicLong(0);
	private final AtomicLong failTasks = new AtomicLong(0);

	private final ConcurrentMap<String, AtomicLong> lastTaskConsumeTimeTable = new ConcurrentHashMap<String, AtomicLong>();
	/**
	 * only collect success invoke.
	 */
	private final ConcurrentMap<String, AtomicLong> largestTaskConsumeTimeTable = new ConcurrentHashMap<String, AtomicLong>();

	private final Set<String> failInvokeIgnoreUris = new ConcurrentHashSet<String>();

	private ExecutorService executorService;

	public void init() {
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "FailInvocation-record-thread");
			}
		});
	}

	public void destroy() {
		executorService.shutdownNow();
	}

	public void increaseSuccessTask(HttpInvocation invocation) {
		successTasks.incrementAndGet();
		AtomicLong lastTaskConsumeTime = lastTaskConsumeTimeTable.putIfAbsent(invocation.getUri(),
				new AtomicLong(0L));
		if (null == lastTaskConsumeTime) {
			lastTaskConsumeTime = lastTaskConsumeTimeTable.get(invocation.getUri());
		}
		lastTaskConsumeTime.set(invocation.getConsumingTime());

		AtomicLong largestTimeContainer = largestTaskConsumeTimeTable.putIfAbsent(
				invocation.getUri(), new AtomicLong(0));
		if (null == largestTimeContainer) {
			largestTimeContainer = largestTaskConsumeTimeTable.get(invocation.getUri());
		}

		processLargestTaskConsumeTime(largestTimeContainer, invocation.getConsumingTime());
	}

	private void processLargestTaskConsumeTime(AtomicLong largestTimeContainer, long consumingTime) {
		int retry = 0;
		boolean done = false;
		do {
			long expect = largestTimeContainer.longValue();
			if (consumingTime > expect) {
				done = largestTimeContainer.compareAndSet(expect, consumingTime);
			}
			retry++;
		} while ((!done) && (retry < 3));
	}

	/*-- statistics --*/
	public long getSuccessTasks() {
		return successTasks.longValue();
	}

	public void increaseFailTask(HttpInvocation invocation) {
		failTasks.incrementAndGet();
		recordFailInvoke(invocation);
	}

	public long getFailTasks() {
		return failTasks.longValue();
	}

	public ConcurrentMap<String, AtomicLong> getLastTaskConsumeTimeTable() {
		return lastTaskConsumeTimeTable;
	}

	public ConcurrentMap<String, AtomicLong> getLargestTaskConsumeTimeTable() {
		return largestTaskConsumeTimeTable;
	}

	/*--  --*/
	public void setFailInvokeIgnoreUris(String desc) {
		if (StringUtils.isBlank(desc))
			throw new IllegalArgumentException("desc");

		String[] items = desc.trim().split(",");
		this.failInvokeIgnoreUris.clear();
		this.failInvokeIgnoreUris.addAll(Sets.newHashSet(items));
	}

	public String getFailInvokeIgnoreUris() {
		if (failInvokeIgnoreUris.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (String uri : failInvokeIgnoreUris) {
			sb.append(uri).append(",");
		}
		return sb.substring(0, sb.length() - 1).toString();
	}

	/**
	 * Simple implement.
	 * 
	 * @param failInvokeDesc
	 */
	protected void recordFailInvoke(final HttpInvocation invocation) {

		if (this.failInvokeIgnoreUris.contains(invocation.getUri())) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Invocation[?] in failInvokeIgnoreUris,ignore.", invocation.getUri());
			return;
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Invoke fail,details:" + invocation.toString());
		}

		executorService.submit(new Runnable() {

			@Override
			public void run() {

				FileOutputStream fos = null;
				try {
					File f = new File(System.getProperty("user.dir") + "//failInvoke_"
							+ DateFormatUtils.format(new Date(), "yyyyMMdd"));

					if (!f.exists()) {
						f.createNewFile();
					}
					fos = new FileOutputStream(f, true);
					fos.write(invocation.toString().getBytes("UTF-8"));
				} catch (Exception e) {
					throw new RuntimeException("recordFailInvoke:", e);
				} finally {
					if (null != fos)
						try {
							fos.close();
						} catch (Exception e) {
							// ignore.
						}
				}
			}
		});
	}
}
