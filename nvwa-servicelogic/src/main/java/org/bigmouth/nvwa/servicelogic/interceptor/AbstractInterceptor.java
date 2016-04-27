package org.bigmouth.nvwa.servicelogic.interceptor;

import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInterceptor.class);

	private final Interceptor next;

	public AbstractInterceptor() {
		this(Interceptor.END);
	}

	public AbstractInterceptor(Interceptor next) {
		this.next = next;
	}

	protected void fireNextInterceptor(TransactionInvocation invocation) {
		if (null == next) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("next interceptor is null,ignore.");
			return;
		}
		next.intercept(invocation);
	}
}
