package org.bigmouth.nvwa.cache.inbound;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.bigmouth.nvwa.cache.DataExtractor;
import org.bigmouth.nvwa.cache.FetchService;
import org.bigmouth.nvwa.cache.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This cache service store data which among the memory in program.<br>
 * Attention:NOT support update cache data.<br>
 * 
 * @author nada
 * 
 */
public final class InMemFetchService implements FetchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemFetchService.class);

	private static final long DEFAULT_NO_EXP = -1;

	private final static class Wrapper {

		private final long time;

		/**
		 * Not support now.
		 */
		private final long exp;

		private final Object value;

		public Wrapper(long time, long exp, Object value) {
			super();
			this.time = time;
			this.exp = exp;
			this.value = value;
		}

		public long getTime() {
			return time;
		}

		public long getExp() {
			return exp;
		}

		public Object getValue() {
			return value;
		}

	}

	private final ConcurrentMap<String, Future<Wrapper>> cache = new ConcurrentHashMap<String, Future<Wrapper>>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fetch(final KeyGenerator keyGenerator, final DataExtractor dataExtractor,
			final Class<T> clazz) {
		if (null == keyGenerator || null == dataExtractor)
			throw new NullPointerException("keyGenerator is null or dataExtractor is null.");

		final String key = keyGenerator.generateKey();
		Future<Wrapper> f = cache.get(key);
		if (null == f) {
			final FutureTask<Wrapper> ft = new FutureTask<Wrapper>(new Callable<Wrapper>() {

				@Override
				public Wrapper call() throws Exception {
					final Object v = dataExtractor.extract();
					return new Wrapper(new Date().getTime(), DEFAULT_NO_EXP, v);
				}

			});

			f = cache.putIfAbsent(key, ft);
			if (null == f) {
				f = ft;
				ft.run();
			}
		}

		Wrapper w = null;
		try {
			w = f.get();
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

		if (null == w)
			throw new NullPointerException("result is null.");

		return (T) w.getValue();
	}

	private void cancel(final String key, Future<Wrapper> f) {
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
		throw new UnsupportedOperationException("");
	}

	@Override
	public Object getOriginalClient() {
		throw new UnsupportedOperationException("");
	}

}
