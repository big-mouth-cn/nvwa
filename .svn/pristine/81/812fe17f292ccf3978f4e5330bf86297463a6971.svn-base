package org.bigmouth.nvwa.servicelogic.log;

import org.bigmouth.nvwa.log.RecordClosure;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;
import org.bigmouth.nvwa.servicelogic.interceptor.AbstractInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.Interceptor;


public class RecordStatusInterceptor extends AbstractInterceptor {

	private final RecordClosure recorder;

	public RecordStatusInterceptor(RecordClosure recorder) {
		this(Interceptor.END, recorder);
	}

	public RecordStatusInterceptor(Interceptor next, RecordClosure recorder) {
		super(next);
		if (null == recorder)
			throw new NullPointerException("recorder");
		this.recorder = recorder;
	}

	@Override
	public void intercept(TransactionInvocation invocation) {
		TransactionExecutor transactionHandler = invocation.getTransactionHandler();
		if (isRecordable(transactionHandler)) {
			doRecord(transactionHandler);
		}

		fireNextInterceptor(invocation);
	}

	private void doRecord(TransactionExecutor transactionHandler) {
		Recordable recordable = (Recordable) transactionHandler;
		Object invokeStatus = recordable.getInvokeStatus();
		if (null == invokeStatus)
			throw new EmptyInvokeStatusException(
					"InvokeStatus is empty,check Recordable.getInvokeStatus() method please.");
		recorder.execute(invokeStatus);
	}

	private boolean isRecordable(TransactionExecutor transactionHandler) {
		return transactionHandler instanceof Recordable;
	}
}
