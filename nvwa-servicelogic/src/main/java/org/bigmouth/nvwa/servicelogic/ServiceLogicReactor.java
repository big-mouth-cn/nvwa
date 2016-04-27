package org.bigmouth.nvwa.servicelogic;

import org.bigmouth.nvwa.dpl.PlugInServiceBus;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.ProceduralService;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.sap.SapResponseStatus;
import org.bigmouth.nvwa.sap.SapResponseUtils;
import org.bigmouth.nvwa.transport.Replier;
import org.bigmouth.nvwa.utils.Closure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServiceLogicReactor implements Closure {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogicReactor.class);

	private final PlugInServiceBus bus;
	private final Replier replier;

	public ServiceLogicReactor(PlugInServiceBus bus, Replier replier) {
		if (null == bus)
			throw new NullPointerException("bus");
		this.bus = bus;
		this.replier = replier;
	}

	@Override
	public void execute(Object input) {
		SapRequest sapRequest = (SapRequest) input;
		String plugInName = sapRequest.getTargetPlugInName();
		String serviceName = sapRequest.getTargetServiceName();

		// create TransactionInvocation
		InvocationContext context = new InvocationContext(bus, replier);
		TransactionInvocation invocation = new DefaultTransactionInvocation(sapRequest, context);

		PlugIn plugIn = bus.lookupPlugIn(plugInName);
		if (null == plugIn) {
			replyException(sapRequest, SapResponseStatus.PLUGIN_OR_SERVICE_UNAVAILABLE);
			return;
		}
		ProceduralService service = (ProceduralService) plugIn.lookupService(serviceName);

		if (null == service) {
			replyException(sapRequest, SapResponseStatus.PLUGIN_OR_SERVICE_UNAVAILABLE);
			return;
		}

		try {
			service.execute(invocation);
		} catch (Exception e) {
			LOGGER.error("ServiceLogicReactor.service.execute:", e);
			// TODO:error,how to response?process here?
			replyException(sapRequest, SapResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// TODO:bad request record
	private void replyException(SapRequest sapRequest, SapResponseStatus status) {
		SapResponse sapResponse = SapResponseUtils.createEmptySapResponse(
				sapRequest.getIdentification(), status);
		replier.reply(sapResponse);
	}
}
