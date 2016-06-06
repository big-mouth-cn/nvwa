package org.bigmouth.nvwa.servicelogic.listener;

import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListenerAdapter;
import org.bigmouth.nvwa.sap.MutableSapResponse;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.sap.SapResponseStatus;
import org.bigmouth.nvwa.sap.SapResponseUtils;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;

public class InternelExceptionListener extends ServiceListenerAdapter {

	@Override
	public void onExecuteFailed(ExecutionFailedEvent event) {
		TransactionInvocation invocation = (TransactionInvocation) event.getInvocation()[0];
		SapResponse response = (MutableSapResponse) invocation.getResponse();
		response = SapResponseUtils.createEmptySapResponse(response.getIdentification(),
				SapResponseStatus.INTERNAL_SERVER_ERROR);
		invocation.getContext().getReplier().reply(response);
	}
}
