package org.bigmouth.nvwa.servicelogic;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.sap.DefaultSapResponse;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;


public class DefaultTransactionInvocation implements MutableTransactionInvocation {

	private final SapRequest request;
	private final SapResponse response;
	private final InvocationContext context;

	private volatile Object requestModel;
	private volatile Object responseModel;
	private volatile TransactionExecutor transactionHandler;

	public DefaultTransactionInvocation(SapRequest sapRequest, InvocationContext context) {
		this.request = sapRequest;
		this.context = context;
		this.response = new DefaultSapResponse(sapRequest);
	}

	@Override
	public SapRequest getRequest() {
		return request;
	}

	@Override
	public void setRequestModel(Object model) {
		this.requestModel = model;
	}

	@Override
	public InvocationContext getContext() {
		return context;
	}

	@Override
	public Object getRequestModel() {
		return requestModel;
	}

	@Override
	public Object getResponseModel() {
		return responseModel;
	}

	@Override
	public void setResponseModel(Object model) {
		this.responseModel = model;
	}

	@Override
	public void setTransactionHandler(TransactionExecutor transactionInterface) {
		this.transactionHandler = transactionInterface;
	}

	@Override
	public TransactionExecutor getTransactionHandler() {
		return transactionHandler;
	}

	@Override
	public SapResponse getResponse() {
		return response;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
