package org.bigmouth.nvwa.log.rdb;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public final class LogAppender {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogAppender.class);
	private static final String DEFAULT_THREAD_NAME = "Rdb-LogAppender-thread";

	private final JdbcTemplate simpleJdbcTemplate;
	private final String threadName;
	private final AtomicInteger appendTaskCount = new AtomicInteger(0);

	private final Object lock = new Object();
	private volatile boolean initialized = false;
	private volatile long consumeInMillis = -1L;

	private ExecutorService exec;

	public LogAppender(JdbcTemplate simpleJdbcTemplate) {
		this(simpleJdbcTemplate, DEFAULT_THREAD_NAME);
	}

	public LogAppender(JdbcTemplate simpleJdbcTemplate, String threadName) {
		if (null == simpleJdbcTemplate)
			throw new NullPointerException("simpleJdbcTemplate");
		if (StringUtils.isBlank(threadName))
			throw new IllegalArgumentException("threadName is blank.");
		this.simpleJdbcTemplate = simpleJdbcTemplate;
		this.threadName = threadName;
	}

	public void record(final String recordSql, final List<Object[]> args) {
		if (!initialized)
			throw new IllegalStateException("LogAppender has not been initialized yet.");
		if (args.size() > 0 && !StringUtils.isBlank(recordSql)) {
			appendTaskCount.incrementAndGet();

			exec.submit(new Runnable() {

				public void run() {
					long startTime = System.currentTimeMillis();
					try {
						simpleJdbcTemplate.batchUpdate(recordSql, args);
					} catch (Exception e) {
						LOGGER.error("LogAppender JDBC Error:", e);
					}
					int count = appendTaskCount.decrementAndGet();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Pending Append Task Count : " + count);
					}
					consumeInMillis = System.currentTimeMillis() - startTime;
				}
			});
		} else {
			LOGGER.error("recordSql : " + recordSql + ", args size : " + args.size());
		}
	}

	public long getConsumeInMillis() {
		return consumeInMillis;
	}

	public long getPendingMessages() {
		return appendTaskCount.get();
	}

	public void init() {
		synchronized (lock) {
			if (initialized)
				return;
			exec = Executors.newSingleThreadExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, threadName);
				}
			});
			initialized = true;
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("LogAppender has been initialized.");
		}
	}

	public void destroy() {
		synchronized (lock) {
			if (null == exec)
				return;
			initialized = false;
			exec.shutdownNow();
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("LogAppender has been destroyed.");
		}
	}
}
