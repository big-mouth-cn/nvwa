package org.bigmouth.nvwa.servicelogic;

import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;


public interface MutableTransactionInvocation extends TransactionInvocation {

	void setRequestModel(Object model);

	void setResponseModel(Object model);

	void setTransactionHandler(TransactionExecutor transactionHandler);

	TransactionExecutor getTransactionHandler();

	SapResponse getResponse();

	SapRequest getRequest();
}
