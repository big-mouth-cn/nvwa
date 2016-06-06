package org.bigmouth.nvwa.servicelogic;

import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;


public interface TransactionInvocation {

	Object getRequestModel();

	Object getResponseModel();

	InvocationContext getContext();

	TransactionExecutor getTransactionHandler();

	SapRequest getRequest();

	SapResponse getResponse();

	// TODO:
	// boolean isExtendable();

	// TODO:
	// boolean isRecordable();
}
