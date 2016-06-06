package org.bigmouth.nvwa.servicelogic.statistics;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.DateFormatUtils;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;

import com.google.common.collect.Lists;

public class ExtendedServiceStatistics {

	private static final boolean DEFAULT_IS_RECORD_INVOCATION = false;

	private final PlugIn plugIn;
	private final Service service;

	private final AtomicLong hitCaches = new AtomicLong(0);
	private final AtomicLong hitStorages = new AtomicLong(0);

	private volatile boolean isRecordInvocations = DEFAULT_IS_RECORD_INVOCATION;
	private final InvocationList invocationList = new InvocationList();

	public ExtendedServiceStatistics(PlugIn plugIn, Service service) {
		this.plugIn = plugIn;
		this.service = service;
	}

	public void init() {
		invocationList.init();
	}

	public void destroy() {
		invocationList.destroy();
	}

	private class InvocationList {

		private volatile int amountThreshold = 50;

		private ExecutorService executorService = null;
		private List<String> recentInvocations = new LinkedList<String>();

		private final Object lock = new Object();
		private volatile boolean initialized = false;

		private int getAmountThreshold() {
			return amountThreshold;
		}

		private void setAmountThreshold(int amountThreshold) {
			this.amountThreshold = amountThreshold;
		}

		void recordInvocation(final String iv) {
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					if (satisfyAmountThreshold()) {
						recentInvocations = Lists.newLinkedList();
					}
					recentInvocations.add(iv);
				}
			});
		}

		boolean satisfyAmountThreshold() {
			return recentInvocations.size() >= amountThreshold;
		}

		List<String> getInvocations() {
			try {
				return executorService.submit(new Callable<List<String>>() {

					@Override
					public List<String> call() throws Exception {
						return recentInvocations;
					}
				}).get();
			} catch (InterruptedException e) {
				throw new RuntimeException("getInvocations:", e);
			} catch (ExecutionException e) {
				throw new RuntimeException("getInvocations:", e);
			}
		}

		void init() {
			synchronized (lock) {
				if (initialized)
					return;
				executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {

					public Thread newThread(Runnable r) {
						return new Thread(r, plugIn.getKey() + "-" + service.getKey()
								+ "-statistics-cache-thread");
					}
				});

				initialized = true;
			}
		}

		void destroy() {
			synchronized (lock) {
				if (null == executorService)
					return;
				initialized = false;
				executorService.shutdownNow();
			}
		}
	}

	/* Cache statistics */
	public void increaseHitCache() {
		hitCaches.incrementAndGet();
	}

	public void increaseHitStorage() {
		hitStorages.incrementAndGet();
	}

	public long getHitCaches() {
		return hitCaches.get();
	}

	public long getHitStorages() {
		return hitStorages.get();
	}

	public void recordInvocation(TransactionInvocation iv) {
		if (!isRecordInvocations) {
			return;
		}
		String invocationDesc = new StringBuilder(128)
				.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss SSS")).append("|")
				.append(iv.getRequest().getPeerIp()).append("|").append(iv.getRequestModel())
				.toString();
		invocationList.recordInvocation(invocationDesc);
	}

	public String getInvocations() {
		StringBuilder sb = new StringBuilder();
		for (String iv : invocationList.getInvocations()) {
			sb.append(iv).append("\r\n");
		}
		return sb.toString();
	}

	/* properties config */
	public int getInvocationAmountThreshold() {
		return invocationList.getAmountThreshold();
	}

	public void setInvocationAmountThreshold(int amountThreshold) {
		invocationList.setAmountThreshold(amountThreshold);
	}

	boolean isRecordInvocations() {
		return isRecordInvocations;
	}

	void setRecordInvocations(boolean isRecordInvocations) {
		this.isRecordInvocations = isRecordInvocations;
	}
}
