package org.bigmouth.nvwa.servicelogic.interceptor;

import java.util.Date;

import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;
import org.bigmouth.nvwa.session.DefaultTrackWrapper;
import org.bigmouth.nvwa.session.MutableSession;
import org.bigmouth.nvwa.session.Session;
import org.bigmouth.nvwa.session.SessionHolder;
import org.bigmouth.nvwa.session.Trackable;

public class SessionInjectInterceptor extends AbstractInterceptor {

	private final SessionHolder sessionHolder;

	public SessionInjectInterceptor(SessionHolder sessionHolder) {
		super();
		if (null == sessionHolder)
			throw new NullPointerException("sessionHolder");
		this.sessionHolder = sessionHolder;
	}

	@Override
	public void intercept(TransactionInvocation invocation) {
		TransactionExecutor transactionHandler = invocation.getTransactionHandler();
		if (isSessionAware(transactionHandler)) {
			Trackable tb = getTrackObject(invocation);
			Session session = sessionHolder.get(tb);

			if (!session.isNew()) {
				if (session instanceof MutableSession) {
					((MutableSession) session).update(new Date().getTime());
				}
			}

			SessionAware sessionAware = (SessionAware) transactionHandler;
			sessionAware.setSession(session);
		}
	}

	protected Trackable getTrackObject(TransactionInvocation invocation) {
		return new DefaultTrackWrapper(invocation.getRequestModel(), "sessionId");
	}

	private boolean isSessionAware(TransactionExecutor transactionHandler) {
		return transactionHandler instanceof SessionAware;
	}
}
