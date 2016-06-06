package org.bigmouth.nvwa.cache.factory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.bigmouth.nvwa.utils.Factory;

/**
 * Non-real time cache service.
 * 
 * @author nada
 * 
 */
public class AutoFlushCacheFactory<T> implements Factory<T> {

	private static final String DEFAULT_WORK_THREAD_NAME = "autoflush-cachefactory-thread";

	private final Factory<T> factory;
	/**
	 * millis
	 */
	private final long precision;
	private String threadName = DEFAULT_WORK_THREAD_NAME;

	private ScheduledExecutorService executor = null;

	/*----status----*/
	private volatile boolean started = false;
	private volatile T value = null;

	public AutoFlushCacheFactory(Factory<T> factory, int precision) {
		super();
		if (null == factory)
			throw new NullPointerException("factory");
		if (precision <= 0)
			throw new IllegalArgumentException("precision expect > 0,but <= 0,value:" + precision);

		this.factory = factory;
		this.precision = precision;
	}

	public AutoFlushCacheFactory(Factory<T> factory, int precision, String threadName) {
		this(factory, precision);
		this.threadName = threadName;
	}

	@Override
	public T create() {
		if (!started)
			throw new IllegalStateException(
					"AutoFlushCacheFactory service is not started,never invoke start method? or service has shutdown.");

		return value;
	}

	public void start() {
		executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}

		});
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				value = factory.create();
			}

		}, 0, precision, TimeUnit.MILLISECONDS);

		value = factory.create();
		started = true;
	}

	public void destroy() {
		executor.shutdown();
		started = false;
	}
}
