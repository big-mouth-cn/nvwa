package org.bigmouth.nvwa.cache.inbound;

import java.lang.ref.SoftReference;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.cache.DataExtractor;
import org.bigmouth.nvwa.cache.FetchService;
import org.bigmouth.nvwa.cache.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * dataExtractor.extract() must be thread-safe.
 * 
 * @author nada
 * 
 */
public class ScalableFetchService implements FetchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScalableFetchService.class);
	private final ConcurrentMap<String, Future<SoftReference<Object>>> cache = new ConcurrentHashMap<String, Future<SoftReference<Object>>>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fetch(KeyGenerator keyGenerator, final DataExtractor dataExtractor, Class<T> clazz) {
		if (null == keyGenerator)
			throw new NullPointerException("keyGenerator");
		if (null == dataExtractor)
			throw new NullPointerException("dataExtractor");
		if (null == clazz)
			throw new NullPointerException("clazz");
		String key = keyGenerator.generateKey();
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");

		Future<SoftReference<Object>> f = cache.get(key);
		if (null == f) {
			final FutureTask<SoftReference<Object>> ft = new FutureTask<SoftReference<Object>>(
					new Callable<SoftReference<Object>>() {

						@Override
						public SoftReference<Object> call() throws Exception {
							final Object v = dataExtractor.extract();
							if (null == v) {
								if (LOGGER.isWarnEnabled())
									LOGGER.warn("dataExtractor.extract return null,ignore.");
								return null;
							}
							return new SoftReference<Object>(v);
						}
					});

			f = cache.putIfAbsent(key, ft);
			if (null == f) {
				f = ft;
				ft.run();
			}
		}

		SoftReference<Object> softRef = null;
		try {
			softRef = f.get();
		} catch (InterruptedException e) {
			cancel(key, f);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Being canceled.");
		} catch (CancellationException e) {
			cancel(key, f);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Being canceled.");
		} catch (ExecutionException e) {
			cancel(key, f);

			Throwable t = e.getCause();
			throw launderThrowable(t);
		}

		if (null == softRef)
			return null;

		Object v = softRef.get();
		if (null != v) {
			return (T) v;
		} else {
			cache.remove(key);
			v = dataExtractor.extract();
			if (null == v) {
				if (LOGGER.isWarnEnabled())
					LOGGER.warn("dataExtractor.extract return null,ignore.");
				return null;
			}
			cache.putIfAbsent(key, new InnerFuture(new SoftReference<Object>(v)));
			return (T) v;
		}
	}

	private static final class InnerFuture implements Future<SoftReference<Object>> {

		private final SoftReference<Object> softRef;

		InnerFuture(SoftReference<Object> ref) {
			if (null == ref)
				throw new NullPointerException("ref");
			this.softRef = ref;
		}

		@Override
		public SoftReference<Object> get() throws InterruptedException, ExecutionException {
			return softRef;
		}

		@Override
		public SoftReference<Object> get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			return null;
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public boolean isDone() {
			return true;
		}

	}

	private void cancel(final String key, Future<SoftReference<Object>> f) {
		f.cancel(true);
		cache.remove(key);
	}

	private RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException)
			return (RuntimeException) t;
		else if (t instanceof Error)
			throw (Error) t;
		else
			throw new IllegalStateException("Not checked exception:", t);
	}

	@Override
	public <T> T fetch(KeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz,
			int exp) {
		if (LOGGER.isWarnEnabled())
			LOGGER.warn("exp will be ignored.");
		return fetch(keyGenerator, dataExtractor, clazz);
	}

	@Override
	public Object getOriginalClient() {
		return null;
	}
}
