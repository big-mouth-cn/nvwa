package org.bigmouth.nvwa.dpl.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.status.Status;


public class DefaultProceduralService extends AbstractService implements ProceduralService {

	private final ServiceClosure serviceClosure;

	public DefaultProceduralService(ServiceConfig config, Status status,
			List<ServiceListener> listeners, ExecutorService executor, ServiceClosure serviceClosure) {
		super(config, status, listeners, executor);
		if (null == serviceClosure) {
			this.serviceClosure = ServiceClosure.DEFAULT;
		} else {
			this.serviceClosure = serviceClosure;
		}
	}

	public DefaultProceduralService(ServiceConfig config, Status status,
			List<ServiceListener> listeners, ServiceClosure serviceClosure) {
		this(config, status, listeners, null, serviceClosure);
	}

	public DefaultProceduralService(ServiceConfig config, Status status,
			ServiceClosure serviceClosure) {
		this(config, status, null, serviceClosure);
	}

	@Override
	public void execute(final Object... arguments) {
		getExecutor().execute(new Runnable() {

			@Override
			public void run() {

				beforeExecute(arguments);

				boolean ifSuccess = false;
				try {
					serviceClosure.execute(arguments);
					ifSuccess = true;
				} catch (RuntimeException e) {
					executeFailed(e, arguments);
				} catch (Exception e) {
					executeFailed(e, arguments);
				}

				if (ifSuccess) {
					executeSuccess(arguments);
				}
			}
		});
	}
}
