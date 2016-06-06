package org.bigmouth.nvwa.servicelogic.interceptor;

import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;

public class InvocationInjectInterceptor extends AbstractInterceptor {

	public InvocationInjectInterceptor() {
		super();
	}

	@Override
	public void intercept(TransactionInvocation invocation) {

		TransactionExecutor transactionHandler = invocation.getTransactionHandler();
		if (isTransactionContextAware(transactionHandler)) {
			setTransactionContext(invocation, transactionHandler);
		}
		if (isSapRequestAware(transactionHandler)) {
			setSapRequest(invocation, transactionHandler);
		}
		if (isSapResponseAware(transactionHandler)) {
			setSapResponse(invocation, transactionHandler);
		}
	}

	private void setTransactionContext(TransactionInvocation invocation,
			TransactionExecutor transactionHandler) {
		TransactionContextAware contextAware = (TransactionContextAware) transactionHandler;
		contextAware.setTransactionContext(invocation.getContext());
	}

	private void setSapRequest(TransactionInvocation invocation,
			TransactionExecutor transactionHandler) {
		SapRequestAware sapRequetAware = (SapRequestAware) transactionHandler;
		sapRequetAware.setSapRequest(invocation.getRequest());
	}

	private void setSapResponse(TransactionInvocation invocation,
			TransactionExecutor transactionHandler) {
		SapResponseAware sapResponseAware = (SapResponseAware) transactionHandler;
		sapResponseAware.setSapResponse(invocation.getResponse());
	}

	private boolean isSapResponseAware(TransactionExecutor transactionHandler) {
		return transactionHandler instanceof SapResponseAware;
	}

	private boolean isSapRequestAware(TransactionExecutor transactionHandler) {
		return transactionHandler instanceof SapRequestAware;
	}

	private boolean isTransactionContextAware(TransactionExecutor transactionHandler) {
		return transactionHandler instanceof TransactionContextAware;
	}
}
