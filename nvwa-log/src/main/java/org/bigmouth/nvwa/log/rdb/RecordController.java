package org.bigmouth.nvwa.log.rdb;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.log.RecordClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RecordController implements RecordClosure {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);
	private static final String DEFAULT_THREAD_NAME = "RecordController-thread";
	private static final long DEFAULT_TIME_INTERVAL = 5000L;
	private static final int DEFAULT_QUEUE_THRESHOLD = 1000;

	private final String recordSql;
	private final List<Column> columns;
	private final String threadName;

	private final long timeThreshold;
	private final int amountThreshold;

	private final LogAppender logAppender;

	private ScheduledExecutorService executorService = null;
	private List<Object[]> batchArgs = new LinkedList<Object[]>();
	private long lastBatchExecutedTime = 0;

	private final Object lock = new Object();
	private volatile boolean initialized = false;

	public RecordController(String recordSql, List<Column> columns, LogAppender logAppender) {
		this(recordSql, columns, logAppender, DEFAULT_THREAD_NAME);
	}

	public RecordController(String recordSql, List<Column> columns, LogAppender logAppender,
			String threadName) {
		this(recordSql, columns, logAppender, threadName, DEFAULT_TIME_INTERVAL,
				DEFAULT_QUEUE_THRESHOLD);
	}

	public RecordController(String recordSql, List<Column> columns, LogAppender logAppender,
			String threadName, long timeThreshold, int amountThreshold) {
		if (StringUtils.isBlank(recordSql))
			throw new IllegalArgumentException("recordSql is blank.");
		if (null == columns || 0 == columns.size())
			throw new IllegalArgumentException("columns is blank.");
		if (StringUtils.isBlank(threadName))
			throw new IllegalArgumentException("threadName is blank.");
		if (timeThreshold <= 0)
			throw new IllegalArgumentException("timeThreshold:" + timeThreshold);
		if (amountThreshold <= 0)
			throw new IllegalArgumentException("amountThreshold:" + amountThreshold);
		if (null == logAppender)
			throw new NullPointerException("logAppender");
		this.recordSql = recordSql;
		this.columns = columns;
		this.threadName = threadName;
		this.timeThreshold = timeThreshold;
		this.amountThreshold = amountThreshold;
		this.logAppender = logAppender;
	}

	@Override
	public void execute(final Object logInfo) {
		if (!initialized)
			throw new IllegalStateException("RecordController has not been initialized yet.");
		if (null == logInfo)
			throw new NullPointerException("logInfo");
		executorService.submit(new Runnable() {

			public void run() {
				try {
					Object[] values = getParameters(logInfo);
					batchArgs.add(values);
					if (satisfyAmountThreshold()) {
						fireRecord();
					}
				} catch (Exception e) {
					LOGGER.error("RecordController.execute:", e);
				}
			}

			private boolean satisfyAmountThreshold() {
				return batchArgs.size() >= amountThreshold;
			}

			private Object[] getParameters(final Object logInfo) {
				Object[] values = new Object[columns.size()];
				int idx = 0;
				for (Column column : columns) {
					Object value = column.getValue(logInfo);
					values[idx++] = value;
				}
				return values;
			}
		});
	}

	private void fireRecord() {
		if (!batchArgs.isEmpty()) {
			try {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("do batchRecord " + batchArgs.size());
				this.logAppender.record(recordSql, batchArgs);
			} finally {
				batchArgs = new LinkedList<Object[]>();
				lastBatchExecutedTime = System.currentTimeMillis();
			}
		}
	}

	public long getPendingMessages() {
		if (executorService instanceof ThreadPoolExecutor) {
			ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
			return threadPoolExecutor.getQueue().size();
		} else {
			throw new RuntimeException("executorService expect ThreadPoolExecutor,but "
					+ executorService.getClass());
		}
	}

	public String getRecordSql() {
		return recordSql;
	}

	public void init() {
		synchronized (lock) {
			if (initialized)
				return;
			executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {

				public Thread newThread(Runnable r) {
					return new Thread(r, threadName);
				}
			});

			executorService.scheduleWithFixedDelay(new Runnable() {

				public void run() {
					if (satisfyTimeThreshold()) {
						fireRecord();
					}
				}

				private boolean satisfyTimeThreshold() {
					return System.currentTimeMillis() - lastBatchExecutedTime > timeThreshold;
				}
			}, 0, timeThreshold, TimeUnit.MILLISECONDS);
			initialized = true;
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("RecordController has been initialized,\r\nRecord Sql:" + recordSql);
		}
	}

	public void destroy() {
		synchronized (lock) {
			if (null == executorService)
				return;
			initialized = false;
			executorService.shutdownNow();
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("RecordController has been destroyed.");
		}
	}
}
