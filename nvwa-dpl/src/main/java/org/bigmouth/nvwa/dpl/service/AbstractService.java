package org.bigmouth.nvwa.dpl.service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.bigmouth.nvwa.dpl.GenericLifeCycle;
import org.bigmouth.nvwa.dpl.event.AfterExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.BeforeExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.plugin.PlugInConfig;
import org.bigmouth.nvwa.dpl.status.ImmutableStatus;
import org.bigmouth.nvwa.dpl.status.Status;
import org.bigmouth.nvwa.dpl.status.StatusSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractService extends GenericLifeCycle<ServiceConfig, ServiceListener>
		implements MutableService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);
	private static final String WORK_THREAD_NAEM = "service-workthread-";
	private final Object lock = new Object();

	private final ServiceStatistics statistics;
	private final ThreadLocal<Long> executeBeginTime = new ThreadLocal<Long>();

	private ExecutorService executor;
	private final AtomicInteger workThreadId = new AtomicInteger(0);
	private final boolean createdExecutor;

	private volatile ClassLoader classloader = null;
	private volatile Object attachment = null;

	public AbstractService(ServiceConfig config, Status status) {
		this(config, status, null);
	}

	public AbstractService(ServiceConfig config, Status status, List<ServiceListener> listeners) {
		this(config, status, listeners, null);
	}

	public AbstractService(ServiceConfig config, Status status, List<ServiceListener> listeners,
			ExecutorService executor) {
		super(config, status, listeners);

		if (executor == null) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Service ExecutorService is null,using default executor[Cached].");
			this.executor = Executors.newCachedThreadPool(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, WORK_THREAD_NAEM + workThreadId.incrementAndGet());
				}
			});
			createdExecutor = true;
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Service ExecutorService [" + executor + "].");
			this.executor = executor;
			createdExecutor = false;
		}

		statistics = new ServiceStatistics(this);
	}

	@Override
	public void destroy() {
		synchronized (lock) {
			if (null != executor) {
				if (createdExecutor) {
					this.executor.shutdownNow();
					this.executor = null;
					this.executeBeginTime.remove();
				}
			}
			super.destroy();
		}
	}

	protected ExecutorService getExecutor() {
		return executor;
	}

	protected void executeFailed(Exception e, final Object... arguments) {
		for (Iterator<ServiceListener> it = getAllEventListener(); it.hasNext();) {
			ServiceListener listener = it.next();
			listener.onExecuteFailed(new ExecutionFailedEvent(this, e, arguments));
		}
		processStatistics(false);
	}

	protected void beforeExecute(final Object... arguments) {
		executeBeginTime.set(System.currentTimeMillis());
		reviseThreadClassLoader();

		for (Iterator<ServiceListener> it = this.getAllEventListener(); it.hasNext();) {
			ServiceListener listener = it.next();
			try {
				listener.beforeExecute(new BeforeExecutionFiredEvent(this, arguments));
			} catch (Exception e) {
				LOGGER.error("ServiceListener.beforeExecute occur error,details:", e);
			}
		}
	}

	protected void executeSuccess(final Object... arguments) {
		for (Iterator<ServiceListener> it = this.getAllEventListener(); it.hasNext();) {
			ServiceListener listener = it.next();
			try {
				listener.afterExecute(new AfterExecutionFiredEvent(this, arguments));
			} catch (Exception e) {
				LOGGER.error("ServiceListener.afterExecute occur error,ignore,details:", e);
			}
		}
		processStatistics(true);
	}

	private void processStatistics(boolean isSuccess) {
		long consumingTime = System.currentTimeMillis() - executeBeginTime.get();
		if (isSuccess) {
			getStatistics().increaseSuccessTask(consumingTime);
		} else {
			getStatistics().increaseFailTask(consumingTime);
		}
	}

	private void reviseThreadClassLoader() {
		ClassLoader threadContextClassLoader = getThreadContextClassLoader();
		ClassLoader serviceLoadedClassLoader = getServiceLoadedClassLoader();

		if (threadContextClassLoader != serviceLoadedClassLoader)
			Thread.currentThread().setContextClassLoader(serviceLoadedClassLoader);
	}

	private ClassLoader getServiceLoadedClassLoader() {
		return getClassLoader();
	}

	private ClassLoader getThreadContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public long getLastModifiedTime() {
		return getStatus().getLastModifiedTime();
	}

	@Override
	public boolean isRunning() {
		return getStatus().isRunning();
	}

	@Override
	public void setRunning(boolean running) {
		Status status = new ImmutableStatus(running);
		setStatus(status);
	}

	@Override
	public String getStatusKey() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("status");
		sb.append(StatusSource.SPLIT);
		sb.append("service");
		sb.append(StatusSource.SPLIT);
		sb.append(getKey());
		return sb.toString();
	}

	@Override
	public PlugInConfig getPlugInConfig() {
		// TODO Auto-generated method stub
		// TODO:
		return null;
	}

	@Override
	public String getDesc() {
		return getConfig().getDesc();
	}

	@Override
	public String getKey() {
		return getConfig().getKey();
	}

	@Override
	public String getName() {
		return getConfig().getName();
	}

	@Override
	public ClassLoader getClassLoader() {
		return classloader;
	}

	@Override
	public void setClassLoader(ClassLoader classloader) {
		if (null == classloader)
			throw new NullPointerException("classloader");
		this.classloader = classloader;
	}

	@Override
	public ServiceStatistics getStatistics() {
		return statistics;
	}

	@Override
	public Object getAttachment() {
		return this.attachment;
	}

	@Override
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
}
