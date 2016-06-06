package org.bigmouth.nvwa.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncClosure implements Closure {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncClosure.class);

	private static final String THREAD_FACTORY_NAME = "async-closure-thread";
	private static final int THREAD_COUNT = 1;

	private ExecutorService es = null;
	private Closure impl;

	public AsyncClosure(Closure impl) {
		this(THREAD_COUNT, THREAD_FACTORY_NAME, impl);
	}

	public AsyncClosure(ExecutorService es, Closure impl) {
		this.es = es;
		this.impl = impl;
	}

	public AsyncClosure(final int threadCount, final String threadFactoryName, Closure impl) {

		es = Executors.newFixedThreadPool(threadCount, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadFactoryName);
			}

		});

		this.impl = impl;
	}

	@Override
	public void execute(final Object input) {
		es.execute(new Runnable() {

			@Override
			public void run() {
				try {
					impl.execute(input);
				} catch (Exception e) {
					LOGGER.error("execute:", e);
				}
			}

		});
	}

	public ExecutorService getExecutorService() {
		return es;
	}

	public Closure getImpl() {
		return impl;
	}

	public void stop() {
		es.shutdownNow();
	}
}
