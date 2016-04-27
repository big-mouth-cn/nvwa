package org.bigmouth.nvwa.servicelogic.interceptor;

import java.util.List;

import org.bigmouth.nvwa.sap.MutableSapResponse;
import org.bigmouth.nvwa.sap.SapResponseStatus;
import org.bigmouth.nvwa.servicelogic.MutableTransactionInvocation;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.handler.BaseResponse;
import org.bigmouth.nvwa.servicelogic.handler.CommonBizCode;
import org.bigmouth.nvwa.servicelogic.handler.ResourceNotFoundException;
import org.bigmouth.nvwa.servicelogic.handler.TransactionException;
import org.bigmouth.nvwa.servicelogic.handler.TransactionHandler;
import org.bigmouth.nvwa.session.DefaultTrackWrapper;
import org.bigmouth.nvwa.session.Trackable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public abstract class TransactionInterceptor<REQ, RESP> extends AbstractInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionInterceptor.class);

	private final List<Interceptor> childInterceptors;

	public TransactionInterceptor(Interceptor next) {
		this(next, null);
	}

	public TransactionInterceptor(Interceptor next, List<Interceptor> childInterceptors) {
		super(next);
		if (null == childInterceptors || 0 == childInterceptors.size())
			childInterceptors = Lists.newArrayList();
		this.childInterceptors = childInterceptors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void intercept(TransactionInvocation invocation) {
		TransactionHandler<REQ, RESP> transactionHandler = doCreateTransactionHandler(invocation);

		try {
			for (Interceptor ci : childInterceptors) {
				ci.intercept(invocation);
			}
		} catch (Exception e) {
			LOGGER.error("Interceptor.intercept:", e);
			handleException(invocation, SapResponseStatus.INTERNAL_SERVER_ERROR, CommonBizCode.FAIL);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received requestModel:" + invocation.getRequestModel());
		}

		try {
			transactionHandler.handle((REQ) invocation.getRequestModel(),
					(RESP) invocation.getResponseModel());
		} catch (ResourceNotFoundException e) {
			LOGGER.error("transactionHandler.handle:", e);
			handleException(invocation, SapResponseStatus.INTERNAL_SERVER_ERROR,
					CommonBizCode.RESOURCE_NOT_FOUND);
		} catch (TransactionException e) {
			LOGGER.error("transactionHandler.handle:", e);
			handleException(invocation, SapResponseStatus.INTERNAL_SERVER_ERROR, CommonBizCode.FAIL);
		} catch (RuntimeException e) {
			LOGGER.error("transactionHandler.handle:", e);
			handleException(invocation, SapResponseStatus.INTERNAL_SERVER_ERROR, CommonBizCode.FAIL);
		} catch (Exception e) {
			LOGGER.error("transactionHandler.handle:", e);
			handleException(invocation, SapResponseStatus.INTERNAL_SERVER_ERROR, CommonBizCode.FAIL);
		}

		fireNextInterceptor(invocation);
	}

	private TransactionHandler<REQ, RESP> doCreateTransactionHandler(
			TransactionInvocation invocation) {
		TransactionHandler<REQ, RESP> transactionHandler = createTransactionHandler();
		if (null == transactionHandler)
			throw new RuntimeException("createTransactionHandler() return null.");
		((MutableTransactionInvocation) invocation).setTransactionHandler(transactionHandler);
		return transactionHandler;
	}

	private void handleException(TransactionInvocation invocation, SapResponseStatus status,
			int bizCode) {
		MutableSapResponse sapResponse = (MutableSapResponse) invocation.getResponse();
		sapResponse.setStatus(status);

		Object respModel = invocation.getResponseModel();
		if (respModel instanceof BaseResponse) {
			((BaseResponse) respModel).setBizCode(bizCode);
		}
	}

	protected Trackable getTrackObject(TransactionInvocation invocation) {
		return new DefaultTrackWrapper(invocation.getRequestModel(), "_x_session_id");
	}

	public abstract TransactionHandler<REQ, RESP> createTransactionHandler();
}
