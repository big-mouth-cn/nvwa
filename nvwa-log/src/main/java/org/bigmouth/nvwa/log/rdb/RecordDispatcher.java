package org.bigmouth.nvwa.log.rdb;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.log.RecordClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class RecordDispatcher implements RecordClosure {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordDispatcher.class);
	private static final String DEFAULT_THREAD_NAME = "RecordDispatcher-thread";

	private final RecordControllerFactory recordControllerFactory;
	private final String threadName;
	private final ConcurrentMap<Class<?>, RecordController> controllers = new ConcurrentHashMap<Class<?>, RecordController>();

	private final Object lock = new Object();
	private volatile boolean initialized = false;

	private ExecutorService executorService = null;

	public RecordDispatcher(RecordControllerFactory recordControllerFactory) {
		this(recordControllerFactory, DEFAULT_THREAD_NAME);
	}

	public RecordDispatcher(RecordControllerFactory recordControllerFactory, String threadName) {
		super();
		if (null == recordControllerFactory)
			throw new NullPointerException("recordControllerFactory");
		if (StringUtils.isBlank(threadName))
			throw new IllegalArgumentException("threadName is blank.");
		this.recordControllerFactory = recordControllerFactory;
		this.threadName = threadName;
	}

	@Override
	public void execute(final Object logInfo) {
		if (!initialized)
			throw new IllegalStateException("RecordDispatcher has not been initialized yet.");
		if (null == logInfo)
			throw new NullPointerException("logInfo");
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				doDispatch(logInfo);
			}
		});
	}

	private void doDispatch(Object logInfo) {
		RecordController controller = controllers.get(logInfo.getClass());
		if (null == controller) {
			controller = recordControllerFactory.create(logInfo);
			controller.init();
			registerController(logInfo.getClass(), controller);
		}

		// TODO:if null == controller?
		controller.execute(logInfo);
	}

	public void registerController(Class<?> key, RecordController controller) {
		if (null == key)
			throw new NullPointerException("key");
		if (null == controller)
			throw new NullPointerException("controller");
		controllers.put(key, controller);
	}

	public void init() {
		synchronized (lock) {
			if (initialized)
				return;
			executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {

				public Thread newThread(Runnable r) {
					return new Thread(r, threadName);
				}
			});
			initialized = true;
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("RecordDispatcher has been initialized.");
		}
	}

	public void destroy() {
		synchronized (lock) {
			if (null == executorService)
				return;
			initialized = false;
			executorService.shutdownNow();
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("RecordDispatcher has been destroyed.");
		}
	}
}
