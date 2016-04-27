package org.bigmouth.nvwa.dpl.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultFunctionalService extends AbstractService implements FunctionalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFunctionalService.class);

	private final ServiceFunctor serviceFunctor;

	public DefaultFunctionalService(ServiceConfig config, Status status,
			List<ServiceListener> listeners, ExecutorService executor, ServiceFunctor serviceFunctor) {
		super(config, status, listeners, executor);
		if (null == serviceFunctor) {
			this.serviceFunctor = ServiceFunctor.DEFAULT;
		} else {
			this.serviceFunctor = serviceFunctor;
		}
	}

	public DefaultFunctionalService(ServiceConfig config, Status status,
			List<ServiceListener> listeners, ServiceFunctor serviceFunctor) {
		this(config, status, listeners, null, serviceFunctor);
	}

	public DefaultFunctionalService(ServiceConfig config, Status status,
			ServiceFunctor serviceFunctor) {
		this(config, status, null, serviceFunctor);
	}

	@Override
	public Object execute(final Object... arguments) {
		Future<Object> f = getExecutor().submit(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				beforeExecute(arguments);
				Object ret = serviceFunctor.execute(arguments);
				executeSuccess(arguments);
				return ret;
			}
		});

		boolean isSuccess = false;
		Object ret = null;
		try {
			ret = f.get();
			isSuccess = true;
		} catch (InterruptedException e) {
			cancel(f);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Being canceled.");
			executeFailed(e, arguments);
		} catch (CancellationException e) {
			cancel(f);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Being canceled.");
			executeFailed(e, arguments);
		} catch (ExecutionException e) {
			cancel(f);
			Throwable t = e.getCause();
			executeFailed(launderThrowable(t), arguments);
		}

		if (null != ret) {
			if (ret.getClass() == Void.TYPE) {
				// TODO:
				throw new RuntimeException("Illegal invoke.");
			}
		}

		if (isSuccess) {
			executeSuccess();
		}

		return ret;
	}

	// TODO:
	@Override
	public Object execute(final long timeout, final TimeUnit unit, final Object... arguments)
			throws TimeoutException {
		Future<Object> f = getExecutor().submit(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				beforeExecute();
				Object ret = serviceFunctor.execute(arguments);
				// executeSuccess();
				return ret;
			}
		});

		boolean isSuccess = false;
		Object ret = null;
		try {
			ret = f.get(timeout, unit);// TimeoutException
			isSuccess = true;
		} catch (TimeoutException e) {
			throw e;
		} catch (InterruptedException e) {
			cancel(f);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Being canceled.");
			executeFailed(e);
		} catch (CancellationException e) {
			cancel(f);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Being canceled.");
			executeFailed(e);
		} catch (ExecutionException e) {
			cancel(f);
			Throwable t = e.getCause();
			executeFailed(launderThrowable(t));
		}

		if (null != ret) {
			if (ret.getClass() == Void.class) {
				// TODO:
				throw new RuntimeException("Illegal invoke.");
			}
		}

		if (isSuccess) {
			executeSuccess();
		}

		return ret;
	}

	private RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException)
			return (RuntimeException) t;
		else if (t instanceof Error)
			throw (Error) t;
		else
			throw new IllegalStateException("Not checked exception:", t);
	}

	private void cancel(Future<Object> f) {
		f.cancel(true);
	}
}
